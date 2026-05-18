package com.javafx.csit228capstone.screens.Account;

import com.javafx.csit228capstone.helper.MenuController;
import com.javafx.csit228capstone.utils.AnimationHelper;
import com.javafx.csit228capstone.utils.SceneNavigator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import java.net.URL;
import java.util.ResourceBundle;

public class AboutController implements Initializable {

    @FXML private MenuController menuController;
    @FXML private HBox backBtnRow;
    @FXML private Pane notification;
    private final SceneNavigator sceneNavigator = SceneNavigator.getInstance();
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (menuController != null) {
            menuController.setActiveButton(menuController.getAccountBtn());
        }
        backBtnRow.setOnMouseClicked(event -> onBack());
    }

    @FXML
    private void handleBack() {
        sceneNavigator.navigate(
                "/com/javafx/csit228capstone/account/myaccount.fxml", backBtnRow, "/styles/account.css"
        );
    }

    public void onBack(){
        handleBack();
    }
}