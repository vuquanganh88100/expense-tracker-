package com.example.ltnc.Service;

import com.example.ltnc.Dao.CategoryDao;
import com.example.ltnc.Dao.ExpenseDao;
import com.example.ltnc.Dao.UserDao;
import com.example.ltnc.Entity.Category.CategoryEntiy;
import com.example.ltnc.Entity.ExpenseEntity;
import com.example.ltnc.Entity.FinancialRecord;
import com.example.ltnc.Entity.IncomeEntity;
import com.example.ltnc.Utils.ExportUtils;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExpenseService {
    public void add(Integer categoryId, String item, Long cost ,
                    String description, LocalDate date){
        UserDao userDao=new UserDao();
        CategoryDao categoryDao=new CategoryDao();
        CategoryEntiy categoryEntiy=new CategoryEntiy();
        ExpenseDao expenseDao=new ExpenseDao();
        categoryEntiy=categoryDao.getCategoryById(categoryId);
        ExpenseEntity expense=new ExpenseEntity(userDao.getInfo(),categoryEntiy,
                item,description,cost,date, new Timestamp(System.currentTimeMillis()));
        expenseDao.add(expense);
    }
    public List<ExpenseEntity> getData(int userId){
        ExpenseDao expenseDao=new ExpenseDao();
        return expenseDao.getData(userId);
    }
    public void update(Integer categoryId,String item,Long cost,String description,LocalDate date,Integer expenseId){
        CategoryDao categoryDao=new CategoryDao();
        CategoryEntiy categoryEntiy=new CategoryEntiy();
        ExpenseDao expenseDao=new ExpenseDao();
        categoryEntiy=categoryDao.getCategoryById(categoryId);
        ExpenseEntity expense=new ExpenseEntity();
        expense.setMoney(cost);
        expense.setItem(item);
        expense.setDescription(description);
        expense.setDate(date);
        expense.setCategoryEntiy(categoryEntiy);
        expense.setCreated_at(new Timestamp(System.currentTimeMillis()));
        expense.setId(expenseId);
        expenseDao.update(expense);
    }
    public Map<String,Long> statísticExpense(int userId){
        List<ExpenseEntity>expenseEntityList=getData(userId);
        Long sumExpenseMonth= Long.valueOf(0);
        Long sumExpenseYear= Long.valueOf(0);
        Long sumExpenseDay= Long.valueOf(0);
        Long sumExpenseTotal=Long.valueOf(0);
        LocalDate currentDate=LocalDate.now();
        int month=currentDate.getMonthValue();
        int day=currentDate.getDayOfMonth();
        System.out.println("day "+day);
        int year=currentDate.getYear();
        Map<String,Long> expenseMoney=new HashMap<>();
        for(ExpenseEntity expense:expenseEntityList){
            if(expense.getDate().getMonthValue()==month){
                sumExpenseMonth=sumExpenseMonth+expense.getMoney();
            }
            if(expense.getDate().getYear()==year){
                sumExpenseYear+=expense.getMoney();
            }
            if(expense.getDate().getDayOfMonth()==day){
                sumExpenseDay+=expense.getMoney();
            }
            sumExpenseTotal+=expense.getMoney();
        }
        expenseMoney.put("month",sumExpenseMonth);
        expenseMoney.put("year",sumExpenseYear);
        expenseMoney.put("day",sumExpenseDay);
        expenseMoney.put("total",sumExpenseTotal);
        return expenseMoney;
    }
    public String exportExpensesToExcel(LocalDate startDate, LocalDate endDate) throws Exception {
        int currentUserId=SessionManager.getInstance().getUserId();;
        ExpenseDao expenseDao = new ExpenseDao();
        List<ExpenseEntity> expenses = expenseDao.getExpensesByDateRange(startDate, endDate,currentUserId);

        String filePath = "Expenses_" + startDate + "_to_" + endDate + ".xlsx";
        ExportUtils.createFinancialExcel(expenses, filePath);

        return filePath;
    }
    public void delete(int expenseId) {
        ExpenseDao expenseDao = new ExpenseDao();
        expenseDao.delete(expenseId);
    }

}
