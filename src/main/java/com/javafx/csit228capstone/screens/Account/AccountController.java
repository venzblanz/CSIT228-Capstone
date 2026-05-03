package com.javafx.csit228capstone.screens.Account;


import com.javafx.csit228capstone.Main;
import com.javafx.csit228capstone.helper.MenuController;
import com.javafx.csit228capstone.utils.SceneNavigator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;

import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


public class AccountController implements Initializable {

    // Profile labels
    @FXML private Label profileNameLabel;
    @FXML private Label profileIdLabel;

    // Manage rows
    @FXML private HBox patientRecordsRow;
    @FXML private HBox myAppointmentsRow;
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
    }

    private void onEdit() {
        handleEdit();
    }



    private void handlePatientRecords() {

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
