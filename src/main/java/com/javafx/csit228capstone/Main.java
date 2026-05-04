package com.javafx.csit228capstone;
import com.javafx.csit228capstone.model.User;
import com.javafx.csit228capstone.utils.SessionManager;
import javafx.scene.ImageCursor;
import javafx.scene.text.Font;
import javafx.stage.StageStyle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

public class Main extends Application {
    private final SessionManager sessionManager = SessionManager.getInstance();
    @Override
    public void start(Stage stage) throws IOException {
        /*
            stage.initStyle(StageStyle.UNDECORATED); ug ganahan mog full screen nga
            unadjustable no border no nothing just full screen
            well we can design our own top bar if we want to, sa last part nana HAHHAHAHAH
        */

        // for FONT
        Font.loadFont(getClass().getResourceAsStream("/fonts/InstrumentSans-Regular.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream("/fonts/InstrumentSans-Medium.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream("/fonts/InstrumentSans-SemiBold.ttf"), 12);
        Font.loadFont(getClass().getResourceAsStream("/fonts/InstrumentSans-Bold.ttf"), 12);

        User restoredUser = sessionManager.restoreSession();
        if(restoredUser != null){
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/javafx/csit228capstone/dashboard.fxml"));

            Scene scene = new Scene(fxmlLoader.load(),  1280, 800);
            scene.getStylesheets().add(getClass().getResource("/styles/dashboard.css").toExternalForm());
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/medserveLogo.png")));
            stage.setScene(scene);
        }else{
            FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource("/com/javafx/csit228capstone/login.fxml"));

            Scene scene = new Scene(fxmlLoader.load(),  1280, 800);
            scene.getStylesheets().add(getClass().getResource("/styles/login.css").toExternalForm());
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/medserveLogo.png")));
            stage.setScene(scene);
        }
        stage.setTitle("MedServe");
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