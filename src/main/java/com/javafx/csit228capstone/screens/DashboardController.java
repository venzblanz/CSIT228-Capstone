package com.javafx.csit228capstone.screens;

import com.javafx.csit228capstone.helper.MenuController;
import com.javafx.csit228capstone.model.QueueLine;
import com.javafx.csit228capstone.model.QueueTicket;
import com.javafx.csit228capstone.model.User;
import com.javafx.csit228capstone.utils.AnimationHelper;
import com.javafx.csit228capstone.utils.PatientIdGenerator;
import com.javafx.csit228capstone.utils.QueueLineDAO;
import com.javafx.csit228capstone.utils.SessionManager;
import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class DashboardController {
    @FXML private MenuController menuController;
    @FXML private Label dateLabel;
    @FXML private Label nameLabel;
    @FXML private Pane notification;
    @FXML private VBox dashboardScreen;
    // Recents
    @FXML private VBox recentQueueContainer;

    private final LocalDate localDate = LocalDate.now();
    private final SessionManager sessionManager = SessionManager.getInstance();

    private User currentUser = sessionManager.getCurrentUser();
    private final List<QueueTicket> queueList = QueueLineDAO.getRecentQueue(sessionManager.getUserId());

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
        setUpRecent(queueList);
    }
    private void setUpRecent(List<QueueTicket> queueList) {
        recentQueueContainer.getChildren().clear();

        if (queueList == null || queueList.isEmpty()) {
            Label emptyLabel = new Label("No recent activity yet.");
            emptyLabel.getStyleClass().add("recent");
            recentQueueContainer.getChildren().add(emptyLabel);
            return;
        }

        int limit = Math.min(queueList.size(), 2);

        for (int i = 0; i < limit; i++) {
            QueueTicket queueTicket = queueList.get(i);
            HBox card = createRecentCard(queueTicket);
            recentQueueContainer.getChildren().add(card);
        }
    }
    private HBox createRecentCard(QueueTicket queueTicket) {
        Label title = new Label("Queued: " + queueTicket.getDepartment());
        title.getStyleClass().add("recent-card-title");

        Label queueInfo = new Label(
                "Queue # " + queueTicket.getQueueNumber()
                        + " - Position " + queueTicket.getQueueId()
        );
        queueInfo.getStyleClass().add("recent");

        VBox leftContent = new VBox();
        leftContent.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
        leftContent.getChildren().addAll(title, queueInfo);
        HBox.setHgrow(leftContent, javafx.scene.layout.Priority.ALWAYS);

        Label time = new Label(formatDate(queueTicket.getCreatedAt()));
        time.getStyleClass().add("recent");
        time.setAlignment(javafx.geometry.Pos.CENTER_RIGHT);
        time.setTextAlignment(javafx.scene.text.TextAlignment.RIGHT);

        HBox card = new HBox();
        card.setAlignment(javafx.geometry.Pos.CENTER);
        card.setPrefHeight(100);
        card.getStyleClass().add("dashboard-recent-card");
        card.getChildren().addAll(leftContent, time);

        card.setPadding(new javafx.geometry.Insets(20, 20, 20, 20));

        return card;
    }

    // For Date Format
    private String formatDate(LocalDateTime createdAt){
        LocalDate queueDate = createdAt.toLocalDate();
        LocalDate today = LocalDate.now();

        long daysAgo = ChronoUnit.DAYS.between(queueDate, today);
        if(daysAgo == 0){
            return "Today";
        }else if(daysAgo == 1){
            return "Yesterday";
        }else if(daysAgo < 7){
            return daysAgo + " days ago";
        }else{
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM dd, yyyy");
            return queueDate.format(formatter);
        }
    }
    private String localDate(LocalDate date){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE, MMMM dd");
        return date.format(formatter);
    }
}
