package com.example.ltnc;

import com.example.ltnc.Service.SessionManager;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;

public class HomeController {


        @FXML
        private Button dashboardButton, categoryButton, expenseButton, incomeButton;

        @FXML
        private AnchorPane contentArea;
        private LoginController loginController;
    private static int userId;
    // giong constructor , tự động gọi khi render view
    @FXML
    public void initialize() {
        userId = SessionManager.getInstance().getUserId();

        if (userId == -1) {
            redirectToLogin();
        } else {
            System.out.println("User ID hiện tại: " + userId);
        }
    }
    public void redirectToLogin() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
            Stage stage = (Stage) contentArea.getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void logout(){
        SessionManager.getInstance().logout();
        userId=SessionManager.getInstance().getUserId();
        System.out.println(userId);
        redirectToLogin();
    }

        @FXML
        private void dashboard() {
            loadContent("dashboard.fxml");
        }

        @FXML
        private void category() {
            loadContent("category.fxml");
        }

        @FXML
        private void expense() {
            loadContent("expense.fxml");
        }

        // Method to load the income content
        @FXML
        private void income() {
            loadContent("income.fxml");
        }

        // Method to load a new FXML into the contentArea
        private void loadContent(String fxmlFile) {
            try {
                System.out.println("Path to FXML: " + getClass().getResource(fxmlFile));

                FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
                AnchorPane newContent = loader.load();

                contentArea.getChildren().clear();
                contentArea.getChildren().add(newContent);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }



}
