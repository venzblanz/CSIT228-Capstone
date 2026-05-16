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

public class ProfileUpdatedController implements Initializable {

    @FXML private MenuController menuController;
    @FXML private Button continueBtn;
    private final SceneNavigator sceneNavigator = SceneNavigator.getInstance();
    @FXML private Pane notification;
    @FXML private StackPane profileRoot;
    private NotificationPanelController notifPanelCtrl;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        notifPanelCtrl = new  NotificationPanelController(profileRoot);
        AnimationHelper.ringAnimation(notification);
        notification.setOnMouseClicked(e -> notifPanelCtrl.openPanel());
        if (menuController != null) {
            menuController.setActiveButton(menuController.getAccountBtn());
        }
    }

    @FXML
    private void handleContinue(ActionEvent event) {
        sceneNavigator.navigate(
                "/com/javafx/csit228capstone/account/view_profile.fxml",
                continueBtn,
                "/styles/account.css"
        );
    }
}