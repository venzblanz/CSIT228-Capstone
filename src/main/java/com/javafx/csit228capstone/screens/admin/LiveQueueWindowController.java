package com.javafx.csit228capstone.screens.admin;

import com.javafx.csit228capstone.utils.DatabaseConfig;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class LiveQueueWindowController {

    @FXML private Button tabAll, tabDiag, tabWomens, tabGeneral, tabSpecialized;
    @FXML private Label nowServingNumber, nowServingDept, nowServingStaff;
    @FXML private FlowPane waitingCards, doneCards;
    @FXML private Label refreshLabel;

    private String currentDept = "All";
    private Timeline autoRefresh;

    @FXML
    private void initialize() {
        loadQueue();
        autoRefresh = new Timeline(new KeyFrame(Duration.seconds(10), e -> {
            loadQueue();
            refreshLabel.setText("🔄 Last refreshed: just now");
        }));
        autoRefresh.setCycleCount(Timeline.INDEFINITE);
        autoRefresh.play();
    }

    // Call this when the window is closed so the timer stops
    public void shutdown() {
        if (autoRefresh != null) autoRefresh.stop();
    }

    private void loadQueue() {
        waitingCards.getChildren().clear();
        doneCards.getChildren().clear();

        String whereClause = currentDept.equals("All") ? "" : " AND department = ?";

        // Now serving
        String servingSql = "SELECT queue_number, department, staff_assigned " +
                "FROM queue_line WHERE status = 'Completed'" + whereClause +
                " ORDER BY created_at DESC LIMIT 1";
        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(servingSql)) {
            if (!currentDept.equals("All")) ps.setString(1, currentDept);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                nowServingNumber.setText(rs.getString("queue_number"));
                nowServingDept.setText(rs.getString("department"));
                String staff = rs.getString("staff_assigned");
                nowServingStaff.setText(staff != null && !staff.isEmpty() ? "Staff: " + staff : "");
            } else {
                nowServingNumber.setText("");
                nowServingDept.setText("No one currently being served");
                nowServingStaff.setText("");
            }
        } catch (Exception e) {
            System.err.println("[LiveQueueWindow] " + e.getMessage());
        }

        // Waiting
        String waitingSql = "SELECT queue_number, department FROM queue_line WHERE status = 'Waiting'" +
                (currentDept.equals("All") ? "" : " AND department = ?") +
                " ORDER BY created_at ASC";
        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(waitingSql)) {
            if (!currentDept.equals("All")) ps.setString(1, currentDept);
            ResultSet rs = ps.executeQuery();
            boolean any = false;
            while (rs.next()) {
                any = true;
                waitingCards.getChildren().add(createCard(
                        rs.getString("queue_number"), rs.getString("department"), true));
            }
            if (!any) {
                Label none = new Label("No waiting patients");
                none.setStyle("-fx-text-fill: #94A3B8; -fx-font-size: 14px;");
                waitingCards.getChildren().add(none);
            }
        } catch (Exception e) {
            System.err.println("[LiveQueueWindow] " + e.getMessage());
        }

        // Completed / Cancelled
        String doneSql = "SELECT queue_number, status FROM queue_line " +
                "WHERE status IN ('Completed', 'Cancelled')" +
                (currentDept.equals("All") ? "" : " AND department = ?") +
                " ORDER BY created_at DESC LIMIT 10";
        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(doneSql)) {
            if (!currentDept.equals("All")) ps.setString(1, currentDept);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                doneCards.getChildren().add(createCard(
                        rs.getString("queue_number"), rs.getString("status"), false));
            }
        } catch (Exception e) {
            System.err.println("[LiveQueueWindow] " + e.getMessage());
        }
    }

    private VBox createCard(String number, String subtitle, boolean isActive) {
        VBox card = new VBox();
        card.setAlignment(Pos.CENTER);
        card.setSpacing(8);
        Label numberLabel = new Label(number);
        Label subtitleLabel = new Label(subtitle);
        if (isActive) {
            card.getStyleClass().add("queue-card");
            numberLabel.getStyleClass().add("queue-card-number");
            subtitleLabel.getStyleClass().add("queue-card-dept");
        } else {
            card.getStyleClass().add("queue-card-inactive");
            numberLabel.getStyleClass().add("queue-card-inactive-number");
            subtitleLabel.getStyleClass().add("queue-card-dept");
        }
        card.getChildren().addAll(numberLabel, subtitleLabel);
        return card;
    }

    @FXML private void onTabAll()         { currentDept = "All";                      setActiveTab(tabAll);         loadQueue(); }
    @FXML private void onTabDiag()        { currentDept = "Diagnostics and Laboratory"; setActiveTab(tabDiag);       loadQueue(); }
    @FXML private void onTabWomens()      { currentDept = "Women's Health";            setActiveTab(tabWomens);      loadQueue(); }
    @FXML private void onTabGeneral()     { currentDept = "General Wellness";          setActiveTab(tabGeneral);     loadQueue(); }
    @FXML private void onTabSpecialized() { currentDept = "Specialized Fields";        setActiveTab(tabSpecialized); loadQueue(); }

    private void setActiveTab(Button active) {
        for (Button b : new Button[]{tabAll, tabDiag, tabWomens, tabGeneral, tabSpecialized})
            b.getStyleClass().setAll("queue-tab");
        active.getStyleClass().setAll("queue-tab-active");
    }
}