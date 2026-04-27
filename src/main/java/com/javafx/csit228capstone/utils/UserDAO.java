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
        String sql = "select * from <tblname> where username = ?";
        try(Connection c = DatabaseConfig.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)){
            ps.setString(1, username);

            try(ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    String storedPass = rs.getString("password");
                    if(storedPass.equals(password)){
                        return new User(
                                rs.getInt("id"),
                                rs.getString("username"),
                                rs.getString("full_name"),
                                rs.getInt("mobile_number"),
                                rs.getString("email"),
                                storedPass
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
    public static int register(String username, String fullname, int mobilenumber, String email, String password){
        if(usernameExists(username)) return -2;
        String sql = "insert into <tblname> (username, full_name, mobile_number, email, password) values (?, ?, ?, ?, ?)";
        try(Connection c = DatabaseConfig.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)){
            ps.setString(1, username);
            ps.setString(2, fullname);
            ps.setInt(3, mobilenumber);
            ps.setString(4, email);
            ps.setString(5, password);

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
    public static boolean usernameExists(String username){
        String sql = "select 1 from <tblname> where username = ?";
        try(Connection c = DatabaseConfig.getConnection();
            PreparedStatement ps = c.prepareStatement(sql)){
            ps.setString(1, username);

            try(ResultSet rs = ps.executeQuery()){
                return rs.next();
            }
        }catch(Exception e){
            System.err.println("[UserDAO] Username already exists: " + e.getMessage());
        }
        return false;
    }
}
