package com.javafx.csit228capstone.screens.patient_records;

import com.javafx.csit228capstone.helper.MenuController;
import com.javafx.csit228capstone.model.QueueHistory;
import com.javafx.csit228capstone.model.User;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PatientRecordsController {
    @FXML private TableView<QueueHistory> queueTable;
    @FXML private TableColumn<QueueHistory, Integer> queueNumberColumn;
    @FXML private TableColumn<QueueHistory, String> serviceColumn;
    @FXML private TableColumn<QueueHistory, String> statusColumn;
    @FXML private TableColumn<QueueHistory, String> dateColumn;
    @FXML private TableColumn<QueueHistory, String> staffColumn;

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

    @FXML private Button loadHistoryBtn;

    private boolean updating;

    public void initialize() {
        menuController.setActiveButton(menuController.getAccountBtn());

        initializeDatePicker();
        initializeFilters();
        initializeTable();
    }

    @FXML private void loadHistory() {
        LocalDate fromDate = dateFromFilter.getValue();
        LocalDate toDate = dateToFilter.getValue();

        List<String> selectedStatuses = new ArrayList<>();
        if (pendingStatus.isSelected()) selectedStatuses.add("Pending");
        if (completedStatus.isSelected()) selectedStatuses.add("Completed");
        if (cancelledStatus.isSelected()) selectedStatuses.add("Cancelled");

        List<String> selectedDepts = new ArrayList<>();
        if (genWellnessDept.isSelected()) selectedDepts.add("General Wellness");
        if (womenHealthDept.isSelected()) selectedDepts.add("Women's Health");
        if (specialFieldsDept.isSelected()) selectedDepts.add("Specialized Fields");
        if (diagnosticsLabDept.isSelected()) selectedDepts.add("Diagnostics & Laboratory");

        if (selectedStatuses.isEmpty() || selectedDepts.isEmpty()) {
            queueTable.getItems().clear();
            return;
        }

        List<QueueHistory> allRecords = retrieveRecordsFromDB();
        if (allRecords == null) {
            allRecords = new ArrayList<>();
        }

        List<QueueHistory> filteredList = allRecords.stream()
                .filter(record -> selectedStatuses.contains(record.getStatus()))
                .filter(record -> selectedDepts.contains(record.getDepartment()))
                .filter(record -> {
                    if (fromDate == null && toDate == null) return true;
                    LocalDate d = record.getDate();
                    if (d == null) return false;
                    boolean afterFrom = (fromDate == null || !d.isBefore(fromDate));
                    boolean beforeTo = (toDate == null || !d.isAfter(toDate));
                    return afterFrom && beforeTo;
                })
                .toList();

        queueTable.getItems().setAll(filteredList);
    }

    // TODO
    private List<QueueHistory> retrieveRecordsFromDB() {
        return null;
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
        queueTable.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
        serviceColumn.prefWidthProperty().bind(queueTable.widthProperty().multiply(0.30));
        statusColumn.prefWidthProperty().bind(queueTable.widthProperty().multiply(0.10));
        dateColumn.prefWidthProperty().bind(queueTable.widthProperty().multiply(0.25));
        staffColumn.prefWidthProperty().bind(queueTable.widthProperty().multiply(0.25));
        queueNumberColumn.setCellValueFactory(new PropertyValueFactory<>("queueNumber"));

        serviceColumn.setCellValueFactory(new PropertyValueFactory<>("service"));

        statusColumn.setCellValueFactory(new PropertyValueFactory<>("status"));

        dateColumn.setCellValueFactory(cellData -> {
            LocalDate date = cellData.getValue().getDate();
            return new javafx.beans.property.SimpleStringProperty(date != null ? date.toString() : "N/A");
        });

        staffColumn.setCellValueFactory(new PropertyValueFactory<>("department"));

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
