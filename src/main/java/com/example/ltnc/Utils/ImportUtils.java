package com.example.ltnc.Utils;

import com.example.ltnc.Dao.CategoryDao;
import com.example.ltnc.Dao.ExpenseDao;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.ZoneId;

import com.example.ltnc.Service.SessionManager;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
public class ImportUtils {
    private final CategoryDao categoryDao = new CategoryDao();

    private final ExpenseDao expenseDao = new ExpenseDao();

    public void importFinancialFromExcel(String filePath, FinancialImport financialImport) throws IOException {
        // Mở file Excel
        FileInputStream fileInputStream = new FileInputStream(filePath);
        Workbook workbook = new XSSFWorkbook(fileInputStream);
        Sheet sheet = workbook.getSheetAt(0);

        // Lấy thông tin người dùng hiện tại từ SessionManager
        int userId = SessionManager.getInstance().getUserId();
        if (userId == 0) {
            throw new IllegalStateException("Không có người dùng nào đang đăng nhập");
        }

        for (int i = 1; i <= sheet.getLastRowNum(); i++) {
            Row row = sheet.getRow(i);
            if (row == null || isRowEmpty(row)) continue;

            try {
                // Đọc dữ liệu từ file Excel
                String categoryName = row.getCell(0).getStringCellValue().trim();
                int categoryId = financialImport.findOrCreateCategory(categoryName);

                String item = row.getCell(1).getStringCellValue().trim();
                long cost = (long) row.getCell(2).getNumericCellValue();
                String description = row.getCell(3).getStringCellValue().trim();

                // Kiểm tra và lấy ngày hợp lệ
                LocalDate date;
                if (row.getCell(4).getCellType() == CellType.NUMERIC && DateUtil.isCellDateFormatted(row.getCell(4))) {
                    date = row.getCell(4).getDateCellValue().toInstant()
                            .atZone(ZoneId.systemDefault())
                            .toLocalDate();
                } else {
                    throw new IllegalStateException("Dữ liệu ngày không hợp lệ ở dòng " + (i + 1));
                }

                financialImport.saveRecord(categoryId, item, cost, description, date, new Timestamp(System.currentTimeMillis()));

            } catch (Exception e) {
                System.err.println("Lỗi tại dòng " + (i + 1) + ": " + e.getMessage());
            }
        }

        // Đóng workbook và stream
        workbook.close();
        fileInputStream.close();
    }

    private boolean isRowEmpty(Row row) {
        if (row == null) {
            return true;
        }
        for (int cellNum = 0; cellNum < row.getLastCellNum(); cellNum++) {
            Cell cell = row.getCell(cellNum);
            if (cell != null && cell.getCellType() != CellType.BLANK) {
                return false;
            }
        }
        return true;
    }

}