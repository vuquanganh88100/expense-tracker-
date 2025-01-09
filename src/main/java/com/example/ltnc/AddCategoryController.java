package com.example.ltnc;

import com.example.ltnc.Entity.Category.CategoryEntiy;
import com.example.ltnc.Service.CategoryService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AddCategoryController {
    @FXML
    private TextField nameField;
    @FXML
    private ComboBox<String> typeComboBox;
    @FXML
    private Button submitButton;

    private CategoryEntiy categoryToUpdate;
    private Stage dialogStage;
    private boolean isUpdate = false;

    @FXML
    private void initialize() {
        typeComboBox.setItems(FXCollections.observableArrayList("INCOME", "EXPENSE"));
        typeComboBox.getSelectionModel().selectFirst();
    }

    public void setDialogStage(Stage dialogStage) {
        this.dialogStage = dialogStage;
    }
    @FXML
    public void closeDialog(){
        dialogStage.close();
    }
    public void setCategoryData(CategoryEntiy category) {
        this.categoryToUpdate = category;
        if (category != null) {
            // Set form data for update
            nameField.setText(category.getName());
            typeComboBox.setValue(category.getCategoryEnum().getValue());
            isUpdate = true;
            submitButton.setText("Update");
        }
    }

    @FXML
    private void submitCategory() {
        if (!validateInput()) {
            return;
        }

        String nameCategory = nameField.getText();
        String type = typeComboBox.getValue();
        CategoryService categoryService = new CategoryService();

        try {
            if (isUpdate) {
                categoryService.update(categoryToUpdate.getId(), type, nameCategory);
                showAlert("Success", "Category updated successfully!", Alert.AlertType.INFORMATION);
            } else {
                categoryService.add(nameCategory, type);
                showAlert("Success", "Category added successfully!", Alert.AlertType.INFORMATION);
            }
            dialogStage.close();
        } catch (Exception e) {
            showAlert("Error :", e.getMessage(), Alert.AlertType.ERROR);
        }
    }

    private boolean validateInput() {
        if (nameField.getText().trim().isEmpty()) {
            showAlert(" Error", "Please enter a category name", Alert.AlertType.WARNING);
            return false;
        }
        return true;
    }

    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
}
