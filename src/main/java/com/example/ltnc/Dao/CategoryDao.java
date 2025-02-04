package com.example.ltnc.Dao;

import com.example.ltnc.Entity.Category.CategoryEntiy;
import com.example.ltnc.Entity.Category.CategoryEnum;
import com.example.ltnc.Entity.ExpenseEntity;
import com.example.ltnc.Entity.UserEntity;
import com.example.ltnc.Utils.DatabaseUtils;

import java.sql.*;
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
    private static final String DELETE_CATEGORY="DELETE FROM category WHERE id = ?";
    private static final String UPDATE_CATEGORY= "UPDATE category " +
            "SET type = ?, name = ?, created_at = ? WHERE id = ?";
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
    public List<CategoryEntiy> getCategoryByUserId(int userId){
        List<CategoryEntiy> categoryEntiyList=new ArrayList<>();

        try (Connection connection=databaseUtils.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_CATEGORY_BY_USER_ID)) {
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
    public List<CategoryEntiy> categoryBox(int userId, String type) {
        List<CategoryEntiy> categoryList = new ArrayList<>();
        try (Connection connection = databaseUtils.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(COMBO_BOX)) {
            preparedStatement.setInt(1, userId);
            preparedStatement.setString(2, type);
            ResultSet resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                CategoryEntiy category = new CategoryEntiy();
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
    public CategoryEntiy getCategoryById(int categoryId){
        CategoryEntiy categoryEntity = new CategoryEntiy();

        try (Connection connection=databaseUtils.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_CATEGORY_BY_ID)) {
            preparedStatement.setInt(1,categoryId);
            ResultSet resultSet = preparedStatement.executeQuery();
            System.out.println(resultSet);
            while (resultSet.next()) {
                categoryEntity.setId(resultSet.getInt("id"));
                categoryEntity.setName(resultSet.getString("name"));
                categoryEntity.setCreatedtime(resultSet.getTimestamp("created_at"));
                categoryEntity.setCategoryEnum(CategoryEnum.valueOf(resultSet.getString("type").toUpperCase()));

            }
            System.out.println("Get list category successfully");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error saving category: " + e.getMessage());
        }
        return categoryEntity;
    }
    public void delete(Integer id){
        try (Connection connection = databaseUtils.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_CATEGORY)) {
            preparedStatement.setInt(1,id);
            preparedStatement.executeUpdate();
            System.out.println("Delete category successfully");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error Delete category: " + e.getMessage());
        }
    }
    public void update(CategoryEntiy categoryEntiy){
        try (Connection connection = databaseUtils.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_CATEGORY)) {
            // Set các tham số
            preparedStatement.setString(1, categoryEntiy.getCategoryEnum().getValue());
            preparedStatement.setString(2, categoryEntiy.getName());
            preparedStatement.setTimestamp(3,categoryEntiy.getCreatedtime());
            preparedStatement.setLong(4, categoryEntiy.getId());

            preparedStatement.executeUpdate();
            System.out.println("Update expense successfully");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error update expense: " + e.getMessage());
        }

    }

}
