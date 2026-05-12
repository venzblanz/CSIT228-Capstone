package com.javafx.csit228capstone.screens.Account;

import com.javafx.csit228capstone.helper.MenuController;
import com.javafx.csit228capstone.model.QueueTicket;
import com.javafx.csit228capstone.model.User;
import com.javafx.csit228capstone.utils.AnimationHelper;
import com.javafx.csit228capstone.utils.QueueLineDAO;
import com.javafx.csit228capstone.utils.SceneNavigator;
import com.javafx.csit228capstone.utils.SessionManager;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class QueueHistoryController {
    @FXML private TableView<QueueTicket> queueTable;
    @FXML private TableColumn<QueueTicket, Integer> queueNumberColumn;
    @FXML private TableColumn<QueueTicket, String> serviceColumn;
    @FXML private TableColumn<QueueTicket, String> statusColumn;
    @FXML private TableColumn<QueueTicket, String> dateColumn;
    @FXML private TableColumn<QueueTicket, String> staffColumn;
    @FXML private TableColumn<QueueTicket, String> departmentColumn;

    @FXML private MenuButton departmentFilter;
    @FXML private MenuButton statusFilter;
    @FXML private CheckMenuItem pendingStatus;
    @FXML private CheckMenuItem completedStatus;
    @FXML private CheckMenuItem cancelledStatus;
    @FXML private CheckMenuItem allStatus;
    @FXML private CheckMenuItem genWellnessDept;
    @FXML private CheckMenuItem womenHealthDept;
    @FXML private CheckMenuItem specialFieldsDept;
    @FXML private CheckMenuItem diagnosticsLabDept;
    @FXML private CheckMenuItem allDept;
    @FXML private DatePicker dateFromFilter;
    @FXML private DatePicker dateToFilter;
    @FXML private MenuController menuController;
    @FXML private ImageView backIconBtn;
    @FXML private Label             backBtn;
    @FXML private VBox historyScreen;

    private final SceneNavigator sceneNavigator = SceneNavigator.getInstance();

    private boolean updating;

    public void initialize() {
        AnimationHelper.fadeIn(historyScreen);
        menuController.setActiveButton(menuController.getAccountBtn());
        queueTable.setSelectionModel(null);
        backBtn.setOnMouseClicked(e -> onBack());

        initializeDatePicker();
        initializeFilters();
        initializeTable();
    }

    @FXML private void clearFilters() {
        dateFromFilter.setValue(null);
        dateToFilter.setValue(null);

        allDept.setSelected(false);
        genWellnessDept.setSelected(false);
        womenHealthDept.setSelected(false);
        specialFieldsDept.setSelected(false);
        diagnosticsLabDept.setSelected(false);
        updateMenuText("Select Department", departmentFilter);

        allStatus.setSelected(false);
        pendingStatus.setSelected(false);
        completedStatus.setSelected(false);
        cancelledStatus.setSelected(false);
        updateMenuText("Select Status", statusFilter);
    }

    @FXML private void loadHistory() {
        LocalDate fromDate = dateFromFilter.getValue();
        LocalDate toDate = dateToFilter.getValue();

        List<String> selectedStatuses = new ArrayList<>();
        if (pendingStatus.isSelected()) selectedStatuses.add("Waiting");
        if (completedStatus.isSelected()) selectedStatuses.add("Completed");
        if (cancelledStatus.isSelected()) selectedStatuses.add("Cancelled");

        List<String> selectedDepts = new ArrayList<>();
        if (genWellnessDept.isSelected()) selectedDepts.add("General Wellness");
        if (womenHealthDept.isSelected()) selectedDepts.add("Women's Health");
        if (specialFieldsDept.isSelected()) selectedDepts.add("Specialized Fields");
        if (diagnosticsLabDept.isSelected()) selectedDepts.add("Diagnostics and Laboratory");

        if (selectedStatuses.isEmpty() || selectedDepts.isEmpty()) {
            queueTable.getItems().clear();
            queueTable.setPlaceholder(new Label("No records found."));
            return;
        }

        User currentUser = SessionManager.getInstance().getCurrentUser();

        if (currentUser == null) {
            System.out.println("No user logged in!");
            return;
        }

        List<QueueTicket> userRecords;

        if ("admin".equalsIgnoreCase(currentUser.getRole())) {
            userRecords = QueueLineDAO.getAllRecords();
        } else {
            userRecords = QueueLineDAO.getRecentQueue(currentUser.getUserID());
        }

        if (userRecords == null) {
            userRecords = new ArrayList<>();
        }

        List<QueueTicket> filteredList = userRecords.stream()
                .filter(record -> selectedStatuses.contains(record.getStatus()))
                .filter(record -> selectedDepts.contains(record.getDepartment()))
                .filter(record -> {
                    if (fromDate == null && toDate == null) return true;
                    LocalDate d = record.getCreatedAt().toLocalDate();
                    if (d == null) return false;
                    boolean afterFrom = (fromDate == null || !d.isBefore(fromDate));
                    boolean beforeTo = (toDate == null || !d.isAfter(toDate));
                    return afterFrom && beforeTo;
                })
                .toList();

        queueTable.getItems().setAll(filteredList);
    }

    private void onBack(){
        sceneNavigator.navigate("/com/javafx/csit228capstone/account/myaccount.fxml", backBtn, "/styles/account.css");
    }

    private void initializeDatePicker() {
        dateFromFilter.valueProperty().addListener((obs, oldDate, newDate) -> {
            if (newDate != null) {
                dateToFilter.setDayCellFactory(picker -> new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty || item == null) {
                            return;
                        }

                        if (item.isBefore(newDate)) {
                            setDisable(true);
                            setStyle("-fx-background-color: #ffc0cb;");
                        }
                    }
                });

                if (dateToFilter.getValue() != null &&
                        dateToFilter.getValue().isBefore(newDate)) {
                    dateToFilter.setValue(null);
                }
            }
        });

        dateToFilter.valueProperty().addListener((obs, oldDate, newDate) -> {
            if (newDate != null) {
                dateFromFilter.setDayCellFactory(picker -> new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);

                        if (empty || item == null) return;

                        if (item.isAfter(newDate)) {
                            setDisable(true);
                        }
                    }
                });

                if (dateFromFilter.getValue() != null &&
                        dateFromFilter.getValue().isAfter(newDate)) {
                    dateFromFilter.setValue(null);
                }
            }
        });
    }

    private void initializeFilters() {
        allDept.setSelected(true);

        genWellnessDept.setSelected(true);
        womenHealthDept.setSelected(true);
        specialFieldsDept.setSelected(true);
        diagnosticsLabDept.setSelected(true);

        updateMenuText("Select Department", departmentFilter);

        allStatus.setSelected(true);

        pendingStatus.setSelected(true);
        completedStatus.setSelected(true);
        cancelledStatus.setSelected(true);

        updateMenuText("Select Status", statusFilter);

        pendingStatus.selectedProperty().addListener((obs, oldVal, newVal) -> updateMenuText("Select Status", statusFilter));
        completedStatus.selectedProperty().addListener((obs, oldVal, newVal) -> updateMenuText("Select Status", statusFilter));
        cancelledStatus.selectedProperty().addListener((obs, oldVal, newVal) -> updateMenuText("Select Status", statusFilter));
        allStatus.selectedProperty().addListener((obs, oldVal, newVal) -> {

            if (updating) return;
            updating = true;

            pendingStatus.setSelected(newVal);
            completedStatus.setSelected(newVal);
            cancelledStatus.setSelected(newVal);

            updating = false;

            updateMenuText("Select Status", statusFilter);
        });
        ChangeListener<Boolean> syncAll = (obs, oldVal, newVal) -> {
            if (updating) return;
            updating = true;
            boolean allSelected = pendingStatus.isSelected() && completedStatus.isSelected() && cancelledStatus.isSelected();
            allStatus.setSelected(allSelected);

            updating = false;

            updateMenuText("Select Status", statusFilter);
        };

        pendingStatus.selectedProperty().addListener(syncAll);
        completedStatus.selectedProperty().addListener(syncAll);
        cancelledStatus.selectedProperty().addListener(syncAll);


        genWellnessDept.selectedProperty().addListener((obs, oldVal, newVal) -> updateMenuText("Select Department", departmentFilter));
        womenHealthDept.selectedProperty().addListener((obs, oldVal, newVal) -> updateMenuText("Select Department", departmentFilter));
        specialFieldsDept.selectedProperty().addListener((obs, oldVal, newVal) -> updateMenuText("Select Department", departmentFilter));
        diagnosticsLabDept.selectedProperty().addListener((obs, oldVal, newVal) -> updateMenuText("Select Department", departmentFilter));
        allDept.selectedProperty().addListener((obs, oldVal, newVal) -> {

            if (updating) return;
            updating = true;

            genWellnessDept.setSelected(newVal);
            womenHealthDept.setSelected(newVal);
            specialFieldsDept.setSelected(newVal);
            diagnosticsLabDept.setSelected(newVal);

            updating = false;

            updateMenuText("Select Department", departmentFilter);
        });
        ChangeListener<Boolean> syncAllDept = (obs, oldVal, newVal) -> {

            if (updating) return;
            updating = true;

            boolean allSelected = genWellnessDept.isSelected() && womenHealthDept.isSelected() && specialFieldsDept.isSelected() && diagnosticsLabDept.isSelected();
            allDept.setSelected(allSelected);

            updating = false;

            updateMenuText("Select Department", departmentFilter);
        };

        genWellnessDept.selectedProperty().addListener(syncAllDept);
        womenHealthDept.selectedProperty().addListener(syncAllDept);
        specialFieldsDept.selectedProperty().addListener(syncAllDept);
        diagnosticsLabDept.selectedProperty().addListener(syncAllDept);
    }

    private void initializeTable() {
        serviceColumn.setReorderable(false);
        statusColumn.setReorderable(false);
        dateColumn.setReorderable(false);
        staffColumn.setReorderable(false);
        queueNumberColumn.setReorderable(false);
        departmentColumn.setReorderable(false);
        queueTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        serviceColumn.prefWidthProperty().bind(queueTable.widthProperty().multiply(0.25));
        statusColumn.prefWidthProperty().bind(queueTable.widthProperty().multiply(0.10));
        dateColumn.prefWidthProperty().bind(queueTable.widthProperty().multiply(0.15));
        departmentColumn.prefWidthProperty().bind(queueTable.widthProperty().multiply(0.20));
        staffColumn.prefWidthProperty().bind(queueTable.widthProperty().multiply(0.20));
        queueNumberColumn.prefWidthProperty().bind(queueTable.widthProperty().multiply(0.10));
        queueNumberColumn.setCellValueFactory(new PropertyValueFactory<>("queueNumber"));

        serviceColumn.setCellValueFactory(new PropertyValueFactory<>("purpose"));

        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        dateColumn.setCellValueFactory(cellData -> {
            LocalDate date = cellData.getValue().getCreatedAt().toLocalDate();
            return new javafx.beans.property.SimpleStringProperty(date != null ? date.toString() : "N/A");
        });

        staffColumn.setCellValueFactory(new PropertyValueFactory<>("staff"));

        departmentColumn.setCellValueFactory(new PropertyValueFactory<>("department"));

        loadHistory();
    }

    private void updateMenuText(String text, MenuButton menuButton) {

        List<String> selected = new ArrayList<>();

        for (MenuItem item : menuButton.getItems()) {
            if (item instanceof CheckMenuItem cmi) {
                if (cmi.isSelected()) {
                    selected.add(cmi.getText());
                }
            }
        }

        if (selected.isEmpty()) {
            menuButton.setText(text);
            return;
        }
        if ((menuButton == departmentFilter && allDept.isSelected()) || (menuButton == statusFilter && allStatus.isSelected())) {
            menuButton.setText("All");
            return;
        }

        menuButton.setText(String.join(", ", selected));
    }
}
