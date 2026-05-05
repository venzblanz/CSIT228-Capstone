package com.javafx.csit228capstone.screens.schedule;

import com.javafx.csit228capstone.model.Service;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Window;
import java.util.function.Consumer;

public class AddServiceDialog {

    private final String timeSlot;
    private final String dayLabel;
    private final Consumer<Service> onServiceSelected;

    public AddServiceDialog(String timeSlot, String dayLabel, Consumer<Service> onServiceSelected) {
        this.timeSlot = timeSlot;
        this.dayLabel = dayLabel;
        this.onServiceSelected = onServiceSelected;
    }

    public void show(Window owner) {
        Dialog<Service> dialog = new Dialog<>();
        dialog.initOwner(owner);
        dialog.setTitle("");

        VBox root = new VBox(12);
        root.setPadding(new Insets(20));
        root.setPrefWidth(380);
        root.getStylesheets().add(
                getClass().getResource("/styles/schedule-staff.css").toExternalForm()
        );

        Label title = new Label("Add Service");
        title.getStyleClass().add("dialog-title");

        Label subtitle = new Label(dayLabel + " · " + timeSlot);
        subtitle.getStyleClass().add("dialog-subtitle");

        TextField searchField = new TextField();
        searchField.setPromptText("Search services...");
        searchField.getStyleClass().add("dialog-search");
        searchField.setMaxWidth(Double.MAX_VALUE);

        VBox servicesList = new VBox(0);
        servicesList.getStyleClass().add("dialog-services-list");

        ScrollPane scrollPane = new ScrollPane(servicesList);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(200);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.getStyleClass().add("dialog-scroll");

        // TODO: load services from database here and populate servicesList

        HBox otherServiceRow = buildOtherServiceRow(dialog);

        HBox buttons = new HBox(8);
        buttons.setAlignment(Pos.CENTER_RIGHT);

        Button cancelBtn = new Button("Cancel");
        cancelBtn.getStyleClass().add("dialog-cancel-btn");
        cancelBtn.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(cancelBtn, Priority.ALWAYS);

        Button addBtn = new Button("Add Service");
        addBtn.getStyleClass().add("dialog-add-btn");
        addBtn.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(addBtn, Priority.ALWAYS);
        addBtn.setDisable(true);

        cancelBtn.setOnAction(e -> dialog.close());

        buttons.getChildren().addAll(cancelBtn, addBtn);

        Service[] selected = {null};

        otherServiceRow.setOnMouseClicked(e -> {
            showOtherServiceInput(dialog, onServiceSelected);
        });

        addBtn.setOnAction(e -> {
            if (selected[0] != null) {
                onServiceSelected.accept(selected[0]);
                dialog.close();
            }
        });

        root.getChildren().addAll(title, subtitle, searchField, scrollPane, otherServiceRow, buttons);

        dialog.getDialogPane().setContent(root);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        dialog.getDialogPane().lookupButton(ButtonType.CANCEL).setVisible(false);

        dialog.showAndWait();
    }

    private HBox buildOtherServiceRow(Dialog<?> dialog) {
        HBox row = new HBox(12);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("other-service-row");
        row.setPadding(new Insets(12, 14, 12, 14));
        row.setCursor(javafx.scene.Cursor.HAND);

        Label plusIcon = new Label("+");
        plusIcon.getStyleClass().add("other-service-icon");

        VBox textBox = new VBox(2);
        Label otherLabel = new Label("Other Service");
        otherLabel.getStyleClass().add("other-service-title");
        Label otherHint = new Label("Type a custom service name & pick category");
        otherHint.getStyleClass().add("other-service-hint");
        textBox.getChildren().addAll(otherLabel, otherHint);
        HBox.setHgrow(textBox, Priority.ALWAYS);

        Label arrow = new Label("›");
        arrow.getStyleClass().add("other-service-arrow");

        row.getChildren().addAll(plusIcon, textBox, arrow);
        return row;
    }

    private void showOtherServiceInput(Dialog<?> parent, Consumer<Service> onServiceSelected) {
        Dialog<Service> inputDialog = new Dialog<>();
        inputDialog.initOwner(parent.getOwner());
        inputDialog.setTitle("");

        VBox root = new VBox(12);
        root.setPadding(new Insets(20));
        root.setPrefWidth(360);
        root.getStylesheets().add(
                getClass().getResource("/styles/schedule-staff.css").toExternalForm()
        );

        Label title = new Label("Other Service");
        title.getStyleClass().add("dialog-title");

        TextField nameField = new TextField();
        nameField.setPromptText("Service name");
        nameField.getStyleClass().add("dialog-search");
        nameField.setMaxWidth(Double.MAX_VALUE);

        Label categoryLabel = new Label("Category");
        categoryLabel.getStyleClass().add("dialog-subtitle");

        HBox categoryRow = new HBox(8);
        categoryRow.setAlignment(Pos.CENTER_LEFT);

        ToggleGroup categoryGroup = new ToggleGroup();

        RadioButton rbGeneral  = buildCategoryRadio("General Wellness",        "blue",   categoryGroup);
        RadioButton rbWomens   = buildCategoryRadio("Women's Health",           "pink",   categoryGroup);
        RadioButton rbSpecial  = buildCategoryRadio("Specialized Fields",       "green",  categoryGroup);
        RadioButton rbDiag     = buildCategoryRadio("Diagnostics & Laboratory", "purple", categoryGroup);

        VBox categoryOptions = new VBox(8);
        categoryOptions.getChildren().addAll(rbGeneral, rbWomens, rbSpecial, rbDiag);

        HBox buttons = new HBox(8);
        Button cancelBtn = new Button("Cancel");
        cancelBtn.getStyleClass().add("dialog-cancel-btn");
        cancelBtn.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(cancelBtn, Priority.ALWAYS);

        Button addBtn = new Button("Add Service");
        addBtn.getStyleClass().add("dialog-add-btn");
        addBtn.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(addBtn, Priority.ALWAYS);

        cancelBtn.setOnAction(e -> inputDialog.close());

        addBtn.setOnAction(e -> {
            String name = nameField.getText().trim();
            if (name.isBlank() || categoryGroup.getSelectedToggle() == null) return;

            String serviceType = (String) categoryGroup.getSelectedToggle().getUserData();
            Service service = new Service(name, serviceType);
            onServiceSelected.accept(service);
            inputDialog.close();
            parent.close();
        });

        buttons.getChildren().addAll(cancelBtn, addBtn);

        root.getChildren().addAll(title, nameField, categoryLabel, categoryOptions, buttons);

        inputDialog.getDialogPane().setContent(root);
        inputDialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        inputDialog.getDialogPane().lookupButton(ButtonType.CANCEL).setVisible(false);

        inputDialog.showAndWait();
    }

    private RadioButton buildCategoryRadio(String text, String serviceType, ToggleGroup group) {
        RadioButton rb = new RadioButton(text);
        rb.setToggleGroup(group);
        rb.setUserData(serviceType);
        rb.getStyleClass().add("category-radio-" + serviceType);
        return rb;
    }
}
