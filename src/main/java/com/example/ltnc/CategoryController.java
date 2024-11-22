package com.example.ltnc;

import com.example.ltnc.Entity.Category.CategoryEntiy;
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
    public void addCategory(ActionEvent event) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("addCategory.fxml"));
        Parent dialogRoot = loader.load();
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Add New Category");
        dialogStage.setScene(new Scene(dialogRoot));
        dialogStage.initModality(Modality.APPLICATION_MODAL);
        dialogStage.showAndWait();
    }
    @FXML
    public void initialize(){
        CategoryService categoryService=new CategoryService();
        List<CategoryEntiy> categoryEntiyList=categoryService.categoryEntiy(SessionManager.getInstance().getUserId());
        ObservableList<CategoryEntiy> observableCategoryList = FXCollections.observableArrayList(categoryEntiyList);
        idColumn.setCellValueFactory(new PropertyValueFactory<CategoryEntiy, Integer>("id"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<CategoryEntiy,String>("categoryEnum"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<CategoryEntiy,String>("name"));
        actionColumn.setCellFactory(param -> new TableCell<CategoryEntiy, String>() {
            private final Button updateButton = new Button("Update");
            private final Button deleteButton = new Button("Delete");

            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setGraphic(null); // Nếu dòng rỗng, không hiển thị gì
                } else {
                    // Thêm các nút vào ô cột
                    HBox hbox = new HBox(10);
                    hbox.getChildren().addAll(updateButton, deleteButton);

                    setGraphic(hbox); // Hiển thị HBox với các nút
                }
            }
        });
        categoryTableView.setItems(observableCategoryList);
    }


}
