package com.exam.csvApp.csvUpdater.entity;

public class RequestRecord {
    private String columnHeader;
    private String oldValue;
    private String newValue;

    public RequestRecord(String columnHeader, String oldValue, String newValue) {
        this.columnHeader = columnHeader;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    public String getColumnHeader() {
        return columnHeader;
    }

    public String getOldValue() {
        return oldValue;
    }

    public String getNewValue() {
        return newValue;
    }
}
