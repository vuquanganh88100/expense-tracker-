package com.example.ltnc.Service;

import com.example.ltnc.Dao.CategoryDao;
import com.example.ltnc.Dao.ExpenseDao;
import com.example.ltnc.Dao.IncomeDAO;
import com.example.ltnc.Dao.UserDao;
import com.example.ltnc.Entity.Category.CategoryEntiy;
import com.example.ltnc.Entity.ExpenseEntity;
import com.example.ltnc.Entity.IncomeEntity;
import com.example.ltnc.Utils.ExportUtils;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InComeService {
    public void add(Integer categoryId, String item, Long cost ,
                    String description, LocalDate date){
        UserDao userDao=new UserDao();
        CategoryDao categoryDao=new CategoryDao();
        CategoryEntiy categoryEntiy=new CategoryEntiy();
        IncomeDAO income=new IncomeDAO();
        categoryEntiy=categoryDao.getCategoryById(categoryId);
        IncomeEntity incomeEntity=new IncomeEntity(userDao.getInfo(),categoryEntiy,
                item,description,cost,date, new Timestamp(System.currentTimeMillis()));
        income.add(incomeEntity);
    }
    public List<IncomeEntity> getData(int userId){
        IncomeDAO incomeDAO=new IncomeDAO();
        return incomeDAO.getData(userId);
    }
    public void update(Integer categoryId,String item,Long cost,String description,LocalDate date,Integer expenseId){
        CategoryDao categoryDao=new CategoryDao();
        CategoryEntiy categoryEntiy=new CategoryEntiy();
        IncomeDAO incomeDAO=new IncomeDAO();
        categoryEntiy=categoryDao.getCategoryById(categoryId);
        IncomeEntity income=new IncomeEntity();
        income.setMoney(cost);
        income.setItem(item);
        income.setDescription(description);
        income.setDate(date);
        income.setCategoryEntiy(categoryEntiy);
        income.setCreated_at(new Timestamp(System.currentTimeMillis()));
        income.setId(expenseId);
        incomeDAO.update(income);
    }
    public Map<String,Long> statisticIncome(int userId){
        List<IncomeEntity>incomeEntities=getData(userId);
        Long sumIncomeMonth= Long.valueOf(0);
        Long sumIncomeYear= Long.valueOf(0);
        Long sumIncomeDay= Long.valueOf(0);
        Long sumIncomeTotal=Long.valueOf(0);
        System.out.println(sumIncomeDay + sumIncomeMonth + sumIncomeYear);
        LocalDate currentDate=LocalDate.now();
        int month=currentDate.getMonthValue();
        int day=currentDate.getDayOfMonth();
        System.out.println("day "+day);
        int year=currentDate.getYear();
        Map<String,Long> incomeMoney=new HashMap<>();
        for(IncomeEntity incomeEntity:incomeEntities){
            if(incomeEntity.getDate().getMonthValue()==month){
                sumIncomeMonth=sumIncomeMonth+incomeEntity.getMoney();
            }
            if(incomeEntity.getDate().getYear()==year){
                sumIncomeYear+=incomeEntity.getMoney();
            }
            if(incomeEntity.getDate().getDayOfMonth()==day){
                sumIncomeDay+=incomeEntity.getMoney();
            }
            sumIncomeTotal+=incomeEntity.getMoney();
        }
        incomeMoney.put("month",sumIncomeMonth);
        incomeMoney.put("year",sumIncomeYear);
        incomeMoney.put("day",sumIncomeDay);
        incomeMoney.put("total",sumIncomeTotal);
        return incomeMoney;
    }
    public String exportIncomeToExcel(LocalDate startDate, LocalDate endDate) throws Exception {
        IncomeDAO incomeDAO = new IncomeDAO();

        List<IncomeEntity> incomes = incomeDAO.getIncomeByDateRange(startDate, endDate);

        String filePath = "Income_" + startDate + "_to_" + endDate + ".xlsx";
        ExportUtils.createFinancialExcel(incomes, filePath);

        return filePath;
    }

}
