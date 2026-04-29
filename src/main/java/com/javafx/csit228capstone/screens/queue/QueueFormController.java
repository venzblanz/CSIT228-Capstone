package com.javafx.csit228capstone.screens.queue;

import com.javafx.csit228capstone.utils.SceneNavigator;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class QueueFormController {
    @FXML private ScrollPane scrollPane;
    @FXML private Button cancelBtn;
    @FXML private Button nextBtn;
    @FXML private RadioButton maleRadio;
    @FXML private RadioButton femaleRadio;
    @FXML private RadioButton regularRadio;
    @FXML private RadioButton pwdRadio;
    @FXML private RadioButton seniorRadio;
    @FXML private RadioButton pregnantRadio;
    @FXML private ComboBox<String> purposeCombo;
    @FXML private ImageView backIconBtn;
    @FXML private Label backBtn;
    @FXML private Label typeLabel;
    @FXML private VBox form;

    private String formType;

    private final SceneNavigator sceneNavigator = SceneNavigator.getInstance();
    public void initializeData(String type){
        formType = type;
        if(type.equals("General Wellness")){
            typeLabel.setText("General Wellness");
        }else if (type.equals("Women's Health")){
            typeLabel.setText("Women's Health");
        }else if (type.equals("Specialized Fields")){
            typeLabel.setText("Specialized Fields");
        }else{
            typeLabel.setText("Diagnostics and Laboratory");
        }
    }
    @FXML
    public void initialize() {
//         Bind scroll pane height to scene height minus the top content
//         Use this only when the layout is broken after using Scroll Pane
//        scrollPane.sceneProperty().addListener((obs, oldScene, newScene) -> {
//            if (newScene != null) {
//                newScene.heightProperty().addListener((o, oldH, newH) -> {
//                    scrollPane.setPrefHeight(newH.doubleValue() - 420);
//                });
//            }
//        });
        backIconBtn.setOnMouseClicked(e -> onBack());
        backBtn.setOnMouseClicked(e -> onBack());
        cancelBtn.setOnAction(e -> onCancel());
        nextBtn.setOnAction(e -> onNext());
    }

    private void onBack(){
        sceneNavigator.navigate("/com/javafx/csit228capstone/queue/queue.fxml", cancelBtn, "/styles/queue.css");
    }
    private void onCancel(){
        sceneNavigator.navigate("/com/javafx/csit228capstone/dashboard.fxml", cancelBtn, "/styles/dashboard.css");
    }
    private void onNext(){
        sceneNavigator.navigate("/com/javafx/csit228capstone/queue/queue-schedule.fxml", cancelBtn, "/styles/queue-schedule.css", (QueueScheduleController queueScheduleController) -> queueScheduleController.initializeData(formType));
    }
}
