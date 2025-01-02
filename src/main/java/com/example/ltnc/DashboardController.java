package com.example.ltnc;

import com.example.ltnc.Service.ExpenseService;
import com.example.ltnc.Service.InComeService;
import com.example.ltnc.Service.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.util.Map;

public class DashboardController {
    // data income

    @FXML
    private Label todayIncomeLabel;

    @FXML
    private Label monthIncomeLabel;

    @FXML
    private Label yearIncomeLabel;
    @FXML
    private Label totalIncomeLabel;
    //expense data
    @FXML
    private Label todayExpenseLabel;

    @FXML
    private Label monthExpenseLabel;
    @FXML
    private Label yearExpenseLabel;

    @FXML
    private  Label totalExpenseLabel;
    
    @FXML
    public void initialize() {
        int currentId = SessionManager.getInstance().getUserId();
        InComeService inComeService = new InComeService();
        Map<String, Long> statisticIncome = inComeService.statisticIncome(currentId);
        ExpenseService expenseService=new ExpenseService();
        Map<String,Long>statisticExpense=expenseService.statísticExpense(currentId);
        // Cập nhật các giá trị thu nhập vào Label
        todayIncomeLabel.setText(String.valueOf(statisticIncome.getOrDefault("day", 0L)));
        monthIncomeLabel.setText(String.valueOf(statisticIncome.getOrDefault("month", 0L)));
        yearIncomeLabel.setText(String.valueOf(statisticIncome.getOrDefault("year", 0L)));
        totalIncomeLabel.setText(String.valueOf(statisticIncome.getOrDefault("total", 0L)));

        totalExpenseLabel.setText(String.valueOf(statisticExpense.getOrDefault("total", 0L)));
        monthExpenseLabel.setText(String.valueOf(statisticExpense.getOrDefault("month", 0L)));
        yearExpenseLabel.setText(String.valueOf(statisticExpense.getOrDefault("year", 0L)));
        todayExpenseLabel.setText(String.valueOf(statisticExpense.getOrDefault("today", 0L)));
    }
}
