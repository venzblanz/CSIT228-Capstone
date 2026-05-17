package com.javafx.csit228capstone.screens.queue;

import com.javafx.csit228capstone.helper.MenuController;
import com.javafx.csit228capstone.model.Form;
import com.javafx.csit228capstone.utils.FormManager;
import com.javafx.csit228capstone.utils.SceneNavigator;
import com.javafx.csit228capstone.utils.AnimationHelper;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.time.Period;
import java.util.function.UnaryOperator;

public class QueueFormController {
    @FXML private ScrollPane        scrollPane;
    @FXML private Button            cancelBtn;
    @FXML private Button            nextBtn;
    @FXML private ImageView         backIconBtn;
    @FXML private Label             backBtn;
    @FXML private Label             typeLabel;
    @FXML private Label             error_message;
    @FXML private HBox              error_container;
    @FXML private VBox              form;
    @FXML private VBox              mainPane;
    @FXML private Pane              formColorPane;

    // data getters within the forms
    @FXML private TextField         fname_field;
    @FXML private TextField         mi_field;
    @FXML private TextField         lname_field;
    @FXML private DatePicker        birthDatePicker;
    @FXML private TextField         age_field;
    @FXML private ToggleGroup       genderRadio;
    @FXML private ToggleGroup       civilStatusRadio;
    @FXML private ToggleGroup       patientTypeRadio;
    @FXML private RadioButton       maleRadio;
    @FXML private RadioButton       femaleRadio;
    @FXML private RadioButton       regularRadio;
    @FXML private RadioButton       pwdRadio;
    @FXML private RadioButton       seniorRadio;
    @FXML private RadioButton       pregnantRadio;
    @FXML private RadioButton       singleRadio;
    @FXML private RadioButton       marriedRadio;
    @FXML private RadioButton       widowedRadio;
    @FXML private RadioButton       separatedRadio;
    @FXML private TextField         addressField;
    @FXML private ComboBox<String>  nationalityCombo;
    @FXML private ComboBox<String>  religionCombo;
    @FXML private TextField         contact_field;
    @FXML private TextField         emailAddressField;
    @FXML private TextField         contactPersonField;
    @FXML private ComboBox<String>  relationCombo;
    @FXML private TextField         emergencyContactField;
    @FXML private TextField         symptoms_field;
    @FXML private MenuController    menuController;

    private final SceneNavigator    sceneNavigator = SceneNavigator.getInstance();
    private final FormManager       formManager = FormManager.getInstance();

    // Helpers
    UnaryOperator<TextFormatter.Change> filter = change -> {
        if (change.getControlNewText().matches("\\d*")) {
            return change;
        }
        return null;
    };

    private String                  formType;
    RadioButton[]                   patientRadios;
    RadioButton[]                   civilStatusRadios;
    RadioButton[]                   genderRadios;
    Form                            loadedForm;

    public void initializeData(String type){
        loadedForm = formManager.loadForm();
        formType = type;
        if(type.equals("General Wellness")){
            typeLabel.setText("General Wellness");
            gwInit();
        }else if (type.equals("Women's Health")){
            typeLabel.setText("Women's Health");
            whInit();
        }else if (type.equals("Specialized Fields")){
            typeLabel.setText("Specialized Fields");
            sfInit();
        }else{
            typeLabel.setText("Diagnostics and Laboratory");
            dlInit();
        }
    }

    @FXML
    public void initialize() {
        AnimationHelper.fadeIn(mainPane);
        menuController.setActiveButton(menuController.getQueueBtn());
        birthDatePicker.setEditable(false);
        // for the whole page except buttons
        initializePage();

        // for the buttons only
        backIconBtn.setOnMouseClicked(e -> onBack());
        backBtn.setOnMouseClicked(e -> onBack());
        cancelBtn.setOnAction(e -> onCancel());
        nextBtn.setOnAction(e -> onNext());
    }

