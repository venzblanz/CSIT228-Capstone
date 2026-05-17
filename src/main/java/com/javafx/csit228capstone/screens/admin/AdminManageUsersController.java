package com.javafx.csit228capstone.screens.admin;

import com.javafx.csit228capstone.model.User;
import com.javafx.csit228capstone.utils.DatabaseConfig;
import com.javafx.csit228capstone.utils.UserDAO;
import com.javafx.csit228capstone.utils.SceneNavigator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.util.Callback;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Comparator;

public class AdminManageUsersController {

    @FXML private TextField searchField;
    @FXML private ComboBox<String> sortComboBox;
    @FXML private TableView<User> userTable;
    @FXML private TableColumn<User, Integer> colId;
    @FXML private TableColumn<User, String> colName, colMobile, colDate, colStatus;
    @FXML private TableColumn<User, Void> colAction;

    private final ObservableList<User> userData = FXCollections.observableArrayList();
    private FilteredList<User> filteredData;

    @FXML
    public void initialize() {
        setupTable();
        loadDataFromDatabase();
        setupSearchAndFilterLogic();
        setupStatusBadgeColumn();
        setupActionControlColumn();
        setupRowClickNavigation();
    }

    private void setupTable() {
        colId.setCellValueFactory(new PropertyValueFactory<>("userID"));
        colName.setCellValueFactory(new PropertyValueFactory<>("fullname"));
        colMobile.setCellValueFactory(new PropertyValueFactory<>("mobilenumber"));
        colDate.setCellValueFactory(new PropertyValueFactory<>("birthday"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("status"));
    }

    private void loadDataFromDatabase() {
        userData.clear();
        String query = "SELECT patient_id, full_name, mobile_number, birthday, gender, address, status FROM patients";

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

                String status = rs.getString("status");
                user.setStatus(status == null || status.isBlank() ? "active" : status);

                userData.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setupStatusBadgeColumn() {
        colStatus.setCellFactory(param -> new TableCell<>() {
            private final Label badgeLabel = new Label();
            private final HBox wrapper = new HBox(badgeLabel);
            {
                wrapper.setAlignment(Pos.CENTER);
            }

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableView() == null || getIndex() >= getTableView().getItems().size()) {
                    setGraphic(null);
                } else {
                    User user = getTableView().getItems().get(getIndex());
                    if (user != null) {
                        String currentStatus = user.getStatus() != null ? user.getStatus().toLowerCase() : "active";
                        badgeLabel.setText(currentStatus.toUpperCase());

                        if ("active".equals(currentStatus)) {
                            badgeLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 12px; -fx-padding: 4 12 4 12; -fx-background-radius: 12; -fx-background-color: #E6F4EA !important; -fx-text-fill: #137333 !important;");
                        } else {
                            badgeLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 12px; -fx-padding: 4 12 4 12; -fx-background-radius: 12; -fx-background-color: #FCE8E6 !important; -fx-text-fill: #C5221F !important;");
                        }

                        setAlignment(Pos.CENTER);
                        setGraphic(wrapper);
                    } else {
                        setGraphic(null);
                    }
                }
            }
        });
    }

    private void setupActionControlColumn() {
        colAction.setCellFactory(param -> new TableCell<>() {
            private ComboBox<String> dropdown;
            private Button saveBtn;
            private HBox container;
            {
                dropdown = new ComboBox<>(FXCollections.observableArrayList("active", "inactive"));
                saveBtn = new Button("Save");
                container = new HBox(10, dropdown, saveBtn);

                container.setAlignment(Pos.CENTER);
                HBox.setHgrow(dropdown, Priority.ALWAYS);
                container.setMaxWidth(Double.MAX_VALUE);

                dropdown.setPrefWidth(110);
                dropdown.setPrefHeight(32);
                dropdown.setStyle("-fx-font-size: 13px; -fx-text-fill: #1E293B; -fx-cursor: hand;");

                saveBtn.setFont(javafx.scene.text.Font.font("System", javafx.scene.text.FontWeight.BOLD, 12));
                saveBtn.setPrefWidth(65);
                saveBtn.setPrefHeight(32);
                saveBtn.setStyle("-fx-background-color: #7A0016; -fx-text-fill: white; -fx-background-radius: 6; -fx-cursor: hand;");

                saveBtn.setOnAction(event -> {
                    User user = getTableView().getItems().get(getIndex());
                    String targetStatus = dropdown.getValue();
                    if (user != null && targetStatus != null) {
                        handleCommitChanges(user, targetStatus);
                    }
                });
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                if (empty || getTableView() == null || getIndex() >= getTableView().getItems().size()) {
                    setGraphic(null);
                } else {
                    User user = getTableView().getItems().get(getIndex());
                    if (user != null) {
                        String statusVal = (user.getStatus() != null) ? user.getStatus().toLowerCase() : "active";
                        dropdown.setValue(statusVal);

                        setGraphic(container);
                        setAlignment(Pos.CENTER);
                    } else {
                        setGraphic(null);
                    }
                }
            }
        });
    }

