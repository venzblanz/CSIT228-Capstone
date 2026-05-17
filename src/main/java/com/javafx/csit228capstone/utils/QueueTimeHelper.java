package com.javafx.csit228capstone.utils;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.control.Label;
import javafx.util.Duration;

import java.time.LocalDateTime;

public class QueueTimeHelper {

    public static int getExpectedMinutes(int position) {
        return 5 + ((position - 1) * 6);
    }

    public static Timeline startCountdown(Label label, LocalDateTime createdAt, int position, boolean withWaitText) {
        int expectedMinutes = getExpectedMinutes(position);
        LocalDateTime expectedTime = createdAt.plusMinutes(expectedMinutes);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    java.time.Duration remaining = java.time.Duration.between(
                            LocalDateTime.now(),
                            expectedTime
                    );

                    long totalSeconds = remaining.getSeconds();

                    if (totalSeconds <= 0) {
                        label.setText(withWaitText ? "Ready soon" : "Ready");
                        return;
                    }

                    long minutes = totalSeconds / 60;
                    long seconds = totalSeconds % 60;

                    if (withWaitText) {
                        label.setText(minutes + "m " + seconds + "s wait");
                    } else {
                        label.setText(minutes + "m " + seconds + "s");
                    }
                })
        );

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        return timeline;
    }

    public static Timeline startCountdownFromNow(Label label, int position, boolean withWaitText) {
        int expectedMinutes = getExpectedMinutes(position);
        LocalDateTime expectedTime = LocalDateTime.now().plusMinutes(expectedMinutes);

        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> {
                    java.time.Duration remaining = java.time.Duration.between(
                            LocalDateTime.now(),
                            expectedTime
                    );

                    long totalSeconds = remaining.getSeconds();

                    if (totalSeconds <= 0) {
                        label.setText(withWaitText ? "Ready soon" : "Ready");
                        return;
                    }

                    long minutes = totalSeconds / 60;
                    long seconds = totalSeconds % 60;

                    if (withWaitText) {
                        label.setText(minutes + "m " + seconds + "s wait");
                    } else {
                        label.setText(minutes + "m " + seconds + "s");
                    }
                })
        );

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();

        return timeline;
    }

    public static String getExpectedWaitingText(int position) {
        int expectedMinutes = getExpectedMinutes(position);
        return expectedMinutes + " minutes";
    }
}