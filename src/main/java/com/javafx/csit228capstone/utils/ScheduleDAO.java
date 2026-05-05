package com.javafx.csit228capstone.utils;

import com.javafx.csit228capstone.model.Service;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class ScheduleDAO {

    private final Connection connection;

    public ScheduleDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Service> getAllServices() throws SQLException {
        List<Service> list = new ArrayList<>();
        String sql = "SELECT * FROM services ORDER BY service_type, service_name";
        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(new Service(
                        rs.getInt("service_id"),
                        rs.getString("service_name"),
                        rs.getString("service_type")
                ));
            }
        }
        return list;
    }

    public Map<String, List<Service>> getScheduleForDate(LocalDate date) throws SQLException {
        Map<String, List<Service>> slotMap = new LinkedHashMap<>();
        String sql = """
            SELECT sc.time_slot, s.service_id, s.service_name, s.service_type
            FROM schedules sc
            JOIN services s ON sc.service_id = s.service_id
            WHERE sc.schedule_date = ?
            ORDER BY sc.time_slot
        """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(date));
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String slot = rs.getString("time_slot");
                    slotMap.computeIfAbsent(slot, k -> new ArrayList<>()).add(new Service(
                            rs.getInt("service_id"),
                            rs.getString("service_name"),
                            rs.getString("service_type")
                    ));
                }
            }
        }
        return slotMap;
    }

    public void addServiceToSlot(LocalDate date, String timeSlot, int serviceId) throws SQLException {
        String sql = "INSERT INTO schedules (service_id, schedule_date, time_slot) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, serviceId);
            ps.setDate(2, Date.valueOf(date));
            ps.setString(3, timeSlot);
            ps.executeUpdate();
        }
    }

    public void removeServiceFromSlot(LocalDate date, String timeSlot, int serviceId) throws SQLException {
        String sql = "DELETE FROM schedules WHERE service_id = ? AND schedule_date = ? AND time_slot = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, serviceId);
            ps.setDate(2, Date.valueOf(date));
            ps.setString(3, timeSlot);
            ps.executeUpdate();
        }
    }

    public Service addCustomService(String name, String serviceType,
                                    LocalDate date, String timeSlot) throws SQLException {
        String insertService = "INSERT INTO services (service_name, service_type) VALUES (?, ?)";
        int newId;
        try (PreparedStatement ps = connection.prepareStatement(insertService, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.setString(2, serviceType);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                newId = keys.getInt(1);
            }
        }
        addServiceToSlot(date, timeSlot, newId);
        return new Service(newId, name, serviceType);
    }
}