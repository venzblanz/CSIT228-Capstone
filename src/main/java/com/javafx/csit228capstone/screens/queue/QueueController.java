package com.javafx.csit228capstone.screens.queue;

import com.javafx.csit228capstone.helper.MenuController;
import com.javafx.csit228capstone.model.QueueTicket;
import com.javafx.csit228capstone.screens.NotificationPanelController;
import com.javafx.csit228capstone.utils.*;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Duration;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


public class QueueController {
    @FXML private MenuController menuController;
    @FXML private Pane notification;
    @FXML private VBox queueScreen;
    @FXML private VBox activeQueueContainer;
    @FXML private HBox gwBtn;
    @FXML private HBox whBtn;
    @FXML private HBox sfBtn;
    @FXML private HBox dlBtn;
    @FXML private StackPane root;
    @FXML private Label seeAllBtn;

    private final SceneNavigator sceneNavigator = SceneNavigator.getInstance();
    private Boolean isActive = false;

    @FXML
    public void initialize() {
        menuController.setActiveButton(menuController.getQueueBtn());
        gwBtn.setOnMouseClicked(e -> onForm("General Wellness"));
        whBtn.setOnMouseClicked(e -> onForm("Women's Health"));
        sfBtn.setOnMouseClicked(e -> onForm("Specialized Fields"));
        dlBtn.setOnMouseClicked(e -> onForm("Diagnostics and Laboratory"));
        seeAllBtn.setOnMouseClicked(e -> setIsActive());
        generateLayout();
        showActiveQueue(false);

        // for notification animation
        AnimationHelper.ringAnimation(notification);
        NotificationPanelController notifPanelCtrl = new NotificationPanelController(root);
        notification.setOnMouseClicked(e -> notifPanelCtrl.openPanel());
    }

    // ------------ Initializers ---------------------------------------------------------------------------------------
    private void initializeCards(HBox btn) {
        ScaleTransition scaleUp = new ScaleTransition(Duration.millis(150), btn);
        scaleUp.setToX(1.05); scaleUp.setToY(1.05);
        scaleUp.setInterpolator(Interpolator.EASE_OUT);

        ScaleTransition scaleDown = new ScaleTransition(Duration.millis(150), btn);
        scaleDown.setToX(1.0); scaleDown.setToY(1.0);
        scaleDown.setInterpolator(Interpolator.EASE_IN);

        btn.setOnMouseEntered(e -> { scaleUp.playFromStart(); });
        btn.setOnMouseExited(e -> { scaleDown.playFromStart(); });
    }
    private void generateLayout(){
        AnimationHelper.staggerFadeIn(queueScreen);
        initializeCards(gwBtn);
        initializeCards(whBtn);
        initializeCards(sfBtn);
        initializeCards(dlBtn);
    }
    private void setIsActive(){
        isActive = !isActive;
        if(isActive){
            seeAllBtn.setText("See Less");
        }else{
            seeAllBtn.setText("See All");
        }
        showActiveQueue(isActive);
    }
    private void showActiveQueue(Boolean isActive){
        activeQueueContainer.getChildren().clear();
        List<QueueTicket> queueList = QueueLineDAO.getActiveQueue(SessionManager.getInstance().getUserId());
        int index = 0;
        if (queueList.isEmpty()) {
            seeAllBtn.setVisible(false);
            Label emptyLabel = new Label("No active queue.");
            emptyLabel.getStyleClass().add("recent");
            activeQueueContainer.getChildren().add(emptyLabel);
            return;
        }
        if(isActive){
            while(index < queueList.size()){
                HBox card = createCard(queueList.get(index));
                activeQueueContainer.getChildren().add(card);
                index++;
            }
        }else{
            activeQueueContainer.getChildren().add(createCard(queueList.get(0)));
        }
    }
    private HBox createCard(QueueTicket queueTicket) {
        // Check lang if ang iyang queue kay lapas na sa karon para matarong ang display
        boolean isOld = queueTicket.getCreatedAt().isBefore(LocalDate.now().atStartOfDay());

        // Design kunohay nga bar HAHAHAHHAAH
        Pane colorBar = new Pane();
        colorBar.setPrefWidth(6);
        colorBar.setMaxWidth(Region.USE_PREF_SIZE);
        colorBar.setMinWidth(Region.USE_PREF_SIZE);

        // For department Icon
        Pane icon = new Pane();
        icon.setPrefSize(30, 30);
        icon.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        icon.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);

