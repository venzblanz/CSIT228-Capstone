package com.javafx.csit228capstone.utils;

import com.javafx.csit228capstone.model.User;
import java.io.*;

// This one is responsible for storing, reading and deleting user serialized file

public class SessionManager {
    private static final String SESSION_FILE = "user_session.ser";
    private static SessionManager instance;
    private User currentUser = null;

    public SessionManager() {}

    public User getCurrentUser() { return currentUser;}
    public boolean isLoggedIn(){ return currentUser != null; }

    public static SessionManager getInstance(){
        if(instance == null) instance = new SessionManager();
        return instance;
    }

    public void saveSession(User user){
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SESSION_FILE))){
            oos.writeObject(user);
            currentUser = user;
            System.out.println("Saved session for " + user.getFullname());
        }catch(Exception e){
            System.err.println("[SessionManager] Error saving session: " + e.getMessage());
        }
    }

    public User restoreSession(){
        File f = new File(SESSION_FILE);
        if(!f.exists()) return null;
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f))){
            User restoredUser = (User) ois.readObject();
            currentUser = restoredUser;
            System.out.println("Restored session for " + currentUser.getFullname());
            return restoredUser;
        }catch (Exception e){
            System.err.println("[SessionManager] Error reading session file: " + e.getMessage());
        }
        return null;
    }

    public void clearSession(){
        currentUser = null;
        File f = new File(SESSION_FILE);
        if(f.exists() && f.delete()){
            System.out.println("Session deleted, user logged out");
        }
    }
}
