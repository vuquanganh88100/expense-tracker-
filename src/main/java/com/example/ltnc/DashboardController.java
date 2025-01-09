package com.example.ltnc;

import com.example.ltnc.Service.ExpenseService;
import com.example.ltnc.Service.InComeService;
import com.example.ltnc.Service.SessionManager;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.text.NumberFormat;
import java.util.Locale;
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

        NumberFormat numberFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));todayIncomeLabel.setText(numberFormat.format(statisticIncome.getOrDefault("day", 0L)));
        monthIncomeLabel.setText(numberFormat.format(statisticIncome.getOrDefault("month", 0L)));
        yearIncomeLabel.setText(numberFormat.format(statisticIncome.getOrDefault("year", 0L)));
        totalIncomeLabel.setText(numberFormat.format(statisticIncome.getOrDefault("total", 0L)));

        todayExpenseLabel.setText(numberFormat.format(statisticExpense.getOrDefault("today", 0L)));
        monthExpenseLabel.setText(numberFormat.format(statisticExpense.getOrDefault("month", 0L)));
        yearExpenseLabel.setText(numberFormat.format(statisticExpense.getOrDefault("year", 0L)));
        totalExpenseLabel.setText(numberFormat.format(statisticExpense.getOrDefault("total", 0L)));
    }
}
