package com.javafx.csit228capstone.screens.Account;

import com.javafx.csit228capstone.helper.MenuController;
import com.javafx.csit228capstone.screens.NotificationPanelController;
import com.javafx.csit228capstone.utils.AnimationHelper;
import com.javafx.csit228capstone.utils.SceneNavigator;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

public class PasswordUpdatedController implements Initializable {

    @FXML private MenuController menuController;
    @FXML private Button backToSecurityBtn;
    private final SceneNavigator sceneNavigator = SceneNavigator.getInstance();
    @FXML private Pane notification;
    @FXML private StackPane updateRoot;
    private NotificationPanelController notifPanelCtrl;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        notifPanelCtrl = new  NotificationPanelController(updateRoot);
        AnimationHelper.ringAnimation(notification);
        notification.setOnMouseClicked(e -> notifPanelCtrl.openPanel());
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
