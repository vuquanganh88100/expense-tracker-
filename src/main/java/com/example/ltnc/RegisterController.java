package com.example.ltnc;

import com.example.ltnc.Service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

public class RegisterController {
    @FXML
    private TextField UserName;
    @FXML
    private TextField Gmail;
    @FXML
    private TextField Password;
    @FXML
    private Button submitButton;
    private static UserService userService=new UserService();
    public void addUser(ActionEvent actionEvent){
        String userName=UserName.getText();
        String gmail=Gmail.getText();
        String password=Password.getText();
        userService.registerUser(userName,gmail,password);
    }
}
