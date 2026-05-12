package com.javafx.csit228capstone.screens.schedule;

import com.javafx.csit228capstone.model.Service;
import com.javafx.csit228capstone.utils.ScheduleDAO;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Window;

import java.util.List;
import java.util.function.Consumer;

public class AddServiceDialog {

    private final String timeSlot;
    private final String dayLabel;
    private final Consumer<Service> onServiceSelected;
    private final ScheduleDAO scheduleDAO;

    public AddServiceDialog(String timeSlot, String dayLabel, Consumer<Service> onServiceSelected, ScheduleDAO scheduleDAO) {
        this.timeSlot = timeSlot;
        this.dayLabel = dayLabel;
        this.onServiceSelected = onServiceSelected;
        this.scheduleDAO = scheduleDAO;
    }

    public void show(Window owner) {
        Dialog<Service> dialog = new Dialog<>();
        dialog.initOwner(owner);
        dialog.setTitle("");

        VBox root = new VBox(12);
        root.setPadding(new Insets(20));
        root.setPrefWidth(380);
        root.getStylesheets().add(
                getClass().getResource("/styles/schedule-admin.css").toExternalForm()
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

        Button addBtn = new Button("Add Service");
        addBtn.getStyleClass().add("dialog-add-btn");
        addBtn.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(addBtn, Priority.ALWAYS);
        addBtn.setDisable(true);

        Service[] selected = {null}; // move this up here

        try {
            List<Service> allServices = scheduleDAO.getAllServices();
            for (Service svc : allServices) {
                HBox row = buildServiceRow(svc, selected, addBtn);
                servicesList.getChildren().add(row);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        // Filter on search
        searchField.textProperty().addListener((obs, oldVal, newVal) -> {
            String lower = newVal == null ? "" : newVal.toLowerCase();
            servicesList.getChildren().forEach(node -> {
                if (node instanceof HBox row) {
                    boolean matches = row.getChildren().stream().anyMatch(child ->
                            child instanceof Label lbl && lbl.getText().toLowerCase().contains(lower)
                    );
                    row.setVisible(matches);
                    row.setManaged(matches);
                }
            });
        });

        // ── Recurring toggle ──────────────────────────────────────────────
        CheckBox recurringCheck = new CheckBox("Repeat weekly (every " + getDayName() + ")");
        recurringCheck.setSelected(true); // default to recurring
        recurringCheck.getStyleClass().add("dialog-subtitle");

        HBox otherServiceRow = buildOtherServiceRow(dialog, recurringCheck);

        HBox buttons = new HBox(8);
        buttons.setAlignment(Pos.CENTER_RIGHT);

        Button cancelBtn = new Button("Cancel");
        cancelBtn.getStyleClass().add("dialog-cancel-btn");
        cancelBtn.setMaxWidth(Double.MAX_VALUE);
        HBox.setHgrow(cancelBtn, Priority.ALWAYS);

        cancelBtn.setOnAction(e -> dialog.close());

        buttons.getChildren().addAll(cancelBtn, addBtn);

        addBtn.setOnAction(e -> {
            if (selected[0] != null) {
                // apply the recurring choice to the selected service
                Service withRecurring = new Service(
                        selected[0].getServiceId(),
                        selected[0].getName(),
                        selected[0].getServiceType(),
                        recurringCheck.isSelected()
                );
                onServiceSelected.accept(withRecurring);
                dialog.close();
            }
        });

        root.getChildren().addAll(title, subtitle, searchField, scrollPane, recurringCheck, otherServiceRow, buttons);

        dialog.getDialogPane().setContent(root);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);
        dialog.getDialogPane().lookupButton(ButtonType.CANCEL).setVisible(false);

        dialog.showAndWait();
    }

    private HBox buildServiceRow(Service svc, Service[] selected, Button addBtn) {
        HBox row = new HBox(12);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("other-service-row");
        row.setPadding(new Insets(10, 14, 10, 14));
        row.setCursor(javafx.scene.Cursor.HAND);

        // Colored dot
        javafx.scene.shape.Circle dot = new javafx.scene.shape.Circle(5);
        dot.getStyleClass().addAll("dot", "dot-" + svc.getChipColor());

        VBox textBox = new VBox(2);
        Label nameLabel = new Label(svc.getName());
        nameLabel.getStyleClass().add("other-service-title");
        Label typeLabel = new Label(svc.getServiceType());
        typeLabel.getStyleClass().add("other-service-hint");
        textBox.getChildren().addAll(nameLabel, typeLabel);
        HBox.setHgrow(textBox, Priority.ALWAYS);

        row.getChildren().addAll(dot, textBox);

        row.setOnMouseClicked(e -> {
            // Deselect all rows
            if (row.getParent() instanceof VBox list) {
                list.getChildren().forEach(n -> n.getStyleClass().remove("other-service-row-selected"));
            }
            row.getStyleClass().add("other-service-row-selected");
            selected[0] = svc;
            addBtn.setDisable(false);
        });

        return row;
    }

    private HBox buildOtherServiceRow(Dialog<?> dialog, CheckBox recurringCheck) {
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

        // pass recurringCheck into the custom service dialog
        row.setOnMouseClicked(e -> showOtherServiceInput(dialog, onServiceSelected, recurringCheck.isSelected()));

        return row;
    }

    private void showOtherServiceInput(Dialog<?> parent, Consumer<Service> onServiceSelected, boolean inheritedRecurring) {
        Dialog<Service> inputDialog = new Dialog<>();
        inputDialog.initOwner(parent.getOwner());
        inputDialog.setTitle("");

        VBox root = new VBox(12);
        root.setPadding(new Insets(20));
        root.setPrefWidth(360);
        root.getStylesheets().add(
                getClass().getResource("/styles/schedule-admin.css").toExternalForm()
        );

        Label title = new Label("Other Service");
        title.getStyleClass().add("dialog-title");

        TextField nameField = new TextField();
        nameField.setPromptText("Service name");
        nameField.getStyleClass().add("dialog-search");
        nameField.setMaxWidth(Double.MAX_VALUE);

        Label categoryLabel = new Label("Category");
        categoryLabel.getStyleClass().add("dialog-subtitle");

        ToggleGroup categoryGroup = new ToggleGroup();
        RadioButton rbGeneral = buildCategoryRadio("General Wellness",        "blue",   categoryGroup);
        RadioButton rbWomens  = buildCategoryRadio("Women's Health",           "pink",   categoryGroup);
        RadioButton rbSpecial = buildCategoryRadio("Specialized Fields",       "green",  categoryGroup);
        RadioButton rbDiag    = buildCategoryRadio("Diagnostics & Laboratory", "purple", categoryGroup);

        VBox categoryOptions = new VBox(8);
        categoryOptions.getChildren().addAll(rbGeneral, rbWomens, rbSpecial, rbDiag);

        // ── Recurring toggle (inherits choice from parent dialog) ─────────
        CheckBox recurringCheck = new CheckBox("Repeat weekly (every " + getDayName() + ")");
        recurringCheck.setSelected(inheritedRecurring);
        recurringCheck.getStyleClass().add("dialog-subtitle");

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
            Service service = new Service(name, serviceType, recurringCheck.isSelected());
            onServiceSelected.accept(service);
            inputDialog.close();
            parent.close();
        });

        buttons.getChildren().addAll(cancelBtn, addBtn);

        root.getChildren().addAll(title, nameField, categoryLabel, categoryOptions, recurringCheck, buttons);

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

    // extracts the day name from dayLabel e.g. "Monday, May 8" → "Monday"
    private String getDayName() {
        return dayLabel.split(",")[0].trim();
    }
}