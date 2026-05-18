package com.javafx.csit228capstone.screens.Account;


import com.javafx.csit228capstone.helper.MenuController;
import com.javafx.csit228capstone.model.User;
import com.javafx.csit228capstone.utils.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

import java.net.URL;
import java.util.ResourceBundle;


public class AccountController implements Initializable {

    @FXML private Label profileNameLabel;
    @FXML private Label profileIdLabel;

    @FXML private HBox viewProfileRow;
    @FXML private HBox queueHistoryRow;
    @FXML private HBox queueStatusRow;
   // @FXML private HBox notificationSettingsRow;
    @FXML private HBox securityRow;
    @FXML private Button editBtn;
    @FXML private HBox supportRow;
    @FXML private HBox termsRow;
    @FXML private HBox aboutRow;
    @FXML private ImageView profileImageView;
    @FXML private MenuController menuController;
    @FXML private Pane notification;
    private final SceneNavigator sceneNavigator = SceneNavigator.getInstance();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menuController.setActiveButton(menuController.getAccountBtn());
        AnimationHelper.ringAnimation(notification);

        User currentUser = SessionManager.getInstance().getCurrentUser();

        if (currentUser != null) {
            User latestData = UserDAO.getLatestUpdate(currentUser.getUserID());

            if (latestData != null) {
                profileNameLabel.setText(latestData.getFullname());
                ImageUtils.loadUpdateImage(currentUser.getUserID(), profileImageView);
            } else {
                profileNameLabel.setText(currentUser.getFullname());
                ImageUtils.loadProfileImage(currentUser.getUserID(), profileImageView);
            }

            profileIdLabel.setText(PatientIdGenerator.getPatientId(currentUser.getUserID()));

            ImageUtils.makeRounded(profileImageView);
        }
        double heroSize = 100.0;
        profileImageView.setFitWidth(heroSize);
        profileImageView.setFitHeight(heroSize);
        Circle clip = new Circle(heroSize / 2, heroSize / 2, heroSize / 2);
        profileImageView.setClip(clip);

        editBtn.setOnAction(e -> onEdit());
//        patientRecordsRow.setOnMouseClicked(e -> onPatientRecords());
      queueStatusRow.setOnMouseClicked(e -> onQueueStatus());
//        myAppointmentsRow.setOnMouseClicked(e -> onMyAppointments());
        securityRow.setOnMouseClicked(e -> onSecurityPrivacy());
        termsRow.setOnMouseClicked(e -> onTermsAndConditions());
        aboutRow.setOnMouseClicked(e -> onAboutMedServe());
        queueHistoryRow.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                handleQueueHistory();
            }
        });

        viewProfileRow.setOnMouseClicked(e -> sceneNavigator.navigate(
                "/com/javafx/csit228capstone/account/view_profile.fxml",
                viewProfileRow,
                "/styles/account.css"
        ));
    }

    private void onEdit() {
        handleEdit();
    }

    private void onPatientRecords() {
    }

    private void onMyAppointments() {
        handleMyAppointments();
    }

    private void onQueueStatus() {
        handleCheckQueueStatus();
    }

    private void onSecurityPrivacy() {
        handleSecurityPrivacy();
    }

    private void handleQueueHistory() {
        sceneNavigator.navigate(
                "/com/javafx/csit228capstone/account/queue_history.fxml", editBtn, "/styles/queue-history.css"
        );
    }

    private void handleMyAppointments() {

    }

    private void handleCheckQueueStatus() {
        sceneNavigator.navigate(
                "/com/javafx/csit228capstone/dashboard.fxml", securityRow, "/styles/dashboard.css"
        );
    }



    private void handleSecurityPrivacy() {
        sceneNavigator.navigate(
                "/com/javafx/csit228capstone/account/security_privacy.fxml", securityRow, "/styles/account.css"
        );
    }

    private void handleTermsAndConditions() {
        sceneNavigator.navigate(
                "/com/javafx/csit228capstone/account/terms_condition.fxml", termsRow, "/styles/account.css"
        );
    }
    private void onTermsAndConditions() {
        handleTermsAndConditions();
    }

    private void handleAboutMedServe() {
        sceneNavigator.navigate(
                "/com/javafx/csit228capstone/account/about_medserve.fxml", aboutRow, "/styles/account.css"
        );
    }
    private void onAboutMedServe() {
        handleAboutMedServe();
    }


    @FXML
    private void handleEdit() {
        sceneNavigator.navigate(
                "/com/javafx/csit228capstone/account/edit_profile.fxml", editBtn, "/styles/account-edit.css"
        );
    }


    @FXML
    private void handleDeleteAccount() {

    }

}
