package com.javafx.csit228capstone.utils;

import com.javafx.csit228capstone.model.QueueHistory;
import com.javafx.csit228capstone.model.User;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class QueueHistoryDAO {
    public List<QueueHistory> getRecordsByUserId(User loggedInUser) {
        List<QueueHistory> records = new ArrayList<>();

        String sql = "SELECT * FROM queue_history WHERE patient_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, loggedInUser.getUserID());

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    records.add(mapResultSetToQueueHistory(rs, loggedInUser));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return records;
    }

    private QueueHistory mapResultSetToQueueHistory(ResultSet rs, User patient) throws SQLException {
        int qNum = rs.getInt("queue_number");
        String service = rs.getString("service");
        String status = rs.getString("status");
        LocalDate date = rs.getDate("queue_date") != null ? rs.getDate("queue_date").toLocalDate() : null;
        String dept = rs.getString("department");
        String staffName = rs.getString("staff_name");

        return new QueueHistory(qNum, service, status, date, dept, patient, staffName);
    }
}
