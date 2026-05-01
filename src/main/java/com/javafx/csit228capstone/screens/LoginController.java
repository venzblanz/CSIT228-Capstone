package com.javafx.csit228capstone.screens;

import com.javafx.csit228capstone.model.User;
import com.javafx.csit228capstone.utils.SceneNavigator;
import com.javafx.csit228capstone.utils.SessionManager;
import com.javafx.csit228capstone.utils.UserDAO;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class LoginController {
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private Button loginBtn;
    @FXML private Hyperlink signupLink;
    @FXML private Label errorLabel;

    private final SceneNavigator sceneNavigator = SceneNavigator.getInstance();

    @FXML
    private void initialize() {
        loginBtn.setOnAction(e -> onLogin());
    }
    @FXML
    private void onLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        // Basic validation
        if (email.isEmpty() || password.isEmpty()) {
            showError("Please fill in all fields.");
            return;
        }

        User user = UserDAO.authenticate(email, password);

        if (user == null) {
            showError("Invalid email or password.");
            return;
        }

        // Save session
        SessionManager.getInstance().saveSession(user);

        // Navigate based on role
        if (user.getRole().equals("admin")) {
            sceneNavigator.navigate(
                    "/com/javafx/csit228capstone/admin_dashboard.fxml",
                    loginBtn,
                    "/styles/dashboard.css"
            );
        } else {
            sceneNavigator.navigate(
                    "/com/javafx/csit228capstone/dashboard.fxml",
                    loginBtn,
                    "/styles/dashboard.css"
            );
        }
    }
    @FXML
    private void onSignup() {
        sceneNavigator.navigate(
                "/com/javafx/csit228capstone/register.fxml",
                signupLink,
                "/styles/register.css"
        );
    }

    private void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
    }
}