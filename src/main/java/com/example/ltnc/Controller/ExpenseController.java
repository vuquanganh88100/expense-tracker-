package com.example.ltnc.Controller;

import com.almasb.fxgl.entity.action.Action;
import com.example.ltnc.Dao.CategoryDao;
import com.example.ltnc.Dao.ExpenseDao;
import com.example.ltnc.Entity.Category.CategoryEntity;
import com.example.ltnc.Entity.ExpenseEntity;
import com.example.ltnc.Service.ExpenseService;
import com.example.ltnc.Service.SessionManager;
import com.example.ltnc.Utils.ExpenseImport;
import com.example.ltnc.Utils.ExportUtils;
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
import org.apache.poi.ss.usermodel.Workbook;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpenseController {
    private ExpenseService expenseService = new ExpenseService();

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

    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;

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
    private AnchorPane dateRangePane;
    private static final Map<String, Integer> categoryIdMap = new HashMap<>();
    private static Integer expenseId;

    public Map<String, Integer> getCategoryIdMap() {
        return categoryIdMap;
    }

    @FXML
    public void initialize() {

        loadExpenses();

        int userId = SessionManager.getInstance().getUserId();

        // combobox
        CategoryDao categoryDao = new CategoryDao();
        List<CategoryEntity> categories = categoryDao.categoryBox(userId, "Expense");

        List<String> categoryNames = new ArrayList<>();

        categories.forEach(category -> {
            categoryIdMap.put(category.getName(), category.getId());
            categoryNames.add(category.getName());
        });

        categoryComboBox.setItems(FXCollections.observableArrayList(categoryNames));
        categoryComboBox.getSelectionModel().selectFirst();


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

    @FXML
    public void add() {
        try {
            // Lấy dữ liệu từ giao diện
            String category = categoryComboBox.getValue();
            int categoryId = categoryIdMap.get(category);
            String item = itemTextField.getText();
            Long cost = Long.valueOf(costTextField.getText());
            String description = Description.getText();
            LocalDate date = getLocalDate(category, item, cost);

            // Tìm category từ database
            CategoryDao categoryDao = new CategoryDao();
            CategoryEntity categoryEntity  = categoryDao.getCategoryById(SessionManager.getInstance().getUserId());

            if (categoryEntity == null) {
                throw new IllegalStateException("Category '" + category + "' không tồn tại trong database.");
            }

            // Thêm expense vào database
            ExpenseService expenseService = new ExpenseService();
            expenseService.add(categoryId, item, cost, description, date);

            // Load lại danh sách expenses sau khi thêm
            loadExpenses();

        } catch (NumberFormatException e) {
            // Xử lý lỗi định dạng số
            showError("Cost phải là số hợp lệ.");
        } catch (IllegalArgumentException | IllegalStateException e) {
            // Xử lý lỗi logic
            showError(e.getMessage());
        } catch (Exception e) {
            // Xử lý lỗi chung
            e.printStackTrace();
            showError("Đã xảy ra lỗi khi thêm dữ liệu.");
        }
    }

    @NotNull
    private LocalDate getLocalDate(String category, String item, Long cost) {
        LocalDate date = datePicker.getValue();

        // Kiểm tra dữ liệu đầu vào
        if (category == null || category.isEmpty()) {
            throw new IllegalArgumentException("Category không được để trống.");
        }
        if (item == null || item.isEmpty()) {
            throw new IllegalArgumentException("Item không được để trống.");
        }
        if (cost == null || cost <= 0) {
            throw new IllegalArgumentException("Cost phải là số dương.");
        }
        if (date == null) {
            throw new IllegalArgumentException("Date không được để trống.");
        }
        return date;
    }

    private void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
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
    public void handleDeleteExpense(ActionEvent actionEvent) {
        ExpenseEntity selectedExpense = expenseTable.getSelectionModel()
                .getSelectedItem();

        if (selectedExpense != null) {
            // Xác nhận trước khi xoá
            Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmationAlert.setTitle("Delete Confirmation");
            confirmationAlert.setHeaderText("Are you sure you want to delete this expense?");
            confirmationAlert.setContentText("Expense ID: " + selectedExpense.getId());

            // Hiển thị hộp thoại xác nhận
            confirmationAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {

                    // Gọi Service để xoá
                    ExpenseService expenseService = new ExpenseService();
                    expenseService.delete(selectedExpense.getId());

                    // Tải lại dữ liệu sau khi xoá
                    loadExpenses();

                    // Hiển thị thông báo
                    Alert infoAlert = new Alert(Alert.AlertType.INFORMATION);
                    infoAlert.setTitle("Delete Successful");
                    infoAlert.setHeaderText(null);
                    infoAlert.setContentText("Expense deleted successfully!");
                    infoAlert.showAndWait();
                }
            });
        } else {
            // Thông báo nếu không có mục nào được chọn
            Alert warningAlert = new Alert(Alert.AlertType.WARNING);
            warningAlert.setTitle("No selection");
            warningAlert.setHeaderText(null);
            warningAlert.setContentText("Please select an expense to delete");
            warningAlert.showAndWait();
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

    @FXML
    private void handleExportData(ActionEvent event) {
        dateRangePane.setVisible(true);
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
    public void cancelExport(ActionEvent actionEvent) {
        dateRangePane.setVisible(false);
    }
}

