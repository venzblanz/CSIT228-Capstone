package com.javafx.csit228capstone.screens;

import javafx.fxml.FXML;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class TermsPopupController {
    @FXML private HBox backBtn;

    @FXML
    private void initialize() {
        backBtn.setOnMouseClicked(e -> onBack());
    }

    @FXML
    private void onBack() {
        Stage stage = (Stage) backBtn.getScene().getWindow();
        stage.close();
    }
}