package com.javafx.csit228capstone.utils;

import com.javafx.csit228capstone.model.QueueInsertValue;
import com.javafx.csit228capstone.model.QueueTicket;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class QueueLineDAO {
    private static String getPrefix(String department){
        if (department == null || department.isBlank()) {
            return "Q";
        }
        return switch (department) {
            case "General Wellness" -> "G";
            case "Women's Health" -> "W";
            case "Specialized Fields" -> "S";
            case "Diagnostics and Laboratory" -> "D";
            default -> "Q";
        };
    }
    private static String getQueueNumber(String department){
        String prefix = getPrefix(department);
        String sql = """
            SELECT COUNT(*) + 1 AS next_number
            FROM queue_line
            WHERE department = ?
            AND DATE(created_at) = CURDATE()
            """;


        try(Connection c = DatabaseConfig.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)){
            ps.setString(1, department);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    int nextNumber = rs.getInt("next_number");
                    return prefix + "-" + String.format("%02d", nextNumber);
                }
            }
        }catch(Exception e){
            System.err.println("[QueueLineDAO] Error getting the queue number " + e.getMessage());
        }
        return prefix + "-01";
    }
    public static QueueInsertValue insertQueue(int formId, int userId, String department){
        String queueNumber = getQueueNumber(department);
        String sql = "insert into queue_line (form_id, user_id, department, queue_number, status) values(?,?,?,?,?)";
        try(Connection c = DatabaseConfig.getConnection();
            PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setInt(1, formId);
            ps.setInt(2, userId);
            ps.setString(3, department);
            ps.setString(4, queueNumber);
            ps.setString(5, "Waiting");

            int newRow = ps.executeUpdate();
            if(newRow == 0){
                System.err.println("[QueueLineDAO] No queue was inserted.");
                return null;
            }
            try(ResultSet rs = ps.getGeneratedKeys()){
                if(rs.next()){
                    System.out.println("[QueueLineDAO] Inserted queue " + queueNumber);
                    return new QueueInsertValue(rs.getInt(1), formId);
                }
            }
        }catch(Exception e){
            System.err.println("[QueueLineDAO] Error inserting the queue " + e.getMessage());
        }
        return null;
    }
    public static void updateQueue(String queueNumber, boolean isDone){
        String status = isDone ? "Done" : "Waiting";
        String sql = "update queue_line SET status = ? where queue_number = ? AND DATE(created_at) = CURDATE()";
        try(Connection c = DatabaseConfig.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)){
            ps.setString(1, status);
            ps.setString(2, queueNumber);
            ps.executeUpdate();
            System.out.println("[QueueLineDAO] Updated queue " + queueNumber);
        }catch(Exception e){
            System.err.println("[QueueLineDAO] Error updating the queue " + e.getMessage());
        }
    }
    public static void displayQueue(){
        String sql = "select * from queue_line where date(created_at) = CURDATE() and status in ('Waiting','Serving') ORDER BY created_at ASC";
        try(Connection c = DatabaseConfig.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)){
            ResultSet rs = ps.executeQuery();
        }catch(Exception e){
            System.err.println("[QueueLineDAO] Error displaying the queue " + e.getMessage());
        }
    }
    public static QueueTicket getQueueTicket(int queueId){
        String sql = """
                SELECT
                q.queue_id,
                q.queue_number,
                q.department,
                q.created_at,
                f.first_name,
                f.middle_initial,
                f.last_name
                FROM queue_line q, queue_form f
                WHERE q.form_id = f.form_id 
                AND q.queue_id = ?
                """;
        try(Connection c = DatabaseConfig.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)){
            ps.setInt(1, queueId);
            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    return new QueueTicket(
                            rs.getInt("queue_id"),
                            rs.getString("queue_number"),
                            rs.getString("department"),
                            rs.getString("first_name"),
                            rs.getString("middle_initial"),
                            rs.getString("last_name"),
                            rs.getTimestamp("created_at").toLocalDateTime()
                    );
                }
            }
        }catch(Exception e){
            System.err.println("[QueueLineDAO] Error getting the queue ticket " + e.getMessage());
        }
        return null;
    }
    public static List<QueueTicket> getRecentQueue(int userId) {
        String sql = """
            SELECT
                q.queue_id,
                q.queue_number,
                q.department,
                q.created_at,
                f.first_name,
                f.middle_initial,
                f.last_name
            FROM queue_line q, queue_form f
            WHERE q.form_id = f.form_id
            AND q.user_id = ?
            ORDER BY q.queue_id DESC
            """;

        List<QueueTicket> queueList = new ArrayList<>();

        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, userId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    QueueTicket ticket = new QueueTicket(
                            rs.getInt("queue_id"),
                            rs.getString("queue_number"),
                            rs.getString("department"),
                            rs.getString("first_name"),
                            rs.getString("middle_initial"),
                            rs.getString("last_name"),
                            rs.getTimestamp("created_at").toLocalDateTime()
                    );

                    queueList.add(ticket);
                }
            }

        } catch (Exception e) {
            System.err.println("[QueueLineDAO] Error getting the queue history " + e.getMessage());
        }

        return queueList;
    }
}
