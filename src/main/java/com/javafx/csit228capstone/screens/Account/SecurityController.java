package com.javafx.csit228capstone.screens.Account;

import com.javafx.csit228capstone.helper.MenuController;
import com.javafx.csit228capstone.utils.SceneNavigator;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import java.net.URL;
import java.util.ResourceBundle;

public class SecurityController implements Initializable {

    @FXML private MenuController menuController;
    @FXML private HBox changePasswordRow;
    @FXML private HBox clearCacheRow;
    private final SceneNavigator sceneNavigator = SceneNavigator.getInstance();
    @FXML private Button backBtn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
            menuController.setActiveButton(menuController.getAccountBtn());
            changePasswordRow.setOnMouseClicked(e -> onChangePassword());
            backBtn.setOnAction(e-> onBack());
    }


    private void onChangePassword() {
        handleChangePassword();
    }

    private void onBack(){
        handleBack();
    }

    @FXML
    private void handleChangePassword() {
        sceneNavigator.navigate(
                "/com/javafx/csit228capstone/account/change_password.fxml", changePasswordRow, "/styles/account-edit.css"
        );
    }

    @FXML
    private void handleClearCache() {

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Security");
        alert.setHeaderText(null);
        alert.setContentText("Local cache has been cleared successfully.");
        alert.showAndWait();
    }

    @FXML
    private void handlePrivacyPolicy() {}

    @FXML
    private void handleBack() {
        sceneNavigator.navigate(
                "/com/javafx/csit228capstone/account/myaccount.fxml", changePasswordRow, "/styles/account.css"
        );

    }

}