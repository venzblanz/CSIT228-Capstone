package com.javafx.csit228capstone.model;

import java.time.LocalDate;

public class QueueInformation {

    private String firstName;
    private String middleName;
    private String lastName;

    private int age;
    private String gender;
    private String patientType;
    private String department;

    private String time;
    private String service;
    private LocalDate date;

    private String civilStatus;
    private String nationality;
    private String religion;

    private String address;
    private LocalDate birthdate;
    private String contact;
    private String email;

    private String emergencyContactPerson;
    private String emergencyContact;
    private String relation;

    private String symptoms;

    private String queueNumber;
    private String status;

    public QueueInformation() {}

    public QueueInformation(
            String firstName,
            String middleName,
            String lastName,
            int age,
            String gender,
            String patientType,
            String department,
            String time,
            LocalDate date,
            String service,
            String civilStatus,
            String nationality,
            String religion,
            String address,
            LocalDate birthdate,
            String contact,
            String email,
            String emergencyContactPerson,
            String emergencyContact,
            String relation,
            String symptoms,
            String queueNumber,
            String status
    ) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.age = age;
        this.gender = gender;
        this.patientType = patientType;
        this.department = department;
        this.time = time;
        this.date = date;
        this.service = service;
        this.civilStatus = civilStatus;
        this.nationality = nationality;
        this.religion = religion;
        this.address = address;
        this.birthdate = birthdate;
        this.contact = contact;
        this.email = email;
        this.emergencyContactPerson = emergencyContactPerson;
        this.emergencyContact = emergencyContact;
        this.relation = relation;
        this.symptoms = symptoms;
        this.queueNumber = queueNumber;
        this.status = status;
    }

    public String getFullName() {
        return firstName + " " +
                (middleName != null && !middleName.isEmpty()
                        ? middleName + " "
                        : "") +
                lastName;
    }

    public String getService() {
        return service;
    }
    public void setService(String service) {
        this.service = service;
    }

    public String getEmergencyContactPerson() {
        return emergencyContactPerson;
    }
    public void setEmergencyContactPerson(String emergencyContactPerson) {
        this.emergencyContactPerson = emergencyContactPerson;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        this.birthdate = birthdate;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPatientType() {
        return patientType;
    }

    public void setPatientType(String patientType) {
        this.patientType = patientType;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getCivilStatus() {
        return civilStatus;
    }

    public void setCivilStatus(String civilStatus) {
        this.civilStatus = civilStatus;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public String getReligion() {
        return religion;
    }

    public void setReligion(String religion) {
        this.religion = religion;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmergencyContact() {
        return emergencyContact;
    }

    public void setEmergencyContact(String emergencyContact) {
        this.emergencyContact = emergencyContact;
    }

    public String getRelation() {
        return relation;
    }

    public void setRelation(String relation) {
        this.relation = relation;
    }

    public String getSymptoms() {
        return symptoms;
    }

    public void setSymptoms(String symptoms) {
        this.symptoms = symptoms;
    }

    public String getQueueNumber() {
        return queueNumber;
    }

    public void setQueueNumber(String queueNumber) {
        this.queueNumber = queueNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}