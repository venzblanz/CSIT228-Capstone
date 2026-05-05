package com.javafx.csit228capstone.screens.Account;

import com.javafx.csit228capstone.helper.MenuController;
import com.javafx.csit228capstone.utils.SceneNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import java.net.URL;
import java.util.ResourceBundle;

public class ChangePasswordController implements Initializable {

    @FXML private MenuController menuController;
    @FXML private PasswordField currentPasswordField;
    @FXML private PasswordField newPasswordField;
    @FXML private PasswordField confirmNewPasswordField;
    @FXML private Button saveBtn;
    @FXML private Button cancelBtn;
    private final SceneNavigator sceneNavigator = SceneNavigator.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle rb) {

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
    private void onSave(){
        handleSavePassword();
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