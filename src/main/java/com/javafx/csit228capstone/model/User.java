package com.javafx.csit228capstone.model;

import java.io.Serializable;

// TODO finalize User fields
//  I only made this based on the register page which contains these fields

public class User implements Serializable {
    private int userID;
    private String username;
    private String fullname;
    private int mobilenumber;
    private String email;

    private transient String password;

    public User(int userID, String username, String fullname, int mobilenumber, String email, String password) {
        this.userID = userID;
        this.username = username;
        this.fullname = fullname;
        this.mobilenumber = mobilenumber;
        this.email = email;
        this.password = password;
    }

    public int getUserID() {
        return userID;
    }
    public void setUserID(int userID) {}

    public String getUsername() {
        return username;
    }

    public String getFullname() {
        return fullname;
    }

    public int getMobilenumber() {
        return mobilenumber;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }
}
