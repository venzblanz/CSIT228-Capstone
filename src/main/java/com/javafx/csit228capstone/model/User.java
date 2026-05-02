package com.javafx.csit228capstone.model;

import java.io.Serializable;

// TODO finalize User fields
//  I only made this based on the register page which contains these fields

public class User implements Serializable {
    private int userID;
    private String fullname;
    private String mobilenumber;
    private String email;
    private String role;
    private transient String password;

    public User(int userID, String fullname, String mobilenumber, String email, String password, String role) {
        this.userID = userID;
        this.fullname = fullname;
        this.mobilenumber = mobilenumber;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public int getUserID() { return userID; }
    public String getFullname() { return fullname; }
    public String getMobilenumber() { return mobilenumber; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
}
