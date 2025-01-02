package com.example.ltnc;

import com.example.ltnc.Dao.CategoryDao;
import com.example.ltnc.Dao.ExpenseDao;
import com.example.ltnc.Dao.IncomeDAO;
import com.example.ltnc.Entity.Category.CategoryEntiy;
import com.example.ltnc.Entity.ExpenseEntity;
import com.example.ltnc.Entity.IncomeEntity;
import com.example.ltnc.Service.ExpenseService;
import com.example.ltnc.Service.InComeService;
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

public class IncomeController {
    @FXML
    private ComboBox<String> categoryComboBox;
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
    private TableView<IncomeEntity> incomeTable;

    @FXML
    private TableColumn<IncomeEntity, Integer> idData;
    @FXML
    private TableColumn<IncomeEntity, String> categoryData;
    @FXML
    private TableColumn<IncomeEntity, String> itemData;
    @FXML
    private TableColumn<IncomeEntity, Long> costData;
    @FXML
    private TableColumn<IncomeEntity, String> descriptionData;
    @FXML
    private TableColumn<IncomeEntity, LocalDate> dateData;
    private Map<String, Integer> categoryIdMap = new HashMap<>();
    private static int incomeId;
    public void initialize() {
        int userId = SessionManager.getInstance().getUserId();
        //column data
        InComeService incomeService=new InComeService();
        List<IncomeEntity> incomeEntities=incomeService.getData(userId);
        idData.setCellValueFactory(new PropertyValueFactory<IncomeEntity, Integer>("id"));
        categoryData.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCategoryEntiy().getName()));
        itemData.setCellValueFactory(new PropertyValueFactory<IncomeEntity, String>("item"));
        costData.setCellValueFactory(new PropertyValueFactory<IncomeEntity,Long>("money"));
        descriptionData.setCellValueFactory(new PropertyValueFactory<IncomeEntity, String>("description"));
        dateData.setCellValueFactory(new PropertyValueFactory<IncomeEntity, LocalDate>("date"));
        ObservableList<IncomeEntity> observableList = FXCollections.observableArrayList(incomeEntities);
        incomeTable.setItems(observableList);
        // combobox
        String type = "Income";
        CategoryDao categoryDao=new CategoryDao();
        List<CategoryEntiy> categories = categoryDao.categoryBox(userId, type);

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
        String categoryName=categoryComboBox.getValue();
        Integer categoryId = categoryIdMap.get(categoryName); // Lấy id từ Map
        System.out.println(categoryId);
        String item=itemTextField.getText();
        System.out.println(item);
        Long cost= Long.valueOf(costTextField.getText());
        String description=Description.getText();
        LocalDate date= datePicker.getValue();
        InComeService inComeService=new InComeService();
        inComeService.add(categoryId,item,cost,description,date);
    }
    @FXML
    public void getIncomeDetail(MouseEvent mouseEvent){
        IncomeEntity selectedItem=incomeTable.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            incomeId = selectedItem.getId();
            String category = selectedItem.getCategoryEntiy().getName();
            String item = selectedItem.getItem();
            long cost = selectedItem.getMoney();
            String description = selectedItem.getDescription();
            LocalDate date = selectedItem.getDate();
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
        InComeService inComeService=new InComeService();
        inComeService.update(categoryId,item,cost,description,date,incomeId);
    }
    @FXML
    public void delete(){
        IncomeEntity selectedItem=incomeTable.getSelectionModel().getSelectedItem();
        if(selectedItem!=null){
            incomeId=selectedItem.getId();
            IncomeDAO incomeDAO=new IncomeDAO();
            incomeDAO.delete(incomeId);
        }
    }
}
