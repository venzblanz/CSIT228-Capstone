package com.javafx.csit228capstone.helper;

import com.javafx.csit228capstone.utils.FormManager;
import com.javafx.csit228capstone.utils.SceneNavigator;
import com.javafx.csit228capstone.utils.SessionManager;
import javafx.animation.Interpolator;
import javafx.animation.ScaleTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

public class MenuController {
    @FXML private VBox root;
    @FXML private HBox profileBtn;
    @FXML private Button dashboardBtn;
    @FXML private Button queueBtn;
    @FXML private Button scheduleBtn;
    @FXML private Button accountBtn;
    @FXML private HBox logoutBtn;

    private final SceneNavigator sceneNavigator = SceneNavigator.getInstance();
    private final SessionManager sessionManager = SessionManager.getInstance();
    private final FormManager formManager = FormManager.getInstance();

    public Button getQueueBtn() { return queueBtn; }
    public Button getDashboardBtn() { return dashboardBtn; }
    public Button getScheduleBtn() { return scheduleBtn; }
    public Button getAccountBtn() { return accountBtn; }


    @FXML
    public void initialize(){
        root.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                root.prefHeightProperty().bind(newScene.heightProperty());
            }
        });
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
        sceneNavigator.navigate("/com/javafx/csit228capstone/queue/queue.fxml", queueBtn,"/styles/queue.css");
    }
    public void goToDashboard(){
        sceneNavigator.navigate("/com/javafx/csit228capstone/dashboard.fxml", queueBtn,"/styles/dashboard.css");
    }
    private void goToSchedule() {
        sceneNavigator.navigate("/com/javafx/csit228capstone/schedule/schedule_patient.fxml", scheduleBtn, "/styles/schedule-patient.css");
    }
    public void setActiveButton(Button active) {
        dashboardBtn.setStyle("");
        queueBtn.setStyle("");
        scheduleBtn.setStyle("");
        accountBtn.setStyle("");

        active.setStyle("-fx-background-color: #218AD5; -fx-text-fill: white; -fx-font-family: \"Instrument Sans Bold\"; -fx-font-size: 15px;");
    }
    public void goToAccount() {
        sceneNavigator.navigate(
                "/com/javafx/csit228capstone/account/myaccount.fxml", accountBtn, "/styles/account.css"
        );

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
