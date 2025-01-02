package com.example.ltnc;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;

public class    HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {


        URL fxmlFile = getClass().getResource("/com/example/ltnc/login.fxml");
//        URL fxmlFile = getClass().getResource("/com/example/ltnc/ManageStudent.fxml");

        if (fxmlFile == null) {
            throw new IllegalStateException("FXML file not found!");
        }
        Parent root = FXMLLoader.load(fxmlFile);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Login Application");
        stage.show();

    }
    public static void main(String[] args) {
        launch();
    }
}
