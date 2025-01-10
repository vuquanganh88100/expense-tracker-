package com.example.ltnc;

import com.example.ltnc.Dao.CategoryDao;
import com.example.ltnc.Dao.ExpenseDao;
import com.example.ltnc.Entity.Category.CategoryEntiy;
import com.example.ltnc.Entity.ExpenseEntity;
import com.example.ltnc.Service.ExpenseService;
import com.example.ltnc.Service.SessionManager;
import com.example.ltnc.Utils.ExpenseImport;
import com.example.ltnc.Utils.ImportUtils;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;

import java.io.File;
import java.io.IOException;
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
    private static final Map<String, Integer> categoryIdMap = new HashMap<>();
    private static Integer expenseId;
    public Map<String, Integer> getCategoryIdMap() {
        return categoryIdMap;
    }

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;
    @FXML
    private AnchorPane dateRangePane;

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
    public void add() {
        try {
            if (!validateInput()) {
                return;
                // have error jump to catch , but else jump to save ...
            }
            String category = categoryComboBox.getValue();
            int categoryId = categoryIdMap.get(category);
            String item = itemTextField.getText();
            Long cost = Long.valueOf(costTextField.getText());
            String description = Description.getText();
            LocalDate date = datePicker.getValue();

            if (showAlertComfirm("Add", "Category: " + category + "\nItem: " + item)) {
                ExpenseService expenseService = new ExpenseService();
                expenseService.add(categoryId, item, cost, description, date);
                showSuccessAlert("Add");
                loadExpenses();
                clearFields();
            }
        } catch (NumberFormatException e) {
            showValidationAlert("Please enter a valid cost amount.");
        } catch (Exception e) {
            showAlert("Error", "An error  when adding the expense.", Alert.AlertType.ERROR);
        }
    }
    @FXML
    public void update() {
        try {
            if (!validateInput()) {
                return;
            }

            String category = categoryComboBox.getValue();
            int categoryId = categoryIdMap.get(category);
            String item = itemTextField.getText();
            Long cost = Long.valueOf(costTextField.getText());
            String description = Description.getText();
            LocalDate date = datePicker.getValue();

            if (showAlertComfirm("Update", "Expense ID : " + expenseId + "\nCategory: " + category + "\nItem: " + item)) {
                ExpenseService expenseService = new ExpenseService();
                expenseService.update(categoryId, item, cost, description, date, expenseId);
                showSuccessAlert("Update");
                loadExpenses();
                clearFields();
            }
        } catch (NumberFormatException e) {
            showValidationAlert("Please enter a valid cost amount");
        } catch (Exception e) {
            showAlert("Error", "An error when update the expense.", Alert.AlertType.ERROR);
        }
    }
    @FXML
    public void delete(ActionEvent actionEvent) {
        ExpenseEntity selectedExpense = expenseTable.getSelectionModel().getSelectedItem();

        if (selectedExpense != null) {
            String itemDetails = "Expense ID: " + selectedExpense.getId() +
                    "\nCategory: " + selectedExpense.getCategoryEntiy().getName() +
                    "\nItem: " + selectedExpense.getItem();

            if (showAlertComfirm("Delete", itemDetails)) {
                try {
                    ExpenseService expenseService = new ExpenseService();
                    expenseService.delete(selectedExpense.getId());
                    showSuccessAlert("Delete");
                    loadExpenses();
                    clearFields();
                } catch (Exception e) {
                    showAlert("Error", "An error  when delete the expense.", Alert.AlertType.ERROR);
                }
            }
        } else {
            showValidationAlert("Please select the expense to delete");
        }
    }
    @FXML
    public void handleImportData(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            try {
                ImportUtils importUtils = new ImportUtils();
                importUtils.importFinancialFromExcel(file.getAbsolutePath(), new ExpenseImport());

                loadExpenses();

            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Import thanh cong");
        }
    }

    public void loadExpenses() {
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
    }
    // Hiển thị thông báo
    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
    @FXML
    private void confirmExport(ActionEvent event) {
        ExpenseService expenseService = new ExpenseService();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        // Kiểm tra dữ liệu nhập vào
        if (startDate == null || endDate == null) {
            showAlert("Error", "Please select both start and end dates!", Alert.AlertType.ERROR);
            return;
        }

        try {
            // Gọi hàm export từ ExpenseService
            String filePath = expenseService.exportExpensesToExcel(startDate, endDate);
            showAlert("Success", "Data exported successfully! File saved at: " + filePath, Alert.AlertType.INFORMATION);

            // Ẩn khung chọn ngày tháng sau khi export thành công
            dateRangePane.setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to export data. Please try again.", Alert.AlertType.ERROR);
        }
    }
    @FXML
    private void handleExportData(ActionEvent event) {
        dateRangePane.setVisible(true);
    }
    @FXML
    public void cancelExport(ActionEvent actionEvent) {
        dateRangePane.setVisible(false);
    }

    private boolean showAlertComfirm(String action,String itemDetails){
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle(action + "Notification");
        confirmationAlert.setHeaderText("Are you sure want to" + action.toLowerCase() + " this expense");
        confirmationAlert.setContentText(itemDetails);

        return confirmationAlert.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .isPresent();
    }
    private void showSuccessAlert(String action) {
        showAlert(
                action + " Successful",
                "Expense " + action.toLowerCase() + "successfully!",
                Alert.AlertType.INFORMATION
        );
    }
    private void showValidationAlert(String message) {
        showAlert(
                "Validation Error",
                message,
                Alert.AlertType.WARNING
        );
    }
    private boolean validateInput() {
        if (categoryComboBox.getValue() == null || categoryComboBox.getValue().trim().isEmpty()) {
            showValidationAlert("Please select a category");
            return false;
        }
        if (itemTextField.getText().trim().isEmpty()) {
            showValidationAlert("Please enter an item");
            return false;
        }
        if (costTextField.getText().trim().isEmpty()) {
            showValidationAlert("Please enter a cost");
            return false;
        }
        if (datePicker.getValue() == null) {
            showValidationAlert("Please select a date");
            return false;
        }
        return true;
    }
    private void clearFields() {
        categoryComboBox.getSelectionModel().selectFirst();
        itemTextField.clear();
        costTextField.clear();
        Description.clear();
        datePicker.setValue(null);
    }

}
