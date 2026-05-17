package com.javafx.csit228capstone.screens.schedule;

import com.javafx.csit228capstone.model.Service;
import com.javafx.csit228capstone.utils.ScheduleDAO;
import javafx.animation.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.net.URL;
import java.time.LocalDate;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.layout.StackPane;

public class ScheduleDialogController implements Initializable {

    @FXML private Label dialogSubtitle;
    @FXML private TextField dialogSearchField;
    @FXML private VBox dialogServicesList;
    @FXML private CheckBox recurringCheck;
    @FXML private Button dialogAddBtn;
    @FXML private HBox otherServiceRow;

    private String timeSlot;
    private LocalDate selectedDate;
    private ScheduleDAO scheduleDAO;
    private Consumer<Service> onServiceSelected;
    private Service[] selected = { null };

    @Override
    public void initialize(URL url, ResourceBundle rb) {
    }

    public void init(String timeSlot, String dayLabel, LocalDate selectedDate, ScheduleDAO scheduleDAO, Consumer<Service> onServiceSelected) {
        this.timeSlot = timeSlot;
        this.selectedDate = selectedDate;
        this.scheduleDAO = scheduleDAO;
        this.onServiceSelected = onServiceSelected;

        dialogSubtitle.setText(dayLabel + " · " + timeSlot);
        recurringCheck.setText("Repeat weekly (every " + getDayName(dayLabel) + ")");
        recurringCheck.setSelected(true);

        loadServiceRows();

        dialogSearchField.textProperty().addListener(new javafx.beans.value.ChangeListener<String>() {
            @Override
            public void changed(javafx.beans.value.ObservableValue<? extends String> obs, String o, String n) {
                filterRows(n);
            }
        });
    }

    private void loadServiceRows() {
        dialogServicesList.getChildren().clear();
        try {
            List<Service> all = scheduleDAO.getAllServices();
            for (Service svc : all) {
                dialogServicesList.getChildren().add(buildServiceRow(svc));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private HBox buildServiceRow(Service svc) {
        HBox row = new HBox(12);
        row.setAlignment(Pos.CENTER_LEFT);
        row.getStyleClass().add("service-list-row");
        row.setPadding(new Insets(10, 14, 10, 14));
        row.setCursor(javafx.scene.Cursor.HAND);

        Circle dot = new Circle(5);
        dot.getStyleClass().addAll("dot", "dot-" + svc.getChipColor());

        VBox textBox = new VBox(2);
        Label nameLabel = new Label(svc.getName());
        nameLabel.getStyleClass().add("other-service-title");
        Label typeLabel = new Label(svc.getServiceType());
        typeLabel.getStyleClass().add("other-service-hint");
        textBox.getChildren().addAll(nameLabel, typeLabel);
        HBox.setHgrow(textBox, Priority.ALWAYS);

        row.getChildren().addAll(dot, textBox);

        row.setOnMouseClicked(new javafx.event.EventHandler<javafx.scene.input.MouseEvent>() {
            @Override
            public void handle(javafx.scene.input.MouseEvent e) {
                for (javafx.scene.Node n : dialogServicesList.getChildren()) {
                    n.getStyleClass().removeAll("service-list-row-selected");
                }
                row.getStyleClass().add("service-list-row-selected");
                selected[0] = svc;
                dialogAddBtn.setDisable(false);

                ScaleTransition pulse = new ScaleTransition(Duration.millis(100), row);
                pulse.setFromX(0.97);
                pulse.setFromY(0.97);
                pulse.setToX(1.0);
                pulse.setToY(1.0);
                pulse.play();
            }
        });

        return row;
    }

    private void filterRows(String query) {
        String lower;
        if (query == null) {
            lower = "";
        } else {
            lower = query.toLowerCase();
        }

        for (javafx.scene.Node node : dialogServicesList.getChildren()) {
            if (node instanceof HBox) {
                HBox row = (HBox) node;
                boolean show = false;

                for (javafx.scene.Node child : row.getChildren()) {
                    if (child instanceof VBox) {
                        VBox vb = (VBox) child;
                        for (javafx.scene.Node lbl : vb.getChildren()) {
                            if (lbl instanceof Label) {
                                Label l = (Label) lbl;
                                if (l.getText().toLowerCase().contains(lower)) {
                                    show = true;
                                    break;
                                }
                            }
                        }
                    }
                    if (show) {
                        break;
                    }
                }

                row.setVisible(show);
                row.setManaged(show);
            }
        }
    }

    @FXML
    private void onAdd() {
        if (selected[0] == null) {
            return;
        }

        Service withRecurring = new Service(selected[0].getServiceId(), selected[0].getName(), selected[0].getServiceType(), recurringCheck.isSelected());

        try {
            if (withRecurring.isRecurring()) {
                scheduleDAO.addRecurringServiceToSlot(selectedDate.getDayOfWeek().getValue(), timeSlot, withRecurring.getServiceId());
            } else {
                scheduleDAO.addOneTimeServiceToSlot(selectedDate, timeSlot, withRecurring.getServiceId());
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        onServiceSelected.accept(withRecurring);
        close();
    }

    @FXML
    private void onCancel() {
        close();
    }

    @FXML
    private void onOtherServiceClicked() {
        showCustomServiceDialog();
    }

    private void showCustomServiceDialog() {
        try {
            javafx.fxml.FXMLLoader loader = new javafx.fxml.FXMLLoader(getClass().getResource("/com/javafx/csit228capstone/schedule/schedule-custom-service.fxml"));
            javafx.scene.Parent root = loader.load();

            ScheduleCustomServiceController ctrl = loader.getController();
            ctrl.init(selectedDate, getDayName(dialogSubtitle.getText().split("·")[0].trim()), recurringCheck.isSelected(), scheduleDAO, timeSlot, new Consumer<Service>() {
                @Override
                public void accept(Service service) {
                    onServiceSelected.accept(service);
                    close();
                }
            });

            Stage stage = new Stage(javafx.stage.StageStyle.TRANSPARENT);
            stage.initModality(javafx.stage.Modality.APPLICATION_MODAL);
            stage.initOwner(dialogAddBtn.getScene().getWindow());

            javafx.scene.layout.StackPane wrapper = new javafx.scene.layout.StackPane(root);
            wrapper.setStyle("-fx-background-color: transparent; -fx-padding: 24px;");

            javafx.scene.Scene scene = new javafx.scene.Scene(wrapper);
            scene.setFill(javafx.scene.paint.Color.TRANSPARENT);

            scene.getStylesheets().add(getClass().getResource("/styles/schedule.css").toExternalForm());
            stage.setScene(scene);

            ColorAdjust dim = new ColorAdjust();
            dim.setBrightness(-0.4);
            dialogAddBtn.getScene().getRoot().setEffect(dim); // Dim the parent dialog
            stage.setOnHidden(e -> dialogAddBtn.getScene().getRoot().setEffect(null));

            stage.showAndWait();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void close() {
        Stage stage = (Stage) dialogAddBtn.getScene().getWindow();

        FadeTransition ft = new FadeTransition(Duration.millis(160), dialogAddBtn.getScene().getRoot());
        ft.setToValue(0);
        ft.setOnFinished(new javafx.event.EventHandler<javafx.event.ActionEvent>() {
            @Override
            public void handle(javafx.event.ActionEvent e) {
                stage.close();
            }
        });
        ft.play();
    }

    private String getDayName(String dayLabel) {
        return dayLabel.split(",")[0].trim();
    }
}