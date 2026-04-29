package com.javafx.csit228capstone.screens.queue;

import com.javafx.csit228capstone.helper.MenuController;
import com.javafx.csit228capstone.utils.SceneNavigator;
import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class QueueController {
    @FXML private MenuController menuController;
    @FXML private Pane notification;
    @FXML private VBox gwBtn;
    @FXML private VBox whBtn;
    @FXML private VBox sfBtn;
    @FXML private VBox dlBtn;

    private final SceneNavigator sceneNavigator = SceneNavigator.getInstance();

    @FXML
    public void initialize() {
        menuController.setActiveButton(menuController.getQueueBtn());
        gwBtn.setOnMouseClicked(e -> onForm("General Wellness"));
        whBtn.setOnMouseClicked(e -> onForm("Women's Health"));
        sfBtn.setOnMouseClicked(e -> onForm("Specialized Fields"));
        dlBtn.setOnMouseClicked(e -> onForm("Diagnostics and Laboratory"));
        // for notification animation
        RotateTransition ring = new RotateTransition(Duration.millis(80), notification);
        ring.setByAngle(15);
        ring.setCycleCount(6);
        ring.setAutoReverse(true);

        notification.setOnMouseEntered(e -> ring.play());
    }
    private void onForm(String type){ goToForm(type); }
    private void goToForm(String type){
        sceneNavigator.navigate(
                "/com/javafx/csit228capstone/queue/queue-form.fxml",
                gwBtn,
                "/styles/queue-form.css",
                (QueueFormController queueFormController) -> queueFormController.initializeData(type)
        );
    }
}
