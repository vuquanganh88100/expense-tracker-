package com.example.ltnc.Dao;

import com.example.ltnc.Entity.Category.CategoryEntity;
import com.example.ltnc.Entity.Category.CategoryEnum;
import com.example.ltnc.Entity.UserEntity;
import com.example.ltnc.Utils.DatabaseUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CategoryDao {
    private static DatabaseUtils databaseUtils=new DatabaseUtils();

    private static final String INSERT_CATEGORT="INSERT INTO category (user_id, type, name, created_at) VALUES (?, ?, ?, ?)";
    private static final String GET_CATEGORY_BY_USER_ID="SELECT * FROM category WHERE category.user_id = ?";
    private static final String GET_CATEGORY_BY_ID="SELECT * FROM category WHERE category.id = ?";

    private static final String COMBO_BOX="" +
            "SELECT * FROM category WHERE category.user_id = ?  AND category.type = ?" +
            "";
    public void add(CategoryEntity categoryEntiy){
        try (Connection connection=databaseUtils.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_CATEGORT)) {
            preparedStatement.setInt(1,categoryEntiy.getUser().getId());
            preparedStatement.setString(2,categoryEntiy.getCategoryEnum().getValue());
            preparedStatement.setString(3,categoryEntiy.getName());
            preparedStatement.setTimestamp(4, categoryEntiy.getCreatedTime());
            preparedStatement.executeUpdate();
            System.out.println("Category saved successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error saving category: " + e.getMessage());
        }
    }
    public List<CategoryEntity> getCategoryByUserId(int userId){
        List<CategoryEntity> categoryEntiyList=new ArrayList<>();

        try (Connection connection=databaseUtils.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_CATEGORY_BY_USER_ID)) {
            preparedStatement.setInt(1,userId);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println(resultSet);
            while (resultSet.next()) {
                CategoryEntity categoryEntity = new CategoryEntity();
                categoryEntity.setId(resultSet.getInt("id"));
                categoryEntity.setName(resultSet.getString("name"));
                categoryEntity.setCreatedTime(resultSet.getTimestamp("created_at"));
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
    public List<CategoryEntity> categoryBox(int userId, String type) {
        List<CategoryEntity> categoryList = new ArrayList<>();
        try (Connection connection = databaseUtils.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(COMBO_BOX)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, type);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                CategoryEntity category = new CategoryEntity();
                category.setId(resultSet.getInt("id"));
                category.setName(resultSet.getString("name"));
                categoryList.add(category);
            }

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error get category: " + e.getMessage());
        }
        return categoryList;
    }
    public CategoryEntity getCategoryById(int categoryId){
        CategoryEntity categoryEntity = new CategoryEntity();

        try (Connection connection=databaseUtils.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_CATEGORY_BY_ID)) {
            preparedStatement.setInt(1,categoryId);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println(resultSet);
            while (resultSet.next()) {
                categoryEntity.setId(resultSet.getInt("id"));
                categoryEntity.setName(resultSet.getString("name"));
                categoryEntity.setCreatedTime(resultSet.getTimestamp("created_at"));
                categoryEntity.setCategoryEnum(CategoryEnum.valueOf(resultSet.getString("type").toUpperCase()));

            }
            System.out.println("Get list category successfully");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error saving category: " + e.getMessage());
        }
        return categoryEntity;
    }

    public CategoryEntity findByNameAndUserId(String categoryName, int userId) {
        String sql = "SELECT * FROM category WHERE name = ? AND user_id = ?";
        try (Connection connection = databaseUtils.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, categoryName);
            preparedStatement.setInt(2, userId);

            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                CategoryEntity category = new CategoryEntity();
                category.setId(resultSet.getInt("id"));
                category.setName(resultSet.getString("name"));
                category.setUserId(resultSet.getInt("user_id"));
                category.setCategoryEnum(CategoryEnum.fromString(resultSet.getString("type")));
                category.setCreatedTime(resultSet.getTimestamp("created_at"));
                return category;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void insertCategory(CategoryEntity category) {
        String sql = "INSERT INTO category (user_id, name, type, created_at) VALUES (?, ?, ?, ?)";
        try (Connection connection = databaseUtils.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(sql)) {

            preparedStatement.setInt(1, category.getUserId());
            preparedStatement.setString(2, category.getName());
            preparedStatement.setString(3, String.valueOf(category.getCategoryEnum()));
            preparedStatement.setTimestamp(4, category.getCreatedTime());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
