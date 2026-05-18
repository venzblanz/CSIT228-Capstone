package com.javafx.csit228capstone.utils;

import com.javafx.csit228capstone.model.Service;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class ScheduleDAO {

    private static ScheduleDAO instance;
    private final Connection connection;

    private ScheduleDAO(Connection connection) {
        this.connection = connection;
    }

    public static ScheduleDAO getInstance() {
        if (instance == null) {
            instance = new ScheduleDAO(DatabaseConfig.getConnection());
        }
        return instance;
    }

    public List<Service> getAllServices() throws SQLException {
        List<Service> list = new ArrayList<>();
        String sql = "SELECT * FROM services ORDER BY service_type, service_name";

        try (PreparedStatement ps = connection.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                list.add(Service.fromDatabase(
                        rs.getInt("service_id"),
                        rs.getString("service_name"),
                        rs.getString("service_type"),
                        null,
                        true
                ));
            }
        }
        return list;
    }

    public Map<String, List<Service>> getScheduleForDate(LocalDate date) throws SQLException {
        Map<String, List<Service>> slotMap = new LinkedHashMap<>();
        int dayOfWeek = date.getDayOfWeek().getValue();

        String sql = """
            SELECT sc.schedule_id,
                   sc.time_slot,
                   sc.doctor_name,
                   s.service_id, s.service_name, s.service_type,
                   (sc.day_of_week IS NOT NULL) AS is_recurring
            FROM schedules sc
            JOIN services s ON sc.service_id = s.service_id
            WHERE sc.day_of_week = ? OR sc.specific_date = ?
            ORDER BY sc.time_slot
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, dayOfWeek);
            ps.setDate(2, Date.valueOf(date));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    String slot = rs.getString("time_slot");
                    boolean recurring = rs.getBoolean("is_recurring");

                    Service svc = Service.fromDatabase(
                            rs.getInt("service_id"),
                            rs.getString("service_name"),
                            rs.getString("service_type"),
                            rs.getString("doctor_name"),
                            recurring
                    );

                    slotMap.computeIfAbsent(slot, k -> new ArrayList<>()).add(svc);
                }
            }
        }
        return slotMap;
    }

    public void addRecurringServiceToSlot(int dayOfWeek, String timeSlot, int serviceId) throws SQLException {
        String sql = """
            INSERT INTO schedules (service_id, day_of_week, specific_date, time_slot)
            VALUES (?, ?, NULL, ?)
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, serviceId);
            ps.setInt(2, dayOfWeek);
            ps.setString(3, timeSlot);
            ps.executeUpdate();
        }
    }

    public void addOneTimeServiceToSlot(LocalDate date, String timeSlot, int serviceId) throws SQLException {
        String sql = """
            INSERT INTO schedules (service_id, day_of_week, specific_date, time_slot)
            VALUES (?, NULL, ?, ?)
        """;

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, serviceId);
            ps.setDate(2, Date.valueOf(date));
            ps.setString(3, timeSlot);
            ps.executeUpdate();
        }
    }

    public void removeServiceFromSlot(LocalDate date, String timeSlot, int serviceId, boolean recurring) throws SQLException {
        String sql;
        if (recurring) {
            sql = "DELETE FROM schedules WHERE service_id=? AND day_of_week=? AND time_slot=?";
        } else {
            sql = "DELETE FROM schedules WHERE service_id=? AND specific_date=? AND time_slot=?";
        }

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            ps.setInt(1, serviceId);
            if (recurring) {
                ps.setInt(2, date.getDayOfWeek().getValue());
            } else {
                ps.setDate(2, Date.valueOf(date));
            }
            ps.setString(3, timeSlot);
            ps.executeUpdate();
        }
    }

    public void updateDoctorForSlot(LocalDate date, String timeSlot, int serviceId, boolean recurring, String doctorName) throws SQLException {
        String sql;
        if (recurring) {
            sql = "UPDATE schedules SET doctor_name = ? WHERE service_id = ? AND day_of_week = ? AND time_slot = ?";
        } else {
            sql = "UPDATE schedules SET doctor_name = ? WHERE service_id = ? AND specific_date = ? AND time_slot = ?";
        }

        try (PreparedStatement ps = connection.prepareStatement(sql)) {
            if (doctorName == null || doctorName.isBlank()) {
                ps.setNull(1, Types.VARCHAR);
            } else {
                ps.setString(1, doctorName.trim());
            }
            ps.setInt(2, serviceId);
            if (recurring) {
                ps.setInt(3, date.getDayOfWeek().getValue());
            } else {
                ps.setDate(3, Date.valueOf(date));
            }
            ps.setString(4, timeSlot);
            ps.executeUpdate();
        }
    }

    public Service addCustomService(String name, String serviceType, LocalDate date, String timeSlot, boolean recurring) throws SQLException {
        String insertSql = "INSERT INTO services (service_name, service_type) VALUES (?, ?)";
        int newId;

        try (PreparedStatement ps = connection.prepareStatement(insertSql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, name);
            ps.setString(2, serviceType);
            ps.executeUpdate();

            try (ResultSet keys = ps.getGeneratedKeys()) {
                keys.next();
                newId = keys.getInt(1);
            }
        }

        if (recurring) {
            addRecurringServiceToSlot(date.getDayOfWeek().getValue(), timeSlot, newId);
        } else {
            addOneTimeServiceToSlot(date, timeSlot, newId);
        }

        return Service.createNewWithId(newId, name, serviceType, recurring);
    }
}