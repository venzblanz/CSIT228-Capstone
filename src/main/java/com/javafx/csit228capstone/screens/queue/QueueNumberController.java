package com.javafx.csit228capstone.screens.queue;

import com.javafx.csit228capstone.helper.MenuController;
import com.javafx.csit228capstone.model.Form;
import com.javafx.csit228capstone.model.QueueInsertValue;
import com.javafx.csit228capstone.model.QueueTicket;
import com.javafx.csit228capstone.utils.*;
import com.mysql.cj.Session;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class QueueNumberController {
    @FXML private MenuController menuController;
    @FXML private Button saveBtn;
    @FXML private Button doneBtn;
    @FXML private Label qTypeLabel;
    @FXML private Label qNameLabel;
    @FXML private Label qTimeLabel;
    @FXML private Label qNumberLabel;
    @FXML private Label qDateLabel;
    @FXML private Label patientIdLabel;
    @FXML private ImageView qQrImage;

    private final SceneNavigator sceneNavigator = SceneNavigator.getInstance();
    private final FormManager formManager = FormManager.getInstance();

    private final Form loadedForm = formManager.loadForm();
    private String qType;
    private QueueInsertValue qiv;
    private QueueTicket qt;

    public void initializeData(String type){
        menuController.setActiveButton(menuController.getQueueBtn());
        qType = type;

        Form f = formManager.loadForm();
        int formId = QueueFormDAO.addForm(
                f.getFirstName(),
                f.getMiddleName(),
                f.getLastName(),
                f.getAge(),
                f.getGender(),
                f.getPurpose(),
                f.getSymptoms(),
                f.getPatientType(),
                f.getContactNumber(),
                f.getFormType());
        if (formId == -1) {
            System.err.println("Form was not saved. Queue will not be created.");
            return;
        }
        qiv = QueueLineDAO.insertQueue(formId, SessionManager.getInstance().getUserId(), qType);
        assert qiv != null;
        qt = QueueLineDAO.getQueueTicket(qiv.getQueueId());
        formManager.clearForm();

        setCard();

        if(type.equals("General Wellness")){
            qTypeLabel.setText("General Wellness");
        }else if (type.equals("Women's Health")){
            qTypeLabel.setText("Women's Health");
        }else if (type.equals("Specialized Fields")){
            qTypeLabel.setText("Specialized Fields");
        }else{
            qTypeLabel.setText("Diagnostics and Laboratory");
        }
    }

    @FXML
    public void initialize() {
        saveBtn.setOnAction(e -> onSave());
        doneBtn.setOnAction(e -> onDone());
    }
    private void onSave(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to save?");
        alert.showAndWait();
    }
    private void onDone(){
        sceneNavigator.navigate("/com/javafx/csit228capstone/dashboard.fxml", doneBtn, "/styles/dashboard.css");
    }
    private void setCard(){
        String qrText = "QUEUE_ID=" + qt.getQueueId();
        Image image = QRCodeGenerator.generateQRCode(qrText,220,220);

        if(image != null){
            qQrImage.setImage(image);
        }

        qNameLabel.setText(qt.getFullName());
        qTimeLabel.setText(formatTime(qt.getCreatedAt()));
        qNumberLabel.setText(qt.getQueueNumber());
        qDateLabel.setText(formatDate(qt.getCreatedAt()));
        patientIdLabel.setText(PatientIdGenerator.getPatientId());
    }

    // Date and Time
    private String formatDate(LocalDateTime dateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
        return dateTime.format(formatter);
    }
    private String formatTime(LocalDateTime dateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("hh:mm a");
        return dateTime.format(formatter);
    }
}
