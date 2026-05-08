package com.javafx.csit228capstone.screens.Account;

import com.javafx.csit228capstone.helper.MenuController;
import com.javafx.csit228capstone.model.User;
import com.javafx.csit228capstone.utils.AnimationHelper;
import com.javafx.csit228capstone.utils.SceneNavigator;
import com.javafx.csit228capstone.utils.SessionManager;
import com.javafx.csit228capstone.utils.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.Pane;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class ConfirmPasswordController implements Initializable {

    @FXML private MenuController menuController;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    private final SceneNavigator sceneNavigator = SceneNavigator.getInstance();
    @FXML private Button backBtn;
    @FXML private Button confirmBtn;
    @FXML private Pane notification;
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        AnimationHelper.ringAnimation(notification);
            menuController.setActiveButton(menuController.getAccountBtn());
            backBtn.setOnAction(e -> onHandleBack());
            confirmBtn.setOnAction(e -> onHandleConfirm());


    }
    private void onHandleBack() {
        handleBack();
    }
    private void onHandleConfirm() {
        handleConfirm();
    }

    @FXML
    private void handleBack() {
        sceneNavigator.navigate(
                "/com/javafx/csit228capstone/account/myaccount.fxml", backBtn, "/styles/account.css"
        );
    }
    private void showSuccessAndNavigate() {
        SceneNavigator.getInstance().navigate(
                "/com/javafx/csit228capstone/account/profile_updated.fxml",
                passwordField,
                "/styles/account-edit.css"
        );
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setContentText(message);
        alert.show();
    }

    @FXML
    private void handleConfirm() {
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText(); // Make sure this FX:ID matches your FXML

        passwordField.getStyleClass().remove("error-field");
        confirmPasswordField.getStyleClass().remove("error-field");


        if (!password.equals(confirmPassword)) {
            passwordField.getStyleClass().add("error-field");
            confirmPasswordField.getStyleClass().add("error-field");
            confirmPasswordField.textProperty().addListener((obs, oldText, newText) -> {
                confirmPasswordField.getStyleClass().remove("error-field");
            });

            return;
        }

        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (UserDAO.verifyPassword(currentUser.getUserID(), password)) {
            User pending = SessionManager.getInstance().getPendingUpdate();
            File imageFile = SessionManager.getInstance().getPendingImageFile();

            boolean success = UserDAO.insertProfileUpdate(
                    pending.getUserID(),
                    pending.getFullname(),
                    pending.getMobilenumber(),
                    pending.getBirthday(),
                    pending.getGender(),
                    pending.getAddress(),
                    imageFile
            );

            if (success) {
                showSuccessAndNavigate();
            } else {
                showError("Database Error: Could not save update.");
            }
        } else {
            passwordField.getStyleClass().add("error-field");
            passwordField.textProperty().addListener((obs, oldText, newText) -> {
                passwordField.getStyleClass().remove("error-field");
            });
        }
    }
}
