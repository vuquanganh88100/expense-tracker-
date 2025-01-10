package com.example.ltnc.Service;

import com.example.ltnc.Dao.CategoryDao;
import com.example.ltnc.Dao.ExpenseDao;
import com.example.ltnc.Dao.UserDao;
import com.example.ltnc.Entity.Category.CategoryEntiy;
import com.example.ltnc.Entity.Category.CategoryEnum;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryService {
    public void add(String name,String type){
        UserDao userDao=new UserDao();
        CategoryEntiy categoryEntiy=new CategoryEntiy();
        CategoryDao categoryDao=new CategoryDao();
        categoryEntiy.setUser(userDao.getInfo());
        categoryEntiy.setCreatedtime(new Timestamp(System.currentTimeMillis())); // Thời gian hiện tạ
        categoryEntiy.setName(name);
        categoryEntiy.setCategoryEnum(CategoryEnum.fromString(type.toUpperCase()));
        categoryDao.add(categoryEntiy);
    }

    public List<CategoryEntiy> categoryEntiy(int userId){
        CategoryDao categoryDao=new CategoryDao();
        CategoryEntiy categoryEntiy=new CategoryEntiy();
        return  categoryDao.getCategoryByUserId(userId);
    }
    public void delete(int categoryId) {
        CategoryDao categoryDao = new CategoryDao();
        categoryDao.delete(categoryId);
    }
    public void update(int categoryId,String type,String name){
        CategoryDao categoryDao =new CategoryDao();
        CategoryEntiy categoryEntiy=new CategoryEntiy();
        categoryEntiy.setId(categoryId);
        categoryEntiy.setCreatedtime(new Timestamp(System.currentTimeMillis()));
        categoryEntiy.setName(name);
        categoryEntiy.setCategoryEnum(CategoryEnum.fromString(type.toUpperCase()));
        categoryDao.update(categoryEntiy);
    }

    //    public Map<String,Integer>getCategoryIdMap(String type,int userId){
//        CategoryDao categoryDao=new CategoryDao();
//        List<CategoryEntiy> categories = categoryDao.categoryBox(userId, type);
//
//        Map<String, Integer> categoryIdMap = new HashMap<>();
//        List<String> categoryNames = new ArrayList<>();
//
//        categories.forEach(category -> {
//            categoryIdMap.put(category.getName(), category.getId());
//            categoryNames.add(category.getName());
//        });
//        return
//    }
}
