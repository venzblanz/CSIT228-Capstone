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

    // Radio group containers — add fx:id to these HBox/VBox wrappers in your FXML
    // so we can highlight the group border when none is selected.
    @FXML private HBox              genderGroup;
    @FXML private HBox              patientTypeGroup;
    @FXML private HBox              civilStatusGroup;

    private final SceneNavigator    sceneNavigator = SceneNavigator.getInstance();
    private final FormManager       formManager = FormManager.getInstance();

    private static final String ERROR_STYLE =
            "-fx-border-color: red; -fx-border-radius: 4; -fx-border-width: 1.5;";

    // Helpers
    UnaryOperator<TextFormatter.Change> filter = change -> {
        if (change.getControlNewText().matches("\\d*")) {
            return change;
        }
        return null;
    };

    private String      formType;
    RadioButton[]       patientRadios;
    RadioButton[]       civilStatusRadios;
    RadioButton[]       genderRadios;
    Form                loadedForm;

    public void initializeData(String type) {
        loadedForm = formManager.loadForm();
        formType = type;
        if (type.equals("General Wellness")) {
            typeLabel.setText("General Wellness");
            gwInit();
        } else if (type.equals("Women's Health")) {
            typeLabel.setText("Women's Health");
            whInit();
        } else if (type.equals("Specialized Fields")) {
            typeLabel.setText("Specialized Fields");
            sfInit();
        } else {
            typeLabel.setText("Diagnostics and Laboratory");
            dlInit();
        }
    }

    @FXML
    public void initialize() {
        AnimationHelper.staggerFadeIn(mainPane);
        menuController.setActiveButton(menuController.getQueueBtn());
        birthDatePicker.setEditable(false);
        initializePage();

        backIconBtn.setOnMouseClicked(e -> onBack());
        backBtn.setOnMouseClicked(e -> onBack());
        cancelBtn.setOnAction(e -> onCancel());
        nextBtn.setOnAction(e -> onNext());
    }

    private void initializePage() {
        AnimationHelper.staggerFadeIn(form);
        error_message.setVisible(false);

        age_field.setTextFormatter(new TextFormatter<>(filter));
        contact_field.setTextFormatter(new TextFormatter<>(filter));
        emergencyContactField.setTextFormatter(new TextFormatter<>(filter));

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

        nationalityCombo.setPromptText("Select Nationality...");
        nationalityCombo.getItems().addAll(
                "Filipino", "American", "Chinese", "Japanese", "Korean",
                "Indian", "Indonesian", "Malaysian", "Singaporean", "Thai",
                "Vietnamese", "British", "Canadian", "Australian", "German",
                "French", "Spanish", "Italian", "Other"
        );
        religionCombo.setPromptText("Select Religion...");
        religionCombo.getItems().addAll(
                "Roman Catholic", "Iglesia ni Cristo", "Born Again Christian",
                "Protestant", "Evangelical Christian", "Seventh-day Adventist",
                "Jehovah's Witness", "Islam", "Buddhism", "Hinduism", "Judaism",
                "Sikhism", "Taoism", "No Religion", "Other", "Prefer not to say"
        );
        relationCombo.setPromptText("Select Relation...");
        relationCombo.getItems().addAll(
                "Father", "Mother", "Brother", "Sister", "Spouse", "Son",
                "Daughter", "Grandfather", "Grandmother", "Uncle", "Aunt",
                "Cousin", "Guardian", "Relative", "Friend", "Neighbor",
                "Caregiver", "Other"
        );

        // Clear field errors on interaction
        attachClearListeners();

        patientRadios = new RadioButton[]{ regularRadio, pregnantRadio, pwdRadio, seniorRadio };
        civilStatusRadios = new RadioButton[]{ singleRadio, marriedRadio, widowedRadio, separatedRadio };
        genderRadios = new RadioButton[]{ maleRadio, femaleRadio };

        birthDatePicker.valueProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null) { age_field.setText("0"); return; }
            int age = Period.between(newVal, LocalDate.now()).getYears();
            age_field.setText(String.valueOf(age));
        });

        age_field.textProperty().addListener((obs, oldVal, newVal) -> {
            if (newVal == null || newVal.isEmpty()) { seniorRadio.setDisable(false); return; }
            try {
                int age = Integer.parseInt(newVal);
                if (age < 60) {
                    seniorRadio.setDisable(true);
                    if (seniorRadio.isSelected()) patientTypeRadio.selectToggle(null);
                } else {
                    seniorRadio.setDisable(false);
                }
            } catch (NumberFormatException e) {
                seniorRadio.setDisable(true);
            }
        });

        genderRadio.selectedToggleProperty().addListener((obs, oldToggle, newToggle) -> {
            if (newToggle == null) { pregnantRadio.setDisable(false); return; }
            if (newToggle.getUserData().toString().equals("Male")) {
                pregnantRadio.setDisable(true);
                if (pregnantRadio.isSelected()) patientTypeRadio.selectToggle(null);
            } else {
                pregnantRadio.setDisable(false);
            }
        });
    }

    // ── attach listeners so each field clears its own error highlight on edit ──

    private void attachClearListeners() {
        fname_field.textProperty().addListener((o, ov, nv)            -> clearFieldError(fname_field));
        mi_field.textProperty().addListener((o, ov, nv)               -> clearFieldError(mi_field));
        lname_field.textProperty().addListener((o, ov, nv)            -> clearFieldError(lname_field));
        age_field.textProperty().addListener((o, ov, nv)              -> clearFieldError(age_field));
        addressField.textProperty().addListener((o, ov, nv)           -> clearFieldError(addressField));
        contact_field.textProperty().addListener((o, ov, nv)          -> clearFieldError(contact_field));
        emailAddressField.textProperty().addListener((o, ov, nv)      -> clearFieldError(emailAddressField));
        emergencyContactField.textProperty().addListener((o, ov, nv)  -> clearFieldError(emergencyContactField));
        symptoms_field.textProperty().addListener((o, ov, nv)         -> clearFieldError(symptoms_field));

        birthDatePicker.valueProperty().addListener((o, ov, nv)       -> clearPickerError(birthDatePicker));

        nationalityCombo.valueProperty().addListener((o, ov, nv)      -> clearComboError(nationalityCombo));
        religionCombo.valueProperty().addListener((o, ov, nv)         -> clearComboError(religionCombo));
        relationCombo.valueProperty().addListener((o, ov, nv)         -> clearComboError(relationCombo));

        genderRadio.selectedToggleProperty().addListener((o, ov, nv)      -> clearGroupError(genderGroup));
        patientTypeRadio.selectedToggleProperty().addListener((o, ov, nv) -> clearGroupError(patientTypeGroup));
        civilStatusRadio.selectedToggleProperty().addListener((o, ov, nv) -> clearGroupError(civilStatusGroup));
    }

    // ── per-field error helpers ──

    private void markFieldError(TextField field) {
        field.setStyle(ERROR_STYLE);
    }
    private void markPickerError(DatePicker picker) {
        picker.setStyle(ERROR_STYLE);
    }
    private void markComboError(ComboBox<?> combo) {
        combo.setStyle(ERROR_STYLE);
    }
    /** Highlights the container HBox that wraps the radio buttons for the group. */
    private void markGroupError(HBox group) {
        if (group != null)
            group.setStyle(ERROR_STYLE + " -fx-padding: 4; -fx-background-radius: 4;");
    }

    private void clearFieldError(TextField field)   { field.setStyle(""); }
    private void clearPickerError(DatePicker picker) { picker.setStyle(""); }
    private void clearComboError(ComboBox<?> combo)  { combo.setStyle(""); }
    private void clearGroupError(HBox group)         { if (group != null) group.setStyle(""); }

    private void showError(String message) {
        error_container.setVisible(true);
        error_message.setVisible(true);
        error_message.setText(message);
        error_message.setStyle("-fx-text-fill: red;");
        // Scroll to top so the error banner is visible
        scrollPane.setVvalue(0);
    }

    private void clearAllErrors() {
        clearFieldError(fname_field);
        clearFieldError(mi_field);
        clearFieldError(lname_field);
        clearFieldError(age_field);
        clearFieldError(addressField);
        clearFieldError(contact_field);
        clearFieldError(emailAddressField);
        clearFieldError(emergencyContactField);
        clearFieldError(symptoms_field);
        clearPickerError(birthDatePicker);
        clearComboError(nationalityCombo);
        clearComboError(religionCombo);
        clearComboError(relationCombo);
        clearGroupError(genderGroup);
        clearGroupError(patientTypeGroup);
        clearGroupError(civilStatusGroup);
        error_container.setVisible(false);
        error_message.setVisible(false);
    }

    // ── scene init helpers ──

    private void gwInit() {
        if (loadedForm != null && loadedForm.getFormType().equals("General Wellness")) reloadForm();
    }
    private void whInit() {
        formColorPane.setStyle("-fx-background-color: #FFBCD3;");
        if (loadedForm != null && loadedForm.getFormType().equals("Women's Health")) reloadForm();
    }
    private void sfInit() {
        formColorPane.setStyle("-fx-background-color: rgba(75, 245, 220);");
        if (loadedForm != null && loadedForm.getFormType().equals("Specialized Fields")) reloadForm();
    }
    private void dlInit() {
        formColorPane.setStyle("-fx-background-color: #543BE9;");
        if (loadedForm != null && loadedForm.getFormType().equals("Diagnostics and Laboratory")) reloadForm();
    }

    private void reloadForm() {
        if (loadedForm == null) return;
        String type        = loadedForm.getPatientType();
        String gender      = loadedForm.getGender();
        String civilStatus = loadedForm.getCivilStatus();

        fname_field.setText(loadedForm.getFirstName());
        mi_field.setText(loadedForm.getMiddleName());
        lname_field.setText(loadedForm.getLastName());
        birthDatePicker.setValue(loadedForm.getBirthDate());
        age_field.setText(String.valueOf(loadedForm.getAge()));

        for (RadioButton rb : genderRadios)      { if (rb.getUserData().equals(gender))      { genderRadio.selectToggle(rb);      break; } }
        for (RadioButton rb : civilStatusRadios) { if (rb.getUserData().equals(civilStatus)) { civilStatusRadio.selectToggle(rb); break; } }
        for (RadioButton rb : patientRadios)     { if (rb.getUserData().equals(type))        { patientTypeRadio.selectToggle(rb); break; } }

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

    private void onBack() {
        sceneNavigator.navigate("/com/javafx/csit228capstone/queue/queue.fxml", cancelBtn, "/styles/queue.css");
    }
    private void onCancel() {
        sceneNavigator.navigate("/com/javafx/csit228capstone/dashboard.fxml", cancelBtn, "/styles/dashboard.css");
    }

    private void onNext() {
        clearAllErrors();

        String fname               = fname_field.getText().trim();
        String mi                  = mi_field.getText().trim();
        String lname               = lname_field.getText().trim();
        LocalDate birthDate        = birthDatePicker.getValue();
        String ageText             = age_field.getText().trim();
        String address             = addressField.getText().trim();
        String nationality         = nationalityCombo.getValue();
        String contact             = contact_field.getText().trim();
        String email               = emailAddressField.getText().trim();
        String contactPerson       = contactPersonField.getText().trim();
        String contactPersonRelation = relationCombo.getSelectionModel().getSelectedItem();
        String emergencyPersonContact = emergencyContactField.getText().trim();
        String symptoms            = symptoms_field.getText().trim();
        String religion            = religionCombo.getSelectionModel().getSelectedItem();

        boolean hasError = false;

        // ── required fields ──
        if (fname.isEmpty()) {
            markFieldError(fname_field);
            hasError = true;
        }
        if (lname.isEmpty()) {
            markFieldError(lname_field);
            hasError = true;
        }
        if (birthDate == null) {
            markPickerError(birthDatePicker);
            hasError = true;
        }
        if (ageText.isEmpty()) {
            markFieldError(age_field);
            hasError = true;
        }
        if (genderRadio.getSelectedToggle() == null) {
            markGroupError(genderGroup);
            hasError = true;
        }
        if (patientTypeRadio.getSelectedToggle() == null) {
            markGroupError(patientTypeGroup);
            hasError = true;
        }
        if (civilStatusRadio.getSelectedToggle() == null) {
            markGroupError(civilStatusGroup);
            hasError = true;
        }
        if (address.isEmpty()) {
            markFieldError(addressField);
            hasError = true;
        }
        if (nationality == null || nationality.isEmpty()) {
            markComboError(nationalityCombo);
            hasError = true;
        }
        if (contact.isEmpty()) {
            markFieldError(contact_field);
            hasError = true;
        }

        if (hasError) {
            showError("Please complete all required fields");
            return;
        }

        // ── format validations ──
        if (!fname.matches("^[A-Za-zÑñ]+(?:[ .''-][A-Za-zÑñ]+)*$")) {
            markFieldError(fname_field);
            showError("Please enter a valid first name");
            return;
        }
        if (!lname.matches("^[A-Za-zÑñ]+(?:[ .''-][A-Za-zÑñ]+)*$")) {
            markFieldError(lname_field);
            showError("Please enter a valid last name");
            return;
        }
        if (!mi.isEmpty() && !mi.matches("^[A-Za-zÑñ]$")) {
            markFieldError(mi_field);
            showError("Please enter only one (1) character for the middle initial");
            return;
        }
        if (!contact.matches("^[0-9]{11}$")) {
            markFieldError(contact_field);
            showError("Please enter a valid 11-digit mobile number (e.g. 09123456789).");
            return;
        }
        if (!emergencyPersonContact.isEmpty() && !emergencyPersonContact.matches("^[0-9]{11}$")) {
            markFieldError(emergencyContactField);
            showError("Please enter a valid 11-digit mobile number (e.g. 09123456789).");
            return;
        }
        if (!email.isEmpty() && !email.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            markFieldError(emailAddressField);
            showError("Please enter a valid email.");
            return;
        }

        // ── age validation ──
        int age;
        try {
            age = Integer.parseInt(ageText);
            if (age <= 0 || age > 130) {
                markFieldError(age_field);
                showError("Invalid age");
                return;
            }
        } catch (NumberFormatException e) {
            markFieldError(age_field);
            showError("Age must be a valid number");
            return;
        }

        String ptype = patientTypeRadio.getSelectedToggle().getUserData().toString();
        if (ptype.equals("Senior Citizen") && age < 60) {
            markGroupError(patientTypeGroup);
            markFieldError(age_field);
            showError("Senior Citizen patient type is only allowed for ages 60 and above");
            return;
        }

        String gender      = genderRadio.getSelectedToggle().getUserData().toString();
        String civilStatus = civilStatusRadio.getSelectedToggle().getUserData().toString();

        formManager.saveForm(new Form(
                fname, mi, lname, birthDate, age, gender, civilStatus,
                symptoms, ptype, address, nationality, religion,
                contact, email, contactPerson, contactPersonRelation,
                emergencyPersonContact, formType
        ));
        sceneNavigator.navigate(
                "/com/javafx/csit228capstone/queue/queue-schedule.fxml",
                cancelBtn,
                "/styles/queue-schedule.css",
                (QueueScheduleController c) -> c.initializeData(formType)
        );
    }
}