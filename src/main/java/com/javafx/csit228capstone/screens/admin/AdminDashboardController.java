package com.javafx.csit228capstone.screens.admin;

import com.javafx.csit228capstone.utils.DatabaseConfig;
import com.javafx.csit228capstone.utils.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class AdminDashboardController {

    @FXML private Label adminNameLabel;
    @FXML private Label dateLabel;
    @FXML private Label totalPatientsLabel;
    @FXML private Label activeQueueLabel;
    @FXML private Label completedLabel;
    @FXML private Label totalUsersLabel;
    @FXML private VBox recentQueueList;
    @FXML private Label noActivityLabel;

    @FXML private AdminMenuController menuController;

    @FXML
    private void initialize() {
        menuController.setActiveButton(menuController.getDashboardBtn());
        // Set admin name
        String name = SessionManager.getInstance().getCurrentUser().getFullname();
        adminNameLabel.setText(name);

        // Set date
        String date = LocalDate.now().format(DateTimeFormatter.ofPattern("EEEE, MMMM dd"));
        dateLabel.setText(date);

        // Load stats
        loadStats();
        loadRecentQueue();
    }

    private void loadStats() {
        try (Connection c = DatabaseConfig.getConnection()) {

            // Total patients today
            String today = LocalDate.now().toString();
            PreparedStatement ps1 = c.prepareStatement(
                    "SELECT COUNT(*) FROM queue_line WHERE DATE(created_at) = ?");
            ps1.setString(1, today);
            ResultSet rs1 = ps1.executeQuery();
            if (rs1.next()) totalPatientsLabel.setText(String.valueOf(rs1.getInt(1)));

            // Active queue (Waiting)
            PreparedStatement ps2 = c.prepareStatement(
                    "SELECT COUNT(*) FROM queue_line WHERE status = 'Waiting'");
            ResultSet rs2 = ps2.executeQuery();
            if (rs2.next()) activeQueueLabel.setText(String.valueOf(rs2.getInt(1)));

            // Completed today
            PreparedStatement ps3 = c.prepareStatement(
                    "SELECT COUNT(*) FROM queue_line WHERE status = 'Completed' AND DATE(created_at) = ?");
            ps3.setString(1, today);
            ResultSet rs3 = ps3.executeQuery();
            if (rs3.next()) completedLabel.setText(String.valueOf(rs3.getInt(1)));

            // Total registered users (patients only)
            PreparedStatement ps4 = c.prepareStatement(
                    "SELECT COUNT(*) FROM users WHERE role = 'patient'");
            ResultSet rs4 = ps4.executeQuery();
            if (rs4.next()) totalUsersLabel.setText(String.valueOf(rs4.getInt(1)));

        } catch (Exception e) {
            System.err.println("[AdminDashboardController] Error loading stats: " + e.getMessage());
        }
    }

    private void loadRecentQueue() {
        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(
                     "SELECT ql.queue_number, p.full_name, ql.department, ql.status, ql.created_at " +
                             "FROM queue_line ql " +
                             "JOIN users u ON ql.user_id = u.user_id " +
                             "JOIN patients p ON u.user_id = p.user_id " +
                             "ORDER BY ql.created_at DESC LIMIT 10")) {

            ResultSet rs = ps.executeQuery();
            boolean hasData = false;

            while (rs.next()) {
                hasData = true;
                HBox row = new HBox(10);
                row.setStyle("-fx-padding: 8; -fx-border-color: #E2E8F0; -fx-border-width: 0 0 1 0;");

                Label queueNum = new Label(rs.getString("queue_number"));
                queueNum.setMinWidth(80);

                Label patientName = new Label(rs.getString("full_name"));
                patientName.setMinWidth(150);

                Label dept = new Label(rs.getString("department"));
                dept.setMinWidth(180);

                Label status = new Label(rs.getString("status"));
                status.setMinWidth(100);
                // Color code status
                switch (rs.getString("status")) {
                    case "Waiting" -> status.setStyle("-fx-text-fill: #D97706;");
                    case "Completed" -> status.setStyle("-fx-text-fill: #5DAE52;");
                    case "Cancelled" -> status.setStyle("-fx-text-fill: #D82F2F;");
                }

                Label time = new Label(rs.getString("created_at").substring(11, 16));

                row.getChildren().addAll(queueNum, patientName, dept, status, time);
                recentQueueList.getChildren().add(row);
            }

            noActivityLabel.setVisible(!hasData);

        } catch (Exception e) {
            System.err.println("[AdminDashboardController] Error loading queue: " + e.getMessage());
        }
    }
}