package com.javafx.csit228capstone.screens;

import com.javafx.csit228capstone.helper.MenuController;
import com.javafx.csit228capstone.model.QueueLine;
import com.javafx.csit228capstone.model.QueueTicket;
import com.javafx.csit228capstone.model.User;
import com.javafx.csit228capstone.utils.AnimationHelper;
import com.javafx.csit228capstone.utils.QueueLineDAO;
import com.javafx.csit228capstone.utils.SessionManager;
import javafx.animation.RotateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
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
    @FXML private Label cardTitle1;
    @FXML private Label cardTitle2;
    @FXML private Label cardTime1;
    @FXML private Label cardTime2;
    @FXML private Label cardQ1;
    @FXML private Label cardQ2;

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
    private void setUpRecent(List<QueueTicket> queueList){
        int i = 0;
        for(QueueTicket queueTicket : queueList){
            if(i == 0){
                cardTitle1.setText("Queued: " + queueTicket.getDepartment());
                cardTime1.setText(formatDate(queueTicket.getCreatedAt()));
                cardQ1.setText("Queue # " + queueTicket.getQueueNumber() + " - Position " + queueTicket.getQueueId());
            }else{
                cardTitle2.setText("Queued: " + queueTicket.getDepartment());
                cardTime2.setText(formatDate(queueTicket.getCreatedAt()));
                cardQ2.setText("Queue # " + queueTicket.getQueueNumber() + " - Position " + queueTicket.getQueueId());
            }
            i++;
        }
    }
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
