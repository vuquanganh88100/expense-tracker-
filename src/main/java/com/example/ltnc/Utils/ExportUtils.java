package com.example.ltnc.Utils;

import com.example.ltnc.Entity.FinancialRecord;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ExportUtils {
    public static void createFinancialExcel(List<? extends FinancialRecord> records, String filePath) throws IOException {
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Financial Recodes");

        // Tạo header
        Row headerRow = sheet.createRow(0);
        String[] headers = {"ID", "Category", "Item", "Cost", "Description", "Date"};
        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
        }

        // Ghi dữ liệu
        int rowIndex = 1;
        for (FinancialRecord record : records) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(record.getId());
            System.out.println("record "+ record);
            System.out.println("category: " + record.getCategoryName());
            row.createCell(1).setCellValue(record.getCategoryName());
            row.createCell(2).setCellValue(record.getItem());
            row.createCell(3).setCellValue(record.getMoney());
            row.createCell(4).setCellValue(record.getDescription());
            row.createCell(5).setCellValue(record.getDate().toString());
        }

        // Lưu file
        try (FileOutputStream fileOut = new FileOutputStream(filePath)) {
            workbook.write(fileOut);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        workbook.close();
    }
}
