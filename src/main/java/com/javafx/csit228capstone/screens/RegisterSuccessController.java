package com.javafx.csit228capstone.screens;

import com.javafx.csit228capstone.utils.SceneNavigator;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.stage.Stage;

public class RegisterSuccessController {

    @FXML private Button continueBtn;
    private Node mainNode;  // node from the main window's scene

    // Called by RegisterController before popup is shown
    public void setMainNode(Node node) {
        this.mainNode = node;
    }

    @FXML
    private void onContinue() {
        // Close the popup first
        Stage stage = (Stage) continueBtn.getScene().getWindow();
        stage.close();

        // Navigate using the main window's node
        SceneNavigator.getInstance().navigate(
                "/com/javafx/csit228capstone/login.fxml",
                mainNode, "/styles/login.css");
    }
}