package com.javafx.csit228capstone;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;

public class LoginController {
    public Button loginBtn;
    public void onLogin() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("dashboard.fxml"));
        Scene scene = new Scene(loader.load(),1280,800);
        Stage stage = (Stage) loginBtn.getScene().getWindow();
        stage.setScene(scene);
    }

}
