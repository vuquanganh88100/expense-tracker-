package com.example.ltnc.Service;

import com.example.ltnc.Dao.UserDao;
import com.example.ltnc.Entity.UserEntity;
import javafx.scene.control.Alert;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.Time;
import java.time.LocalTime;

public class UserService {
    private  static  UserDao userDao=new UserDao();
    public boolean isUserExist(String userName,String gmail,String password){
        if(userDao.checkExist(userName,gmail)){
            infoBox("Tên người dùng hoặc gmail đã tồn tại","Đăng ký thất bại","Thông báo");
            return true;
        }else{
            String hashPassword=BCrypt.hashpw(password, BCrypt.gensalt(12));
            UserEntity user=new UserEntity(userName,gmail,Time.valueOf(LocalTime.now()),hashPassword);
            userDao.add(user);
            infoBox("Đăng ký thành công ","Đăng ký","Thông báo");
            return false;
        }
    }
    public UserEntity  authenticated(String userName, String password) {
        UserEntity user = userDao.user(userName);
        if (user == null) {
            infoBox("Tên người dùng không đúng!", "Đăng nhập thất bại", "Thông báo");
            return  null;
        } else {
            boolean isCorrectPassword=BCrypt.checkpw(password,user.getPassword());
            if (isCorrectPassword) {
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
