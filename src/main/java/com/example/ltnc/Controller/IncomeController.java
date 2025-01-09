package com.example.ltnc.Controller;

import com.example.ltnc.Dao.CategoryDao;
import com.example.ltnc.Dao.ExpenseDao;
import com.example.ltnc.Dao.IncomeDAO;
import com.example.ltnc.Entity.Category.CategoryEntity;
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
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;
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
    @FXML
    private AnchorPane dateRangePane;
    @FXML
    private DatePicker startDatePicker;

    @FXML
    private DatePicker endDatePicker;


    private final static Map<String, Integer> categoryIdMap = new HashMap<>();
    private static int incomeId;

    public Map<String, Integer> getCategoryIdMap() {
        return categoryIdMap;
    }

    public void initialize() {

        loadIncome();
        int userId = SessionManager.getInstance().getUserId();

        // combobox
        String type = "Income";
        CategoryDao categoryDao=new CategoryDao();
        List<CategoryEntity> categories = categoryDao.categoryBox(userId, type);

        List<String> categoryNames = new ArrayList<>();

        categories.forEach(category -> {
            categoryIdMap.put(category.getName(), category.getId());
            categoryNames.add(category.getName());
        });

        categoryComboBox.setItems(FXCollections.observableArrayList(categoryNames));
        categoryComboBox.getSelectionModel().selectFirst();
    }

    public void loadIncome() {
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
    }


    @FXML
    public void add(){
        try {
            String categoryName = categoryComboBox.getValue();
            Integer categoryId = categoryIdMap.get(categoryName); // Lấy id từ Map
//            System.out.println(categoryId);
            String item = itemTextField.getText();
//            System.out.println(item);
            Long cost = Long.valueOf(costTextField.getText());
            String description = Description.getText();
            LocalDate date = getLocalDate(categoryName, item, cost);

            // Tìm category từ database
            CategoryDao categoryDao = new CategoryDao();
            CategoryEntity categoryEntity  = categoryDao.getCategoryById(SessionManager.getInstance().getUserId());

            if (categoryEntity == null) {
                throw new IllegalStateException("Category '" + categoryName + "' không tồn tại trong database.");
            }

            InComeService inComeService = new InComeService();
            inComeService.add(categoryId,item,cost,description,date);

            loadIncome();

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
    public void handleDeleteIncome(ActionEvent actionEvent) {
        IncomeEntity selectedExpense = incomeTable.getSelectionModel()
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
                    InComeService inComeService = new InComeService();
                    inComeService.delete(selectedExpense.getId());

                    // Tải lại dữ liệu sau khi xoá
                    loadIncome();

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

                loadIncome();

            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Import thanh cong");
        }
    }

    public void handleExportData(ActionEvent actionEvent) {
        dateRangePane.setVisible(true);
    }
}