        StackPane stackPane = new StackPane(icon);
        stackPane.setPrefSize(60, 60);
        stackPane.setMaxSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        stackPane.setMinSize(Region.USE_PREF_SIZE, Region.USE_PREF_SIZE);
        HBox.setMargin(stackPane, new Insets(20, 20, 20, 20));


        // Vbox for department and Queue number
        Label department = new Label(queueTicket.getDepartment());
        department.setStyle("-fx-font-size: 14;" +
                "-fx-font-weight: bold;");

        Label queueNumber = new Label("Queue # " + queueTicket.getQueueNumber());
        queueNumber.getStyleClass().add("recent");
        VBox.setMargin(queueNumber, new Insets(5, 0, 5, 0));

        VBox queue = new VBox();
        HBox.setMargin(queue, new Insets(20, 10, 20, 20));
        HBox.setHgrow(queue, Priority.ALWAYS);
        queue.setAlignment(Pos.CENTER_LEFT);
        queue.getChildren().addAll(department, queueNumber);


        // Separator
        Separator s1 = createSeparator();


        // VBox for Waiting time
        Label estimteLabel = new Label("Estimated Waiting Time");
        ImageView clock = new ImageView();
        clock.setFitHeight(15);
        clock.setFitWidth(15);


        clock.setPreserveRatio(true);
        HBox.setMargin(clock, new Insets(5, 5, 5, 5));

        int positionValue = QueueLineDAO.getPosition(
                queueTicket.getDepartment(),
                queueTicket.getQueueNumber()
        );

        Label time = new Label();

        if(!isOld){
            if (queueTicket.getStatus().equals("Serving")) {
                time.setText("Serving now");
            } else {
                QueueTimeHelper.startCountdownFromNow(
                        time,
                        positionValue,
                        true
                );
            }
        }

        HBox timeContainer = new HBox();
        timeContainer.setPrefHeight(19);
        timeContainer.setPrefWidth(200);
        timeContainer.setAlignment(Pos.CENTER);
        VBox.setMargin(timeContainer, new Insets(4, 9, 4, 9));
        timeContainer.getChildren().addAll(clock, time);

        VBox waitingTimeContainer = new VBox();
        waitingTimeContainer.setAlignment(Pos.CENTER);
        HBox.setMargin(waitingTimeContainer, new Insets(20, 10, 20, 10));
        HBox.setHgrow(waitingTimeContainer, Priority.ALWAYS);
        waitingTimeContainer.getChildren().addAll(estimteLabel, timeContainer);


        // Separator
        Separator s2 = createSeparator();


        // VBox for Status
        Label statusLabel = new Label("Status");
        Label status = new Label("-");
        if(!isOld){
            status.setText(queueTicket.getStatus());
        }
        VBox.setMargin(status, new Insets(5, 10, 5, 10));

        VBox statusContainer = new VBox();
        statusContainer.setAlignment(Pos.CENTER);
        HBox.setMargin(statusContainer, new Insets(20, 10, 20, 10));
        HBox.setHgrow(statusContainer, Priority.ALWAYS);
        statusContainer.getChildren().addAll(statusLabel, status);


        // Separator
        Separator s3 = createSeparator();


        // Vbox for Position
        Label positionLabel = new Label("Position");
        Label position = null;
        if(isOld){
            position = new Label("-");
        }else{
            if (queueTicket.getStatus().equals("Serving")) {
                position = new Label("Now");
            } else {
                position = new Label("#" + positionValue);
            }
        }
        VBox.setMargin(position, new Insets(3, 8, 3, 8));

        VBox positionContainer = new VBox();
        positionContainer.setAlignment(Pos.CENTER);
        HBox.setMargin(positionContainer, new Insets(20, 10, 20, 10));
        HBox.setHgrow(positionContainer, Priority.ALWAYS);
        positionContainer.getChildren().addAll(positionLabel, position);

