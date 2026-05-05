package com.javafx.csit228capstone;
import javafx.stage.StageStyle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/javafx/csit228capstone/login.fxml"));

        Scene scene = new Scene(fxmlLoader.load(),  1280, 800);
        scene.getStylesheets().add(getClass().getResource("/styles/login.css").toExternalForm());
        stage.setTitle("MedServe");
        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/medserveLogo.png")));
//        stage.initStyle(StageStyle.UNDECORATED);
        stage.setScene(scene);
        stage.setMinHeight(700);
        stage.setMinWidth(1024);
        stage.setMaximized(true);
        stage.setResizable(true);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
