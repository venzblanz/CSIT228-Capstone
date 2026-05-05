package com.javafx.csit228capstone.utils;

import javafx.animation.FadeTransition;
import javafx.animation.RotateTransition;
import javafx.scene.Node;
import javafx.util.Duration;

public class AnimationHelper {
    public static void fadeIn(Node node){
        // Fade in Transition para dili tikig
        FadeTransition fade = new FadeTransition(Duration.millis(600), node);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }
    public static void ringAnimation(Node node){
        RotateTransition ring = new RotateTransition(Duration.millis(80), node);
        ring.setByAngle(15);
        ring.setCycleCount(6);
        ring.setAutoReverse(true);
        node.setOnMouseEntered(e -> ring.play());
    }
}
