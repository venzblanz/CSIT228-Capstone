package com.javafx.csit228capstone.screens.admin;

import com.javafx.csit228capstone.utils.SceneNavigator;
import com.javafx.csit228capstone.utils.SessionManager;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
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

        logoutBtnTransition();

        dashboardBtn.setOnAction(e -> sceneNavigator.navigate(
                "/com/javafx/csit228capstone/admin/admin_dashboard.fxml",
                dashboardBtn, "/styles/dashboard.css"));

        liveQueueBtn.setOnAction(e -> openLiveQueueWindow());


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

        active.setStyle("-fx-background-color: #218AD5; -fx-text-fill: white; -fx-font-weight: bold; -fx-font-size: 14px;");
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

    private void openLiveQueueWindow() {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/com/javafx/csit228capstone/admin/live_queue_window.fxml"));
            VBox root = loader.load();
            LiveQueueWindowController controller = loader.getController();

            Stage stage = new Stage();
            stage.setTitle("MedServe — Live Queue");
            stage.setScene(new Scene(root));
            stage.setMaximized(true);
            stage.initStyle(StageStyle.DECORATED);
            stage.setOnCloseRequest(e -> controller.shutdown());
            stage.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}