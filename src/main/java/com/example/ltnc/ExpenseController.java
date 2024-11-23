package com.example.ltnc;

import com.example.ltnc.Dao.CategoryDao;
import com.example.ltnc.Entity.ExpenseEntity;
import com.example.ltnc.Service.ExpenseService;
import com.example.ltnc.Service.SessionManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.List;

public class ExpenseController {
    // form add data
    @FXML
     private ComboBox<String>categoryComboBox;
    @FXML
    private TextField itemTextField;
    @FXML
    private TextField costTextField;
    @FXML
    private TextField Description;
    @FXML
    private DatePicker datePicker;

    //table get data
    @FXML
    private TableView<ExpenseEntity> expenseTable;

    @FXML
    private TableColumn<ExpenseEntity, Integer> idData;
    @FXML
    private TableColumn<ExpenseEntity, String> categoryData;
    @FXML
    private TableColumn<ExpenseEntity, String> itemData;
    @FXML
    private TableColumn<ExpenseEntity, Long> costData;
    @FXML
    private TableColumn<ExpenseEntity, String> descriptionData;
    @FXML
    private TableColumn<ExpenseEntity, LocalDate> dateData;

    @FXML
    public void initialize() {
        int userId = SessionManager.getInstance().getUserId();
        //column data
        ExpenseService expenseService=new ExpenseService();
        List<ExpenseEntity> expenseEntityList=expenseService.getData(userId);
        idData.setCellValueFactory(new PropertyValueFactory<ExpenseEntity, Integer>("id"));
        categoryData.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategoryEntiy().getName()));
        itemData.setCellValueFactory(new PropertyValueFactory<ExpenseEntity, String>("item"));
        costData.setCellValueFactory(new PropertyValueFactory<ExpenseEntity,Long>("money"));
        descriptionData.setCellValueFactory(new PropertyValueFactory<ExpenseEntity, String>("description"));
        dateData.setCellValueFactory(new PropertyValueFactory<ExpenseEntity, LocalDate>("date"));
        ObservableList<ExpenseEntity> observableList = FXCollections.observableArrayList(expenseEntityList);
        expenseTable.setItems(observableList);
        System.out.println(expenseEntityList);

        // combobox
        String type = "Expense";
        CategoryDao categoryDao=new CategoryDao();
        List<String> categoryListName = categoryDao.categoryBox(userId, type);
        for(String s:categoryListName){
            System.out.println(s);
        }
        ObservableList<String> observableCategoryList = FXCollections.observableArrayList(categoryListName);

        categoryComboBox.setItems(observableCategoryList);
        categoryComboBox.getSelectionModel().selectFirst();


    }

    @FXML
    public void add(){
        String category=categoryComboBox.getValue();
        System.out.println(category);
        String item=itemTextField.getText();
        System.out.println(item);
        Long cost= Long.valueOf(costTextField.getText());
        String description=Description.getText();
        LocalDate date= datePicker.getValue();
        ExpenseService expenseService=new ExpenseService();
        expenseService.add(category,item,cost,description,date);
    }

}
