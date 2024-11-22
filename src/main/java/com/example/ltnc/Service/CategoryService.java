package com.example.ltnc.Service;

import com.example.ltnc.Dao.CategoryDao;
import com.example.ltnc.Dao.UserDao;
import com.example.ltnc.Entity.Category.CategoryEntiy;
import com.example.ltnc.Entity.Category.CategoryEnum;

import java.sql.Timestamp;
import java.util.List;

public class CategoryService {
    public void add(String name,String type){
        UserDao userDao=new UserDao();
        CategoryEntiy categoryEntiy=new CategoryEntiy();
        CategoryDao categoryDao=new CategoryDao();
        categoryEntiy.setUser(userDao.getInfo());
        categoryEntiy.setCreatedtime(new Timestamp(System.currentTimeMillis())); // Thời gian hiện tạ
        categoryEntiy.setName(name);
        categoryEntiy.setCategoryEnum(CategoryEnum.fromString(type));
        categoryDao.add(categoryEntiy);
    }
    public List<CategoryEntiy> categoryEntiy(int userId){
        CategoryDao categoryDao=new CategoryDao();
        CategoryEntiy categoryEntiy=new CategoryEntiy();
        return  categoryDao.getCategoryById(userId);
    }
}
