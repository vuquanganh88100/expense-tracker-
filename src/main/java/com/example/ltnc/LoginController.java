package com.example.ltnc;

import com.example.ltnc.Dao.UserDao;
import com.example.ltnc.Entity.UserEntity;
import com.example.ltnc.Service.SessionManager;
import com.example.ltnc.Service.UserService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;

public class LoginController {
    public LoginController() {
    }

    private  UserDao userDao;
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;

    @FXML
    private Button signupButton;
    @FXML
    private  Button login;
    public void register(ActionEvent event) throws IOException, SQLException {
        root= FXMLLoader.load(getClass().getResource("register.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene=new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
    public void login(ActionEvent event) throws IOException {
        String userName= usernameField.getText();
        String password=passwordField.getText();
        UserService userService=new UserService();
        UserEntity user=userService.authenticated(userName,password);
        if(user!=null){
            SessionManager.getInstance().setUserId(user.getId());
            Parent root = FXMLLoader.load(getClass().getResource("home.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        }
    }
}
