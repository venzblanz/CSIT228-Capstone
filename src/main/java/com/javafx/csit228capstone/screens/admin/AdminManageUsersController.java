package com.javafx.csit228capstone.screens.admin;

import com.javafx.csit228capstone.model.User;
import com.javafx.csit228capstone.utils.DatabaseConfig;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import java.sql.*;
import java.util.Comparator;

public class AdminManageUsersController {

    @FXML private TextField searchField;
    @FXML private ComboBox<String> sortComboBox;
    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, Integer> colId;
    @FXML private TableColumn<User, String> colName, colRole, colDate, colStatus;

    private final ObservableList<User> userData = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        setupTable();
        loadDataFromDatabase();
        setupSearchAndSort();
    }

    private void setupTable() {
        // Matches User.java: userID, fullname, mobilenumber, birthday, gender
        colId.setCellValueFactory(new PropertyValueFactory<>("userID"));
        colName.setCellValueFactory(new PropertyValueFactory<>("fullname"));
        colRole.setCellValueFactory(new PropertyValueFactory<>("mobilenumber"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("birthday"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("gender"));
    }

    private void loadDataFromDatabase() {
        userData.clear();
        // Query targets the 'patients' table as seen in your phpMyAdmin
        String query = "SELECT patient_id, full_name, mobile_number, birthday, gender, address FROM patients";

        try (Connection conn = DatabaseConfig.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                User user = new User();
                user.setUserID(rs.getInt("patient_id"));
                user.setFullname(rs.getString("full_name"));
                user.setMobilenumber(rs.getString("mobile_number"));
                user.setBirthday(rs.getString("birthday"));
                user.setGender(rs.getString("gender"));
                user.setAddress(rs.getString("address"));

                userData.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setupSearchAndSort() {
        FilteredList<User> filteredData = new FilteredList<>(userData, p -> true);

        searchField.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(user -> {
                if (newValue == null || newValue.isBlank()) return true;
                String lowerCaseFilter = newValue.toLowerCase();

                if (user.getFullname().toLowerCase().contains(lowerCaseFilter)) return true;
                if (String.valueOf(user.getUserID()).contains(lowerCaseFilter)) return true;
                return false;
            });
        });

        SortedList<User> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(userTable.comparatorProperty());
        userTable.setItems(sortedData);

        sortComboBox.setItems(FXCollections.observableArrayList("Name (A-Z)", "ID (Ascending)"));
        sortComboBox.setOnAction(e -> {
            String selection = sortComboBox.getValue();
            if ("Name (A-Z)".equals(selection)) {
                userData.sort(Comparator.comparing(User::getFullname));
            } else if ("ID (Ascending)".equals(selection)) {
                userData.sort(Comparator.comparing(User::getUserID));
            }
        });
    }
}