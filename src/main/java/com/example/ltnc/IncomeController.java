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
import com.example.ltnc.Utils.ImportUtils;
import com.example.ltnc.Utils.IncomeImport;
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

public class IncomeController {
    @FXML
    private AnchorPane dateRangePane;
    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;
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
    private static Map<String, Integer> categoryIdMap = new HashMap<>();
    private static Integer incomeId;
    public Map<String, Integer> getCategoryIdMap() {
        return categoryIdMap;
    }
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
    public void add() {
        try {
            if (!validateInput()) {
                return;
            }

            String categoryName = categoryComboBox.getValue();
            Integer categoryId = categoryIdMap.get(categoryName);
            String item = itemTextField.getText();
            Long cost = Long.valueOf(costTextField.getText());
            String description = Description.getText();
            LocalDate date = datePicker.getValue();
            if (showAlertComfirm("Add", getDetailItem(incomeId,categoryName,item,cost,date))) {
                InComeService inComeService = new InComeService();
                inComeService.add(categoryId, item, cost, description, date);
                showSuccessAlert("Add");
                initialize();
                clearFields();
            }
        } catch (NumberFormatException e) {
            showValidationAlert("Please enter a valid cost amount");
        } catch (Exception e) {
            showAlert("Error", "An error when add the income.", Alert.AlertType.ERROR);
            e.printStackTrace();
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



            if (showAlertComfirm("Update", getDetailItem(incomeId,category,item,cost,date))) {
                InComeService inComeService = new InComeService();
                inComeService.update(categoryId, item, cost, description, date, incomeId);
                showSuccessAlert("Update");
                initialize();
                clearFields();
            }
        } catch (NumberFormatException e) {
            showValidationAlert("Please enter a valid cost amount");
        } catch (Exception e) {
            showAlert("Error", "An error occurred when update the income.", Alert.AlertType.ERROR);
            e.printStackTrace();
        }
    }

    @FXML
    public void delete() {
        IncomeEntity selectedItem = incomeTable.getSelectionModel().getSelectedItem();

        if (selectedItem != null) {
            String itemDetails = String.format(
                    "Income ID: %d\nCategory: %s\nItem: %s\nCost: %d\nDate: %s",
                    selectedItem.getId(),
                    selectedItem.getCategoryEntiy().getName(),
                    selectedItem.getItem(),
                    selectedItem.getMoney(),
                    selectedItem.getDate()
            );

            if (showAlertComfirm("Delete", itemDetails)) {
                try {
                    IncomeDAO incomeDAO = new IncomeDAO();
                    incomeDAO.delete(selectedItem.getId());
                    showSuccessAlert("Delete");
                    initialize();
                    clearFields();
                } catch (Exception e) {
                    showAlert("Error", "An error occurred when delte  the income.", Alert.AlertType.ERROR);
                    e.printStackTrace();
                }
            }
        } else {
            showValidationAlert("Please select an income to delete");
        }
    }
    // handle file
    private void showAlert(String title, String content, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
    public void confirmExport(ActionEvent actionEvent) {
        InComeService inComeService = new InComeService();
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (startDate == null || endDate == null) {
            showAlert("Error", "Please select both start and end dates!", Alert.AlertType.ERROR);
            return;
        }

        try {
            // Gọi hàm export từ ExpenseService
            String filePath = inComeService.exportIncomeToExcel(startDate, endDate);
            showAlert("Success", "Data exported successfully! File saved at: " + filePath, Alert.AlertType.INFORMATION);

            dateRangePane.setVisible(false);
        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Error", "Failed to export data. Please try again.", Alert.AlertType.ERROR);
        }
    }

    public void cancelExport(ActionEvent actionEvent) {
        dateRangePane.setVisible(false);
    }

    public void handleImportData(ActionEvent actionEvent) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters()
                .add(new FileChooser.ExtensionFilter("Excel Files", "*.xlsx"));
        File file = fileChooser.showOpenDialog(null);

        if (file != null) {
            try {
                ImportUtils importUtils = new ImportUtils();
                importUtils.importFinancialFromExcel(file.getAbsolutePath(), new IncomeImport());

                initialize();

            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Import thanh cong");
        }
    }

    public void handleExportData(ActionEvent actionEvent) {
        dateRangePane.setVisible(true);
    }


    // handle validate crud and show alert
    public boolean showAlertComfirm(String action,String itemDetails){
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle(action + "Notification");
        confirmationAlert.setHeaderText("Are you sure want to" + action.toLowerCase() + " this expense");
        confirmationAlert.setContentText(itemDetails);

        return confirmationAlert.showAndWait()
                .filter(response -> response == ButtonType.OK)
                .isPresent();
    }
    public void showSuccessAlert(String action) {
        showAlert(
                action + " Successful",
                "Expense " + action.toLowerCase() + "successfully!",
                Alert.AlertType.INFORMATION
        );
    }
    public void showValidationAlert(String message) {
        showAlert(
                "Validation Error",
                message,
                Alert.AlertType.WARNING
        );
    }
    public boolean validateInput() {
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
    public void clearFields() {
        categoryComboBox.getSelectionModel().selectFirst();
        itemTextField.clear();
        costTextField.clear();
        Description.clear();
        datePicker.setValue(null);
    }
    public String getDetailItem(Integer incomeId, String category, String item, Long cost, LocalDate date) {
        String detailItem = "IncomeId : " + incomeId + "\n" +
                "Category : " + category + "\n" +
                "Item: " + item + "\n" +
                "Cost : " + cost + "\n" +
                "Date: " + date;
        return detailItem;
    }



}