    private void initializePage(){
        error_message.setVisible(false);

        // set non character fields to be exclusive
        age_field.setTextFormatter(new TextFormatter<>(filter));
        contact_field.setTextFormatter(new TextFormatter<>(filter));

        // Radio
        maleRadio.setUserData("Male");
        femaleRadio.setUserData("Female");
        regularRadio.setUserData("Regular");
        pregnantRadio.setUserData("Pregnant");
        pwdRadio.setUserData("Person with Disability");
        seniorRadio.setUserData("Senior Citizen");
        singleRadio.setUserData("Single");
        widowedRadio.setUserData("Widowed");
        marriedRadio.setUserData("Married");
        separatedRadio.setUserData("Separated");

        // Combo
        nationalityCombo.setPromptText("Select Nationality...");
        nationalityCombo.getItems().addAll(
                "Filipino",
                        "American",
                        "Chinese",
                        "Japanese",
                        "Korean",
                        "Indian",
                        "Indonesian",
                        "Malaysian",
                        "Singaporean",
                        "Thai",
                        "Vietnamese",
                        "British",
                        "Canadian",
                        "Australian",
                        "German",
                        "French",
                        "Spanish",
                        "Italian",
                        "Other"
        );
        religionCombo.setPromptText("Select Religion...");
        religionCombo.getItems().addAll(
                "Roman Catholic",
                        "Iglesia ni Cristo",
                        "Born Again Christian",
                        "Protestant",
                        "Evangelical Christian",
                        "Seventh-day Adventist",
                        "Jehovah's Witness",
                        "Islam",
                        "Buddhism",
                        "Hinduism",
                        "Judaism",
                        "Sikhism",
                        "Taoism",
                        "No Religion",
                        "Other",
                        "Prefer not to say"
        );
        relationCombo.setPromptText("Select Relation...");
        relationCombo.getItems().addAll(
                "Father",
                        "Mother",
                        "Brother",
                        "Sister",
                        "Spouse",
                        "Son",
                        "Daughter",
                        "Grandfather",
                        "Grandmother",
                        "Uncle",
                        "Aunt",
                        "Cousin",
                        "Guardian",
                        "Relative",
                        "Friend",
                        "Neighbor",
                        "Caregiver",
                        "Other"
        );
        form.setOnMouseClicked(e -> clearError());

        // initialize the Radio Arrays
        patientRadios = new RadioButton[]{
                regularRadio,
                pregnantRadio,
                pwdRadio,
                seniorRadio
        };
        civilStatusRadios = new RadioButton[]{
                singleRadio,
                marriedRadio,
                widowedRadio,
                separatedRadio
        };
        genderRadios = new RadioButton[] {
                maleRadio,
                femaleRadio
        };
        birthDatePicker.valueProperty().addListener((observable, oldValue, newValue) -> {
            if(newValue == null){
                age_field.setText("0");
                return;
            }
            int age = Period.between(newValue, LocalDate.now()).getYears();
            age_field.setText(String.valueOf(age));
        });
        age_field.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null || newValue.isEmpty()) {
                seniorRadio.setDisable(false);
                return;
            }

