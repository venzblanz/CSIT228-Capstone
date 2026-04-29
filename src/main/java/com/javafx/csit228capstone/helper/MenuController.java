package com.javafx.csit228capstone.helper;

import com.javafx.csit228capstone.utils.SceneNavigator;
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

    private final SceneNavigator sceneNavigator = SceneNavigator.getInstance();

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
        sceneNavigator.navigate("/com/javafx/csit228capstone/queue/queue.fxml", queueBtn,"/styles/queue.css");
    }
    public void goToDashboard(){
        sceneNavigator.navigate("/com/javafx/csit228capstone/dashboard.fxml", queueBtn,"/styles/dashboard.css");
    }
    public void setActiveButton(Button active) {
        dashboardBtn.setStyle("");
        queueBtn.setStyle("");
        scheduleBtn.setStyle("");
        accountBtn.setStyle("");

        active.setStyle("-fx-background-color: #218AD5; -fx-text-fill: white; -fx-style: bold;");
    }
}
