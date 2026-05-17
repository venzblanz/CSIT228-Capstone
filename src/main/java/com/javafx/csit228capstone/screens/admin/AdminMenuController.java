package com.javafx.csit228capstone.screens.admin;

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

public class AdminMenuController {

    @FXML private VBox root;
    @FXML private Label nameLabel;
    @FXML private Button dashboardBtn;
    @FXML private Button liveQueueBtn;
    @FXML private Button manageQueueBtn;
    @FXML private Button manageUsersBtn;
    @FXML private Button manageScheduleBtn;
    @FXML private HBox logoutBtn;

    private Button activeButton;

    private final SceneNavigator sceneNavigator = SceneNavigator.getInstance();

    public Button getDashboardBtn()      { return dashboardBtn; }
    public Button getLiveQueueBtn()      { return liveQueueBtn; }
    public Button getManageQueueBtn()    { return manageQueueBtn; }
    public Button getManageUsersBtn()    { return manageUsersBtn; }
    public Button getScheduleBtn()       { return manageScheduleBtn; }

    @FXML
    private void initialize() {
        nameLabel.setText(SessionManager.getInstance().getCurrentUser().getFullname());

        root.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                root.prefHeightProperty().bind(newScene.heightProperty());
            }
        });
        hoverAnimation();
        logoutBtnTransition();

        dashboardBtn.setOnAction(e -> sceneNavigator.navigate(
                "/com/javafx/csit228capstone/admin/admin_dashboard.fxml",
                dashboardBtn, "/styles/dashboard.css"));

        liveQueueBtn.setOnAction(e -> sceneNavigator.navigate(
                "/com/javafx/csit228capstone/admin/admin_live_queue.fxml",
                liveQueueBtn, "/styles/dashboard.css"));

        manageQueueBtn.setOnAction(e -> sceneNavigator.navigate(
                "/com/javafx/csit228capstone/admin/admin-manage-queue.fxml",
                manageQueueBtn, "/styles/admin-manage-queue.css"));

        manageUsersBtn.setOnAction(e -> sceneNavigator.navigate(
                "/com/javafx/csit228capstone/admin/admin_manage_users.fxml",
                manageUsersBtn, "/styles/dashboard.css"));

        manageScheduleBtn.setOnAction(e -> sceneNavigator.navigate(
                "/com/javafx/csit228capstone/admin/admin-manage-schedule.fxml",
                manageScheduleBtn, "/styles/schedule.css"));

        logoutBtn.setOnMouseClicked(e -> {
            SessionManager.getInstance().clearSession();
            sceneNavigator.navigate(
                    "/com/javafx/csit228capstone/login.fxml",
                    logoutBtn, "/styles/login.css");
        });
    }

    public void setActiveButton(Button active) {
        activeButton = active;

        dashboardBtn.setStyle("");
        liveQueueBtn.setStyle("");
        manageQueueBtn.setStyle("");
        manageUsersBtn.setStyle("");
        manageScheduleBtn.setStyle("");

        dashboardBtn.setGraphicTextGap(10);
        liveQueueBtn.setGraphicTextGap(10);
        manageScheduleBtn.setGraphicTextGap(10);
        manageUsersBtn.setGraphicTextGap(10);
        manageQueueBtn.setGraphicTextGap(10);

        active.setStyle("-fx-background-color: #218AD5; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");
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


        liveQueueBtn.setOnMouseEntered(event -> {
            setButtonIcon(liveQueueBtn, "/images/queue_white.png");
        });

        liveQueueBtn.setOnMouseExited(event -> {
            if (liveQueueBtn == activeButton) {
                setButtonIcon(liveQueueBtn, "/images/queue_white.png");
            } else {
                setButtonIcon(liveQueueBtn, "/images/queue.png");
            }
        });

        manageQueueBtn.setOnMouseEntered(event -> {
            setButtonIcon(manageQueueBtn, "/images/queue_white.png");
        });

        manageQueueBtn.setOnMouseExited(event -> {
            if (manageQueueBtn == activeButton) {
                setButtonIcon(manageQueueBtn, "/images/queue_white.png");
            } else {
                setButtonIcon(manageQueueBtn, "/images/queue.png");
            }
        });


        manageScheduleBtn.setOnMouseEntered(event -> {
            setButtonIcon(manageScheduleBtn, "/images/calendar_white.png");
        });

        manageScheduleBtn.setOnMouseExited(event -> {
            if (manageScheduleBtn == activeButton) {
                setButtonIcon(manageScheduleBtn, "/images/calendar_white.png");
            } else {
                setButtonIcon(manageScheduleBtn, "/images/calendar_black.png");
            }
        });


        manageUsersBtn.setOnMouseEntered(event -> {
            setButtonIcon(manageUsersBtn, "/images/user_white.png");
        });

        manageUsersBtn.setOnMouseExited(event -> {
            if (manageUsersBtn == activeButton) {
                setButtonIcon(manageUsersBtn, "/images/user_white.png");
            } else {
                setButtonIcon(manageUsersBtn, "/images/user.png");
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
    private void logoutBtnTransition() {
        ScaleTransition grow = new ScaleTransition(new Duration(200), logoutBtn);
        grow.setToX(1.05);
        grow.setToY(1.05);
        grow.setInterpolator(Interpolator.EASE_OUT);

        ScaleTransition back = new ScaleTransition(new Duration(200), logoutBtn);
        back.setToX(1.0);
        back.setToY(1.0);
        back.setInterpolator(Interpolator.EASE_IN);

        logoutBtn.setOnMouseEntered(event -> grow.playFromStart());
        logoutBtn.setOnMouseExited(event -> back.playFromStart());
    }
}