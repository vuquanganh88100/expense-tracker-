package com.example.ltnc.Dao;

import com.example.ltnc.Entity.UserEntity;
import com.example.ltnc.Utils.DatabaseUtils;

import java.sql.*;

public class UserDao {
    private static DatabaseUtils databaseUtils=new DatabaseUtils();
    private static final String INSERT_USER_SQL = "INSERT INTO user (user_name, user_gmail, password,created_at) VALUES (?, ?, ?,?)";
    private static final String GET_USER_SQL="select * FROM user";
    static Statement stm = null;
    static ResultSet rs = null;
    private static final String Login="SELECT *FROM user  WHERE user_name=?";

    public void add(UserEntity userEntity) {
        try (Connection connection=databaseUtils.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER_SQL)) {

            preparedStatement.setString(1, userEntity.getUserName());
            preparedStatement.setString(2, userEntity.getUserGmail());
            preparedStatement.setString(3, userEntity.getPassword());
            preparedStatement.setTime(4, userEntity.getCreatedTime());
            ((PreparedStatement) preparedStatement).executeUpdate();
            System.out.println("User saved successfully.");

        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error saving user: " + e.getMessage());
        }
    }
    public void getUser() throws SQLException {
        try (Connection connection = databaseUtils.connect();
             Statement stm = connection.createStatement();
             ResultSet rs = stm.executeQuery(GET_USER_SQL)) {

            while (rs.next()) {
                System.out.println("User: " + rs.getString("user_name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println( e.getMessage());
        }
    }
    public UserEntity user(String userName){
        UserEntity user=new UserEntity();

        try (Connection connection=databaseUtils.connect();
             PreparedStatement preparedStatement = connection.prepareStatement(Login)) {
            // gan userName vao ?
            preparedStatement.setString(1,userName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                user.setId(resultSet.getInt("id"));
                user.setUserGmail(resultSet.getString("user_gmail"));
                user.setPassword(resultSet.getString("password"));
                System.out.println(resultSet.getTime("created_at").getClass().getName());
                user.setCreatedTime(resultSet.getTime("created_at"));
            }else{
                return  null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Error " + e.getMessage());
        }
        return user;
    }
}
