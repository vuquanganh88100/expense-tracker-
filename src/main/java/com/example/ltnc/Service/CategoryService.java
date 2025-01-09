package com.example.ltnc.Service;

import com.example.ltnc.Dao.CategoryDao;
import com.example.ltnc.Dao.UserDao;
import com.example.ltnc.Entity.Category.CategoryEntity;
import com.example.ltnc.Entity.Category.CategoryEnum;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryService {
    public void add(String name,String type){
        UserDao userDao=new UserDao();
        CategoryEntity categoryEntiy=new CategoryEntity();
        CategoryDao categoryDao=new CategoryDao();
        categoryEntiy.setUser(userDao.getInfo());
        categoryEntiy.setCreatedTime(new Timestamp(System.currentTimeMillis())); // Thời gian hiện tạ
        categoryEntiy.setName(name);
        categoryEntiy.setCategoryEnum(CategoryEnum.fromString(type));
        categoryDao.add(categoryEntiy);
    }
    public List<CategoryEntity> categoryEntiy(int userId){
        CategoryDao categoryDao=new CategoryDao();
        CategoryEntity categoryEntiy=new CategoryEntity();
        return  categoryDao.getCategoryByUserId(userId);
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
