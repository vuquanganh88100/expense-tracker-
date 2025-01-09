package com.example.ltnc;

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

public class RegisterController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private TextField UserName;
    @FXML
    private TextField Gmail;
    @FXML
    private TextField Password;
    @FXML
    private Button submitButton;
    private static UserService userService=new UserService();
    public void addUser(ActionEvent actionEvent) throws SQLException, IOException {
        String userName=UserName.getText();
        String gmail=Gmail.getText();
        String password=Password.getText();
        boolean userExist=userService.isUserExist(userName,gmail,password);
        if(!userExist){
            login(actionEvent);
        }
    }
    public void login(ActionEvent event) throws IOException, SQLException {
        root= FXMLLoader.load(getClass().getResource("login.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene=new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
