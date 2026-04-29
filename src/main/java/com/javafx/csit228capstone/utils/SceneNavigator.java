package com.javafx.csit228capstone.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.function.Consumer;

public class SceneNavigator {
    private static SceneNavigator instance;
    public SceneNavigator() {}

    public static SceneNavigator getInstance() {
        if(instance == null){
            instance = new SceneNavigator();
        }
        return instance;
    }
    public void navigate(String path, Node button, String css){
        navigate(path, button, css, null);
    }
    public <T> void navigate(String path, Node button, String css, Consumer<T> controllerInitialize) {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Stage stage = (Stage) button.getScene().getWindow();
            double width = stage.getWidth() - 16.00;
            double height = stage.getHeight() - 39.00;
            boolean wasMaximized = stage.isMaximized();

            Scene scene = new Scene(loader.load(), width, height);
            T controller = loader.getController();
            if(controllerInitialize != null){
                controllerInitialize.accept(controller);
            }

            scene.getStylesheets().add(getClass().getResource(css).toExternalForm());
            stage.setScene(scene);

            if (wasMaximized) {
                stage.setMaximized(true);
            }
            stage.setResizable(true);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
}
