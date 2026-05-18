package com.javafx.csit228capstone.screens.schedule;

import com.javafx.csit228capstone.model.Service;
import com.javafx.csit228capstone.utils.ScheduleDAO;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class ScheduleCustomServiceController implements Initializable {

    @FXML private TextField nameField;
    @FXML private RadioButton rbGeneral;
    @FXML private RadioButton rbWomens;
    @FXML private RadioButton rbSpecial;
    @FXML private RadioButton rbDiag;
    @FXML private CheckBox recurringCheck;
    @FXML private Button addBtn;

    private final ToggleGroup categoryGroup = new ToggleGroup();
    private LocalDate selectedDate;
    private String timeSlot;
    private final ScheduleDAO scheduleDAO = ScheduleDAO.getInstance();
    private Consumer<Service> onServiceSelected;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        rbGeneral.setToggleGroup(categoryGroup);
        rbGeneral.setUserData("General Wellness");
        rbWomens.setToggleGroup(categoryGroup);
        rbWomens.setUserData("Women's Health");
        rbSpecial.setToggleGroup(categoryGroup);
        rbSpecial.setUserData("Specialized Fields");
        rbDiag.setToggleGroup(categoryGroup);
        rbDiag.setUserData("Diagnostics & Laboratory");
    }

    public void init(LocalDate selectedDate, String dayName, boolean inheritedRecurring, String timeSlot, Consumer<Service> onServiceSelected) {
        this.selectedDate = selectedDate;
        this.timeSlot = timeSlot;
        this.onServiceSelected = onServiceSelected;

        recurringCheck.setSelected(inheritedRecurring);
        recurringCheck.setText("Repeat weekly (every " + dayName + ")");

        nameField.textProperty().addListener((obs, o, n) -> refreshAddBtn());
        categoryGroup.selectedToggleProperty().addListener((obs, o, n) -> refreshAddBtn());
    }

    @FXML
    private void onCategorySelected() { refreshAddBtn(); }

    private void refreshAddBtn() {
        boolean ready = !nameField.getText().trim().isBlank() && categoryGroup.getSelectedToggle() != null;
        addBtn.setDisable(!ready);
    }

    @FXML
    private void onAdd() {
        String name = nameField.getText().trim();
        String serviceType = (String) categoryGroup.getSelectedToggle().getUserData();
        boolean recurring = recurringCheck.isSelected();

        new Thread(() -> {
            try {
                Service service = scheduleDAO.addCustomService(name, serviceType, selectedDate, timeSlot, recurring);
                Platform.runLater(() -> {
                    onServiceSelected.accept(service);
                    close();
                });
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    @FXML
    private void onCancel() { close(); }

    private void close() {
        Stage stage = (Stage) addBtn.getScene().getWindow();
        FadeTransition ft = new FadeTransition(Duration.millis(140), addBtn.getScene().getRoot());
        ft.setToValue(0);
        ft.setOnFinished(e -> stage.close());
        ft.play();
    }
}