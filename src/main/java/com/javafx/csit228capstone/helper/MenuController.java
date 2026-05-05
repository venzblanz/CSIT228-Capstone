package com.javafx.csit228capstone.helper;

import com.javafx.csit228capstone.utils.FormManager;
import com.javafx.csit228capstone.utils.SceneNavigator;
import com.javafx.csit228capstone.utils.SessionManager;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class MenuController {
    @FXML private VBox      root;
    @FXML private HBox      profileBtn;
    @FXML private Button    dashboardBtn;
    @FXML private Button    queueBtn;
    @FXML private Button    scheduleBtn;
    @FXML private Button    accountBtn;
    @FXML private HBox      logoutBtn;
    @FXML private Label     nameLabel;

    private Button activeButton;

    private final SceneNavigator sceneNavigator = SceneNavigator.getInstance();
    private final SessionManager sessionManager = SessionManager.getInstance();
    private final FormManager formManager = FormManager.getInstance();

    public Button getQueueBtn()         { return queueBtn; }
    public Button getDashboardBtn()     { return dashboardBtn; }
    public Button getScheduleBtn()      { return scheduleBtn; }
    public Button getAccountBtn()       { return accountBtn; }


    @FXML
    public void initialize(){
        root.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                root.prefHeightProperty().bind(newScene.heightProperty());
            }
        });

        nameLabel.setText(sessionManager.getCurrentUser().getFullname());

        hoverAnimation();
        logoutBtnTransition();

        logoutBtn.setOnMouseClicked(event -> onLogout());
        queueBtn.setOnAction(e -> onQueue());
        dashboardBtn.setOnAction(e -> onDashboard());
        scheduleBtn.setOnAction(e -> onSchedule());
        accountBtn.setOnAction(e -> onAccount());
    }

    private void onQueue() {
        goToQueue();
    }
    private void onSchedule(){ goToSchedule(); }
    private void onAccount(){
        goToAccount();
    }
    private void onDashboard(){
        goToDashboard();
    }
    private void onLogout(){ goToLogin(); }

    public void goToLogin(){
        sessionManager.clearSession();
        formManager.clearForm();
        sceneNavigator.navigate("/com/javafx/csit228capstone/login.fxml", queueBtn,"/styles/login.css");
    }
    public void goToQueue() {
        sceneNavigator.navigate("/com/javafx/csit228capstone/queue/queue.fxml", queueBtn, "/styles/queue.css");
    }
    public void goToDashboard(){
        sceneNavigator.navigate("/com/javafx/csit228capstone/dashboard.fxml", queueBtn,"/styles/dashboard.css");
    }
    private void goToSchedule() {
        sceneNavigator.navigate("/com/javafx/csit228capstone/schedule/schedule-patient.fxml", scheduleBtn, "/styles/schedule-patient.css");
    }
    public void goToAccount() {
        sceneNavigator.navigate(
                "/com/javafx/csit228capstone/account/myaccount.fxml", accountBtn, "/styles/account.css"
        );

    }
    public void setActiveButton(Button active) {
        activeButton = active;

        dashboardBtn.setStyle("");
        queueBtn.setStyle("");
        scheduleBtn.setStyle("");
        accountBtn.setStyle("");

        dashboardBtn.setGraphicTextGap(10);
        queueBtn.setGraphicTextGap(10);
        scheduleBtn.setGraphicTextGap(10);
        accountBtn.setGraphicTextGap(10);

        setButtonIcon(dashboardBtn, "/images/icon_menu.png");
        setButtonIcon(queueBtn, "/images/queue.png");
        setButtonIcon(scheduleBtn, "/images/calendar.png");
        setButtonIcon(accountBtn, "/images/user.png");

        active.setStyle("-fx-background-color: #218AD5; -fx-text-fill: white; -fx-font-family: \"Instrument Sans Bold\"; -fx-font-size: 15px;");

        if (active == dashboardBtn) {
            setButtonIcon(dashboardBtn, "/images/icon_general.png");
        } else if (active == queueBtn) {
            setButtonIcon(queueBtn, "/images/queue_white.png");
        } else if (active == scheduleBtn) {
            setButtonIcon(scheduleBtn, "/images/calendar_white.png");
        } else if (active == accountBtn) {
            setButtonIcon(accountBtn, "/images/user_white.png");
        }
    }
    private void hoverAnimation() {
        dashboardBtn.setOnMouseEntered(event -> {
            setButtonIcon(dashboardBtn, "/images/icon_general.png");
        });

        dashboardBtn.setOnMouseExited(event -> {
            if (dashboardBtn == activeButton) {
                setButtonIcon(dashboardBtn, "/images/icon_general.png");
            } else {
                setButtonIcon(dashboardBtn, "/images/icon_menu.png");
            }
        });


        queueBtn.setOnMouseEntered(event -> {
            setButtonIcon(queueBtn, "/images/queue_white.png");
        });

        queueBtn.setOnMouseExited(event -> {
            if (queueBtn == activeButton) {
                setButtonIcon(queueBtn, "/images/queue_white.png");
            } else {
                setButtonIcon(queueBtn, "/images/queue.png");
            }
        });


        scheduleBtn.setOnMouseEntered(event -> {
            setButtonIcon(scheduleBtn, "/images/calendar_white.png");
        });

        scheduleBtn.setOnMouseExited(event -> {
            if (scheduleBtn == activeButton) {
                setButtonIcon(scheduleBtn, "/images/calendar_white.png");
            } else {
                setButtonIcon(scheduleBtn, "/images/calendar_black.png");
            }
        });


        accountBtn.setOnMouseEntered(event -> {
            setButtonIcon(accountBtn, "/images/user_white.png");
        });

        accountBtn.setOnMouseExited(event -> {
            if (accountBtn == activeButton) {
                setButtonIcon(accountBtn, "/images/user_white.png");
            } else {
                setButtonIcon(accountBtn, "/images/user.png");
            }
        });
    }

    private void setButtonIcon(Button button, String imagePath) {
        ImageView icon = new ImageView(
                new Image(getClass().getResource(imagePath).toExternalForm())
        );

        icon.setFitWidth(20);
        icon.setFitHeight(20);
        icon.setPreserveRatio(true);

        button.setGraphic(icon);
    }

    // TRANSITIONS
    private void logoutBtnTransition(){
        ScaleTransition grow = new ScaleTransition(new Duration(200), logoutBtn);
        grow.setToX(1.05); grow.setToY(1.05);
        grow.setInterpolator(Interpolator.EASE_OUT);

        ScaleTransition back = new ScaleTransition(new Duration(200), logoutBtn);
        back.setToX(1.0); back.setToY(1.0);
        back.setInterpolator(Interpolator.EASE_IN);

        logoutBtn.setOnMouseEntered(event -> {
            grow.playFromStart();
        });
        logoutBtn.setOnMouseExited(event -> {
            back.playFromStart();
        });
    }
}
