package com.example.ltnc;

import com.example.ltnc.Service.CategoryService;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;

public class AddCategoryController {
    @FXML
    TextField nameField;
    @FXML
    private ComboBox<String> typeComboBox;

    @FXML
    private void initialize() {
        typeComboBox.setItems(FXCollections.observableArrayList("Income", "Expense"));
        typeComboBox.getSelectionModel().selectFirst();
    }
    @FXML
    private void submitCategory(ActionEvent event){
        String nameCategory=nameField.getText();
        String type=typeComboBox.getValue();
        CategoryService categoryService=new CategoryService();
        categoryService.add(nameCategory,type);
    }
}
