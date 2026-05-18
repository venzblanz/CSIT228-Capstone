package com.javafx.csit228capstone.screens.admin;

import com.javafx.csit228capstone.utils.DatabaseConfig;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AdminLiveQueueController {

    @FXML private VBox mainContent;
    @FXML private Button fullscreenBtn;
    @FXML private Button tabAll;
    @FXML private Button tabDiag;
    @FXML private Button tabWomens;
    @FXML private Button tabGeneral;
    @FXML private Button tabSpecialized;
    @FXML private Label nowServingNumber;
    @FXML private Label nowServingDept;
    @FXML private Label nowServingStaff;
    @FXML private HBox waitingCards;
    @FXML private HBox doneCards;
    @FXML private Label emptyLabel;
    @FXML private Label refreshLabel;
    @FXML private AdminMenuController menuController;

    private String currentDept = "All";
    private boolean isFullscreen = false;
    private BorderPane rootPane;
    private VBox sidebarRef;
    private Timeline autoRefresh;

    @FXML
    private void initialize() {
        menuController.setActiveButton(menuController.getLiveQueueBtn());
        loadQueue();
        startAutoRefresh();
    }

    private void startAutoRefresh() {
        autoRefresh = new Timeline(new KeyFrame(Duration.seconds(10), e -> {
            loadQueue();
            refreshLabel.setText("🔄 Last refreshed: just now");
        }));
        autoRefresh.setCycleCount(Timeline.INDEFINITE);
        autoRefresh.play();
    }

    private void loadQueue() {
        // ALWAYS clear the cards first to prevent infinite duplicates
        waitingCards.getChildren().clear();
        doneCards.getChildren().clear();

        String whereClause = currentDept.equals("All") ? " AND DATE(ql.created_at) = CURDATE()" : " AND ql.department = ? AND DATE(ql.created_at) = CURDATE()";

        // 1. Now serving — most recent Serving or last Completed
        String servingSql = "SELECT ql.queue_number, ql.department, ql.staff_assigned " +
                "FROM queue_line ql " +
                "WHERE ql.status = 'Serving'" + whereClause +
                " ORDER BY ql.created_at DESC LIMIT 1";

        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(servingSql)) {
            if (!currentDept.equals("All")) ps.setString(1, currentDept);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                nowServingNumber.setText(rs.getString("queue_number"));
                nowServingDept.setText(rs.getString("department"));
                nowServingStaff.setText(rs.getString("staff_assigned").isEmpty()
                        ? "" : "Staff: " + rs.getString("staff_assigned"));
            } else {
                nowServingNumber.setText("---");
                nowServingDept.setText("No one being served");
                nowServingStaff.setText("");
            }
        } catch (Exception e) {
            System.err.println("[LiveQueue] Error loading serving: " + e.getMessage());
        }

        // 2. Waiting queue
        String waitingSql = "SELECT queue_number, department FROM queue_line " +
                "WHERE status = 'Waiting' AND DATE(created_at) = CURDATE()" +
                (currentDept.equals("All") ? "" : " AND department = ?") +
                " ORDER BY created_at ASC";

        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(waitingSql)) {
            if (!currentDept.equals("All")) ps.setString(1, currentDept);
            ResultSet rs = ps.executeQuery();
            boolean hasWaiting = false;
            while (rs.next()) {
                hasWaiting = true;
                // Use true for active waiting patients
                VBox card = createQueueCard(
                        rs.getString("queue_number"),
                        rs.getString("department"),
                        true
                );
                waitingCards.getChildren().add(card);
            }
            if (!hasWaiting) {
                Label none = new Label("No waiting patients");
                none.setStyle("-fx-text-fill: #94A3B8; -fx-font-size: 16px;");
                waitingCards.getChildren().add(none);
            }
        } catch (Exception e) {
            System.err.println("[LiveQueue] Error loading waiting: " + e.getMessage());
        }

        // 3. Completed/Cancelled
        String doneSql = "SELECT queue_number, department, status FROM queue_line " +
                "WHERE status IN ('Done', 'Cancelled') AND DATE(created_at) = CURDATE()" +
                (currentDept.equals("All") ? "" : " AND department = ?") +
                " ORDER BY created_at DESC LIMIT 10";

        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(doneSql)) {
            if (!currentDept.equals("All")) ps.setString(1, currentDept);
            ResultSet rs = ps.executeQuery();
            boolean hasDone = false;
            while (rs.next()) {
                hasDone = true;
                // Use false for completed/cancelled patients
                VBox card = createQueueCard(
                        rs.getString("queue_number"),
                        rs.getString("status"),
                        false
                );
                doneCards.getChildren().add(card);
            }
            if (!hasDone) {
                Label none = new Label("No completed entries");
                none.setStyle("-fx-text-fill: #94A3B8; -fx-font-size: 16px;");
                doneCards.getChildren().add(none);
            }
        } catch (Exception e) {
            System.err.println("[LiveQueue] Error loading done: " + e.getMessage());
        }
    }

    /**
     * Creates a styled card for the queue.
     * @param queueNumber The ticket number
     * @param subtitle The department or status
     * @param isActive True for waiting queue, False for completed/cancelled
     * @return A fully styled VBox card
     */
    private VBox createQueueCard(String queueNumber, String subtitle, boolean isActive) {
        // 1. Create the container
        VBox card = new VBox();
        card.setAlignment(Pos.CENTER);
        card.setSpacing(8);

        // 2. Create the labels
        Label numberLabel = new Label(queueNumber);
        Label subtitleLabel = new Label(subtitle);

        // 3. Apply the correct CSS classes from live_queue.css
        if (isActive) {
            card.getStyleClass().add("queue-card");
            numberLabel.getStyleClass().add("queue-card-number");
            subtitleLabel.getStyleClass().add("queue-card-dept");
        } else {
            card.getStyleClass().add("queue-card-inactive");
            numberLabel.getStyleClass().add("queue-card-inactive-number");
            subtitleLabel.getStyleClass().add("queue-card-dept");
        }

        // 4. Put the labels inside the card
        card.getChildren().addAll(numberLabel, subtitleLabel);

        return card;
    }

    @FXML
    private void toggleFullscreen() {
        Stage stage = (Stage) mainContent.getScene().getWindow();
        BorderPane root = (BorderPane) mainContent.getScene().getRoot();

        if (!isFullscreen) {
            // Hide sidebar
            sidebarRef = (VBox) root.getLeft();
            root.setLeft(null);
            fullscreenBtn.setText("✕ Exit Fullscreen");
            isFullscreen = true;
        } else {
            // Show sidebar again
            root.setLeft(sidebarRef);
            fullscreenBtn.setText("⛶ Fullscreen");
            isFullscreen = false;
        }
    }

    // Tab handlers
    @FXML private void onTabAll() {
        currentDept = "All";
        setActiveTab(tabAll);
        loadQueue();
    }
    @FXML private void onTabDiag() {
        currentDept = "Diagnostics and Laboratory";
        setActiveTab(tabDiag);
        loadQueue();
    }
    @FXML private void onTabWomens() {
        currentDept = "Women's Health";
        setActiveTab(tabWomens);
        loadQueue();
    }
    @FXML private void onTabGeneral() {
        currentDept = "General Wellness";
        setActiveTab(tabGeneral);
        loadQueue();
    }
    @FXML private void onTabSpecialized() {
        currentDept = "Specialized Fields";
        setActiveTab(tabSpecialized);
        loadQueue();
    }

    private void setActiveTab(Button active) {
        tabAll.getStyleClass().setAll("queue-tab");
        tabDiag.getStyleClass().setAll("queue-tab");
        tabWomens.getStyleClass().setAll("queue-tab");
        tabGeneral.getStyleClass().setAll("queue-tab");
        tabSpecialized.getStyleClass().setAll("queue-tab");
        active.getStyleClass().setAll("queue-tab-active");
    }
}