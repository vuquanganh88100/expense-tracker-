package com.example.ltnc.Dao;

import com.example.ltnc.Entity.Category.CategoryEntiy;
import com.example.ltnc.Entity.ExpenseEntity;
import com.example.ltnc.Entity.IncomeEntity;
import com.example.ltnc.Utils.DatabaseUtils;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class IncomeDAO {
    private static DatabaseUtils databaseUtils=new DatabaseUtils();
    private static final String INSERT_INCOME="INSERT INTO income (user_id, category_id, item, description, money, created_at, date) \n" +
            "VALUES (?, ?, ?, ?, ?, ?, ?);";
    private static final String GET_INCOME="SELECT * FROM income inner JOIN category ON income.category_id=category.id\n" +
            "WHERE income.user_id = ?";
    private static final String UPDATE_INCOME="UPDATE income \n" +
            "SET \n" +
            "    category_id = ?, \n" +
            "    item = ?, \n" +
            "    description = ?, \n" +
            "    money = ?, \n" +
            "    created_at = ?, \n" +
            "    date = ? \n" +
            "WHERE id = ?;\n";
    private static final String DELETE_INCOME="DELETE FROM income WHERE id = ? ";



    public void add(IncomeEntity income){
        try (Connection connection = databaseUtils.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_INCOME)) {
            // Set các tham số
            preparedStatement.setInt(1, income.getUser().getId());
            preparedStatement.setInt(2, income.getCategoryEntiy().getId());
            preparedStatement.setString(3, income.getItem());
            preparedStatement.setString(4, income.getDescription());
            preparedStatement.setLong(5, income.getMoney());
            preparedStatement.setTimestamp(6,income.getCreated_at());
            preparedStatement.setDate(7, Date.valueOf(income.getDate()));
            preparedStatement.executeUpdate();
            System.out.println("Insert income successfully");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error insert income: " + e.getMessage());
        }

    }
    public List<IncomeEntity> getData(int userId){
        List<IncomeEntity > incomeEntities=new ArrayList<>();
        try (Connection connection = databaseUtils.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(GET_INCOME)) {
            preparedStatement.setInt(1,userId);
            ResultSet resultSet=preparedStatement.executeQuery();
            while(resultSet.next()){
                IncomeEntity incomeEntity=new IncomeEntity();
                incomeEntity.setId(resultSet.getInt("id"));
                incomeEntity.setDate(resultSet.getDate("date").toLocalDate());
                incomeEntity.setItem(resultSet.getString("item"));
                CategoryEntiy categoryEntity = new CategoryEntiy();
                categoryEntity.setName(resultSet.getString("name"));
                incomeEntity.setCategoryEntiy(categoryEntity);
                incomeEntity.setDescription(resultSet.getString("description"));
                incomeEntity.setMoney(resultSet.getLong("money"));
                incomeEntities.add(incomeEntity);
            }
            System.out.println("Get data income succesfully ");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error get data income: " + e.getMessage());
        }
        return incomeEntities;
    }
    public void update(IncomeEntity income){
        try (Connection connection = databaseUtils.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(UPDATE_INCOME)) {
            // Set các tham số
            preparedStatement.setInt(1, income.getCategoryEntiy().getId());
            preparedStatement.setString(2, income.getItem());
            preparedStatement.setString(3, income.getDescription());
            preparedStatement.setLong(4, income.getMoney());
            preparedStatement.setTimestamp(5,income.getCreated_at());
            preparedStatement.setDate(6, Date.valueOf(income.getDate()));
            preparedStatement.setInt(7,income.getId());
            preparedStatement.executeUpdate();
            System.out.println("Update income successfully");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error update income: " + e.getMessage());
        }

    }
    public void delete(Integer id){
        try (Connection connection = databaseUtils.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(DELETE_INCOME)) {
            preparedStatement.setInt(1,id);
            preparedStatement.executeUpdate();
            System.out.println("Delete income successfully");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error Delete income: " + e.getMessage());
        }
    }
}
