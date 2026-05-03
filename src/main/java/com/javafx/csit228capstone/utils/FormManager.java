package com.javafx.csit228capstone.utils;

import com.javafx.csit228capstone.model.Form;

import java.io.*;

public class FormManager {
    private static final String FORM_FILE = "file.ser";
    private static FormManager instance;
    private Form currentForm = null;

    public FormManager() {}
    public static FormManager getInstance() {
        if(instance == null) {
            instance = new FormManager();
        }
        return instance;
    }
    public Form getCurrentForm(){ return currentForm; }
    public boolean isFormActive(){ return currentForm != null; }

    // save, restore, clear

    public void saveForm(Form form){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(FORM_FILE))){
            oos.writeObject(form);
            currentForm = form;
            System.out.printf("Saving form... \nType: [" + form.getFormType() + "] ");
        }catch (Exception e){
            System.err.println("[FormManager] Error saving form " + e.getMessage());
        }
    }

    public Form loadForm(){
        File f = new File(FORM_FILE);
        if(!f.exists()) {
            clearForm();
            return null;
        }
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))){
            Form restoredForm = (Form) ois.readObject();
            currentForm = restoredForm;
            System.out.println("Loading form...");
            return restoredForm;
        }catch (Exception e){
            System.err.println("[FormManager] Error loading form " + e.getMessage());
        }
        return null;
    }
    public void clearForm(){
        currentForm = null;
        File f = new File(FORM_FILE);
        if(f.exists() && f.delete()) {
            System.out.println("[FormManager] Form cleared, queue already added");
        }
    }
}
