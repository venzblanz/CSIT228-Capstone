package com.javafx.csit228capstone.screens;

import com.javafx.csit228capstone.utils.SceneNavigator;
import com.javafx.csit228capstone.utils.UserDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class RegisterController {
    @FXML private TextField fullnameField;
    @FXML private TextField mobilenumberField;
    @FXML private TextField regEmailField;
    @FXML private PasswordField regpassField;
    @FXML private PasswordField regpassField1;
    @FXML private CheckBox termsCheckbox;
    @FXML private Button registerBtn;
    @FXML private Pane backBtn;
    @FXML private Label errorLabel;
    @FXML private Hyperlink termsLink;
    private final SceneNavigator sceneNavigator = SceneNavigator.getInstance();

    @FXML
    private void initialize() {
        registerBtn.setOnAction(e -> onRegister());
        backBtn.setOnMouseClicked(e -> onBack());
    }

    @FXML
    private void onRegister() {
        hideError();

        String fullname = fullnameField.getText().trim();
        String mobile = mobilenumberField.getText().trim();
        String email = regEmailField.getText().trim();
        String password = regpassField.getText().trim();
        String confirmPassword = regpassField1.getText().trim();

        // Empty field validation
        if (fullname.isEmpty() || mobile.isEmpty() || email.isEmpty()
                || password.isEmpty() || confirmPassword.isEmpty()) {
            showError("Please fill in all fields.");
            return;
        }

        // Full name validation
        if (fullname.length() < 2) {
            showError("Please enter your full name.");
            return;
        }

        // Mobile number validation — 10 digits without leading 0
        if (!mobile.matches("^[0-9]{10}$")) {
            showError("Please enter a valid 10-digit mobile number (e.g. 9123456789).");
            return;
        }

        // Prepend 0 — so 9123456789 becomes 09123456789
        String mobileWithPrefix = "0" + mobile;

        // Email format validation
        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            showError("Please enter a valid email address.");
            return;
        }

        // Password validations
        if (password.length() < 6) {
            showError("Password must be at least 6 characters.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match.");
            return;
        }

        // Terms validation
        if (!termsCheckbox.isSelected()) {
            showError("Please agree to the Terms of Service.");
            return;
        }

        // Attempt registration
        int result;
        try {
            result = UserDAO.register(fullname, mobileWithPrefix, email, password);
        } catch (Exception e) {
            showError("Unable to connect to the database. Please try again.");
            System.err.println("[RegisterController] Database error: " + e.getMessage());
            return;
        }

        if (result == 1) {
            try {
                sceneNavigator.navigate(
                        "/com/javafx/csit228capstone/login.fxml",
                        registerBtn,
                        "/styles/login.css"
                );
            } catch (Exception e) {
                showError("Registration successful but failed to load login page.");
                System.err.println("[RegisterController] Navigation error: " + e.getMessage());
            }
        } else if (result == -2) {
            showError("Email already exists. Please use a different email.");
        } else {
            showError("Registration failed. Please try again.");
        }
    }

    private void onBack() {
        try {
            sceneNavigator.navigate(
                    "/com/javafx/csit228capstone/login.fxml",
                    registerBtn,
                    "/styles/login.css"
            );
        } catch (Exception e) {
            showError("Failed to load login page.");
            System.err.println("[RegisterController] Navigation error: " + e.getMessage());
        }
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }

    private void hideError() {
        errorLabel.setText("");
        errorLabel.setVisible(false);
    }

    @FXML
    private void onTermsClicked() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(
                    "/com/javafx/csit228capstone/terms_popup.fxml"));
            VBox content = loader.load();

            Stage popupStage = new Stage();
            popupStage.initModality(Modality.APPLICATION_MODAL);
            popupStage.initStyle(StageStyle.UNDECORATED);
            popupStage.setTitle("Terms of Service");
            popupStage.setScene(new Scene(content));
            popupStage.setWidth(600);
            popupStage.setHeight(800);
            popupStage.centerOnScreen();


            popupStage.showAndWait();
        } catch (Exception e) {
            System.err.println("[RegisterController] Failed to load terms popup: " + e.getMessage());
        }
    }
}