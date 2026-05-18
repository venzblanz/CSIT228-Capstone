package com.javafx.csit228capstone.utils;

import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.TranslateTransition;
import javafx.scene.Node;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class AnimationHelper {
    public static FadeTransition fadeIn1(Node node){
        FadeTransition ft = new FadeTransition(Duration.millis(300), node);
        ft.setFromValue(0);
        ft.setToValue(1);
        ft.play();

        return ft;
    }
    public static void fadeIn(Node node){
        // Fade in Transition para dili tikig
        FadeTransition fade = new FadeTransition(Duration.millis(600), node);
        fade.setFromValue(0);
        fade.setToValue(1);
        fade.play();
    }
    public static void staggerFadeIn(VBox container) {
        int delay = 0;

        for (Node node : container.getChildren()) {

            node.setOpacity(0);
            node.setTranslateY(15);

            FadeTransition fade = new FadeTransition(Duration.millis(300), node);
            fade.setFromValue(0);
            fade.setToValue(1);

            TranslateTransition slide = new TranslateTransition(Duration.millis(300), node);
            slide.setFromY(15);
            slide.setToY(0);

            ParallelTransition animation =
                    new ParallelTransition(fade, slide);

            animation.setDelay(Duration.millis(delay));
            animation.play();

            delay += 70;
        }
    }
    public static void ringAnimation(Node node){
        RotateTransition ring = new RotateTransition(Duration.millis(80), node);
        ring.setByAngle(15);
        ring.setCycleCount(6);
        ring.setAutoReverse(true);
        node.setOnMouseEntered(e -> ring.play());
    }
}
