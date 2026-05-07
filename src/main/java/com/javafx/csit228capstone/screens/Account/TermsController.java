package com.javafx.csit228capstone.screens.Account;

import com.javafx.csit228capstone.helper.MenuController;
import com.javafx.csit228capstone.utils.AnimationHelper;
import com.javafx.csit228capstone.utils.SceneNavigator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class TermsController implements Initializable {

    @FXML private MenuController menuController;

    @FXML private HBox backBtnRow;
    private final SceneNavigator sceneNavigator = SceneNavigator.getInstance();

    @FXML private CheckBox agreeCheckBox;
    @FXML private Button confirmBtn;
    @FXML private Pane notification;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        AnimationHelper.ringAnimation(notification);
        if (menuController != null) {
            menuController.setActiveButton(menuController.getAccountBtn());
        }

        confirmBtn.disableProperty().bind(agreeCheckBox.selectedProperty().not());
        confirmBtn.setOnAction(event -> onBack());
    }

    @FXML
    private void handleBack() {
        sceneNavigator.navigate(
                "/com/javafx/csit228capstone/account/myaccount.fxml", confirmBtn, "/styles/account.css"
        );
    }

    private void onBack(){
        handleBack();
    }
}