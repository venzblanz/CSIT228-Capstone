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
    @FXML private CheckBox rememberMeCheckbox;

    private final SessionManager sessionManager = SessionManager.getInstance();
    private final SceneNavigator sceneNavigator = SceneNavigator.getInstance();

    @FXML
    private void initialize() {
        loginBtn.setOnAction(e -> onLogin());
    }

    @FXML
    private void onLogin() {
        hideError();

        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        // Empty field validation
        if (email.isEmpty() || password.isEmpty()) {
            showError("Please fill in all fields.");
            return;
        }

        // Email format validation
        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            showError("Please enter a valid email address.");
            return;
        }

        // Authenticate
        User user;
        try {
            user = UserDAO.authenticate(email, password);
        } catch (Exception e) {
            showError("Unable to connect to the database. Please try again.");
            System.err.println("[LoginController] Database error: " + e.getMessage());
            return;
        }

        if (user == null) {
            showError("Invalid email or password.");
            return;
        }

        // Remember Me
        try {
            if (rememberMeCheckbox.isSelected()) {
                sessionManager.saveSession(user);
            } else {
                sessionManager.setCurrentUser(user);
            }
        } catch (Exception e) {
            showError("Failed to save session. Please try again.");
            System.err.println("[LoginController] Session error: " + e.getMessage());
            return;
        }

        // Navigate based on role
        try {
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
        } catch (Exception e) {
            showError("Failed to load dashboard. Please try again.");
            System.err.println("[LoginController] Navigation error: " + e.getMessage());
        }
    }

    @FXML
    private void onSignup() {
        try {
            sceneNavigator.navigate(
                    "/com/javafx/csit228capstone/register.fxml",
                    signupLink,
                    "/styles/register.css"
            );
        } catch (Exception e) {
            showError("Failed to load register page.");
            System.err.println("[LoginController] Navigation error: " + e.getMessage());
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

    public String getEmail() {
        return emailField.getText().trim();
    }
}