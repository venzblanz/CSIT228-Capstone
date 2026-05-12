package com.javafx.csit228capstone.screens.admin;

import com.javafx.csit228capstone.model.User;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;

import java.util.List;
import java.util.Queue;

public class AdminManageQueueController {
    @FXML private HBox gwButton;
    @FXML private HBox hwButton;
    @FXML private HBox sfButton;
    @FXML private HBox dlButton;

    @FXML
    public void initialize(){
        gwButton.setOnMouseClicked(e->initializeTable("General Wellness"));
        gwButton.setOnMouseClicked(e->initializeTable("Women's Health"));
        gwButton.setOnMouseClicked(e->initializeTable("Specialized Fields"));
        gwButton.setOnMouseClicked(e->initializeTable("Diagnostics and Laboratory"));
    }

    private void initializeTable(String type){
        initializeCards();
    }

    private void initializeCards(){

    }
}
