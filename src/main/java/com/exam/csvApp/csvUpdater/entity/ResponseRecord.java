package com.exam.csvApp.csvUpdater.entity;

public class ResponseRecord {
    private int rowsUpdated;
    private String csvId;

    public ResponseRecord(int rowsUpdated, String csvId) {
        this.rowsUpdated = rowsUpdated;
        this.csvId = csvId;
    }

    public int getRowsUpdated() {
        return rowsUpdated;
    }

    public String getCsvId() {
        return csvId;
    }
}
