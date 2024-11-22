package com.example.ltnc.Service;

import com.example.ltnc.Dao.UserDao;
import com.example.ltnc.Entity.UserEntity;
import javafx.scene.control.Alert;
import javafx.stage.Window;

import java.sql.Time;
import java.time.LocalTime;

public class UserService {
    private  static  UserDao userDao=new UserDao();
    public void registerUser(String userName,String gmail,String password){
        UserEntity user=new UserEntity(userName,gmail,Time.valueOf(LocalTime.now()),password);
        userDao.add(user);
    }
    public UserEntity  authenticated(String userName, String password) {
        UserEntity user = userDao.user(userName);
        if (user == null) {
            infoBox("Tên người dùng không đúng!", "Đăng nhập thất bại", "Thông báo");
            return  null;
        } else {
            if (user.getPassword().equals(password)) {
                infoBox("Đăng nhập thành công!", "Đăng nhập", "Thông báo");
                return  user;
            } else {
                infoBox("Mật khẩu không đúng!", "Đăng nhập thất bại", "Thông báo");
                return  null;
            }
        }
    }


    public static void infoBox(String infoMessage, String headerText, String title) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setContentText(infoMessage);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.showAndWait();
    }


}
