package com.example.ltnc.Dao;

import com.example.ltnc.Entity.Category.CategoryEntiy;
import com.example.ltnc.Entity.Category.CategoryEnum;
import com.example.ltnc.Entity.UserEntity;
import com.example.ltnc.Utils.DatabaseUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryDao {
    private static DatabaseUtils databaseUtils=new DatabaseUtils();

    private static final String INSERT_CATEGORT="INSERT INTO category (user_id, type, name, created_at) VALUES (?, ?, ?, ?)";
    private static final String GET_CATEGORY_BY_ID="SELECT * FROM category WHERE category.user_id = ?";
    private static final String COMBO_BOX="" +
            "SELECT * FROM category WHERE category.user_id = ?  AND category.type = ?" +
            "";
    public void add(CategoryEntiy categoryEntiy){
        try (Connection connection=databaseUtils.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CATEGORT)) {
            preparedStatement.setInt(1,categoryEntiy.getUser().getId());
            preparedStatement.setString(2,categoryEntiy.getCategoryEnum().getValue());
            preparedStatement.setString(3,categoryEntiy.getName());
            preparedStatement.setTimestamp(4, categoryEntiy.getCreatedtime());
            preparedStatement.executeUpdate();
            System.out.println("Category saved successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error saving category: " + e.getMessage());
        }
    }
    public List<CategoryEntiy> getCategoryById(int userId){
        List<CategoryEntiy> categoryEntiyList=new ArrayList<>();

        try (Connection connection=databaseUtils.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_CATEGORY_BY_ID)) {
            preparedStatement.setInt(1,userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println(resultSet);
            while (resultSet.next()) {
                CategoryEntiy categoryEntity = new CategoryEntiy();
                categoryEntity.setId(resultSet.getInt("id"));
                categoryEntity.setName(resultSet.getString("name"));
                categoryEntity.setCreatedtime(resultSet.getTimestamp("created_at"));
                categoryEntity.setCategoryEnum(CategoryEnum.valueOf(resultSet.getString("type").toUpperCase()));

                categoryEntiyList.add(categoryEntity);
            }
            System.out.println(categoryEntiyList);
            System.out.println("Get list category successfully");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error saving category: " + e.getMessage());
        }
        return categoryEntiyList;
    }
    public List<String> categoryBox(int userId,String type){
        List<String> categoryListName=new ArrayList<>();

        try (Connection connection=databaseUtils.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(COMBO_BOX)) {
            preparedStatement.setInt(1,userId);
            preparedStatement.setString(2,type);
            ResultSet resultSet = preparedStatement.executeQuery();
            while ((resultSet.next())){
                String categoryName=resultSet.getString("name");
                categoryListName.add(categoryName);
            }
            for(String s:categoryListName){
                System.out.println(s);
            }
            System.out.println("get categorybox successfully");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error get  category: " + e.getMessage());
        }
        return categoryListName;
    }

}
