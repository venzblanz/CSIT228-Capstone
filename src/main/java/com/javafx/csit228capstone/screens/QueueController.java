package com.javafx.csit228capstone.screens;

import com.javafx.csit228capstone.helper.MenuController;
import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

public class QueueController {
    @FXML private MenuController menuController;
    @FXML private Pane notification;
    @FXML
    public void initialize() {
        menuController.setActiveButton(menuController.getQueueBtn());

        // for notification animation
        RotateTransition ring = new RotateTransition(Duration.millis(80), notification);
        ring.setByAngle(15);
        ring.setCycleCount(6);
        ring.setAutoReverse(true);

        notification.setOnMouseEntered(e -> ring.play());
    }
}
