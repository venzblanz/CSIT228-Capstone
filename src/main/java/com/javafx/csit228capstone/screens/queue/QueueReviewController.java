package com.javafx.csit228capstone.screens.queue;

import com.javafx.csit228capstone.helper.MenuController;
import com.javafx.csit228capstone.model.Form;
import com.javafx.csit228capstone.utils.*;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.stage.StageStyle;

import java.util.Optional;

public class QueueReviewController {
    @FXML private MenuController menuController;
    @FXML private ScrollPane scrollPane;
    @FXML private Button editBtn;
    @FXML private Button nextBtn;
    @FXML private ImageView backIconBtn;
    @FXML private Label backBtn;
    @FXML private VBox reviewScreen;

    // Form
    @FXML private TextField         fnameField;
    @FXML private TextField         miField;
    @FXML private TextField         lnameField;
    @FXML private TextField         ageField;
    @FXML private TextField         symptomsField;
    @FXML private TextField         contactField;
    @FXML private ToggleGroup       genderRadio;
    @FXML private ToggleGroup       patientTypeRadio;
    @FXML private RadioButton       maleRadio;
    @FXML private RadioButton       femaleRadio;
    @FXML private ComboBox<String>  purposeCombo;
    @FXML private RadioButton       regularRadio;
    @FXML private RadioButton       pwdRadio;
    @FXML private RadioButton       seniorRadio;
    @FXML private RadioButton       pregnantRadio;

    private final SceneNavigator sceneNavigator = SceneNavigator.getInstance();
    private final FormManager formManager = FormManager.getInstance();


    private final Form loadedForm = formManager.loadForm();
    RadioButton[] patientRadios;
    RadioButton[] genderRadios;

    private String formType;


    public void initializeData(String type){
        menuController.setActiveButton(menuController.getQueueBtn());
        formType = type;
        if(type.equals("General Wellness")){
            backBtn.setText("Back to General Wellness Schedule");
            reloadForm();
        }else if (type.equals("Women's Health")){
            backBtn.setText("Back to Women's Health Schedule");
            reloadForm();
        }else if (type.equals("Specialized Fields")){
            backBtn.setText("Back to Specialized Fields Schedule");
            reloadForm();
        }else{
            backBtn.setText("Back to Diagnostics and Laboratory Schedule");
            reloadForm();
        }
    }

    @FXML
    public void initialize() {
        AnimationHelper.fadeIn(reviewScreen);
        // Radio
        maleRadio.setUserData("Male");
        femaleRadio.setUserData("Female");
        regularRadio.setUserData("Regular");
        pregnantRadio.setUserData("Pregnant");
        pwdRadio.setUserData("Person with Disability");
        seniorRadio.setUserData("Senior Citizen");

        // initialize the Radio Arrays
        patientRadios = new RadioButton[]{
                regularRadio,
                pregnantRadio,
                pwdRadio,
                seniorRadio
        };
        genderRadios = new RadioButton[] {
                maleRadio,
                femaleRadio
        };

        backIconBtn.setOnMouseClicked(e -> onBack());
        backBtn.setOnMouseClicked(e -> onBack());
        editBtn.setOnAction(e -> onEdit());
        nextBtn.setOnAction(e -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirmation");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure all the information is correct?");
            alert.getDialogPane().getStylesheets().add(
                    getClass().getResource("/styles/alert.css").toExternalForm()
            );
            Optional<ButtonType> result = alert.showAndWait();
            if(result.isPresent() && result.get() == ButtonType.OK){
                System.out.println("Queue will now be added to the line");
                onNext();
            }else{
                System.out.println("Confirmation cancelled");
            }
        });
    }
    private void reloadForm(){
        if(loadedForm != null){
            String type = loadedForm.getPatientType();
            String gender = loadedForm.getGender();
            fnameField.setText(loadedForm.getFirstName());
            miField.setText(loadedForm.getMiddleName());
            lnameField.setText(loadedForm.getLastName());
            ageField.setText(String.valueOf(loadedForm.getAge()));
            for(RadioButton rb : genderRadios){
                if(rb.getUserData().equals(gender)){
                    genderRadio.selectToggle(rb);
                    break;
                }
            }
            purposeCombo.getSelectionModel().select(loadedForm.getPurpose());
            symptomsField.setText(loadedForm.getSymptoms());
            for(RadioButton rb : patientRadios){
                if(rb.getUserData().equals(type)){
                    patientTypeRadio.selectToggle(rb);
                    break;
                }
            }
            contactField.setText(loadedForm.getContactNumber());
        }
    }
    private void onBack(){
        sceneNavigator.navigate("/com/javafx/csit228capstone/queue/queue-schedule.fxml", backBtn, "/styles/queue-schedule.css", (QueueScheduleController queueScheduleController) -> queueScheduleController.initializeData(formType));
    }
    private void onEdit(){
        sceneNavigator.navigate("/com/javafx/csit228capstone/queue/queue-form.fxml", editBtn, "/styles/queue-form.css", (QueueFormController queueFormController) -> queueFormController.initializeData(formType));
    }
    private void onNext(){
        sceneNavigator.navigate("/com/javafx/csit228capstone/queue/queue-number.fxml", nextBtn, "/styles/queue-number.css", (QueueNumberController queueNumberController) -> queueNumberController.initializeData(formType));
    }
}
