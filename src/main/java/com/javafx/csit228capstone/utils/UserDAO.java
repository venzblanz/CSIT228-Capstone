package com.javafx.csit228capstone.utils;

import com.javafx.csit228capstone.model.User;

import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;

public class UserDAO {
    /*
        authenticate (login), register, username validation (no duplicate)
        -- just add user related methods to the database here --
        TODO probably add "update" method in case user wants to change email or password
            and update the table names if database is already created
     */

    // authentication
    public static User authenticate(String email, String password) {
        String sql = "SELECT u.user_id, u.email, u.password, u.role, " +
                "uu.full_name, uu.mobile_number " +
                "FROM users u " +
                "LEFT JOIN users_update uu ON u.user_id = uu.user_id " +
                "WHERE u.email = ? " +
                "ORDER BY uu.created_at DESC LIMIT 1";
        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String storedPass = rs.getString("password"); // ← add this line
                    if (!storedPass.equals(password)) return null; // ← check password

                    String fullName = rs.getString("full_name");
                    if (fullName == null || fullName.isEmpty()) {
                        fullName = rs.getString("role").equals("admin") ? "Admin" : "Patient";
                    }

                    return new User(
                            rs.getInt("user_id"),
                            fullName,
                            rs.getString("mobile_number") != null ? rs.getString("mobile_number") : "",
                            rs.getString("email"),
                            storedPass,
                            rs.getString("role")
                    );
                }
            }
        } catch (Exception e) {
            System.err.println("[UserDAO] Error authenticating user: " + e.getMessage());
        }
        return null;
    }

    // register — insert into users first, then users_update
    public static int register(String fullname, String mobilenumber, String email, String password) {
        if (emailExists(email)) return -2;

        Connection c = null;
        try {
            c = DatabaseConfig.getConnection();
            c.setAutoCommit(false); // transaction

            // Insert into users
            String sql1 = "INSERT INTO users (email, password) VALUES (?, ?)";
            PreparedStatement ps1 = c.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS);
            ps1.setString(1, email);
            ps1.setString(2, password);
            ps1.executeUpdate();

            // Get the generated user_id
            ResultSet keys = ps1.getGeneratedKeys();
            if (!keys.next()) {
                c.rollback();
                return -1;
            }
            int newUserId = keys.getInt(1);

            // Insert into users_update
            String sql2 = "INSERT INTO users_update (user_id, full_name, mobile_number) VALUES (?, ?, ?)";
            PreparedStatement ps2 = c.prepareStatement(sql2);
            ps2.setInt(1, newUserId);
            ps2.setString(2, fullname);
            ps2.setString(3, mobilenumber);
            ps2.executeUpdate();

            c.commit();
            return 1;

        } catch (Exception e) {
            try { if (c != null) c.rollback(); } catch (Exception ex) { ex.printStackTrace(); }
            System.err.println("[UserDAO] Error registering user: " + e.getMessage());
        } finally {
            try { if (c != null) c.setAutoCommit(true); } catch (Exception ex) { ex.printStackTrace(); }
        }
        return -1;
    }

    // username verify
    public static boolean emailExists(String email) {
        String sql = "SELECT 1 FROM users WHERE email = ?";
        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (Exception e) {
            System.err.println("[UserDAO] Error checking email: " + e.getMessage());
        }
        return false;
    }


    public static User getLatestUpdate(int userId) {
        String sql = "SELECT * FROM users_update WHERE user_id = ? ORDER BY created_at DESC LIMIT 1";
        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    User user = new User(); // This will not be red if User() {} exists in User.java
                    user.setUserID(rs.getInt("user_id"));
                    user.setFullname(rs.getString("full_name"));
                    user.setMobilenumber(rs.getString("mobile_number"));
                    user.setBirthday(rs.getString("birthday"));
                    user.setGender(rs.getString("gender"));
                    user.setAddress(rs.getString("address"));
                    return user;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static InputStream getUpdateProfilePicture(int userId) {
        String sql = "SELECT profile_picture FROM users_update WHERE user_id = ? ORDER BY created_at DESC LIMIT 1";
        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getBinaryStream("profile_picture");
            }
        } catch (Exception e) { e.printStackTrace(); }
        return null;
    }

//    public static InputStream getUserProfilePicture(int userId) {
//        String sql = "SELECT profile_picture FROM users WHERE user_id = ?";
//        try (Connection c = DatabaseConfig.getConnection();
//             PreparedStatement ps = c.prepareStatement(sql)) {
//            ps.setInt(1, userId);
//            ResultSet rs = ps.executeQuery();
//            if (rs.next()) {
//                return rs.getBinaryStream("profile_picture");
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return null;
//    }

    public static InputStream getUserProfilePicture(int userId) {
        return null; // Stop the database from even trying to look for it in 'users'
    }

    public static boolean insertProfileUpdate(int userId, String fullName, String mobileNumber, String birthday, String gender, String address, java.io.File imageFile) {
        String sql = "INSERT INTO users_update (user_id, full_name, mobile_number, birthday, gender, address, profile_picture) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (java.sql.Connection c = DatabaseConfig.getConnection();
             java.sql.PreparedStatement ps = c.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ps.setString(2, fullName);
            ps.setString(3, mobileNumber);
            ps.setString(4, birthday);
            ps.setString(5, gender);
            ps.setString(6, address);

            if (imageFile != null) {
                java.io.FileInputStream fis = new java.io.FileInputStream(imageFile);
                ps.setBinaryStream(7, fis, (int) imageFile.length());
            } else {
                java.io.InputStream existingImage = getUpdateProfilePicture(userId);
                if (existingImage == null) {
                    existingImage = getUserProfilePicture(userId);
                }
                ps.setBinaryStream(7, existingImage);
            }

            int rowsAffected = ps.executeUpdate();
            return rowsAffected > 0;

        } catch (Exception e) {
            System.err.println("[UserDAO] Error inserting profile update: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    public static boolean verifyPassword(int userId, String password) {
        String sql = "SELECT password FROM users WHERE user_id = ? AND password = ?";
        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next(); // Returns true if a match is found
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean updatePassword(int userId, String newPassword) {
        String sql = "UPDATE users SET password = ? WHERE user_id = ?";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, newPassword);
            pstmt.setInt(2, userId);

            return pstmt.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
