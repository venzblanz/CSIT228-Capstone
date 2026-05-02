package com.javafx.csit228capstone.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// TODO create database

public class DatabaseConfig {
    public static final String URL = "jdbc:mysql://localhost:3306/javacapstone";
    public static final String USER = "root";
    public static final String PASS = "";

    public static Connection getConnection(){
        Connection c = null;

        try{
            c = DriverManager.getConnection(URL,USER,PASS);
            System.out.println("Connected to the database successfully");
        }catch (SQLException e){
            e.printStackTrace();
        }
        return c;
    }

    static void main() {
        getConnection(); //test connection
    }
}
