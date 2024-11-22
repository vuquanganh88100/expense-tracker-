package com.example.ltnc.Service;

public class SessionManager {
    private static SessionManager instance;

    private int userId;
    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }
    public void logout(){
        userId=-1;
}
}
