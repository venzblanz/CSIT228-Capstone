package com.javafx.csit228capstone.screens.Account;

import com.javafx.csit228capstone.helper.MenuController;
import com.javafx.csit228capstone.utils.AnimationHelper;
import com.javafx.csit228capstone.utils.SceneNavigator;
import com.javafx.csit228capstone.utils.SessionManager;
import com.javafx.csit228capstone.utils.UserDAO;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class ChangePasswordController implements Initializable {

    @FXML private MenuController menuController;
    @FXML private PasswordField currentPasswordField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmNewPasswordField;
    @FXML private Button saveBtn;
    @FXML private Button cancelBtn;
    @FXML private Pane notification;
    private final SceneNavigator sceneNavigator = SceneNavigator.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        AnimationHelper.ringAnimation(notification);

            menuController.setActiveButton(menuController.getAccountBtn());
            cancelBtn.setOnAction(e -> onBack());
            saveBtn.setOnAction(e -> onSave());

    }

    @FXML
    private void handleSavePassword() {

        sceneNavigator.navigate(
                "/com/javafx/csit228capstone/account/password_updated.fxml", saveBtn, "/styles/account-edit.css"
        );
    }


    private UserDAO userDAO = new UserDAO();

    // Update your onSave to call the logic first
    private void onSave() {
        handleSave();
    }

    @FXML
    private void handleSave() {
        String newPwd = newPasswordField.getText();
        String confirmPwd = confirmNewPasswordField.getText();
        if (newPwd.isEmpty() || newPwd.length() < 6) {
            System.out.println("Password must be at least 6 characters!");
            return;
        }

        if (!newPwd.equals(confirmPwd)) {
            System.out.println("Passwords do not match!");
            return;
        }

        int currentUserId = SessionManager.getInstance().getUserId();

        boolean isUpdated = userDAO.updatePassword(currentUserId, newPwd);

        if (isUpdated) {
            sceneNavigator.navigate(
                    "/com/javafx/csit228capstone/account/password_updated.fxml",
                    saveBtn,
                    "/styles/dashboard.css"
            );
        } else {
            System.out.println("Update failed in database.");
        }
    }


    private void onBack(){
        handleBack();
    }

    @FXML
    private void handleBack() {
        sceneNavigator.navigate(
                "/com/javafx/csit228capstone/account/security_privacy.fxml", cancelBtn, "/styles/account.css"
        );
    }
}