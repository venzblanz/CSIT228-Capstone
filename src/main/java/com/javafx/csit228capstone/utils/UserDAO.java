package com.javafx.csit228capstone.utils;

import com.javafx.csit228capstone.model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

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
                                rs.getInt("id"),
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
                    return rs.getInt("id");
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
}
