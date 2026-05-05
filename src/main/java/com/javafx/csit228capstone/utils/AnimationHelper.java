package com.javafx.csit228capstone.utils;

import javafx.animation.FadeTransition;
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
}
