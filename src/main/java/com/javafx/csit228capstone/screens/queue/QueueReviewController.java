package com.javafx.csit228capstone.screens.queue;

import com.javafx.csit228capstone.utils.SceneNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class QueueReviewController {
    @FXML
    private ScrollPane scrollPane;
    @FXML private Button editBtn;
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

    private final SceneNavigator sceneNavigator = SceneNavigator.getInstance();
    private String formType;

    public void initializeData(String type){
        formType = type;
        if(type.equals("General Wellness")){
            backBtn.setText("Back to General Wellness Schedule");
        }else if (type.equals("Women's Health")){
            backBtn.setText("Back to Women's Health Schedule");
        }else if (type.equals("Specialized Fields")){
            backBtn.setText("Back to Specialized Fields Schedule");
        }else{
            backBtn.setText("Back to Diagnostics and Laboratory Schedule");
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
        editBtn.setOnAction(e -> onEdit());
        nextBtn.setOnAction(e -> onNext());
    }

    private void onBack(){
        sceneNavigator.navigate("/com/javafx/csit228capstone/queue/queue-schedule.fxml", backBtn, "/styles/queue-schedule.css", (QueueScheduleController queueScheduleController) -> queueScheduleController.initializeData(formType));
    }
    private void onEdit(){
        sceneNavigator.navigate("/com/javafx/csit228capstone/queue/queue-form.fxml", editBtn, "/styles/queue-form.css", (QueueFormController queueFormController) -> queueFormController.initializeData(formType));
    }
    private void onNext(){
        sceneNavigator.navigate("/com/javafx/csit228capstone/queue/queue-number.fxml", nextBtn, "/styles/queue-number.css", (QueueNumberController queueNumberController) -> queueNumberController.initializeData(formType));
    }
}
