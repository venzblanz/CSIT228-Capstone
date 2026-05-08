package com.javafx.csit228capstone.utils;

import com.javafx.csit228capstone.model.User;

import java.io.File;
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
        String sql = "SELECT * FROM patients WHERE user_id = ? ORDER BY created_at DESC LIMIT 1";
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
        String sql = "SELECT profile_picture FROM patients WHERE user_id = ? ORDER BY created_at DESC LIMIT 1";
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
        return null;
    }

    public static boolean insertProfileUpdate(int userId, String fullName, String mobileNumber, String birthday, String gender, String address, java.io.File imageFile) {
        try (Connection c = DatabaseConfig.getConnection()) {
             String updateSql = "UPDATE patients SET full_name = ?, mobile_number = ?, birthday = ?, gender = ?, address = ?, profile_picture = ? WHERE user_id = ?";

            try (PreparedStatement ps = c.prepareStatement(updateSql)) {
                ps.setString(1, fullName);
                ps.setString(2, mobileNumber);

                if (birthday == null || birthday.equals("Not Set") || birthday.isEmpty()) {
                    ps.setNull(3, java.sql.Types.DATE);
                } else {
                    ps.setString(3, birthday);
                }

                ps.setString(4, gender);
                ps.setString(5, address);
                handleImageStream(ps, 6, imageFile, userId);
                ps.setInt(7, userId);

                int rows = ps.executeUpdate();

                if (rows == 0) {
                    String insertSql = "INSERT INTO patients (user_id, full_name, mobile_number, birthday, gender, address, profile_picture) VALUES (?, ?, ?, ?, ?, ?, ?)";
                    try (PreparedStatement psInsert = c.prepareStatement(insertSql)) {
                        psInsert.setInt(1, userId);
                        psInsert.setString(2, fullName);
                        psInsert.setString(3, mobileNumber);

                        if (birthday == null || birthday.equals("Not Set") || birthday.isEmpty()) {
                            psInsert.setNull(4, java.sql.Types.DATE);
                        } else {
                            psInsert.setString(4, birthday);
                        }

                        psInsert.setString(5, gender);
                        psInsert.setString(6, address);
                        handleImageStream(psInsert, 7, imageFile, userId);

                        return psInsert.executeUpdate() > 0;
                    }
                }
                return true;
            }
        } catch (Exception e) {
            System.err.println("[UserDAO] Upsert failed: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private static void handleImageStream(PreparedStatement ps, int index, File imageFile, int userId) throws Exception {
        if (imageFile != null && imageFile.exists()) {
            FileInputStream fis = new FileInputStream(imageFile);
            ps.setBinaryStream(index, fis, (int) imageFile.length());
        } else {
            InputStream existing = getUpdateProfilePicture(userId);
            if (existing != null) {
                ps.setBinaryStream(index, existing);
            } else {
                ps.setNull(index, java.sql.Types.BLOB);
            }
        }
    }
    public static boolean updatePassword(int userId, String newPassword) {
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
    public static boolean verifyPassword(int userId, String password) {
        String sql = "SELECT password FROM users WHERE user_id = ? AND password = ?";
        try (Connection c = DatabaseConfig.getConnection();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ps.setString(2, password);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


}
