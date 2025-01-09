package com.example.ltnc.Entity;

import com.example.ltnc.Entity.Category.CategoryEntity;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;

public class ExpenseEntity implements FinancialRecord{
    public ExpenseEntity(){

    }
    private int id;
    private UserEntity user;
    private CategoryEntity categoryEntity;
    private String item;
    private String description;
    private Long money;
    private LocalDate date;
    private Timestamp createdAt;
    private String categoryName;


    public void setCreated_at(Timestamp created_at) {
        this.createdAt = created_at;
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

    public void setCategoryEntiy(CategoryEntity categoryEntity) {
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


    public Timestamp getCreatedAt() {
        return createdAt;
    }

    @Override
    public String getCategoryName() {
        return this.categoryName != null ? this.categoryName :
                (this.categoryEntity != null ? this.categoryEntity.getName() : "");
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public ExpenseEntity(UserEntity user, CategoryEntity categoryEntiy, String item, String description, Long money, LocalDate date, Timestamp createdAt) {

        this.user = user;
        this.categoryEntity = categoryEntiy;
        this.item = item;
        this.description = description;
        this.money = money;
        this.date = date;
        this.createdAt = createdAt;
    }
}
