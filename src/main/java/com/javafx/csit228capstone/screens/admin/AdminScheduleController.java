package com.javafx.csit228capstone.screens.admin;

import com.javafx.csit228capstone.model.Service;
import com.javafx.csit228capstone.screens.schedule.BaseScheduleController;
import com.javafx.csit228capstone.screens.schedule.ScheduleDialogController;
import javafx.animation.*;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Duration;

import java.util.ArrayList;
import java.util.List;

public class AdminScheduleController extends BaseScheduleController {

    @FXML private AdminMenuController menuController;
    @FXML private VBox legendBox;

    @Override
    protected boolean allowPastDates() {
        return true;
    }

    @Override
    protected void setupSpecifics() {
        if (menuController != null) {
            menuController.setActiveButton(menuController.getScheduleBtn());
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
        List<Service> display = new ArrayList<>(services);

        for (int idx = 0; idx < display.size(); idx++) {
            Service svc = display.get(idx);
            HBox chip = buildAdminChip(svc, timeSlot, chipsBox);
            animateChipIn(chip, idx * 30L);
            chipsBox.getChildren().add(chip);
        }

        chipsBox.getChildren().add(buildAddButton(timeSlot, chipsBox));
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

    private HBox buildAdminChip(Service service, String timeSlot, HBox chipsBox) {
        HBox chip = new HBox(4);
        chip.setAlignment(Pos.CENTER_LEFT);
        chip.getStyleClass().addAll("chip", "chip-" + service.getChipColor());

        Circle dot = new Circle(3.5);
        dot.getStyleClass().addAll("dot", "dot-" + service.getChipColor());

        Label nameLabel = new Label(service.getName());
        nameLabel.getStyleClass().add("chip-text");

        Button removeBtn = new Button("×");
        removeBtn.setMnemonicParsing(false);
        removeBtn.getStyleClass().add("chip-remove");
        removeBtn.setOnAction(e -> {
            new Thread(() -> {
                try {
                    scheduleDAO.removeServiceFromSlot(selectedDate, timeSlot, service.getServiceId(), service.isRecurring());
                    Platform.runLater(() -> {
                        FadeTransition ft = new FadeTransition(Duration.millis(160), chip);
                        ScaleTransition st = new ScaleTransition(Duration.millis(160), chip);
                        ft.setToValue(0);
                        st.setToX(0.7);
                        st.setToY(0.7);
                        ParallelTransition out = new ParallelTransition(chip, ft, st);
                        out.setOnFinished(ev -> {
                            slotServices.get(timeSlot).remove(service);
                            chipsBox.getChildren().remove(chip);
                        });
                        out.play();
                    });
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }).start();
        });

        chip.getChildren().addAll(dot, nameLabel, removeBtn);
        return chip;
    }

    private Button buildAddButton(String timeSlot, HBox chipsBox) {
        Button addBtn = new Button("+ Add");
        addBtn.getStyleClass().add("add-button");
        addBtn.setOnAction(e -> showAddServiceDialog(timeSlot, chipsBox, addBtn));

        ScaleTransition hoverIn = new ScaleTransition(Duration.millis(110), addBtn);
        hoverIn.setToX(1.07);
        hoverIn.setToY(1.07);
        ScaleTransition hoverOut = new ScaleTransition(Duration.millis(110), addBtn);
        hoverOut.setToX(1.0);
        hoverOut.setToY(1.0);

        addBtn.setOnMouseEntered(e -> hoverIn.playFromStart());
        addBtn.setOnMouseExited(e -> hoverOut.playFromStart());

        return addBtn;
    }

    private void showAddServiceDialog(String timeSlot, HBox chipsBox, Button addBtn) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/javafx/csit228capstone/schedule/schedule-dialog.fxml"));
            Parent root = loader.load();

            ScheduleDialogController ctrl = loader.getController();
            ctrl.init(timeSlot, selectedDateLabel.getText(), selectedDate, service -> {
                slotServices.get(timeSlot).add(service);
                HBox chip = buildAdminChip(service, timeSlot, chipsBox);
                int addIndex = chipsBox.getChildren().indexOf(addBtn);
                chipsBox.getChildren().add(addIndex, chip);
                animateChipIn(chip, 0);
            });

            Stage dialog = new Stage(StageStyle.TRANSPARENT);
            dialog.initModality(Modality.APPLICATION_MODAL);
            dialog.initOwner(timeSlotsContainer.getScene().getWindow());

            StackPane wrapper = new StackPane(root);
            wrapper.setStyle("-fx-background-color: transparent; -fx-padding: 24px;");

            Scene scene = new Scene(wrapper);
            scene.setFill(Color.TRANSPARENT);
            scene.getStylesheets().add(getClass().getResource("/styles/schedule.css").toExternalForm());
            dialog.setScene(scene);

            root.setTranslateY(-18);
            root.setOpacity(0);
            TranslateTransition tt = new TranslateTransition(Duration.millis(250), root);
            tt.setToY(0);
            tt.setInterpolator(Interpolator.EASE_OUT);
            FadeTransition ft = new FadeTransition(Duration.millis(250), root);
            ft.setToValue(1);
            new ParallelTransition(root, tt, ft).play();

            ColorAdjust dim = new ColorAdjust();
            dim.setBrightness(-0.4);
            timeSlotsContainer.getScene().getRoot().setEffect(dim);
            dialog.setOnHidden(e -> timeSlotsContainer.getScene().getRoot().setEffect(null));

            dialog.showAndWait();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}