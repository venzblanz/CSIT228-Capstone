package com.javafx.csit228capstone.screens.Account;


import com.javafx.csit228capstone.helper.MenuController;
import com.javafx.csit228capstone.utils.SceneNavigator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.input.MouseButton;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.ResourceBundle;


public class AccountController implements Initializable {

    // Profile labels
    @FXML private Label profileNameLabel;
    @FXML private Label profileIdLabel;

    // Manage rows
    @FXML private HBox patientRecordsRow;
    @FXML private HBox queueHistoryRow;
    @FXML private HBox queueStatusRow;

    // Settings & Privacy rows
    @FXML private HBox notificationSettingsRow;
    @FXML private HBox securityRow;
    @FXML private Button editBtn;

    // Others rows
    @FXML private HBox supportRow;
    @FXML private HBox termsRow;
    @FXML private HBox aboutRow;
    @FXML private MenuController menuController;
    private final SceneNavigator sceneNavigator = SceneNavigator.getInstance();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menuController.setActiveButton(menuController.getAccountBtn());
        // profileNameLabel.setText(SessionManager.getUser().getFullName());
        // profileIdLabel.setText(SessionManager.getUser().getPatientId());

        editBtn.setOnAction(e -> onEdit());
        queueHistoryRow.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                handleQueueHistory();
            }
        });
    }

    private void onEdit() {
        handleEdit();
    }



    private void handleQueueHistory() {
        sceneNavigator.navigate(
                "/com/javafx/csit228capstone/account/queue_history.fxml", editBtn, "/styles/queue-history.css"
        );
    }

    private void handleMyAppointments() {

    }

    private void handleCheckQueueStatus() {

    }


    private void handleNotificationSettings() {

    }

    private void handleSecurityPrivacy() {

    }

    // ── Others ───────────────────────────────────────────────────────────────

    private void handleContactSupport() {

    }

    private void handleTermsAndConditions() {

    }

    private void handleAboutMedServe() {


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

    @FXML
    private void handleLogout() {

    }
}
