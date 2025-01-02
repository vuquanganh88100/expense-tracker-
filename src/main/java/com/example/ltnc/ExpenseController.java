package com.example.ltnc;

import com.example.ltnc.Dao.CategoryDao;
import com.example.ltnc.Dao.ExpenseDao;
import com.example.ltnc.Entity.Category.CategoryEntiy;
import com.example.ltnc.Entity.ExpenseEntity;
import com.example.ltnc.Service.ExpenseService;
import com.example.ltnc.Service.SessionManager;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    private Map<String, Integer> categoryIdMap = new HashMap<>();
    private static Integer expenseId;
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
        CategoryDao categoryDao = new CategoryDao();
        List<CategoryEntiy> categories = categoryDao.categoryBox(userId, "Expense");

        List<String> categoryNames = new ArrayList<>();

        categories.forEach(category -> {
            categoryIdMap.put(category.getName(), category.getId());
            categoryNames.add(category.getName());
        });

        categoryComboBox.setItems(FXCollections.observableArrayList(categoryNames));
        categoryComboBox.getSelectionModel().selectFirst();


    }

    @FXML
    public void add(){
        String category=categoryComboBox.getValue();
        int categoryId=categoryIdMap.get(category);
        System.out.println(category);
        String item=itemTextField.getText();
        System.out.println(item);
        Long cost= Long.valueOf(costTextField.getText());
        String description=Description.getText();
        LocalDate date= datePicker.getValue();
        ExpenseService expenseService=new ExpenseService();
        expenseService.add(categoryId,item,cost,description,date);
    }
    @FXML
    public void getExpenseDetail(MouseEvent mouseEvent){
        ExpenseEntity selectedExpense=expenseTable.getSelectionModel().getSelectedItem();
        if (selectedExpense != null) {
            expenseId = selectedExpense.getId();
            System.out.println(expenseId);
            String category = selectedExpense.getCategoryEntiy().getName();
            String item = selectedExpense.getItem();
            long cost = selectedExpense.getMoney();
            String description = selectedExpense.getDescription();
            LocalDate date = selectedExpense.getDate();

            categoryComboBox.setValue(category);
            itemTextField.setText(item);
            costTextField.setText(String.valueOf(cost));
            Description.setText(description);
            datePicker.setValue(date);
        }
    }
    @FXML
    public void update(){
        String category=categoryComboBox.getValue();
        int categoryId=categoryIdMap.get(category);
        System.out.println(category);
        String item=itemTextField.getText();
        System.out.println(item);
        Long cost= Long.valueOf(costTextField.getText());
        String description=Description.getText();
        LocalDate date= datePicker.getValue();
        ExpenseService expenseService=new ExpenseService();
        expenseService.update(categoryId,item,cost,description,date,expenseId);
    }
    @FXML
    public void delete(){
        ExpenseEntity selectedExpense=expenseTable.getSelectionModel().getSelectedItem();
        if(selectedExpense!=null){
            expenseId=selectedExpense.getId();
            ExpenseDao expenseDao=new ExpenseDao();
            expenseDao.delete(expenseId);
        }
    }


}
