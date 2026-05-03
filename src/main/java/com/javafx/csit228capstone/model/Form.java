package com.javafx.csit228capstone.model;

import java.io.Serializable;

public class Form implements Serializable {
    private String firstName;
    private String middleName;
    private String lastName;
    private int age;
    private String gender;
    private String purpose;
    private String symptoms;
    private String patientType;
    private String contactNumber;
    private String formType;

    public Form(String firstName, String middleName, String lastName, int age, String gender, String purpose, String symptoms, String patientType, String contactNumber, String formType) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
        this.purpose = purpose;
        this.symptoms = symptoms;
        this.patientType = patientType;
        this.contactNumber = contactNumber;
        this.formType = formType;
    }

    public String getFirstName() { return firstName; }
    public String getMiddleName() { return middleName; }
    public String getLastName() { return lastName; }
    public int getAge() { return age; }
    public String getGender() { return gender; }
    public String getPurpose() { return purpose; }
    public String getSymptoms() { return symptoms; }
    public String getPatientType() { return patientType; }
    public String getContactNumber() { return contactNumber; }
    public String getFormType() { return formType; }
}
