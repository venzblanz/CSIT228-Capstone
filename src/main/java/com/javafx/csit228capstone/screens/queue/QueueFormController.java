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
    @FXML private TextField         age_field;
    @FXML private TextField         symptoms_field;
    @FXML private TextField         contact_field;
    @FXML private ToggleGroup       genderRadio;
    @FXML private ToggleGroup       patientTypeRadio;
    @FXML private RadioButton       maleRadio;
    @FXML private RadioButton       femaleRadio;
    @FXML private ComboBox<String>  purposeCombo;
    @FXML private RadioButton       regularRadio;
    @FXML private RadioButton       pwdRadio;
    @FXML private RadioButton       seniorRadio;
    @FXML private RadioButton       pregnantRadio;
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

        // Combo
        purposeCombo.setPromptText("Select Purpose...");
        form.setOnMouseClicked(e -> clearError());

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
        // Combo
        purposeCombo.getItems().addAll(
                "Consultation",
                "Check-up",
                "Vaccination",
                "Follow-up",
                "Medical Certificate"
        );
        if(loadedForm != null && loadedForm.getFormType().equals("General Wellness")){
            reloadForm();
        }
    }
    private void whInit(){
        formColorPane.setStyle("-fx-background-color: #FFBCD3;");
        // Combo
        purposeCombo.getItems().addAll(
                "Prenatal Check-up",
                "Postnatal Care",
                "Menstrual Concerns",
                "Breast Examination",
                "Family Planning Consultation",
                "Pap Smear / Screening"
        );
        if(loadedForm != null && loadedForm.getFormType().equals("Women's Health")){
            reloadForm();
        }
    }
    private void sfInit(){
        formColorPane.setStyle("-fx-background-color: rgba(75, 245, 220);");
        // Combo
        purposeCombo.getItems().addAll(
                "Cardiology Consultation",
                "Dermatology Check",
                "Orthopedic Consultation",
                "Pediatrics Consultation",
                "Neurology Consultation",
                "ENT (Ear, Nose, Throat)"
        );
        if(loadedForm != null && loadedForm.getFormType().equals("Specialized Fields")){
            reloadForm();
        }
    }
    private void dlInit(){
        formColorPane.setStyle("-fx-background-color: #543BE9;");
        // Combo
        purposeCombo.getItems().addAll(
                "Blood Test",
                "Urinalysis",
                "X-ray",
                "Ultrasound",
                "ECG",
                "Medical Examination Package"
        );
        if(loadedForm != null && loadedForm.getFormType().equals("Diagnostics and Laboratory")){
            reloadForm();
        }
    }
    private void reloadForm(){
        if(loadedForm != null){
            String type = loadedForm.getPatientType();
            String gender = loadedForm.getGender();
            fname_field.setText(loadedForm.getFirstName());
            mi_field.setText(loadedForm.getMiddleName());
            lname_field.setText(loadedForm.getLastName());
            age_field.setText(String.valueOf(loadedForm.getAge()));
            for(RadioButton rb : genderRadios){
                if(rb.getUserData().equals(gender)){
                    genderRadio.selectToggle(rb);
                    break;
                }
            }
            purposeCombo.getSelectionModel().select(loadedForm.getPurpose());
            symptoms_field.setText(loadedForm.getSymptoms());
            for(RadioButton rb : patientRadios){
                if(rb.getUserData().equals(type)){
                    patientTypeRadio.selectToggle(rb);
                    break;
                }
            }
            contact_field.setText(loadedForm.getContactNumber());
        }
    }
    private void onBack(){
        sceneNavigator.navigate("/com/javafx/csit228capstone/queue/queue.fxml", cancelBtn, "/styles/queue.css");
    }
    private void onCancel(){
        sceneNavigator.navigate("/com/javafx/csit228capstone/dashboard.fxml", cancelBtn, "/styles/dashboard.css");
    }
    private void onNext(){
        if (genderRadio.getSelectedToggle() == null ||
                patientTypeRadio.getSelectedToggle() == null ||
                purposeCombo.getValue() == null) {
            showError("Complete all fields");
            return;
        }
        String fname = fname_field.getText().trim();
        String mi = mi_field.getText().trim();
        String lname = lname_field.getText().trim();
        String ageText = age_field.getText().trim();
        String gender = genderRadio.getSelectedToggle().getUserData().toString();
        String purpose = purposeCombo.getSelectionModel().getSelectedItem();
        String symptoms = symptoms_field.getText().trim();
        String ptype = (String) patientTypeRadio.getSelectedToggle().getUserData();
        String contact = contact_field.getText().trim();

        if(fname.isEmpty()
                || mi.isEmpty()
                ||  lname.isEmpty()
                ||  ageText.isEmpty()
                ||  gender.isEmpty()
                ||  purpose.isEmpty()
                ||  symptoms.isEmpty()
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

        formManager.saveForm(new Form(fname,mi,lname,age, gender, purpose,symptoms,ptype,contact,formType));
        sceneNavigator.navigate("/com/javafx/csit228capstone/queue/queue-schedule.fxml", cancelBtn, "/styles/queue-schedule.css", (QueueScheduleController queueScheduleController) -> queueScheduleController.initializeData(formType));
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
