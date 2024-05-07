package com.exam.csvApp.csvUpdater.Helpers.fileReadersAndWriters;

import com.opencsv.CSVWriter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVResultGenerator implements ResultGenerator {
    private String fileName;

    public CSVResultGenerator(String fileName) {
        this.fileName = fileName;
    }

    public void writeAllLines( List<String[]> allData) throws IOException {
        CSVWriter csvWriter = new CSVWriter(new FileWriter(fileName));
        csvWriter.writeAll(allData);
        csvWriter.close();
    }
}
