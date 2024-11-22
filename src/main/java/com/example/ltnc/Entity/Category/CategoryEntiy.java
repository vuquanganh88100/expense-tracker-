package com.example.ltnc.Entity.Category;

import com.example.ltnc.Entity.UserEntity;

import java.sql.Time;
import java.sql.Timestamp;

public class CategoryEntiy {
    private int id;
    private UserEntity user;
    private  CategoryEnum categoryEnum ; //type
    private String name;
    private Timestamp createdtime;

    public CategoryEntiy(){

    }


    public CategoryEntiy(int id, UserEntity user, CategoryEnum categoryEnum, String name, Timestamp createdtime) {
        this.id = id;
        this.user = user;
        this.categoryEnum = categoryEnum;
        this.name = name;
        this.createdtime = createdtime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public UserEntity getUser() {
        return user;
    }

    public void setUser(UserEntity user) {
        this.user = user;
    }

    public CategoryEnum getCategoryEnum() {
        return categoryEnum;
    }

    public void setCategoryEnum(CategoryEnum categoryEnum) {
        this.categoryEnum = categoryEnum;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Timestamp getCreatedtime() {
        return createdtime;
    }

    public void setCreatedtime(Timestamp createdtime) {
        this.createdtime = createdtime;
    }
}
