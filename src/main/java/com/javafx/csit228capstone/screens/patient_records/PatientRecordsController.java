package com.javafx.csit228capstone.screens.patient_records;

import com.javafx.csit228capstone.model.QueueHistory;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PatientRecordsController {
    @FXML private TableView<QueueHistory> queueTable;
    @FXML private TableColumn<QueueHistory, Integer> queueNumberColumn;
    @FXML private TableColumn<QueueHistory, String> patientColumn;
    @FXML private TableColumn<QueueHistory, String> serviceColumn;
    @FXML private TableColumn<QueueHistory, String> statusColumn;
    @FXML private TableColumn<QueueHistory, String> dateColumn;
    @FXML private TableColumn<QueueHistory, String> staffColumn;

    @FXML private MenuButton serviceFilter;
    @FXML private MenuButton statusFilter;
    @FXML private CheckMenuItem pendingStatus;
    @FXML private CheckMenuItem completedStatus;
    @FXML private CheckMenuItem cancelledStatus;
    @FXML private DatePicker dateFromFilter;
    @FXML private DatePicker dateToFilter;

    @FXML private Button loadHistoryBtn;

    public void initialize() {
        LocalDate selectedDateFrom = dateFromFilter.getValue();
        LocalDate selectedDateTo = dateToFilter.getValue();

        pendingStatus.selectedProperty().addListener((obs, oldVal, newVal) -> updateMenuText());
        completedStatus.selectedProperty().addListener((obs, oldVal, newVal) -> updateMenuText());
        cancelledStatus.selectedProperty().addListener((obs, oldVal, newVal) -> updateMenuText());
    }

    private void updateMenuText() {

        List<String> selected = new ArrayList<>();

        for (MenuItem item : statusFilter.getItems()) {
            if (item instanceof CheckMenuItem cmi && cmi.isSelected()) {
                selected.add(cmi.getText());
            }
        }

        if (selected.isEmpty()) {
            statusFilter.setText("Select Status");
        } else {
            statusFilter.setText(String.join(", ", selected));
        }
    }
}
