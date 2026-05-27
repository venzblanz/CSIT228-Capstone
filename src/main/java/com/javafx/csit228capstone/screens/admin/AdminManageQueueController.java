package com.javafx.csit228capstone.screens.admin;

import com.javafx.csit228capstone.utils.DatabaseConfig;
import com.javafx.csit228capstone.utils.NotificationDAO;
import com.javafx.csit228capstone.utils.QueueLineDAO;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Queue;
import java.util.concurrent.atomic.AtomicReference;

public class AdminManageQueueController {
    @FXML private HBox gwButton;
    @FXML private HBox whButton;
    @FXML private HBox sfButton;
    @FXML private HBox dlButton;
    @FXML private Label dateLabel;
    @FXML private VBox recentQueueList;
    @FXML private Label noActivityLabel;
    @FXML private DatePicker datePicker;
    @FXML private AdminMenuController menuController;
    @FXML private Button callNumberButton;

    private HBox selectedType;
    LocalDate date = null;
    LocalDate today = null;
    private String selectedCategory = null;
    @FXML
    public void initialize() {
        menuController.setActiveButton(menuController.getManageQueueBtn());
        datePicker.setEditable(false);
        today = LocalDate.now();
        date = today;
        datePicker.setValue(today);
        String dateText = today.format(DateTimeFormatter.ofPattern("EEEE, MMMM dd"));
        dateLabel.setText(dateText);

        callNumberButton.setOnAction(e -> {
            servingFirstNumber(selectedCategory);
        });

        gwButton.setOnMouseClicked(e -> {
            selectedCategory = "General Wellness";
            if(selectedType != null) {
                selectedType.setStyle("");
            }
            selectedType = gwButton;
            selectedType.setStyle("-fx-border-color: #218ad5; -fx-border-radius: 10; -fx-border-width: 1.5;");
            loadRecentQueue("General Wellness");
        });
        whButton.setOnMouseClicked(e -> {
            selectedCategory = "Women's Health";
            if(selectedType != null) {
                selectedType.setStyle("");
            }
            selectedType = whButton;
            selectedType.setStyle("-fx-border-color: #DB2777; -fx-border-radius: 10; -fx-border-width: 1.5;");
            loadRecentQueue("Women's Health");
        });
        sfButton.setOnMouseClicked(e -> {
            selectedCategory = "Specialized Fields";
            if(selectedType != null) {
                selectedType.setStyle("");
            }
            selectedType = sfButton;
            selectedType.setStyle("-fx-border-color: #0F766E; -fx-border-radius: 10; -fx-border-width: 1.5;");
            loadRecentQueue("Specialized Fields");
        });
        dlButton.setOnMouseClicked(e -> {
            selectedCategory = "Diagnostics and Laboratory";
            if(selectedType != null) {
                selectedType.setStyle("");
            }
            selectedType = dlButton;
            selectedType.setStyle("-fx-border-color: #543BE9; -fx-border-radius: 10; -fx-border-width: 1.5;");
            loadRecentQueue("Diagnostics and Laboratory");
        });

        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }
            date = newValue;
            dateLabel.setText(date.format(DateTimeFormatter.ofPattern("EEEE, MMMM dd")));
            loadRecentQueue(selectedCategory);
        });
        loadRecentQueue(selectedCategory);
    }

    private void servingFirstNumber(String selectedCategory) {
        if (selectedCategory == null) {
            return;
        }
        String checkServingSql =
                "SELECT ql.queue_number " +
                        "FROM queue_line ql " +
                        "WHERE ql.status = 'Serving' " +
                        "AND DATE(ql.created_at) = ? " +
                        "AND ql.department = ? " +
                        "LIMIT 1";
        String getWaitingSql =
                "SELECT ql.queue_number " +
                        "FROM queue_line ql " +
                        "JOIN queue_form qf ON ql.form_id = qf.form_id " +
                        "WHERE ql.status = 'Waiting' " +
                        "AND DATE(ql.created_at) = ? " +
                        "AND ql.department = ? " +
                        "ORDER BY " +
                        "  CASE WHEN LOWER(qf.patient_type) IN ('pwd','pregnant','senior') THEN 0 ELSE 1 END ASC, " +
                        "  ql.created_at ASC " +
                        "LIMIT 1";
        try (Connection c = DatabaseConfig.getConnection()) {
            try (PreparedStatement checkPs = c.prepareStatement(checkServingSql)) {
                checkPs.setDate(1, java.sql.Date.valueOf(date));
                checkPs.setString(2, selectedCategory);
                ResultSet servingRs = checkPs.executeQuery();
                if (servingRs.next()) {
                    System.out.println("There is already a queue being served.");
                    return;
                }
            }
            try (PreparedStatement waitingPs = c.prepareStatement(getWaitingSql)) {
                waitingPs.setDate(1, java.sql.Date.valueOf(date));
                waitingPs.setString(2, selectedCategory);
                ResultSet waitingRs = waitingPs.executeQuery();
                if (waitingRs.next()) {
                    String queueNum = waitingRs.getString("queue_number");
                    QueueLineDAO.servingQueue(queueNum);
                }
            }
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        loadRecentQueue(selectedCategory);
    }


    private void loadRecentQueue(String type) {
        recentQueueList.getChildren().clear();

        if (date == null) {
            date = LocalDate.now();
        }

        if (date.isBefore(today)) {
            autoCancelOldQueues(date);
        }

        String sql =
                "SELECT ql.queue_number, p.full_name, ql.department, ql.status, ql.created_at " +
                        "FROM queue_line ql " +
                        "JOIN users u ON ql.user_id = u.user_id " +
                        "JOIN patients p ON u.user_id = p.user_id " +
                        "JOIN queue_form qf ON ql.form_id = qf.form_id " +
                        "WHERE ql.status IN ('Waiting', 'Serving') " +
                        "AND DATE(ql.created_at) = ? " +
                        "AND department = ? " +
                        "ORDER BY " +
                        "  CASE WHEN LOWER(qf.patient_type) IN ('pwd','pregnant','senior') THEN 0 ELSE 1 END ASC, " +
                        "  ql.created_at ASC " +
                        "LIMIT 10";

        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setDate(1, java.sql.Date.valueOf(date));
            ps.setString(2, type);

            ResultSet rs = ps.executeQuery();

            boolean hasData = false;
            boolean isFirst = true;

            while (rs.next()) {
                String queueNumberValue = rs.getString("queue_number");
                if(date.isBefore(today)){
                    QueueLineDAO.updateQueue(queueNumberValue, false);
                    hasData = false;
                }else{
                    hasData = true;
                }

                HBox row = new HBox(10);
                row.setStyle("-fx-padding: 8; -fx-border-color: #E2E8F0; -fx-border-width: 0 0 1 0;");
                row.setAlignment(Pos.CENTER_LEFT);

                Label queueNum = new Label(queueNumberValue);
                queueNum.setMinWidth(80);

                Label patientName = new Label(rs.getString("full_name"));
                patientName.setMinWidth(220);

                Label dept = new Label(rs.getString("department"));
                dept.setMinWidth(220);

                Label time = new Label(rs.getString("created_at").substring(11, 16));
                time.setMinWidth(80);

                Label status = new Label(rs.getString("status"));
                status.setMaxWidth(Double.MAX_VALUE);
                status.setStyle(rs.getString("status").equals("Waiting") ? "-fx-text-fill: #218ad5;" : "-fx-text-fill: green");
                HBox.setHgrow(status, Priority.ALWAYS);

                row.getChildren().addAll(queueNum, patientName, dept, time, status);

                if (isFirst) {
                    Button cancel = new Button("Cancel");
                    cancel.setMinWidth(80);
                    cancel.getStyleClass().add("cancel-button");
                    Button complete = new Button("Complete");
                    complete.setMinWidth(80);
                    complete.getStyleClass().add("next-button");
                    if(rs.getString("status").equals("Waiting")){
                        cancel.setVisible(false);
                        complete.setVisible(false);
                    }else{
                        cancel.setVisible(true);
                        complete.setVisible(true);
                        cancel.setOnAction(e -> {
                            // 1. Fetch the patient's user_id before cancelling
                            String fetchSql = "SELECT user_id FROM queue_line WHERE queue_number = ? AND DATE(created_at) = CURDATE()";
                            try (Connection ca = DatabaseConfig.getConnection();
                                 PreparedStatement psa = ca.prepareStatement(fetchSql)) {
                                psa.setString(1, queueNumberValue);
                                ResultSet rsa = psa.executeQuery();
                                if (rsa.next()) {
                                    int patientUserId = rsa.getInt("user_id");
                                    // 2. Cancel the queue
                                    QueueLineDAO.updateQueue(queueNumberValue, false);
                                    // 3. Notify the patient
                                    NotificationDAO.insert(
                                            patientUserId,
                                            "Queue Cancelled",
                                            "Your queue #" + queueNumberValue + " for " + selectedCategory
                                                    + " has been cancelled by the staff.",
                                            "CANCELLED"
                                    );
                                }
                            } catch (Exception ex) {
                                System.err.println("[CancelQueue] " + ex.getMessage());
                            }
                            loadRecentQueue(selectedCategory);
                        });
                        complete.setOnAction(e -> {
                            QueueLineDAO.updateQueue(queueNumberValue, true);
                            loadRecentQueue(selectedCategory);
                        });
                        row.getChildren().addAll(cancel, complete);
                        isFirst = false;
                    }
                }
                recentQueueList.getChildren().add(row);
            }
            noActivityLabel.setVisible(!hasData);
            noActivityLabel.setManaged(!hasData);
        } catch (Exception e) {
            System.err.println("[AdminDashboardController] Error loading queue: " + e.getMessage());
        }
    }
    private void autoCancelOldQueues(LocalDate selectedDate) {
        String sql =
                "UPDATE queue_line " +
                        "SET status = 'Cancelled' " +
                        "WHERE status = 'Waiting' " +
                        "AND DATE(created_at) = ?";

        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setDate(1, java.sql.Date.valueOf(selectedDate));
            ps.executeUpdate();

        } catch (Exception e) {
            System.err.println("[AdminManageQueueController] Error auto-cancelling queues: " + e.getMessage());
        }
    }
    private void initializeTable(String type){
        initializeCards();
    }

    private void initializeCards(){

    }
}
