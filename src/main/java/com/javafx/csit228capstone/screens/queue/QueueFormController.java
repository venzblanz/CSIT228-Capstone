package com.javafx.csit228capstone.screens.queue;

import javafx.fxml.FXML;
import javafx.scene.control.ScrollPane;

public class QueueFormController {
    @FXML private ScrollPane scrollPane;
    @FXML
    public void initialize() {
//         Bind scroll pane height to scene height minus the top content
//        scrollPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
//            if (newScene != null) {
//                newScene.heightProperty().addListener((o, oldH, newH) -> {
//                    scrollPane.setPrefHeight(newH.doubleValue() - 420);
//                });
//            }
//        });
    }
}
