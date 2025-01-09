package com.example.ltnc.Entity;

import com.example.ltnc.Entity.Category.CategoryEntity;

import java.sql.Timestamp;
import java.time.LocalDate;

public class IncomeEntity implements FinancialRecord{
    private int id;
    private UserEntity user;
    private CategoryEntity categoryEntity;
    private String item;
    private String description;
    private Long money;
    private LocalDate date;
    private Timestamp createdAt;
    private String categoryName;

    public IncomeEntity( UserEntity user, CategoryEntity categoryEntiy, String item, String description, Long money, LocalDate date, Timestamp created_at) {
        this.user = user;
        this.categoryEntity = categoryEntiy;
        this.item = item;
        this.description = description;
        this.money = money;
        this.date = date;
        this.createdAt = created_at;
    }
    public IncomeEntity(){

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

    public CategoryEntity getCategoryEntiy() {
        return categoryEntity;
    }

    public void setCategoryEntity(CategoryEntity categoryEntity) {
        this.categoryEntity = categoryEntity;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getMoney() {
        return money;
    }

    public void setMoney(Long money) {
        this.money = money;
    }

    public LocalDate getDate() {
        return date;
    }

    @Override
    public Timestamp getCreatedAt() {
        return createdAt;
    }

    @Override
    public String getCategoryName() {
        return categoryName;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setCreated_at(Timestamp created_at) {
        this.createdAt = created_at;
    }
}
