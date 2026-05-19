package com.javafx.csit228capstone.screens;

import com.javafx.csit228capstone.helper.MenuController;
import com.javafx.csit228capstone.model.QueueTicket;
import com.javafx.csit228capstone.model.User;
import com.javafx.csit228capstone.utils.AnimationHelper;
import com.javafx.csit228capstone.utils.QueueLineDAO;
import com.javafx.csit228capstone.utils.QueueTimeHelper;
import com.javafx.csit228capstone.utils.SessionManager;
import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Objects;

public class DashboardController {
    @FXML private MenuController menuController;
    @FXML private Label dateLabel;
    @FXML private Label nameLabel;
    @FXML private Label positionLabel;
    @FXML private Label scheduleLabel;
    @FXML private Label timeLabel;
    @FXML private Pane notification;
    @FXML private VBox dashboardScreen;

    // ------------- Recents -------------------------------------------------------------------------------------------
    @FXML private VBox recentQueueContainer;
    // ------------- Active Card ---------------------------------------------------------------------------------------
    @FXML private HBox activeCard;

    private final LocalDate localDate = LocalDate.now();
    private final SessionManager sessionManager = SessionManager.getInstance();

    private final User currentUser = sessionManager.getCurrentUser();
    private final List<QueueTicket> queueList = QueueLineDAO.getRecentQueue(sessionManager.getUserId());
    private final QueueTicket qt = QueueLineDAO.getFirstActiveQueue(sessionManager.getUserId());
    @FXML private StackPane root;

    @FXML
    public void initialize(){
        AnimationHelper.fadeIn(dashboardScreen);
        menuController.setActiveButton(menuController.getDashboardBtn());

        dateLabel.setText(localDate(localDate));
        nameLabel.setText(currentUser.getFullname());

        setUpDashboardCards(qt);

        // for notification animation
        RotateTransition ring = new RotateTransition(Duration.millis(80), notification);
        ring.setByAngle(15);
        ring.setCycleCount(6);
        ring.setAutoReverse(true);

        notification.setOnMouseEntered(e -> ring.play());
        NotificationPanelController notifPanelCtrl = new NotificationPanelController(root);
        notification.setOnMouseClicked(e -> notifPanelCtrl.openPanel());
        setUpRecent(queueList);
    }
    // ----------- For Dashboard Cards ---------------------------------------------------------------------------------
    private void setUpDashboardCards(QueueTicket activeQueue){
        activeCard.getChildren().clear();

        Pane colorBar = new Pane();
        colorBar.getStyleClass().add("active-color-bar");
        colorBar.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        colorBar.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);

