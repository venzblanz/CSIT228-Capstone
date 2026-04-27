package com.javafx.csit228capstone.helper;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import java.io.IOException;

public class MenuController {
    @FXML
    private HBox profileBtn;
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
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/javafx/csit228capstone/queue.fxml"));
            Scene scene = new Scene(loader.load(), 1280, 800);
            scene.getStylesheets().add(getClass().getResource("/styles/queue.css").toExternalForm());
            Stage stage = (Stage) queueBtn.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public void goToDashboard(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/javafx/csit228capstone/dashboard.fxml"));
            Scene scene = new Scene(loader.load(), 1280, 800);
            scene.getStylesheets().add(getClass().getResource("/styles/dashboard.css").toExternalForm());
            Stage stage = (Stage) queueBtn.getScene().getWindow();
            stage.setScene(scene);
            stage.show();
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
