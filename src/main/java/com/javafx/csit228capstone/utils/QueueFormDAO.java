package com.javafx.csit228capstone.utils;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class QueueFormDAO {
    public static int addForm(String fname, String mi,
                              String lname,
                              int age,
                              String gender,
                              String purpose,
                              String symptoms,
                              String ptype,
                              String contactnumber,
                              String formtype)
    {
        String sql = "insert into queue_form (user_id, " +
                "first_name, " +
                "middle_initial, " +
                "last_name, " +
                "age, " +
                "gender, " +
                "purpose, " +
                "symptoms, " +
                "patient_type, " +
                "contact_number," +
                "form_type) " +
                "values (?,?,?,?,?,?,?,?,?,?,?)";

        try(Connection c = DatabaseConfig.getConnection();
            PreparedStatement ps = c.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)){
            int user_id = SessionManager.getInstance().getUserId();
            ps.setInt(1, user_id);
            ps.setString(2, fname);
            ps.setString(3, mi);
            ps.setString(4, lname);
            ps.setInt(5, age);
            ps.setString(6, gender);
            ps.setString(7, purpose);
            ps.setString(8, symptoms);
            ps.setString(9, ptype);
            ps.setString(10, contactnumber);
            ps.setString(11, formtype);
            int rows = ps.executeUpdate();
            if(rows > 0) return 1;
            try(ResultSet rs = ps.getResultSet()) {
                if(rs.next()){
                    return rs.getInt("form_id");
                }
            }
        }catch(Exception e){
            System.err.println("[QueueFormDAO] Error adding form to the database " + e.getMessage());
        }
        return -1;
    }
}
