package com.javafx.csit228capstone.utils;

import java.time.LocalDate;

public class PatientIdGenerator {
    static int year = LocalDate.now().getYear() % 100;
    public static String getPatientId(int userId){
        return "MS-" + String.format("%02d", year) + "-" + String.format("%05d", userId);
    }
}
