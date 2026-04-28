package com.javafx.csit228capstone.screens.queue;

import com.javafx.csit228capstone.helper.MenuController;
import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

public class QueueController {
    @FXML private MenuController menuController;
    @FXML private Pane notification;
    @FXML private VBox gwBtn;
    @FXML
    public void initialize() {
        menuController.setActiveButton(menuController.getQueueBtn());
        gwBtn.setOnMouseClicked(e -> onGeneral());
        // for notification animation
        RotateTransition ring = new RotateTransition(Duration.millis(80), notification);
        ring.setByAngle(15);
        ring.setCycleCount(6);
        ring.setAutoReverse(true);

        notification.setOnMouseEntered(e -> ring.play());
    }
    private void onGeneral(){ goToGeneral(); }
    private void goToGeneral(){
        try{
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/com/javafx/csit228capstone/queue/queue-form.fxml"));
            Scene scene = new Scene(loader.load(),1280,800);
            scene.getStylesheets().add(getClass().getResource("/styles/queue-form.css").toExternalForm());
            Stage stage = (Stage) gwBtn.getScene().getWindow();
            stage.setScene(scene);
            stage.setMinWidth(1024);
            stage.setMinHeight(700);
            stage.show();
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