        if(activeQueue == null){
            positionLabel.setText("-");
            scheduleLabel.setText("0");
            timeLabel.setText("-");

            colorBar.setPrefSize(6,190);
            activeCard.setPrefHeight(300);
            activeCard.setMinHeight(Region.USE_PREF_SIZE);
            activeCard.setMaxHeight(Region.USE_PREF_SIZE);
            colorBar.setStyle("-fx-background-color: #218ad5;");

            // ------- Children for empty card
            // Image
            Image image = new Image(getClass().getResource("/images/no-active.png").toExternalForm());
            ImageView imageView = new ImageView();
            imageView.setFitHeight(450);
            imageView.setFitWidth(400);
            imageView.setPreserveRatio(true);
            HBox.setMargin(imageView, new Insets(10,10,10,20));
            imageView.setImage(image);

            // VBox for description
            Label noActive = new Label("No Active Queue");
            noActive.setStyle("-fx-font-size: 25px; -fx-font-weight: bold;");
            Label desc1 = new Label("You are not currently waiting in any queue.");
            desc1.setStyle("-fx-font-size: 15;");
            Label desc2 = new Label("Join a queue to get your number and estimated waiting time.");
            desc2.setStyle("-fx-font-size: 15px;");

            Image icon = new Image(getClass().getResource("/images/queue_white.png").toExternalForm());
            ImageView queueImage = new ImageView();
            queueImage.setFitHeight(25);
            queueImage.setFitWidth(25);
            queueImage.setImage(icon);
            Button join = new Button("Join Queue", queueImage);
            join.getStyleClass().add("join-button");
            join.setPadding(new Insets(9,14,9,14));
            join.setPrefSize(150,45);
            join.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
            join.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
            join.setGraphicTextGap(10);
            join.setOnAction(e -> menuController.goToQueue());

            HBox bottom = new HBox();
            bottom.setAlignment(Pos.CENTER_RIGHT);
            HBox.setMargin(join, new Insets(40,0,0,0));
            bottom.getChildren().addAll(join);

            VBox description = new VBox();
            description.setAlignment(Pos.CENTER_LEFT);
            description.setPadding(new Insets(40,40,10,10));
            description.setSpacing(10);
            description.getChildren().addAll(noActive, desc1, desc2, bottom);

            HBox.setHgrow(imageView, Priority.ALWAYS);
            HBox.setHgrow(description, Priority.ALWAYS);
            activeCard.getChildren().addAll(colorBar, imageView, description);
        }else{
            int position = QueueLineDAO.getPosition(
                    activeQueue.getDepartment(),
                    activeQueue.getQueueNumber()
            );

            positionLabel.setText(position + "");
            scheduleLabel.setText(activeQueue.getTime());

            QueueTimeHelper.startCountdown(
                    timeLabel,
                    activeQueue.getCreatedAt(),
                    position,
                    false
            );

            activeCard.setPrefHeight(150);
            activeCard.setMinHeight(Region.USE_PREF_SIZE);
            activeCard.setMaxHeight(Region.USE_PREF_SIZE);
            colorBar.setPrefSize(6,140);

            // ------- Children for the card
            // VBox for Active Queue Components
            Label dot = new Label("● ");
            dot.setStyle("-fx-font-size: 10px;");
            dot.getStyleClass().add("active-dot");
            Label activeLabel = new Label("Active Queue");
            activeLabel.setStyle("-fx-font-size: 18px;");
            HBox labelContainer = new HBox();
            labelContainer.setAlignment(Pos.CENTER_LEFT);
            labelContainer.getChildren().addAll(dot, activeLabel);
            Label queueNumber = new Label(activeQueue.getQueueNumber());
            queueNumber.setStyle("-fx-font-size: 40px; -fx-font-weight: bold;");
            queueNumber.getStyleClass().add("q-active");
            queueNumber.setPrefWidth(114);
            Label yourQueue = new Label("Your queue number");
            VBox queueVBox = new VBox();
            queueVBox.setAlignment(Pos.CENTER_LEFT);
            HBox.setHgrow(queueVBox, Priority.ALWAYS);
            queueVBox.setPadding(new Insets(5,0,10,28));
            queueVBox.getChildren().addAll(labelContainer, queueNumber, yourQueue);

            // VBox for Additional Queue Info
            Label department = new Label(activeQueue.getDepartment());
            department.setStyle("-fx-font-size: 18px;");
            Label pos;
            if (activeQueue.getStatus().equals("Serving")) {
                pos = new Label("Now");
            } else {
                pos = new Label("#" + position);
            }
            pos.setStyle("-fx-font-size: 16px; -fx-font-weight: bold;");
            pos.getStyleClass().add("q-position");
            Label wait = new Label();

            if (activeQueue.getStatus().equals("Serving")) {
                wait.setText("Serving now");
            } else {
                QueueTimeHelper.startCountdown(
                        wait,
                        activeQueue.getCreatedAt(),
                        position,
                        true
                );
            }
            Label nowServing = new Label("Now serving " + Objects.requireNonNull(QueueLineDAO.getFirstLineQueue(activeQueue.getDepartment())).getQueueNumber());
            nowServing.getStyleClass().add("recent");
            VBox infoContainer = new VBox();
            infoContainer.setAlignment(Pos.CENTER_RIGHT);
            infoContainer.setPadding(new Insets(0,28,0,28));
            infoContainer.getChildren().addAll(department, pos, wait, nowServing);

            // The actual card
            activeCard.getChildren().addAll(colorBar, queueVBox, infoContainer);
        }
    }

    // ----------- Recent Activity -------------------------------------------------------------------------------------
    private void setUpRecent(List<QueueTicket> queueList) {
        recentQueueContainer.getChildren().clear();

        if (queueList == null || queueList.isEmpty()) {
            Label emptyLabel = new Label("No recent activity yet.");
            emptyLabel.getStyleClass().add("recent");
            VBox.setMargin(emptyLabel, new Insets(0,0,0,10));
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

    // ----------- For Date Format -------------------------------------------------------------------------------------
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
