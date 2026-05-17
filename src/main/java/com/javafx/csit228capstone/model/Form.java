package com.javafx.csit228capstone.model;

import java.io.Serializable;
import java.time.LocalDate;

public class Form implements Serializable {
    private String firstName;
    private String middleName;
    private String lastName;
    private LocalDate birthDate;
    private int age;
    private String gender;
    private String civilStatus;
    private String symptoms;
    private String patientType;
    private String address;
    private String nationality;
    private String religion;
    private String contactNumber;
    private String emailAddress;
    private String emergencyPerson;
    private String emergencyRelation;
    private String emergencyNumber;
    private String formType;

    public Form(String firstName, String middleName, String lastName, LocalDate birthDate, int age, String gender, String civilStatus, String symptoms, String patientType, String address, String nationality, String religion, String contactNumber, String emailAddress, String emergencyPerson, String emergencyRelation, String emergencyNumber, String formType) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.birthDate = birthDate;
        this.age = age;
        this.gender = gender;
        this.civilStatus = civilStatus;
        this.symptoms = symptoms;
        this.patientType = patientType;
        this.address = address;
        this.nationality = nationality;
        this.religion = religion;
        this.contactNumber = contactNumber;
        this.emailAddress = emailAddress;
        this.emergencyPerson = emergencyPerson;
        this.emergencyRelation = emergencyRelation;
        this.emergencyNumber = emergencyNumber;
        this.formType = formType;
    }

    public LocalDate getBirthDate() { return birthDate; }
    public String getCivilStatus() { return civilStatus; }
    public String getAddress() { return address; }
    public String getNationality() { return nationality; }
    public String getReligion() { return religion; }
    public String getEmailAddress() { return emailAddress; }
    public String getEmergencyPerson() { return emergencyPerson; }
    public String getEmergencyRelation() { return emergencyRelation; }
    public String getEmergencyNumber() { return emergencyNumber; }
    public String getFirstName() { return firstName; }
    public String getMiddleName() { return middleName; }
    public String getLastName() { return lastName; }
    public int getAge() { return age; }
    public String getGender() { return gender; }
    public String getSymptoms() { return symptoms; }
    public String getPatientType() { return patientType; }
    public String getContactNumber() { return contactNumber; }
    public String getFormType() { return formType; }
}
