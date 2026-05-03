package com.javafx.csit228capstone.screens.queue;

import com.javafx.csit228capstone.model.Form;
import com.javafx.csit228capstone.utils.FormManager;
import com.javafx.csit228capstone.utils.QueueFormDAO;
import com.javafx.csit228capstone.utils.SceneNavigator;
import com.javafx.csit228capstone.utils.SessionManager;
import com.mysql.cj.Session;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.*;

public class QueueNumberController {
    @FXML private Button saveBtn;
    @FXML private Button doneBtn;
    @FXML private Label qTypeLabel;

    private final SceneNavigator sceneNavigator = SceneNavigator.getInstance();
    private final FormManager formManager = FormManager.getInstance();


    private final Form loadedForm = formManager.loadForm();
    private String qType;

    public void initializeData(String type){
        qType = type;
        if(type.equals("General Wellness")){
            qTypeLabel.setText("General Wellness");
        }else if (type.equals("Women's Health")){
            qTypeLabel.setText("Women's Health");
        }else if (type.equals("Specialized Fields")){
            qTypeLabel.setText("Specialized Fields");
        }else{
            qTypeLabel.setText("Diagnostics and Laboratory");
        }
    }

    @FXML
    public void initialize() {
        saveBtn.setOnAction(e -> onSave());
        doneBtn.setOnAction(e -> onDone());
    }
    private void onSave(){
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Confirmation");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to save?");
        alert.showAndWait();
    }
    private void onDone(){
        Form f = formManager.loadForm();
        QueueFormDAO.addForm(f.getFirstName(),
                f.getMiddleName(),
                f.getLastName(),
                f.getAge(),
                f.getGender(),
                f.getPurpose(),
                f.getSymptoms(),
                f.getPatientType(),
                f.getContactNumber(),
                f.getFormType());
        formManager.clearForm();
        sceneNavigator.navigate("/com/javafx/csit228capstone/dashboard.fxml", doneBtn, "/styles/dashboard.css");
    }
}
