package com.javafx.csit228capstone.screens.schedule;

import com.javafx.csit228capstone.model.Service;
import com.javafx.csit228capstone.utils.ScheduleDAO;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class ScheduleEditServiceController implements Initializable {

    @FXML private Label dialogSubtitle;
    @FXML private Label serviceNameLabel;
    @FXML private TextField doctorField;
    @FXML private Label noDoctorHint;
    @FXML private Label scopeNote;
    @FXML private Button saveBtn;

    private Service service;
    private LocalDate date;
    private String timeSlot;
    private final ScheduleDAO scheduleDAO = ScheduleDAO.getInstance();
    private Consumer<String> onSaved;

    @Override
    public void initialize(URL url, ResourceBundle rb) { }

    public void init(Service service, LocalDate date, String timeSlot, String dayLabel, Consumer<String> onSaved) {
        this.service = service;
        this.date = date;
        this.timeSlot = timeSlot;
        this.onSaved = onSaved;

        dialogSubtitle.setText(dayLabel + " · " + timeSlot);
        serviceNameLabel.setText(service.getName());
        doctorField.setText(service.getDoctorName() != null ? service.getDoctorName() : "");

        if (service.isRecurring()) {
            scopeNote.setText("💡 This is a recurring slot. Saving will update the doctor for every "
                    + date.getDayOfWeek().toString().charAt(0)
                    + date.getDayOfWeek().toString().substring(1).toLowerCase()
                    + " at " + timeSlot + ".");
        } else {
            scopeNote.setText("💡 This is a one-time slot for " + dayLabel + " only.");
        }

        updateNoDoctorHint();
        doctorField.textProperty().addListener((obs, o, n) -> updateNoDoctorHint());
    }

    private void updateNoDoctorHint() {
        boolean blank = doctorField.getText() == null || doctorField.getText().isBlank();
        noDoctorHint.setVisible(blank);
        noDoctorHint.setManaged(blank);
    }

    @FXML
    private void onSave() {
        String newDoctor = doctorField.getText() == null ? null : doctorField.getText().trim();
        String doctorToSave = (newDoctor == null || newDoctor.isBlank()) ? null : newDoctor;

        new Thread(() -> {
            try {
                scheduleDAO.updateDoctorForSlot(date, timeSlot, service.getServiceId(), service.isRecurring(), doctorToSave);
                Platform.runLater(() -> {
                    onSaved.accept(doctorToSave);
                    close();
                });
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    @FXML
    private void onCancel() { close(); }

    private void close() {
        Stage stage = (Stage) saveBtn.getScene().getWindow();
        FadeTransition ft = new FadeTransition(Duration.millis(140), saveBtn.getScene().getRoot());
        ft.setToValue(0);
        ft.setOnFinished(e -> stage.close());
        ft.play();
    }
}