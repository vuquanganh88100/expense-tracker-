package com.example.ltnc.Service;

import com.example.ltnc.Dao.UserDao;
import com.example.ltnc.Entity.UserEntity;

import java.sql.Time;
import java.time.LocalTime;

public class UserService {
    private  static  UserDao userDao=new UserDao();
    public void registerUser(String userName,String gmail,String password){
        UserEntity user=new UserEntity(userName,gmail,Time.valueOf(LocalTime.now()),password);
        userDao.add(user);
    }
}
