package com.javafx.csit228capstone.utils;

import com.javafx.csit228capstone.model.QueueHistory;
import com.javafx.csit228capstone.model.User;
import com.javafx.csit228capstone.utils.DatabaseConfig;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class QueueHistoryDAO {

    public List<QueueHistory> getRecordsByUserId(User loggedInUser) {
        String sql = "SELECT q.queue_number, q.service, q.status, q.queue_date, q.department, q.patient_id, q.staff_name, " +
                "u.full_name, u.mobile_number, u.email, u.role " +
                "FROM queue_history q " +
                "LEFT JOIN users u ON q.patient_id = u.user_id " +
                "WHERE q.patient_id = ?";
        return fetchFromDB(sql, loggedInUser.getUserID());
    }

    public List<QueueHistory> getAllRecords() {
        String sql = "SELECT q.queue_number, q.service, q.status, q.queue_date, q.department, q.patient_id, q.staff_name, " +
                "u.full_name, u.mobile_number, u.email, u.role " +
                "FROM queue_history q " +
                "LEFT JOIN users u ON q.patient_id = u.user_id";
        System.out.println("HELLO");
        return fetchFromDB(sql, null);
    }

    private List<QueueHistory> fetchFromDB(String sql, Integer filterId) {
        List<QueueHistory> records = new ArrayList<>();

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            // Debug (optional)
            System.out.println("Executing SQL: " + sql);
            if (filterId != null) {
                System.out.println("Filter ID: " + filterId);
                stmt.setInt(1, filterId);
            }

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    int queueNumber = rs.getInt("queue_number");

                    // Get raw values once
                    String rawName = rs.getString("full_name");
                    String rawMobile = rs.getString("mobile_number");
                    String rawEmail = rs.getString("email");
                    String rawRole = rs.getString("role");

                    // Handle nulls
                    String name = (rawName != null) ? rawName : "Unknown Patient";
                    String mobile = (rawMobile != null) ? rawMobile : "N/A";
                    String email = (rawEmail != null) ? rawEmail : "N/A";
                    String role = (rawRole != null) ? rawRole : "Patient";

                    User patient = new User(
                            rs.getInt("patient_id"),
                            name,
                            mobile,
                            email,
                            null,
                            role
                    );

                    Date sqlDate = rs.getDate("queue_date");

                    QueueHistory record = new QueueHistory(
                            queueNumber,
                            rs.getString("service"),
                            rs.getString("status"),
                            (sqlDate != null) ? sqlDate.toLocalDate() : null,
                            rs.getString("department"),
                            patient,
                            rs.getString("staff_name")
                    );

                    records.add(record);
                }
            }

            System.out.println("DAO Found Records: " + records.size());

        } catch (SQLException e) {
            System.out.println("SQL ERROR: " + e.getMessage());
            e.printStackTrace();
        }

        return records;
    }
}