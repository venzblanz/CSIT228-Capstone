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

public class ConfirmPasswordController implements Initializable {

    @FXML private MenuController menuController;

    private final SceneNavigator sceneNavigator = SceneNavigator.getInstance();
    @FXML private Button backBtn;
    @FXML private Button confirmBtn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
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

    @FXML
    private void handleConfirm() {
        // Navigate to the success screen
        sceneNavigator.navigate(
                "/com/javafx/csit228capstone/account/profile_updated.fxml",
                confirmBtn,
                "/styles/account-edit.css"
        );
    }
}
