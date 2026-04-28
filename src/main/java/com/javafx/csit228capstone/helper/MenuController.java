package com.javafx.csit228capstone.helper;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {
    @FXML private VBox root;
    @FXML private HBox profileBtn;
    @FXML private Button dashboardBtn;
    @FXML private Button queueBtn;
    @FXML private Button scheduleBtn;
    @FXML private Button accountBtn;
    @FXML private HBox logoutBtn;

    public Button getQueueBtn() { return queueBtn; }
    public Button getDashboardBtn() { return dashboardBtn; }
    public Button getScheduleBtn() { return scheduleBtn; }
    public Button getAccountBtn() { return accountBtn; }

    @FXML
    public void initialize(){
        root.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                root.prefHeightProperty().bind(newScene.heightProperty());
            }
        });
        queueBtn.setOnAction(e -> onQueue());
        dashboardBtn.setOnAction(e -> onDashboard());
    }

    private void onQueue() {
        goToQueue();
    }
    private void onSchedule(){

    }
    private void onAccount(){

    }
    private void onDashboard(){
        goToDashboard();
    }

    public void goToQueue() {
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/javafx/csit228capstone/queue/queue.fxml"));
            Stage stage = (Stage) queueBtn.getScene().getWindow();
            System.out.println(stage.getWidth() + " | "  + stage.getHeight());

            double width = stage.getWidth() - 16.00;
            double height = stage.getHeight() - 39.00;
            boolean wasMaximized = stage.isMaximized();

            Scene scene = new Scene(loader.load(), width, height);
            scene.getStylesheets().add(getClass().getResource("/styles/queue.css").toExternalForm());
            stage.setScene(scene);
            if (wasMaximized) {
                stage.setMaximized(true);
            }
            stage.setResizable(true);
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public void goToDashboard(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/javafx/csit228capstone/dashboard.fxml"));
            Stage stage = (Stage) dashboardBtn.getScene().getWindow();
            System.out.println(stage.getWidth() + " | "  + stage.getHeight());

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
    public void setActiveButton(Button active) {
        // reset all first
        dashboardBtn.setStyle("");
        queueBtn.setStyle("");
        scheduleBtn.setStyle("");
        accountBtn.setStyle("");

        // highlight the active one
        active.setStyle("-fx-background-color: #218AD5; -fx-text-fill: white; -fx-style: bold;");
    }
}
