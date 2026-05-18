package com.javafx.csit228capstone.utils;

import com.javafx.csit228capstone.model.Notification;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {

    // ── Insert ──────────────────────────────────────────────────────────────
    public static void insert(int userId, String title, String message, String type) {
        String sql = "INSERT INTO notifications (user_id, title, message, type) VALUES (?,?,?,?)";
        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, title);
            ps.setString(3, message);
            ps.setString(4, type);
            ps.executeUpdate();
            System.out.println("[NotificationDAO] Inserted notification: " + title);
        } catch (Exception e) {
            System.err.println("[NotificationDAO] Error inserting: " + e.getMessage());
        }
    }

    // ── Fetch all for user ───────────────────────────────────────────────────
    public static List<Notification> getAll(int userId) {
        String sql = "SELECT * FROM notifications WHERE user_id = ? ORDER BY created_at DESC";
        List<Notification> list = new ArrayList<>();
        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(map(rs));
                }
            }
        } catch (Exception e) {
            System.err.println("[NotificationDAO] Error fetching: " + e.getMessage());
        }
        return list;
    }

    // ── Count unread ─────────────────────────────────────────────────────────
    public static int countUnread(int userId) {
        String sql = "SELECT COUNT(*) FROM notifications WHERE user_id = ? AND is_read = FALSE";
        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return rs.getInt(1);
            }
        } catch (Exception e) {
            System.err.println("[NotificationDAO] Error counting: " + e.getMessage());
        }
        return 0;
    }

    // ── Mark all read ────────────────────────────────────────────────────────
    public static void markAllRead(int userId) {
        String sql = "UPDATE notifications SET is_read = TRUE WHERE user_id = ?";
        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (Exception e) {
            System.err.println("[NotificationDAO] Error marking read: " + e.getMessage());
        }
    }

    // ── Mark one read ────────────────────────────────────────────────────────
    public static void markRead(int notifId) {
        String sql = "UPDATE notifications SET is_read = TRUE WHERE notif_id = ?";
        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, notifId);
            ps.executeUpdate();
        } catch (Exception e) {
            System.err.println("[NotificationDAO] Error marking one read: " + e.getMessage());
        }
    }

    // ── Delete all ───────────────────────────────────────────────────────────
    public static void deleteAll(int userId) {
        String sql = "DELETE FROM notifications WHERE user_id = ?";
        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.executeUpdate();
        } catch (Exception e) {
            System.err.println("[NotificationDAO] Error deleting all: " + e.getMessage());
        }
    }

    // ── Delete one ───────────────────────────────────────────────────────────
    public static void deleteOne(int notifId) {
        String sql = "DELETE FROM notifications WHERE notif_id = ?";
        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, notifId);
            ps.executeUpdate();
        } catch (Exception e) {
            System.err.println("[NotificationDAO] Error deleting one: " + e.getMessage());
        }
    }

    // ── Mapper ───────────────────────────────────────────────────────────────
    private static Notification map(ResultSet rs) throws SQLException {
        return new Notification(
                rs.getInt("notif_id"),
                rs.getInt("user_id"),
                rs.getString("title"),
                rs.getString("message"),
                rs.getString("type"),
                rs.getBoolean("is_read"),
                rs.getTimestamp("created_at").toLocalDateTime()
        );
    }
}