    private void handleCommitChanges(User user, String targetStatus) {
        if (targetStatus.equalsIgnoreCase(user.getStatus())) {
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Save Records");
        alert.setHeaderText("Update status for " + user.getFullname() + "?");
        alert.setContentText("Change access status permissions to '" + targetStatus + "'?");

        if (alert.showAndWait().orElse(ButtonType.CANCEL) == ButtonType.OK) {
            boolean success = UserDAO.updatePatientStatusByPatientId(user.getUserID(), targetStatus);
            if (success) {
                user.setStatus(targetStatus);
                userTable.refresh();
                applyFilters();
            }
        } else {
            userTable.refresh();
        }
    }

    private void setupSearchAndFilterLogic() {
        filteredData = new FilteredList<>(userData, p -> true);
        searchField.textProperty().addListener((observable, oldValue, newValue) -> applyFilters());

        SortedList<User> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(userTable.comparatorProperty());
        userTable.setItems(sortedData);

        sortComboBox.setItems(FXCollections.observableArrayList(
                "Default View", "Name (A-Z)", "ID (Ascending)", "Show Active Only", "Show Inactive Only"
        ));
        sortComboBox.getSelectionModel().select("Default View");
        sortComboBox.setOnAction(e -> applyFilters());
    }

    private void applyFilters() {
        String searchText = searchField.getText() == null ? "" : searchField.getText().toLowerCase().trim();
        String sortSelection = sortComboBox.getValue() == null ? "Default View" : sortComboBox.getValue();

        filteredData.setPredicate(user -> {
            boolean matchesSearch = searchText.isEmpty() ||
                    user.getFullname().toLowerCase().contains(searchText) ||
                    String.valueOf(user.getUserID()).contains(searchText);

            if (!matchesSearch) return false;

            if ("Show Active Only".equals(sortSelection)) {
                return "active".equalsIgnoreCase(user.getStatus());
            } else if ("Show Inactive Only".equals(sortSelection)) {
                return "inactive".equalsIgnoreCase(user.getStatus());
            }
            return true;
        });

        if ("Name (A-Z)".equals(sortSelection)) {
            userData.sort(Comparator.comparing(User::getFullname));
        } else if ("ID (Ascending)".equals(sortSelection)) {
            userData.sort(Comparator.comparing(User::getUserID));
        }
    }

    private void setupRowClickNavigation() {
        userTable.setRowFactory(tv -> {
            TableRow<User> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (!row.isEmpty() && event.getClickCount() == 2) {
                    User selectedUser = row.getItem();
                    SceneNavigator.getInstance().navigate(
                            "/com/javafx/csit228capstone/account/viewprofile.fxml",
                            userTable,
                            "/styles/admin-management.css",
                            (Object controller) -> {
                                if (controller instanceof com.javafx.csit228capstone.screens.Account.ViewProfileController) {
                                    var profileCtrl = (com.javafx.csit228capstone.screens.Account.ViewProfileController) controller;
                                    profileCtrl.setPatientView(selectedUser);
                                    if (profileCtrl.getEditProfileBtn() != null) {
                                        profileCtrl.getEditProfileBtn().setVisible(false);
                                        profileCtrl.getEditProfileBtn().setManaged(false);
                                    }
                                }
                            }
                    );
                }
            });
            return row;
        });
    }

    @FXML
    private void handleClearFilters() {
        searchField.clear();
        sortComboBox.getSelectionModel().select("Default View");
        loadDataFromDatabase();
        applyFilters();
    }
}