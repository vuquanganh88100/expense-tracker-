package com.example.ltnc.Dao;

import com.example.ltnc.Entity.Category.CategoryEntiy;
import com.example.ltnc.Entity.ExpenseEntity;
import com.example.ltnc.Entity.UserEntity;
import com.example.ltnc.Utils.DatabaseUtils;

import java.math.BigInteger;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ExpenseDao {
    private static DatabaseUtils databaseUtils=new DatabaseUtils();
    private static final String INSERT_EXPENSE="INSERT INTO expense (user_id, category_id, item, description, money, created_at, date) \n" +
            "VALUES (?, ?, ?, ?, ?, ?, ?);";
    private static final String GET_EXPENSE="SELECT * FROM expense inner JOIN category ON expense.category_id=category.id\n" +
            "WHERE expense.user_id = ?";
    private static final String UPDATE_EXPENSE= "UPDATE expense " +
            "SET category_id=? ,item = ?, description = ?, money = ?,created_at = ?, date = ? WHERE id = ?";
    private static final String DELETE_EXPENSE="DELETE FROM expense WHERE id = ? ";
    public void add(ExpenseEntity expense){
        try (Connection connection = databaseUtils.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_EXPENSE)) {
            // Set các tham số
            preparedStatement.setInt(1, expense.getUser().getId());
            preparedStatement.setInt(2, expense.getCategoryEntiy().getId());
            preparedStatement.setString(3, expense.getItem());
            preparedStatement.setString(4, expense.getDescription());
            preparedStatement.setLong(5, expense.getMoney());
            preparedStatement.setTimestamp(6,expense.getCreated_at());
            preparedStatement.setDate(7, Date.valueOf(expense.getDate()));
            preparedStatement.executeUpdate();
            System.out.println("Update expense successfully");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error update expense: " + e.getMessage());
        }

    }
    public List<ExpenseEntity> getData(int userId){
        List<ExpenseEntity > expenseEntityList=new ArrayList<>();
        try (Connection connection = databaseUtils.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_EXPENSE)) {
            preparedStatement.setInt(1,userId);
            ResultSet resultSet=preparedStatement.executeQuery();
            while(resultSet.next()){
                ExpenseEntity expense=new ExpenseEntity();
                expense.setId(resultSet.getInt("id"));
                expense.setDate(resultSet.getDate("date").toLocalDate());
                expense.setItem(resultSet.getString("item"));
                CategoryEntiy categoryEntity = new CategoryEntiy();
                categoryEntity.setName(resultSet.getString("name"));
                expense.setCategoryEntiy(categoryEntity);
                expense.setDescription(resultSet.getString("description"));
                expense.setMoney(resultSet.getLong("money"));
                expenseEntityList.add(expense);
            }
            System.out.println("Get data expense succesfully ");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error get data expense: " + e.getMessage());
        }
        return expenseEntityList;
    }
    public void update(ExpenseEntity expense){
        try (Connection connection = databaseUtils.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_EXPENSE)) {
            // Set các tham số
            preparedStatement.setInt(1, expense.getCategoryEntiy().getId());
            preparedStatement.setString(2, expense.getItem());
            preparedStatement.setString(3, expense.getDescription());
            preparedStatement.setLong(4, expense.getMoney());
            preparedStatement.setTimestamp(5,expense.getCreated_at());
            preparedStatement.setDate(6, Date.valueOf(expense.getDate()));
            preparedStatement.setInt(7,expense.getId());
            preparedStatement.executeUpdate();
            System.out.println("Update expense successfully");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error update expense: " + e.getMessage());
        }

    }
    public void delete(Integer id){
        try (Connection connection = databaseUtils.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_EXPENSE)) {
           preparedStatement.setInt(1,id);
            preparedStatement.executeUpdate();
            System.out.println("Delete expense successfully");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error Delete expense: " + e.getMessage());
        }
    }
}
