package com.javafx.csit228capstone.screens.Account;

import com.javafx.csit228capstone.helper.MenuController;
import com.javafx.csit228capstone.model.User;
import com.javafx.csit228capstone.utils.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.time.LocalDate;
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
    @FXML private ComboBox<String> genderComboBox;
    @FXML private Pane notification;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        menuController.setActiveButton(menuController.getAccountBtn());
        AnimationHelper.ringAnimation(notification);

        User currentUser = SessionManager.getInstance().getCurrentUser();
        User latestData = UserDAO.getLatestUpdate(currentUser.getUserID());

        if (latestData != null) {
            fullNameField.setText(latestData.getFullname());
            phoneField.setText(latestData.getMobilenumber());
            addressField.setText(latestData.getAddress());

            if (latestData.getBirthday() != null && !latestData.getBirthday().equals("Not Set")) {
                LocalDate birthDate = LocalDate.parse(latestData.getBirthday());
                birthdayDatePicker.setValue(birthDate);
                int age = java.time.Period.between(birthDate, java.time.LocalDate.now()).getYears();
                ageField.setText(String.valueOf(age));
            }

            String savedGender = latestData.getGender();
            if (genderComboBox.getItems().contains(savedGender)) {
                genderComboBox.setValue(savedGender);
            }
            ImageUtils.loadUpdateImage(currentUser.getUserID(), profileImageView);
        } else {
            fullNameField.setText(currentUser.getFullname());
            phoneField.setText(currentUser.getMobilenumber());
            ImageUtils.loadProfileImage(currentUser.getUserID(), profileImageView);
        }

        emailField.setText(currentUser.getEmail());

        birthdayDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                int age = java.time.Period.between(newValue, java.time.LocalDate.now()).getYears();
                ageField.setText(String.valueOf(age));
            }
        });

        final User comparisonData = (latestData != null) ? latestData : currentUser;

        javafx.beans.binding.BooleanBinding noChanges = javafx.beans.binding.Bindings.createBooleanBinding(() -> {
                    boolean nameUnchanged = fullNameField.getText().trim().equals(comparisonData.getFullname());
                    boolean phoneUnchanged = phoneField.getText().trim().equals(comparisonData.getMobilenumber());
                    boolean addressUnchanged = addressField.getText().trim().equals(
                            (comparisonData.getAddress() == null) ? "" : comparisonData.getAddress()
                    );

                    LocalDate originalDate = (comparisonData.getBirthday() != null && !comparisonData.getBirthday().equals("Not Set"))
                            ? LocalDate.parse(comparisonData.getBirthday()) : null;
                    boolean birthdayUnchanged = java.util.Objects.equals(birthdayDatePicker.getValue(), originalDate);
                    boolean genderUnchanged = java.util.Objects.equals(genderComboBox.getValue(), comparisonData.getGender());

                    return nameUnchanged && phoneUnchanged && addressUnchanged && birthdayUnchanged && genderUnchanged;
                },
                fullNameField.textProperty(),
                phoneField.textProperty(),
                addressField.textProperty(),
                birthdayDatePicker.valueProperty(),
                genderComboBox.valueProperty());
        updateBtn.disableProperty().bind(noChanges);
    }

    @FXML
    private void handleChangePicture() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Profile Picture");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(profileImageView.getScene().getWindow());

        if (selectedFile != null) {
            // 1. Create the new image
            Image newImage = new Image(selectedFile.toURI().toString());

            // 2. Set it to the view
            profileImageView.setImage(newImage);

            // 3. IMPORTANT: Re-apply the rounding logic
            // This forces the "D-shape" fix to run on the new image dimensions
            ImageUtils.makeRounded(profileImageView);

            // 4. Save to SessionManager so it can be uploaded in handleUpdate
            SessionManager.getInstance().setPendingImageFile(selectedFile);
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
        User currentUser = SessionManager.getInstance().getCurrentUser();

        // Create a temporary User object with the NEW information
        User pendingUpdate = new User();
        pendingUpdate.setUserID(currentUser.getUserID());
        pendingUpdate.setFullname(fullNameField.getText());
        pendingUpdate.setMobilenumber(phoneField.getText());
        pendingUpdate.setAddress(addressField.getText());

        if (birthdayDatePicker.getValue() != null) {
            pendingUpdate.setBirthday(birthdayDatePicker.getValue().toString());
        }
        if (genderComboBox.getValue() != null) {
            pendingUpdate.setGender(genderComboBox.getValue());
        }

        // Save this to SessionManager so the ConfirmPasswordController can find it
        SessionManager.getInstance().setPendingUpdate(pendingUpdate);

        // Note: The image should be saved to SessionManager inside handleChangePicture()

        sceneNavigator.navigate(
                "/com/javafx/csit228capstone/account/confirmpassword.fxml",
                updateBtn,
                "/styles/account-edit.css"
        );
    }
}
