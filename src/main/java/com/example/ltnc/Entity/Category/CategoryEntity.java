package com.example.ltnc.Entity.Category;

import com.example.ltnc.Entity.UserEntity;

import java.sql.Timestamp;

public class CategoryEntity {
    private int id;
    private UserEntity user;
    private  CategoryEnum categoryEnum ; //type
    private String name;
    private Timestamp createdTime;

    public CategoryEntity(){

    }


    public CategoryEntity(int id, UserEntity user, CategoryEnum categoryEnum, String name, Timestamp createdtime) {
        this.id = id;
        this.user = user;
        this.categoryEnum = categoryEnum;
        this.name = name;
        this.createdTime = createdtime;
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

    public Timestamp getCreatedTime() {
        return createdTime;
    }


    public void setUserId(int userId) {
        this.id = userId;

    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }

    public int getUserId() {
        return this.id;
    }
}
