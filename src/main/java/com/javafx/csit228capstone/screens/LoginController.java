package com.javafx.csit228capstone.screens;

import com.javafx.csit228capstone.utils.SceneNavigator;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    @FXML private Button loginBtn;
    @FXML private Pane backBtn;
    @FXML private AnchorPane loginPane;
    @FXML private AnchorPane registerPane;
    @FXML private Hyperlink signupLink;

    private boolean isSignup = false;

    private final SceneNavigator sceneNavigator = SceneNavigator.getInstance();

    public void onLogin(){
        sceneNavigator.navigate("/com/javafx/csit228capstone/dashboard.fxml", loginBtn, "/styles/dashboard.css");
    }
    public void onSignup() {
        isSignup = !isSignup;
        loginPane.setVisible(!isSignup);
        loginPane.setManaged(!isSignup);
        registerPane.setVisible(isSignup);
        registerPane.setManaged(isSignup);
    }
    @FXML private void initialize() {
        registerPane.setVisible(false);
        registerPane.setManaged(false);
        signupLink.setOnAction(e -> onSignup());
        backBtn.setOnMouseClicked(e -> onSignup());
        loginBtn.setOnAction(e -> onLogin());
    }
}
