package com.javafx.csit228capstone.screens;

import com.javafx.csit228capstone.utils.SceneNavigator;
import com.javafx.csit228capstone.utils.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;

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

    private final SceneNavigator sceneNavigator = SceneNavigator.getInstance();

    @FXML
    private void initialize() {
        registerBtn.setOnAction(e -> onRegister());
        backBtn.setOnMouseClicked(e -> onBack());
    }
    @FXML
    private void onRegister() {
        String fullname = fullnameField.getText().trim();
        String mobile = mobilenumberField.getText().trim();
        String email = regEmailField.getText().trim();
        String password = regpassField.getText().trim();
        String confirmPassword = regpassField1.getText().trim();

        // Validation
        if (fullname.isEmpty() || mobile.isEmpty() || email.isEmpty()
                || password.isEmpty() || confirmPassword.isEmpty()) {
            showError("Please fill in all fields.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match.");
            return;
        }

        if (password.length() < 6) {
            showError("Password must be at least 6 characters.");
            return;
        }

        if (!termsCheckbox.isSelected()) {
            showError("Please agree to the Terms of Service.");
            return;
        }

        // Attempt registration
        int result = UserDAO.register(fullname, mobile, email, password);

        if (result == 1) {
            // Success — go back to login
            sceneNavigator.navigate(
                    "/com/javafx/csit228capstone/login.fxml",
                    registerBtn,
                    "/styles/login.css"
            );
        } else if (result == -2) {
            showError("Email already exists. Please use a different email.");
        } else {
            showError("Registration failed. Please try again.");
        }
    }

    private void onBack() {
        sceneNavigator.navigate(
                "/com/javafx/csit228capstone/login.fxml",
                registerBtn,
                "/styles/login.css"
        );
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}