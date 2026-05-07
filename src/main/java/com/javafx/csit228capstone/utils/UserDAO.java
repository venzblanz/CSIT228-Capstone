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
    public static User authenticate(String username, String password){
        String sql = "select * from users where email = ?";
        try(Connection c = DatabaseConfig.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)){
            ps.setString(1, username);

            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    String storedPass = rs.getString("password");
                    if(storedPass.equals(password)){
                        return new User(
                                rs.getInt("user_id"),
                                rs.getString("full_name"),
                                rs.getString("mobile_number"),
                                rs.getString("email"),
                                storedPass,
                                rs.getString("role")
                        );
                    }
                }
            }
        }catch(Exception e){
            System.err.println("[UserDAO] Error authenticating user: " + e.getMessage());
        }
        return null;
    }

    // register
    public static int register(String fullname, String mobilenumber, String email, String password){
        if(emailExists(email)) return -2;
        String sql = "insert into users (full_name, mobile_number, email, password) values (?, ?, ?, ?)";
        try(Connection c = DatabaseConfig.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)){
            ps.setString(1, fullname);
            ps.setString(2, mobilenumber);
            ps.setString(3, email);
            ps.setString(4, password);
            int rows = ps.executeUpdate();
            if (rows > 0) return 1;
            try(ResultSet rs = ps.getResultSet()){
                if(rs.next()){
                    return rs.getInt("user_id");
                }
            }

        }catch (Exception e){
            System.err.println("[UserDAO] Error registering user: " + e.getMessage());
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
