package com.example.ltnc.Service;

import com.example.ltnc.Dao.CategoryDao;
import com.example.ltnc.Dao.ExpenseDao;
import com.example.ltnc.Dao.UserDao;
import com.example.ltnc.Entity.Category.CategoryEntiy;
import com.example.ltnc.Entity.ExpenseEntity;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class ExpenseService {
    public void add(String category, String item, Long cost ,
                    String description, LocalDate date){
        UserDao userDao=new UserDao();
        CategoryDao categoryDao=new CategoryDao();
        CategoryEntiy categoryEntiy=new CategoryEntiy();
        ExpenseDao expenseDao=new ExpenseDao();
        categoryEntiy=categoryDao.getCategoryById(SessionManager.getInstance().getUserId()).get(0);
        ExpenseEntity expense=new ExpenseEntity(userDao.getInfo(),categoryEntiy,
                item,description,cost,date, new Timestamp(System.currentTimeMillis()));
        expenseDao.add(expense);
    }
    public List<ExpenseEntity> getData(int userId){
        ExpenseDao expenseDao=new ExpenseDao();
        return expenseDao.getData(userId);
    }
}
