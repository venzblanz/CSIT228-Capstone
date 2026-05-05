package com.javafx.csit228capstone.screens.Account;

import com.javafx.csit228capstone.helper.MenuController;
import com.javafx.csit228capstone.utils.SceneNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import java.net.URL;
import java.util.ResourceBundle;

public class PasswordUpdatedController implements Initializable {

    @FXML private MenuController menuController;
    @FXML private Button backToSecurityBtn;
    private final SceneNavigator sceneNavigator = SceneNavigator.getInstance();

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Keeps the "My Account" sidebar button highlighted
        if (menuController != null) {
            menuController.setActiveButton(menuController.getAccountBtn());
        }
        backToSecurityBtn.setOnAction(event -> onHandleBack());
    }

    @FXML
    private void handleBackToSecurity() {
        sceneNavigator.navigate(
                "/com/javafx/csit228capstone/account/security_privacy.fxml", backToSecurityBtn, "/styles/account.css"
        );
    }
    private void onHandleBack() {
        handleBackToSecurity();
    }
}
