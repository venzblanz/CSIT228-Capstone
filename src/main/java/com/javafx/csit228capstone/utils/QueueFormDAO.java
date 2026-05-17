package com.javafx.csit228capstone.utils;
import javax.xml.transform.Result;
import java.security.DrbgParameters;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.LocalDate;

public class QueueFormDAO {
    public static int addForm(String firstName, String middleName, String lastName, LocalDate birthDate, int age, String gender, String civilStatus, String symptoms, String patientType, String address, String nationality, String religion, String contactNumber, String emailAddress, String emergencyPerson, String emergencyRelation, String emergencyNumber, String formType, String time, LocalDate date, String service)
    {
        String sql = "insert into queue_form (" +
                "user_id, " +
                "first_name, " +
                "middle_initial, " +
                "last_name, " +
                "birth_date, " +
                "age, " +
                "gender, " +
                "civil_status, " +
                "patient_type, " +
                "address, " +
                "nationality, " +
                "religion, " +
                "contact_number," +
                "email_address," +
                "emergency_person," +
                "emergency_person_relation," +
                "emergency_person_number," +
                "form_type, " +
                "additional_notes, " +
                "picked_time, " +
                "picked_date, " +
                "picked_service) " +
                "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try(Connection c = DatabaseConfig.getConnection();
            PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            int user_id = SessionManager.getInstance().getUserId();
            ps.setInt(1, user_id);
            ps.setString(2, firstName);
            ps.setString(3, middleName);
            ps.setString(4, lastName);
            ps.setDate(5, java.sql.Date.valueOf(birthDate));
            ps.setInt(6, age);
            ps.setString(7, gender);
            ps.setString(8, civilStatus);
            ps.setString(9, patientType);
            ps.setString(10, address);
            ps.setString(11, nationality);
            ps.setString(12, religion);
            ps.setString(13, contactNumber);
            ps.setString(14, emailAddress);
            ps.setString(15, emergencyPerson);
            ps.setString(16, emergencyRelation);
            ps.setString(17, emergencyNumber);
            ps.setString(18, formType);
            ps.setString(19, symptoms);
            ps.setString(20, time);
            ps.setDate(21, java.sql.Date.valueOf(date));
            ps.setString(22, service);
            int rows = ps.executeUpdate();
            if(rows == 0) {
                return -1;
            }
            try(ResultSet rs = ps.getGeneratedKeys()) {
                if(rs.next()){
                    return rs.getInt(1);
                }
            }
        }catch(Exception e){
            System.err.println("[QueueFormDAO] Error adding form to the database " + e.getMessage());
        }
        return -1;
    }
    public static int addSchedule(int id, String pickedTime, LocalDate pickedDate, String pickedService){
        String sql = """
                insert into queue_form(picked_time, picked_date, picked_service) values(?,?,?) where form_id = id
                """;
        try(Connection c = DatabaseConfig.getConnection();
            PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            ps.setString(1, pickedTime);
            ps.setDate(2, java.sql.Date.valueOf(pickedDate));
            ps.setString(3, pickedService);
            ps.executeUpdate();

            try(ResultSet rs = ps.getGeneratedKeys()){
                if(rs.next()) {
                    return rs.getInt(1);
                }
            }
        }catch (Exception e){
            System.err.println("[QueueFormDAO] Error adding schedule to the database " + e.getMessage());
        }
        return -1;
    }
}
