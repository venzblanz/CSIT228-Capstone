package com.javafx.csit228capstone.screens;

import com.javafx.csit228capstone.helper.MenuController;
import com.javafx.csit228capstone.model.User;
import com.javafx.csit228capstone.utils.AnimationHelper;
import com.javafx.csit228capstone.utils.SessionManager;
import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class DashboardController {
    @FXML private MenuController menuController;
    @FXML private Label dateLabel;
    @FXML private Label nameLabel;
    @FXML private Pane notification;
    @FXML private VBox dashboardScreen;

    private final LocalDate localDate = LocalDate.now();
    private final SessionManager sessionManager = SessionManager.getInstance();

    private User currentUser = sessionManager.getCurrentUser();

    @FXML
    public void initialize(){
        AnimationHelper.fadeIn(dashboardScreen);
        menuController.setActiveButton(menuController.getDashboardBtn());

        dateLabel.setText(localDate(localDate));
        nameLabel.setText(currentUser.getFullname());

        // for notification animation
        RotateTransition ring = new RotateTransition(Duration.millis(80), notification);
        ring.setByAngle(15);
        ring.setCycleCount(6);
        ring.setAutoReverse(true);

        notification.setOnMouseEntered(e -> ring.play());
    }
    private String localDate(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM dd");
        return date.format(formatter);
    }
}
