package com.example.ltnc.Utils;

import java.sql.Timestamp;
import java.time.LocalDate;

public interface FinancialImport {
    int findOrCreateCategory(String categoryName);
    void saveRecord(int categoryId, String item, Long cost, String description, LocalDate date, Timestamp createdAt);
}
