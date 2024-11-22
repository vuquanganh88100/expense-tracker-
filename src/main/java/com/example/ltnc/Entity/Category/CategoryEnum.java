package com.example.ltnc.Entity.Category;

public enum CategoryEnum {
    EXPENSE("Expense"),
    INCOME("Income");
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    CategoryEnum(String value) {
        this.value = value;
    }
    public static CategoryEnum fromString(String text) {
        for (CategoryEnum b : CategoryEnum.values()) {
            if (b.value.equalsIgnoreCase(text)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value: " + text);
    }
}
