package com.javafx.csit228capstone.screens.Account;

import com.javafx.csit228capstone.helper.MenuController;
import com.javafx.csit228capstone.utils.SceneNavigator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class EditProfileController implements Initializable {

    @FXML private TextField fullNameField;
    @FXML private TextField birthdayField;
    @FXML private TextField ageField;
    @FXML private TextField addressField;
    @FXML private TextField phoneField;
    @FXML private TextField emailField;
    @FXML private ImageView profileImageView;
    @FXML private MenuController menuController;
    private final SceneNavigator sceneNavigator = SceneNavigator.getInstance();
    @FXML private Button cancelBtn;
    @FXML private Button updateBtn;
    @FXML private DatePicker birthdayDatePicker;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menuController.setActiveButton(menuController.getAccountBtn());

        birthdayDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                int age = java.time.Period.between(newValue, java.time.LocalDate.now()).getYears();
                ageField.setText(String.valueOf(age));
            }
        });
        cancelBtn.setOnAction(e -> onCancel());
        updateBtn.setOnAction(e -> onUpdate());
    }

    @FXML
    private void handleChangePicture() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Picture");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );
        Stage stage = (Stage) profileImageView.getScene().getWindow();
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            profileImageView.setImage(new Image(selectedFile.toURI().toString()));
        }
    }
    private void onCancel() {
       handleCancel();

    }
    private void onUpdate(){
        handleUpdate();
    }

    @FXML
    private void handleCancel() {
        sceneNavigator.navigate(
                "/com/javafx/csit228capstone/account/myaccount.fxml", cancelBtn, "/styles/account.css"
        );
    }

    @FXML
    private void handleUpdate() {
        sceneNavigator.navigate(
                "/com/javafx/csit228capstone/account/confirmpassword.fxml", updateBtn, "/styles/account-edit.css"
        );
    }
}
