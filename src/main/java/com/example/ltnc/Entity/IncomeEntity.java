package com.example.ltnc.Entity;

import com.example.ltnc.Entity.Category.CategoryEntiy;

import java.sql.Timestamp;
import java.time.LocalDate;

public class IncomeEntity {
    private int id;
    private UserEntity user;
    private CategoryEntiy categoryEntiy;
    private String item;
    private String description;
    private Long money;
    private LocalDate date;
    private Timestamp created_at;

    public IncomeEntity( UserEntity user, CategoryEntiy categoryEntiy, String item, String description, Long money, LocalDate date, Timestamp created_at) {
        this.user = user;
        this.categoryEntiy = categoryEntiy;
        this.item = item;
        this.description = description;
        this.money = money;
        this.date = date;
        this.created_at = created_at;
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

    public CategoryEntiy getCategoryEntiy() {
        return categoryEntiy;
    }

    public void setCategoryEntiy(CategoryEntiy categoryEntiy) {
        this.categoryEntiy = categoryEntiy;
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

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }
}
