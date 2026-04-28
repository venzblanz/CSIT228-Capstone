package com.javafx.csit228capstone.screens.patient_records;

import javafx.fxml.FXML;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableView;

public class PatientRecordsController {
    @FXML
    ChoiceBox<String> sortOrderOption;
    @FXML ChoiceBox<String> filterOption;
    @FXML ChoiceBox<String> serviceOption;
    @FXML ChoiceBox<String> statusOption;
    @FXML TableView<String> patientRecordsTable;

    public void initialize() {

    }
}
