package com.javafx.csit228capstone.screens.Account;

import com.javafx.csit228capstone.helper.MenuController;
import com.javafx.csit228capstone.model.User;
import com.javafx.csit228capstone.screens.NotificationPanelController;
import com.javafx.csit228capstone.utils.*;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

public class ViewProfileController implements Initializable {

    @FXML private Label nameLabel;
    @FXML private Label emailLabel;
    @FXML private Label phoneLabel;
    @FXML private Label birthdayLabel;
    @FXML private Label genderLabel;
    @FXML private Label addressLabel;
    @FXML private Label ageLabel;
    @FXML private ImageView profileImageView;
    @FXML private Button editProfileBtn;
    @FXML private MenuController menuController;
    @FXML private Label profileNameHeader;
    @FXML private Label profileIdHeader;
    @FXML private Pane notification;
    @FXML private StackPane viewRoot;
    private NotificationPanelController notifPanelCtrl;
    private final SceneNavigator sceneNavigator = SceneNavigator.getInstance();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        notifPanelCtrl = new  NotificationPanelController(viewRoot);
        AnimationHelper.ringAnimation(notification);
        notification.setOnMouseClicked(e -> notifPanelCtrl.openPanel());
        User currentUser = SessionManager.getInstance().getCurrentUser();

        if (currentUser != null) {
            User pendingData = UserDAO.getLatestUpdate(currentUser.getUserID());

            if (pendingData != null) {
                displayUser(pendingData);
                emailLabel.setText(currentUser.getEmail());

                ImageUtils.loadUpdateImage(currentUser.getUserID(), profileImageView);
            } else {
                displayUser(currentUser);
                ImageUtils.loadProfileImage(currentUser.getUserID(), profileImageView);
            }
        }
        editProfileBtn.setOnAction(e -> SceneNavigator.getInstance().navigate(
                "/com/javafx/csit228capstone/account/edit_profile.fxml",
                editProfileBtn,
                "/styles/account-edit.css"
        ));
    }

    private void displayUser(User user) {
        nameLabel.setText(user.getFullname());
        phoneLabel.setText(user.getMobilenumber());
        String rawBirthday = user.getBirthday();
        if (rawBirthday != null && !rawBirthday.equals("Not Set") && !rawBirthday.isEmpty()) {
            try {
                LocalDate birthDate = LocalDate.parse(rawBirthday);

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd-yyyy");
                birthdayLabel.setText(birthDate.format(formatter));
            } catch (Exception e) {
                birthdayLabel.setText(rawBirthday);
            }
        } else {
            birthdayLabel.setText("Not Set");
        }
        genderLabel.setText(user.getGender());
        addressLabel.setText(user.getAddress());
        ageLabel.setText(user.getAge());
        profileNameHeader.setText(user.getFullname());

        String formattedId = PatientIdGenerator.getPatientId(user.getUserID());
        profileIdHeader.setText("Patient ID: " + formattedId);
    }
    private void loadProfileData() {
        User currentUser = SessionManager.getInstance().getCurrentUser();
        if (currentUser == null) return;
        User pendingData = UserDAO.getLatestUpdate(currentUser.getUserID());

        if (pendingData != null) {
            displayUser(pendingData);
            ImageUtils.loadUpdateImage(currentUser.getUserID(), profileImageView);
        } else {
            displayUser(currentUser);
            ImageUtils.loadProfileImage(currentUser.getUserID(), profileImageView);
        }
    }


    @FXML
    private void handleEditProfile() {
        sceneNavigator.navigate(
                "/com/javafx/csit228capstone/account/edit_profile.fxml",
                editProfileBtn,
                "/styles/account-edit.css"
        );
    }
    @FXML
    private void handleBack() {
        SceneNavigator.getInstance().navigate(
                "/com/javafx/csit228capstone/account/myaccount.fxml",
                nameLabel,
                "/styles/account.css"
        );
    }
}