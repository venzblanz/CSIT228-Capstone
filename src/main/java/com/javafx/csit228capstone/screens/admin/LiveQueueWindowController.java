package com.javafx.csit228capstone.screens.admin;

import com.javafx.csit228capstone.utils.DatabaseConfig;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.concurrent.Task;
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
import java.util.ArrayList;
import java.util.List;

public class LiveQueueWindowController {

    @FXML private Button tabAll, tabDiag, tabWomens, tabGeneral, tabSpecialized;
    @FXML private Label nowServingNumber, nowServingDept, nowServingStaff;
    @FXML private FlowPane waitingCards, doneCards;
    @FXML private Label refreshLabel;

    private String currentDept = "All";
    private Timeline autoRefresh;

    // Simple data holders so we can pass data from background thread to UI thread
    private record QueueCard(String number, String subtitle, boolean isActive) {}

    @FXML
    private void initialize() {
        loadQueue();
        autoRefresh = new Timeline(new KeyFrame(Duration.seconds(10), e -> loadQueue()));
        autoRefresh.setCycleCount(Timeline.INDEFINITE);
        autoRefresh.play();
    }

    public void shutdown() {
        if (autoRefresh != null) autoRefresh.stop();
    }

    private void loadQueue() {
        // Snapshot the current dept so the background thread uses a stable value
        String dept = currentDept;

        Task<Void> task = new Task<>() {
            // These will hold the fetched data to pass to the UI thread
            String servingNumber = "";
            String servingDept   = "No one currently being served";
            String servingStaff  = "";

            final List<QueueCard> waitingList = new ArrayList<>();
            final List<QueueCard> doneList    = new ArrayList<>();

            @Override
            protected Void call() {
                String whereClause = dept.equals("All") ? "" : " AND department = ?";

                // --- Now Serving ---
                String servingSql = "SELECT queue_number, department, staff_assigned " +
                        "FROM queue_line WHERE status = 'Serving'" + whereClause +
                        " ORDER BY created_at DESC LIMIT 1";
                try (Connection c = DatabaseConfig.getConnection();
                     PreparedStatement ps = c.prepareStatement(servingSql)) {
                    if (!dept.equals("All")) ps.setString(1, dept);
                    ResultSet rs = ps.executeQuery();
                    if (rs.next()) {
                        servingNumber = rs.getString("queue_number");
                        servingDept   = rs.getString("department");
                        String staff  = rs.getString("staff_assigned");
                        servingStaff  = (staff != null && !staff.isEmpty()) ? "Staff: " + staff : "";
                    }
                } catch (Exception e) {
                    System.err.println("[LiveQueue] Now Serving error: " + e.getMessage());
                }

                // --- Waiting ---
                String waitingSql = "SELECT queue_number, department FROM queue_line " +
                        "WHERE status = 'Waiting'" +
                        (dept.equals("All") ? "" : " AND department = ?") +
                        " ORDER BY created_at ASC";
                try (Connection c = DatabaseConfig.getConnection();
                     PreparedStatement ps = c.prepareStatement(waitingSql)) {
                    if (!dept.equals("All")) ps.setString(1, dept);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        waitingList.add(new QueueCard(
                                rs.getString("queue_number"),
                                rs.getString("department"),
                                true
                        ));
                    }
                } catch (Exception e) {
                    System.err.println("[LiveQueue] Waiting error: " + e.getMessage());
                }

                // --- Completed / Cancelled ---
                String doneSql = "SELECT queue_number, status FROM queue_line " +
                        "WHERE status IN ('Completed', 'Cancelled')" +
                        (dept.equals("All") ? "" : " AND department = ?") +
                        " ORDER BY created_at DESC LIMIT 10";
                try (Connection c = DatabaseConfig.getConnection();
                     PreparedStatement ps = c.prepareStatement(doneSql)) {
                    if (!dept.equals("All")) ps.setString(1, dept);
                    ResultSet rs = ps.executeQuery();
                    while (rs.next()) {
                        doneList.add(new QueueCard(
                                rs.getString("queue_number"),
                                rs.getString("status"),
                                false
                        ));
                    }
                } catch (Exception e) {
                    System.err.println("[LiveQueue] Done error: " + e.getMessage());
                }

                return null;
            }

            @Override
            protected void succeeded() {
                // Back on JavaFX thread — safe to update UI here
                nowServingNumber.setText(servingNumber);
                nowServingDept.setText(servingDept);
                nowServingDept.setMaxWidth(Double.MAX_VALUE);
                nowServingDept.setAlignment(Pos.CENTER);
                nowServingStaff.setText(servingStaff);

                waitingCards.getChildren().clear();
                if (waitingList.isEmpty()) {
                    Label none = new Label("No waiting patients");
                    none.setStyle("-fx-text-fill: #94A3B8; -fx-font-size: 14px;");
                    waitingCards.getChildren().add(none);
                } else {
                    for (QueueCard card : waitingList)
                        waitingCards.getChildren().add(createCard(card.number(), card.subtitle(), card.isActive()));
                }

                doneCards.getChildren().clear();
                if (doneList.isEmpty()) {
                    Label none = new Label("No completed entries");
                    none.setStyle("-fx-text-fill: #94A3B8; -fx-font-size: 14px;");
                    doneCards.getChildren().add(none);
                } else {
                    for (QueueCard card : doneList)
                        doneCards.getChildren().add(createCard(card.number(), card.subtitle(), card.isActive()));
                }

                refreshLabel.setText("🔄 Last refreshed: just now");
            }

            @Override
            protected void failed() {
                System.err.println("[LiveQueue] Task failed: " + getException().getMessage());
            }
        };

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();
    }

    private VBox createCard(String number, String subtitle, boolean isActive) {
        VBox card = new VBox();
        card.setAlignment(Pos.CENTER);
        card.setSpacing(8);
        Label numberLabel   = new Label(number);
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

    @FXML private void onTabAll()         { currentDept = "All";                       setActiveTab(tabAll);         loadQueue(); }
    @FXML private void onTabDiag()        { currentDept = "Diagnostics and Laboratory"; setActiveTab(tabDiag);        loadQueue(); }
    @FXML private void onTabWomens()      { currentDept = "Women's Health";             setActiveTab(tabWomens);      loadQueue(); }
    @FXML private void onTabGeneral()     { currentDept = "General Wellness";           setActiveTab(tabGeneral);     loadQueue(); }
    @FXML private void onTabSpecialized() { currentDept = "Specialized Fields";         setActiveTab(tabSpecialized); loadQueue(); }

    private void setActiveTab(Button active) {
        for (Button b : new Button[]{tabAll, tabDiag, tabWomens, tabGeneral, tabSpecialized})
            b.getStyleClass().setAll("queue-tab");
        active.getStyleClass().setAll("queue-tab-active");
    }
}