package com.example.ltnc.Entity;

import java.sql.Time;

public class UserEntity {

    private Integer id;
    private String userName;
    private String userGmail;
    private Time createdTime;
    private String password;

    public UserEntity(String userName, String userGmail, Time createdTime, String password) {
        this.userName = userName;
        this.userGmail = userGmail;
        this.createdTime = createdTime;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserGmail() {
        return userGmail;
    }

    public void setUserGmail(String userGmail) {
        this.userGmail = userGmail;
    }

    public Time getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Time createdTime) {
        this.createdTime = createdTime;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