            try {
                int age = Integer.parseInt(newValue);

                if (age < 60) {
                    seniorRadio.setDisable(true);

                    if (seniorRadio.isSelected()) {
                        patientTypeRadio.selectToggle(null);
                    }
                } else {
                    seniorRadio.setDisable(false);
                }

            } catch (NumberFormatException e) {
                seniorRadio.setDisable(true);
            }
        });
        genderRadio.selectedToggleProperty().addListener((observable, oldToggle, newToggle) -> {
            if (newToggle == null) {
                pregnantRadio.setDisable(false);
                return;
            }

            String selectedGender = newToggle.getUserData().toString();

            if (selectedGender.equals("Male")) {
                pregnantRadio.setDisable(true);

                if (pregnantRadio.isSelected()) {
                    patientTypeRadio.selectToggle(null);
                }
            } else {
                pregnantRadio.setDisable(false);
            }
        });
    }

    private void gwInit(){
        if(loadedForm != null && loadedForm.getFormType().equals("General Wellness")){
            reloadForm();
        }
    }
    private void whInit(){
        formColorPane.setStyle("-fx-background-color: #FFBCD3;");
        if(loadedForm != null && loadedForm.getFormType().equals("Women's Health")){
            reloadForm();
        }
    }
    private void sfInit(){
        formColorPane.setStyle("-fx-background-color: rgba(75, 245, 220);");
        if(loadedForm != null && loadedForm.getFormType().equals("Specialized Fields")){
            reloadForm();
        }
    }
    private void dlInit(){
        formColorPane.setStyle("-fx-background-color: #543BE9;");
        if(loadedForm != null && loadedForm.getFormType().equals("Diagnostics and Laboratory")){
            reloadForm();
        }
    }
    private void reloadForm(){
        if(loadedForm != null){
            String type = loadedForm.getPatientType();
            String gender = loadedForm.getGender();
            String civilStatus = loadedForm.getCivilStatus();

            fname_field.setText(loadedForm.getFirstName());
            mi_field.setText(loadedForm.getMiddleName());
            lname_field.setText(loadedForm.getLastName());
            birthDatePicker.setValue(loadedForm.getBirthDate());
            age_field.setText(String.valueOf(loadedForm.getAge()));
            for(RadioButton rb : genderRadios){
                if(rb.getUserData().equals(gender)){
                    genderRadio.selectToggle(rb);
                    break;
                }
            }
            for(RadioButton rb : civilStatusRadios){
                if(rb.getUserData().equals(civilStatus)){
                    civilStatusRadio.selectToggle(rb);
                    break;
                }
            }
            for(RadioButton rb : patientRadios){
                if(rb.getUserData().equals(type)){
                    patientTypeRadio.selectToggle(rb);
                    break;
                }
            }
            addressField.setText(loadedForm.getAddress());
            nationalityCombo.getSelectionModel().select(loadedForm.getNationality());
            religionCombo.getSelectionModel().select(loadedForm.getReligion());
            contact_field.setText(loadedForm.getContactNumber());
            emailAddressField.setText(loadedForm.getEmailAddress());
            contactPersonField.setText(loadedForm.getEmergencyPerson());
            relationCombo.getSelectionModel().select(loadedForm.getEmergencyRelation());
            emergencyContactField.setText(loadedForm.getEmergencyNumber());
            symptoms_field.setText(loadedForm.getSymptoms());
        }
    }
    private void onBack(){
        sceneNavigator.navigate("/com/javafx/csit228capstone/queue/queue.fxml", cancelBtn, "/styles/queue.css");
    }
    private void onCancel(){
        sceneNavigator.navigate("/com/javafx/csit228capstone/dashboard.fxml", cancelBtn, "/styles/dashboard.css");
    }
    private void onNext(){
        if (genderRadio.getSelectedToggle()                 == null ||
                patientTypeRadio.getSelectedToggle()        == null ||
                nationalityCombo.getValue()                 == null ||
                civilStatusRadio.getSelectedToggle()        == null) {
            showError("Complete all fields");
            return;
        }
        String fname = fname_field.getText().trim();
        String mi = mi_field.getText().trim();
        String lname = lname_field.getText().trim();
        LocalDate BirthDate = birthDatePicker.getValue();
        String ageText = age_field.getText().trim();
        String gender = genderRadio.getSelectedToggle().getUserData().toString();
        String civilStatus = civilStatusRadio.getSelectedToggle().getUserData().toString();
        String ptype = patientTypeRadio.getSelectedToggle().getUserData().toString();
        String address = addressField.getText().trim();
        String nationality = nationalityCombo.getSelectionModel().getSelectedItem();
        String religion = religionCombo.getSelectionModel().getSelectedItem();
        String contact = contact_field.getText().trim();
        String email =  emailAddressField.getText().trim();
        String contactPerson = contactPersonField.getText().trim();
        String contactPersonRelation = relationCombo.getSelectionModel().getSelectedItem();
        String emergencyPersonContact = emergencyContactField.getText().trim();
        String symptoms = symptoms_field.getText().trim();

        if(
                fname.isEmpty()
            ||  mi.isEmpty()
            ||  lname.isEmpty()
            ||  BirthDate == null
            ||  ageText.isEmpty()
            ||  gender.isEmpty()
            ||  address.isEmpty()
            ||  nationality.isEmpty()
            ||  ptype.isEmpty()
            ||  contact.isEmpty()
        ){
            showError("Complete all fields");
            return;
        }else if(!fname.matches("^[A-Za-zÑñ]+(?:[ .'’-][A-Za-zÑñ]+)*$")){
            showError("Please enter a valid first name");
            return;
        }else if(!lname.matches("^[A-Za-zÑñ]+(?:[ .'’-][A-Za-zÑñ]+)*$")){
            showError("Please enter a valid last name");
            return;
        }else if(!mi.matches("^[A-Za-zÑñ]?$")){
            showError("Please enter only one (1) character for the middle initial");
            return;
        }else if (!contact.matches("^[0-9]{11}$")) {
            showError("Please enter a valid 10-digit mobile number (e.g. 09123456789).");
            return;
        }

        int age;
        try {
            age = Integer.parseInt(ageText);

            if (age <= 0 || age > 130) {
                showError("Invalid age");
                return;
            }

        } catch (NumberFormatException e) {
            showError("Age must be a valid number");
            return;
        }

        if (ptype.equals("Senior Citizen") && age < 60) {
            showError("Senior Citizen patient type is only allowed for ages 60 and above");
            return;
        }

        formManager.saveForm(new Form(fname,mi,lname, BirthDate, age, gender, civilStatus, symptoms, ptype, address, nationality, religion, contact, email, contactPerson, contactPersonRelation, emergencyPersonContact, formType));
        sceneNavigator.navigate("/com/javafx/csit228capstone/queue/temp-queue-schedule.fxml", cancelBtn, "/styles/schedule-patient.css", (TempQueueScheduleController queueScheduleController) -> queueScheduleController.initializeData(formType));
    }
    private void showError(String message){
        form.setStyle("-fx-border-color: red;" +
                "-fx-border-radius: 10;");
        error_container.setVisible(true);
        error_message.setVisible(true);
        error_message.setText(message);
        error_message.setStyle("-fx-text-fill: red;");
    }
    private void clearError(){
        form.setStyle("");
        error_container.setVisible(false);
        error_message.setVisible(false);
    }
}