        // Card UI uniformity
        Image image = null;
        switch (queueTicket.getDepartment()) {
            case "General Wellness":
                icon.getStyleClass().add("general-icon");
                colorBar.getStyleClass().add("gw-color-bar");
                stackPane.getStyleClass().add("gw-icon-circle");
                time.getStyleClass().add("gw");
                status.getStyleClass().add("gw");
                position.getStyleClass().add("gw-pos");
                image = new Image(getClass().getResource("/images/clock-blue.png").toExternalForm());
                s1.getStyleClass().add("gw-card-separator");
                s2.getStyleClass().add("gw-card-separator");
                s3.getStyleClass().add("gw-card-separator");
                break;
            case "Women's Health":
                icon.getStyleClass().add("women-icon");
                colorBar.getStyleClass().add("wh-color-bar");
                stackPane.getStyleClass().add("wh-icon-circle");
                time.getStyleClass().add("wh");
                status.getStyleClass().add("wh");
                position.getStyleClass().add("wh-pos");
                image = new Image(getClass().getResource("/images/clock-pink.png").toExternalForm());
                s1.getStyleClass().add("wh-card-separator");
                s2.getStyleClass().add("wh-card-separator");
                s3.getStyleClass().add("wh-card-separator");
                break;
            case "Specialized Fields":
                icon.getStyleClass().add("specialized-icon");
                colorBar.getStyleClass().add("sf-color-bar");
                stackPane.getStyleClass().add("sf-icon-circle");
                time.getStyleClass().add("sf");
                status.getStyleClass().add("sf");
                position.getStyleClass().add("sf-pos");
                image = new Image(getClass().getResource("/images/clock-green.png").toExternalForm());
                s1.getStyleClass().add("sf-card-separator");
                s2.getStyleClass().add("sf-card-separator");
                s3.getStyleClass().add("sf-card-separator");
                break;
            case "Diagnostics and Laboratory":
                icon.getStyleClass().add("lab-icon");
                colorBar.getStyleClass().add("dl-color-bar");
                stackPane.getStyleClass().add("dl-icon-circle");
                time.getStyleClass().add("dl");
                status.getStyleClass().add("dl");
                position.getStyleClass().add("dl-pos");
                image = new Image(getClass().getResource("/images/clock-violet.png").toExternalForm());
                s1.getStyleClass().add("dl-card-separator");
                s2.getStyleClass().add("dl-card-separator");
                s3.getStyleClass().add("dl-card-separator");
                break;
        }
        clock.setImage(image);

        HBox card = new HBox();
        queue.setPrefWidth(240);
        queue.setMinWidth(240);
        queue.setMaxWidth(240);
        card.setAlignment(Pos.CENTER);
        card.setPrefHeight(80);
        card.getStyleClass().add("active-queue-card");
        card.getChildren().addAll(colorBar, stackPane, queue, s1, waitingTimeContainer, s2, statusContainer, s3, positionContainer);

        card.setOnMouseClicked(e -> goToTicket(queueTicket, card));
        card.setStyle("-fx-cursor: hand;");
        return card;
    }
    private Separator createSeparator() {
        Separator separator = new Separator();
        separator.setOrientation(Orientation.VERTICAL);
        HBox.setMargin(separator, new Insets(20,10,20,10));
        return separator;
    }

    // -------------- Navigators ---------------------------------------------------------------------------------------
    private void onForm(String type){ goToForm(type); }
    private void goToForm(String type){
        sceneNavigator.navigate(
                "/com/javafx/csit228capstone/queue/queue-form.fxml",
                gwBtn,
                "/styles/queue-form.css",
                (QueueFormController queueFormController) -> queueFormController.initializeData(type)
        );
    }
    private void goToTicket(QueueTicket queueTicket, HBox card) {
        sceneNavigator.navigate(
                "/com/javafx/csit228capstone/queue/queue-info.fxml",
                card,
                "/styles/queue-number.css",
                (QueueInformationController controller) -> controller.initializeData(queueTicket,card)
        );
    }
}
