package com.javafx.csit228capstone.screens.schedule;

import com.javafx.csit228capstone.model.Service;
import com.javafx.csit228capstone.utils.AnimationHelper;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class ScheduleController extends BaseScheduleController {

    @FXML private com.javafx.csit228capstone.helper.MenuController menuController;
    @FXML private Pane notification;
    @FXML private TextField searchField;
    @FXML private HBox categoryFilterBar;
    @FXML private Button btnGeneralWellness;
    @FXML private Button btnWomensHealth;
    @FXML private Button btnSpecializedFields;
    @FXML private Button btnDiagnosticsLab;

    private String activeCategory = null;

    @Override
    protected boolean allowPastDates() {
        return false;
    }

    @Override
    protected void setupSpecifics() {
        if (menuController != null) {
            menuController.setActiveButton(menuController.getScheduleBtn());
        }

        if (notification != null) {
            AnimationHelper.ringAnimation(notification);
        }

        if (searchField != null) {
            searchField.textProperty().addListener((obs, o, n) -> handleSearch(n));
        }
    }

    @Override
    protected HBox buildTimeRow(String timeSlot) {
        HBox row = new HBox(8);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("time-row");
        row.setPadding(new Insets(10, 16, 10, 16));
        row.setMinHeight(52);

        Label timeLabel = new Label(timeSlot);
        timeLabel.setMinWidth(65);
        timeLabel.getStyleClass().add("time-label");

        if (timeSlot.equals(CLOSING_TIME)) {
            Label closedLabel = new Label("Closed");
            closedLabel.getStyleClass().add("closed-label");
            row.getChildren().addAll(timeLabel, closedLabel);
            return row;
        }

        HBox chipsBox = new HBox(8);
        chipsBox.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(chipsBox, Priority.ALWAYS);

        List<Service> services = slotServices.getOrDefault(timeSlot, List.of());
        List<Service> display = new ArrayList<>();

        if (activeCategory != null) {
            for (Service s : services) {
                if (s.getServiceType().equals(activeCategory)) {
                    display.add(s);
                }
            }
        } else {
            display.addAll(services);
        }

        if (display.isEmpty()) {
            Label dash = new Label("—");
            dash.getStyleClass().add("closed-label");
            chipsBox.getChildren().add(dash);
        } else {
            for (int idx = 0; idx < display.size(); idx++) {
                Service svc = display.get(idx);
                HBox chip = buildPatientChip(svc);
                animateChipIn(chip, idx * 30L);
                chipsBox.getChildren().add(chip);
            }
        }

        row.getChildren().addAll(timeLabel, chipsBox);
        return row;
    }

    private void animateChipIn(HBox chip, long delayMs) {
        chip.setScaleX(0.75);
        chip.setScaleY(0.75);
        chip.setOpacity(0);
        ScaleTransition st = new ScaleTransition(Duration.millis(200), chip);
        st.setToX(1);
        st.setToY(1);
        st.setDelay(Duration.millis(delayMs));
        st.setInterpolator(Interpolator.EASE_OUT);
        FadeTransition ft = new FadeTransition(Duration.millis(200), chip);
        ft.setToValue(1);
        ft.setDelay(Duration.millis(delayMs));
        new ParallelTransition(chip, st, ft).play();
    }

    private HBox buildPatientChip(Service service) {
        HBox chip = new HBox(6);
        chip.setAlignment(Pos.CENTER_LEFT);
        chip.getStyleClass().addAll("chip", "chip-" + service.getChipColor());

        Circle dot = new Circle(3.5);
        dot.getStyleClass().addAll("dot", "dot-" + service.getChipColor());

        Label nameLabel = new Label(service.getName());
        nameLabel.getStyleClass().add("chip-text");

        chip.getChildren().addAll(dot, nameLabel);
        return chip;
    }

    private void setActiveCategory(String category, Button clicked, String baseStyle, String activeStyle) {
        boolean turningOn = !category.equals(activeCategory);
        activeCategory = turningOn ? category : null;

        resetFilterButtons();

        if (turningOn) {
            clicked.getStyleClass().remove(baseStyle);
            if (!clicked.getStyleClass().contains(activeStyle)) {
                clicked.getStyleClass().add(activeStyle);
            }
            if (screenLabel != null) {
                screenLabel.setText(activeCategory);
            }

            ScaleTransition pulse = new ScaleTransition(Duration.millis(130), clicked);
            pulse.setFromX(0.88);
            pulse.setFromY(0.88);
            pulse.setToX(1.0);
            pulse.setToY(1.0);
            pulse.setInterpolator(Interpolator.EASE_OUT);
            pulse.play();
        } else {
            if (screenLabel != null) {
                screenLabel.setText("Schedule");
            }
        }
        renderTimeSlots();
    }

    private void resetFilterButtons() {
        swapStyle(btnGeneralWellness, "filter-btn-blue-active", "filter-btn-blue");
        swapStyle(btnWomensHealth, "filter-btn-pink-active", "filter-btn-pink");
        swapStyle(btnSpecializedFields, "filter-btn-green-active", "filter-btn-green");
        swapStyle(btnDiagnosticsLab, "filter-btn-purple-active", "filter-btn-purple");
    }

    private void swapStyle(Button btn, String remove, String add) {
        if (btn != null) {
            btn.getStyleClass().remove(remove);
            if (!btn.getStyleClass().contains(add)) {
                btn.getStyleClass().add(add);
            }
        }
    }

    @FXML
    private void onClickBtnGeneralWellness() { setActiveCategory("General Wellness", btnGeneralWellness, "filter-btn-blue", "filter-btn-blue-active"); }

    @FXML
    private void onClickBtnWomensHealth() { setActiveCategory("Women's Health", btnWomensHealth, "filter-btn-pink", "filter-btn-pink-active"); }

    @FXML
    private void onClickBtnSpecializedFields() { setActiveCategory("Specialized Fields", btnSpecializedFields, "filter-btn-green", "filter-btn-green-active"); }

    @FXML
    private void onClickBtnDiagnosticsLab() { setActiveCategory("Diagnostics & Laboratory", btnDiagnosticsLab, "filter-btn-purple", "filter-btn-purple-active"); }

    private void handleSearch(String query) {
        if (query == null || query.isBlank()) {
            for (javafx.scene.Node n : timeSlotsContainer.getChildren()) {
                n.setVisible(true);
                n.setManaged(true);
            }
            return;
        }

        String lower = query.toLowerCase();

        for (javafx.scene.Node node : timeSlotsContainer.getChildren()) {
            if (!(node instanceof HBox)) continue;
            HBox row = (HBox) node;
            boolean matches = false;

            if (row.getChildren().size() > 1 && row.getChildren().get(1) instanceof HBox) {
                HBox chipsBox = (HBox) row.getChildren().get(1);
                matches = searchNodeForText(chipsBox, lower);
            }
            node.setVisible(matches);
            node.setManaged(matches);
        }
    }

    private boolean searchNodeForText(javafx.scene.Node node, String query) {
        if (node instanceof Label) {
            Label lbl = (Label) node;
            if (lbl.getText() != null && lbl.getText().toLowerCase().contains(query)) {
                return true;
            }
        } else if (node instanceof javafx.scene.Parent) {
            for (javafx.scene.Node child : ((javafx.scene.Parent) node).getChildrenUnmodifiable()) {
                if (searchNodeForText(child, query)) {
                    return true;
                }
            }
        }
        return false;
    }
}