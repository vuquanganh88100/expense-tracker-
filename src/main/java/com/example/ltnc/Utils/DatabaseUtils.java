package com.example.ltnc.Utils;

import java.sql.*;

public class DatabaseUtils {
    public Connection connect(){
        Connection conn = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");

            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/quanlychitieu", "root", "882002");
//            conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/ltnc", "root", "882002");

            System.out.println("Kết nối thành công");
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Ket Noi khong thanh cong");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return conn;
    }
}
