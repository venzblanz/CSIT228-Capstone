package com.javafx.csit228capstone.screens.Account;

import com.javafx.csit228capstone.helper.MenuController;
import com.javafx.csit228capstone.utils.SceneNavigator;
import com.javafx.csit228capstone.utils.SessionManager;
import com.javafx.csit228capstone.utils.UserDAO;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class ChangePasswordController implements Initializable {

    @FXML private MenuController menuController;
    @FXML private PasswordField currentPasswordField; // Added for current password
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmNewPasswordField;
    @FXML private Button saveBtn;
    @FXML private Button cancelBtn;
    @FXML private Pane notification;

    private final SceneNavigator sceneNavigator = SceneNavigator.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        menuController.setActiveButton(menuController.getAccountBtn());
        cancelBtn.setOnAction(e -> onBack());
        saveBtn.setOnAction(e -> handleSave());
    }

    @FXML
    private void handleSave() {
        String currentPwd = currentPasswordField.getText();
        String newPwd = newPasswordField.getText();
        String confirmPwd = confirmNewPasswordField.getText();

        // 1. Reset error styles
        currentPasswordField.getStyleClass().remove("error-field");
        newPasswordField.getStyleClass().remove("error-field");
        confirmNewPasswordField.getStyleClass().remove("error-field");

        int currentUserId = SessionManager.getInstance().getUserId();

        // 2. Verify current password
        if (currentPwd.isEmpty() || !UserDAO.verifyPassword(currentUserId, currentPwd)) {
            currentPasswordField.getStyleClass().add("error-field");
            return;
        }

        // 3. Validate new password format/length
        if (newPwd.isEmpty() || newPwd.length() < 8 ) {
            newPasswordField.getStyleClass().add("error-field");
            return;
        }

        // 4. Ensure new password is not the same as the old password
        if (newPwd.equals(currentPwd)) {
            newPasswordField.getStyleClass().add("error-field");
            return;
        }

        // 5. Confirm passwords match
        if (!newPwd.equals(confirmPwd)) {
            newPasswordField.getStyleClass().add("error-field");
            confirmNewPasswordField.getStyleClass().add("error-field");
            return;
        }

        // 6. Update password in the database
        boolean isUpdated = UserDAO.updatePassword(currentUserId, newPwd);

        if (isUpdated) {
            sceneNavigator.navigate(
                    "/com/javafx/csit228capstone/account/password_updated.fxml",
                    saveBtn,
                    "/styles/dashboard.css"
            );
        } else {
            System.err.println("Database Update Failed.");
        }
    }

    private void onBack(){
        handleBack();
    }

    @FXML
    private void handleBack() {
        sceneNavigator.navigate(
                "/com/javafx/csit228capstone/account/security_privacy.fxml",
                cancelBtn,
                "/styles/account.css"
        );
    }
}