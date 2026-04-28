package com.javafx.csit228capstone.screens;

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
    @FXML public Button loginBtn;
    @FXML public Pane backBtn;
    @FXML public AnchorPane loginPane;
    @FXML public AnchorPane registerPane;
    @FXML public Hyperlink signupLink;
    public boolean isSignup = false;

    public void onLogin(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/javafx/csit228capstone/dashboard.fxml"));
            Stage stage = (Stage) loginBtn.getScene().getWindow();

            double width = stage.getWidth() - 16.00;
            double height = stage.getHeight() - 39.00;
            boolean wasMaximized = stage.isMaximized();

            Scene scene = new Scene(loader.load(), width, height);
            scene.getStylesheets().add(getClass().getResource("/styles/dashboard.css").toExternalForm());
            stage.setScene(scene);
            if (wasMaximized) {
                stage.setMaximized(true);
            }
            stage.setResizable(true);
        }catch (IOException e){
            e.printStackTrace();
        }

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
