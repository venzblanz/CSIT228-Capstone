package com.javafx.csit228capstone.screens.admin;

import com.javafx.csit228capstone.utils.SceneNavigator;
import com.javafx.csit228capstone.utils.SessionManager;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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

    private final SceneNavigator sceneNavigator = SceneNavigator.getInstance();

    @FXML
    private void initialize() {
        nameLabel.setText(SessionManager.getInstance().getCurrentUser().getFullname());

        root.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                root.prefHeightProperty().bind(newScene.heightProperty());
            }
        });

        // Logout hover animation
        logoutBtnTransition();

        // Navigation
        dashboardBtn.setOnAction(e -> sceneNavigator.navigate(
                "/com/javafx/csit228capstone/admin/admin_dashboard.fxml",
                dashboardBtn, "/styles/dashboard.css"));

        liveQueueBtn.setOnAction(e -> sceneNavigator.navigate(
                "/com/javafx/csit228capstone/admin/admin_live_queue.fxml",
                liveQueueBtn, "/styles/dashboard.css"));

        manageQueueBtn.setOnAction(e -> sceneNavigator.navigate(
                "/com/javafx/csit228capstone/admin/admin_manage_queue.fxml",
                manageQueueBtn, "/styles/dashboard.css"));

        manageUsersBtn.setOnAction(e -> sceneNavigator.navigate(
                "/com/javafx/csit228capstone/admin/admin_manage_users.fxml",
                manageUsersBtn, "/styles/dashboard.css"));

        manageScheduleBtn.setOnAction(e -> sceneNavigator.navigate(
                "/com/javafx/csit228capstone/admin/admin_manage_schedule.fxml",
                manageScheduleBtn, "/styles/dashboard.css"));

        logoutBtn.setOnMouseClicked(e -> {
            SessionManager.getInstance().clearSession();
            sceneNavigator.navigate(
                    "/com/javafx/csit228capstone/login.fxml",
                    logoutBtn, "/styles/login.css");
        });
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