package com.javafx.csit228capstone.screens.queue;

import com.javafx.csit228capstone.helper.MenuController;
import com.javafx.csit228capstone.utils.SceneNavigator;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
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
    @FXML private Label gwLabel;
    @FXML private Label whLabel;
    @FXML private Label sfLabel;
    @FXML private Label dlLabel;

    private final SceneNavigator sceneNavigator = SceneNavigator.getInstance();

    @FXML
    public void initialize() {
        menuController.setActiveButton(menuController.getQueueBtn());
        gwBtn.setOnMouseClicked(e -> onForm("General Wellness"));
        whBtn.setOnMouseClicked(e -> onForm("Women's Health"));
        sfBtn.setOnMouseClicked(e -> onForm("Specialized Fields"));
        dlBtn.setOnMouseClicked(e -> onForm("Diagnostics and Laboratory"));

        generateLayout();

        // for notification animation
        RotateTransition ring = new RotateTransition(Duration.millis(80), notification);
        ring.setByAngle(15);
        ring.setCycleCount(6);
        ring.setAutoReverse(true);

        notification.setOnMouseEntered(e -> ring.play());
    }
    private void generateLayout(){
        initializeCards(gwBtn);
        initializeCards(whBtn);
        initializeCards(sfBtn);
        initializeCards(dlBtn);
    }
    private void initializeCards(VBox btn) {
        FadeTransition fade = new FadeTransition(Duration.millis(1000), btn);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();

        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(150), btn);
        scaleUp.setToX(1.05); scaleUp.setToY(1.05);
        scaleUp.setInterpolator(Interpolator.EASE_OUT);

        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(150), btn);
        scaleDown.setToX(1.0); scaleDown.setToY(1.0);
        scaleDown.setInterpolator(Interpolator.EASE_IN);

        btn.setOnMouseEntered(e -> { scaleUp.playFromStart(); });
        btn.setOnMouseExited(e -> { scaleDown.playFromStart(); });
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
