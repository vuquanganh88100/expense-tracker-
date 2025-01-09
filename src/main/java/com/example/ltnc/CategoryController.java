package com.example.ltnc;

import com.example.ltnc.Entity.Category.CategoryEntiy;
import com.example.ltnc.Entity.ExpenseEntity;
import com.example.ltnc.Service.CategoryService;
import com.example.ltnc.Service.SessionManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CategoryController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    @FXML
    private TableView<CategoryEntiy> categoryTableView;

    @FXML
    private TableColumn<CategoryEntiy, Integer> idColumn;

    @FXML
    private TableColumn<CategoryEntiy, String> nameColumn;

    @FXML
    private TableColumn<CategoryEntiy, String> typeColumn;

    @FXML
    private TableColumn<CategoryEntiy, String> actionColumn;

    @FXML
    public void addCategory(ActionEvent event) throws IOException {
        openCategoryDialog(null, "Add New Category");
    }
    @FXML
    void handleUpdate(CategoryEntiy categoryEntiy) throws IOException {
        openCategoryDialog(categoryEntiy, "Update Category");
    }
    private void openCategoryDialog(CategoryEntiy category, String title) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("addCategory.fxml"));
        Parent dialogRoot = loader.load();

        Stage dialogStage = new Stage();
        dialogStage.setTitle(title);
        dialogStage.initModality(Modality.APPLICATION_MODAL);

        Scene scene = new Scene(dialogRoot);
        dialogStage.setScene(scene);

        AddCategoryController controller = loader.getController();
        controller.setDialogStage(dialogStage);

        if (category != null) {
            controller.setCategoryData(category);
        }

        dialogStage.showAndWait();

        loadCategories();
    }


    @FXML
    public void initialize() {
        loadCategories();
    }

    private void loadCategories() {
        CategoryService categoryService = new CategoryService();
        List<CategoryEntiy> categoryEntiyList = categoryService.categoryEntiy(SessionManager.getInstance().getUserId());
        ObservableList<CategoryEntiy> observableCategoryList = FXCollections.observableArrayList(categoryEntiyList);

        idColumn.setCellValueFactory(new PropertyValueFactory<CategoryEntiy, Integer>("id"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<CategoryEntiy, String>("categoryEnum"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<CategoryEntiy, String>("name"));

        actionColumn.setCellFactory(param -> new TableCell<CategoryEntiy, String>() {
            private final Button updateButton = new Button("Update");
            private final Button deleteButton = new Button("Delete");

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null);
                } else {
                    HBox hbox = new HBox(10);
                    hbox.getChildren().addAll(updateButton, deleteButton);
                    updateButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-border-radius: 5; -fx-padding: 5 10; -fx-font-size: 14px;");
                    updateButton.setOnMouseEntered(event -> updateButton.setStyle("-fx-background-color: #218838; -fx-text-fill: white; -fx-border-radius: 5; -fx-padding: 5 10; -fx-font-size: 14px;"));
                    updateButton.setOnMouseExited(event -> updateButton.setStyle("-fx-background-color: #28a745; -fx-text-fill: white; -fx-border-radius: 5; -fx-padding: 5 10; -fx-font-size: 14px;"));
                    deleteButton.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; -fx-border-radius: 5; -fx-padding: 5 10; -fx-font-size: 14px;");
                    deleteButton.setOnMouseEntered(event -> deleteButton.setStyle("-fx-background-color: #c82333; -fx-text-fill: white; -fx-border-radius: 5; -fx-padding: 5 10; -fx-font-size: 14px;"));
                    deleteButton.setOnMouseExited(event -> deleteButton.setStyle("-fx-background-color: #dc3545; -fx-text-fill: white; -fx-border-radius: 5; -fx-padding: 5 10; -fx-font-size: 14px;"));

                    deleteButton.setOnAction(event -> {
                        CategoryEntiy category = getTableView().getItems().get(getIndex());
                        if (category != null) {
                            handleDelete(category);
                        }
                    });

                    updateButton.setOnAction(event -> {
                        CategoryEntiy category = getTableView().getItems().get(getIndex());
                        if (category != null) {
                            try {
                                handleUpdate(category);
                            } catch (IOException e) {
                                showAlert("Error", "Failed to open update dialog: " + e.getMessage(),
                                        Alert.AlertType.ERROR);
                            }
                        }
                    });

                    setGraphic(hbox);
                }
            }
        });

        categoryTableView.setItems(observableCategoryList);
    }
    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void handleDelete(CategoryEntiy category) {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Delete Confirmation");
        confirmDialog.setHeaderText("Delete Category");
        confirmDialog.setContentText("Are you sure you want to delete category: " + category.getName() + "?");

        confirmDialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    CategoryService categoryService = new CategoryService();
                    categoryService.delete(category.getId());
                    Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
                    successAlert.setTitle("Success");
                    successAlert.setHeaderText(null);
                    successAlert.setContentText("Category deleted successfully");
                    successAlert.showAndWait();

                    loadCategories();
                } catch (Exception e) {
                    Alert errorAlert = new Alert(Alert.AlertType.ERROR);
                    errorAlert.setTitle("Error");
                    errorAlert.setHeaderText(null);
                    errorAlert.setContentText("Failed to delete category: " + e.getMessage());
                    errorAlert.showAndWait();
                }
            }
        });
    }
}