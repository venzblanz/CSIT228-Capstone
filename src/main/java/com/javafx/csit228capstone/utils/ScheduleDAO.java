package com.javafx.csit228capstone.utils;

import com.javafx.csit228capstone.model.Service;
import java.sql.*;
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
        int dayOfWeek = date.getDayOfWeek().getValue(); // 1=Mon ... 7=Sun

        String sql = """
        SELECT sc.time_slot, s.service_id, s.service_name, s.service_type
        FROM schedules sc
        JOIN services s ON sc.service_id = s.service_id
        WHERE sc.day_of_week = ?
        ORDER BY sc.time_slot
    """;
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, dayOfWeek);
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

    public void addServiceToSlot(int dayOfWeek, String timeSlot, int serviceId) throws SQLException {
        String sql = "INSERT INTO schedules (service_id, day_of_week, time_slot) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, serviceId);
            ps.setInt(2, dayOfWeek);
            ps.setString(3, timeSlot);
            ps.executeUpdate();
        }
    }

    public void removeServiceFromSlot(int dayOfWeek, String timeSlot, int serviceId) throws SQLException {
        String sql = "DELETE FROM schedules WHERE service_id = ? AND day_of_week = ? AND time_slot = ?";
        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, serviceId);
            ps.setInt(2, dayOfWeek);
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
        int dayOfWeek = date.getDayOfWeek().getValue();
        addServiceToSlot(dayOfWeek, timeSlot, newId);
        return new Service(newId, name, serviceType);
    }
}