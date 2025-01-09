package com.example.ltnc.Entity;

import java.sql.Timestamp;
import java.time.LocalDate;

public interface FinancialRecord {
    int getId();
    String getItem();
    String getDescription();
    Long getMoney();
    LocalDate getDate();
    Timestamp getCreatedAt();
    String getCategoryName();
}
