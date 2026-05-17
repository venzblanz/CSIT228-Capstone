package com.javafx.csit228capstone.screens.queue;

import com.javafx.csit228capstone.helper.MenuController;
import com.javafx.csit228capstone.model.Form;
import com.javafx.csit228capstone.model.QueueInsertValue;
import com.javafx.csit228capstone.model.QueueTicket;
import com.javafx.csit228capstone.utils.*;
import com.mysql.cj.Session;
import javafx.animation.Animation;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Optional;

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
    @FXML private VBox ticketCard;
    @FXML private VBox ticketScreen;


    private final SceneNavigator sceneNavigator = SceneNavigator.getInstance();
    private final FormManager formManager = FormManager.getInstance();

    private final Form loadedForm = formManager.loadForm();
    private String qType;
    private QueueInsertValue qiv;
    private QueueTicket qt;

    public void initializeData(String type){
        menuController.setActiveButton(menuController.getQueueBtn());
        qType = type;
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
        AnimationHelper.fadeIn(ticketScreen);
        saveBtn.setOnAction(e -> onSave());
        doneBtn.setOnAction(e -> onDone());
    }
    private void onSave(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to save?");
        alert.getDialogPane().getStylesheets().add(
                getClass().getResource("/styles/alert.css").toExternalForm()
        );
        Optional<ButtonType> result = alert.showAndWait();
        if(result.isPresent() && result.get() == ButtonType.OK){
            System.out.println("Saving image");
            saveTicket();
        }else{
            System.out.println("Image not saved");
        }
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
        patientIdLabel.setText(PatientIdGenerator.getPatientId(SessionManager.getInstance().getUserId()));
    }

    // Save Ticket as Image
    private void saveTicket(){
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Ticket");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("PNG Image", "*.png")
        );

        fileChooser.setInitialFileName("Ticket-" + PatientIdGenerator.getPatientId(SessionManager.getInstance().getUserId()) + qNumberLabel.getText() + ".png");

        File f = fileChooser.showSaveDialog(saveBtn.getScene().getWindow());
        if(f == null) return;

        try{
            SnapshotParameters sp = new SnapshotParameters();
            WritableImage image = ticketCard.snapshot(sp, null);

            ImageIO.write(SwingFXUtils.fromFXImage(image, null), "png", f);

        }catch (IOException e){
            System.err.println("Error Saving ticket " + e.getMessage());
        }
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
