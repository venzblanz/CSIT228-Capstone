package com.javafx.csit228capstone.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;

// TODO finalize User fields
//  I only made this based on the register page which contains these fields

public class User implements Serializable {
    private int userID;
    private String fullname;
    private String mobilenumber;
    private String email;
    private String role;
    private String birthday;
    private String gender;
    private String address;
    private String status;
    private transient String password;

    public User(int userID, String fullname, String mobilenumber, String email, String password, String role) {
        this.userID = userID;
        this.fullname = fullname;
        this.mobilenumber = mobilenumber;
        this.email = email;
        this.password = password;
        this.role = role;
        this.birthday = "Not Set";
        this.gender = "Not Set";
        this.address = "Not Set";
        this.status = "active";
    }

    public User() {
    }

    public int getUserID() { return userID; }
    public String getFullname() { return fullname; }
    public String getMobilenumber() { return mobilenumber; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }
    public String getRole() { return role; }
    public String getBirthday() { return (birthday == null || birthday.isEmpty()) ? "Not Set" : birthday; }
    public String getGender() { return (gender == null || gender.isEmpty()) ? "Not Set" : gender; }
    public String getAddress() { return (address == null || address.isEmpty()) ? "Not Set" : address; }
    public String getStatus() { return (status == null || status.isEmpty()) ? "active" : status; }

    public void setUserID(int userID) { this.userID = userID; }
    public void setFullname(String fullname) { this.fullname = fullname; }
    public void setMobilenumber(String mobilenumber) { this.mobilenumber = mobilenumber; }
    public void setEmail(String email) { this.email = email; }
    public void setRole(String role) { this.role = role; }
    public void setBirthday(String birthday) { this.birthday = birthday; }
    public void setGender(String gender) { this.gender = gender; }
    public void setAddress(String address) { this.address = address; }
    public void setStatus(String status) { this.status = status; }
    public void setPassword(String password) { this.password = password; }

    public String getAge() {
        if (birthday == null || birthday.equals("Not Set")) return "Not Set";
        try {
            LocalDate birthDate = LocalDate.parse(birthday);
            return String.valueOf(Period.between(birthDate, LocalDate.now()).getYears());
        } catch (Exception e) {
            return "Not Set";
        }
    }
}

