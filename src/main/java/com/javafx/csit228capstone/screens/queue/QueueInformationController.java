package com.javafx.csit228capstone.screens.queue;

import com.javafx.csit228capstone.model.QueueInformation;
import com.javafx.csit228capstone.model.QueueTicket;
import com.javafx.csit228capstone.utils.AnimationHelper;
import com.javafx.csit228capstone.utils.QueueLineDAO;
import com.javafx.csit228capstone.utils.QueueTimeHelper;
import com.javafx.csit228capstone.utils.SceneNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import javax.swing.text.DateFormatter;
import java.text.DateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class QueueInformationController {
    @FXML private Label qNameLabel;
    @FXML private Label qAgeLabel;
    @FXML private Label qGenderLabel;
    @FXML private Label qPatientTypeLabel;
    @FXML private Label qDepartmentLabel;
    @FXML private Label qTimeLabel;
    @FXML private Label qDateLabel;
    @FXML private Label qCivilStatusLabel;
    @FXML private Label qNationalityLabel;
    @FXML private Label qReligionLabel;
    @FXML private Label qAddressLabel;
    @FXML private Label qBirthdateLabel;
    @FXML private Label qContactLabel;
    @FXML private Label qEmailLabel;
    @FXML private Label qEmergencyContactLabel;
    @FXML private Label qRelationLabel;
    @FXML private Label qSymptomsLabel;
    @FXML private Label qNumberLabel;
    @FXML private Label qPositionLabel;
    @FXML private Label qStatusLabel;
    @FXML private Label qWaitTimeLabel;
    @FXML private Label qCreatedAtLabel;

    @FXML private Button backToQueueBtn;
    @FXML private VBox infoScreen;

    public void initializeData(QueueTicket queueTicket, HBox card) {
        AnimationHelper.staggerFadeIn(infoScreen);
        boolean isOld = queueTicket.getCreatedAt().isBefore(LocalDate.now().atStartOfDay());
        QueueInformation qi = QueueLineDAO.getQueueInformation(queueTicket.getQueueId());
        int positionValue = QueueLineDAO.getPosition(
                queueTicket.getDepartment(),
                queueTicket.getQueueNumber()
        );

        assert qi != null;
        qNameLabel.setText(qi.getFullName());
        qAgeLabel.setText(qi.getAge() + "");
        qGenderLabel.setText(qi.getGender());
        qPatientTypeLabel.setText(qi.getPatientType());
        qDepartmentLabel.setText(qi.getDepartment());
        qTimeLabel.setText(qi.getTime());
        qDateLabel.setText(localDate(qi.getDate()));
        qCivilStatusLabel.setText(qi.getCivilStatus());
        qNationalityLabel.setText(qi.getNationality());
        qReligionLabel.setText(qi.getReligion());
        qAddressLabel.setText(qi.getAddress());
        qBirthdateLabel.setText(localDate(qi.getBirthdate()));
        qContactLabel.setText(qi.getContact());
        qEmailLabel.setText(qi.getEmail());
        qEmergencyContactLabel.setText(qi.getEmergencyContact());
        qRelationLabel.setText(qi.getRelation());
        qSymptomsLabel.setText(qi.getSymptoms());
        qNumberLabel.setText(queueTicket.getQueueNumber());
        if(!isOld){
            qPositionLabel.setText(positionValue + "");
            qStatusLabel.setText(qi.getStatus());
            qWaitTimeLabel.setText(QueueTimeHelper.getExpectedWaitingText(positionValue));
        }
        qCreatedAtLabel.setText(localDate(queueTicket.getCreatedAt().toLocalDate()));

        backToQueueBtn.setOnAction(e->{
            SceneNavigator.getInstance().navigate("/com/javafx/csit228capstone/queue/queue.fxml", backToQueueBtn, "/styles/queue.css");
        });
    }
    private String localDate(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM dd");
        return date.format(formatter);
    }
}
