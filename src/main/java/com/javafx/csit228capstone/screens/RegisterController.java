package com.javafx.csit228capstone.screens;

import com.javafx.csit228capstone.utils.SceneNavigator;
import com.javafx.csit228capstone.utils.UserDAO;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.mindrot.jbcrypt.BCrypt;

public class RegisterController {
    @FXML private TextField fullnameField;
    @FXML private TextField mobilenumberField;
    @FXML private TextField regEmailField;
    @FXML private PasswordField regpassField;
    @FXML private PasswordField regpassField1;
    @FXML private TextField regpassVisible;
    @FXML private TextField regpassVisible1;
    @FXML private CheckBox termsCheckbox;
    @FXML private Button registerBtn;
    @FXML private Pane backBtn;
    @FXML private Label errorLabel;
    @FXML private Hyperlink termsLink;

    private boolean passVisible  = false;
    private boolean passVisible1 = false;

    private final SceneNavigator sceneNavigator = SceneNavigator.getInstance();

    @FXML
    private void initialize() {
        registerBtn.setOnAction(e -> onRegister());
        backBtn.setOnMouseClicked(e -> onBack());
    }

    @FXML
    private void togglePass1() {
        passVisible = !passVisible;
        if (passVisible) {
            regpassVisible.setText(regpassField.getText());
            regpassVisible.setVisible(true);
            regpassVisible.setManaged(true);
            regpassField.setVisible(false);
            regpassField.setManaged(false);
        } else {
            regpassField.setText(regpassVisible.getText());
            regpassField.setVisible(true);
            regpassField.setManaged(true);
            regpassVisible.setVisible(false);
            regpassVisible.setManaged(false);
        }
    }

    @FXML
    private void togglePass2() {
        passVisible1 = !passVisible1;
        if (passVisible1) {
            regpassVisible1.setText(regpassField1.getText());
            regpassVisible1.setVisible(true);
            regpassVisible1.setManaged(true);
            regpassField1.setVisible(false);
            regpassField1.setManaged(false);
        } else {
            regpassField1.setText(regpassVisible1.getText());
            regpassField1.setVisible(true);
            regpassField1.setManaged(true);
            regpassVisible1.setVisible(false);
            regpassVisible1.setManaged(false);
        }
    }

    @FXML
    private void onRegister() {
        hideError();

        String fullname        = fullnameField.getText().trim();
        String mobile          = mobilenumberField.getText().trim();
        String email           = regEmailField.getText().trim();
        String password        = passVisible  ? regpassVisible.getText().trim()  : regpassField.getText().trim();
        String confirmPassword = passVisible1 ? regpassVisible1.getText().trim() : regpassField1.getText().trim();

        if (fullname.isEmpty() || mobile.isEmpty() || email.isEmpty()
                || password.isEmpty() || confirmPassword.isEmpty()) {
            showError("Please fill in all fields.");
            return;
        }

        if (fullname.length() < 2) {
            showError("Please enter your full name.");
            return;
        }

        if (!mobile.matches("^[0-9]{10}$")) {
            showError("Please enter a valid 10-digit mobile number (e.g. 9123456789).");
            return;
        }

        String mobileWithPrefix = "0" + mobile;

        if (!email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            showError("Please enter a valid email address.");
            return;
        }

        if (password.length() < 8) {
            showError("Password must be at least 8 characters.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match.");
            return;
        }

        if (!termsCheckbox.isSelected()) {
            showError("Please agree to the Terms of Service.");
            return;
        }

        String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

        int result;
        try {
            result = UserDAO.register(fullname, mobileWithPrefix, email, hashedPassword);
        } catch (Exception e) {
            showError("Unable to connect to the database. Please try again.");
            System.err.println("[RegisterController] Database error: " + e.getMessage());
            return;
        }

        if (result == 1) {
            try {
                FXMLLoader loader = new FXMLLoader(
                        getClass().getResource("/com/javafx/csit228capstone/register_success_popup.fxml"));
                StackPane root = loader.load();

                RegisterSuccessController controller = loader.getController();
                controller.setMainNode(registerBtn);

                Stage popup = new Stage();
                popup.initModality(Modality.APPLICATION_MODAL);
                popup.initStyle(StageStyle.TRANSPARENT);

                Stage mainStage = (Stage) registerBtn.getScene().getWindow();

                Scene scene = new Scene(root, mainStage.getWidth(), mainStage.getHeight());
                scene.setFill(javafx.scene.paint.Color.TRANSPARENT);

                popup.setScene(scene);

                popup.setX(mainStage.getX());
                popup.setY(mainStage.getY());

                popup.show();
            } catch (Exception e) {
                showError("Registration successful but failed to show popup.");
                System.err.println("[RegisterController] Popup error: " + e.getMessage());
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
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/javafx/csit228capstone/terms_popup.fxml"));
            StackPane root = loader.load();

            Stage popup = new Stage();
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.initStyle(StageStyle.TRANSPARENT);
            Scene scene = new Scene(root);
            scene.setFill(javafx.scene.paint.Color.TRANSPARENT);
            popup.setScene(scene);
            popup.show();
        } catch (Exception e) {
            System.err.println("[RegisterController] Failed to load terms popup: " + e.getMessage());
        }
    }
}