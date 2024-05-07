package com.exam.csvApp.csvUpdater.Helpers.fileReadersAndWriters;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

public class CSVRecordReader implements RecordReader {
    private String fileName;

    public CSVRecordReader(String fileName) {
        this.fileName = fileName;
    }

    public List<String[]> readAllLines() throws IOException {
        FileReader filereader = new FileReader(fileName);
        CSVReader csvReader = new CSVReaderBuilder(filereader)
                .build();
        List<String[]> allData = csvReader.readAll();
        System.out.println(allData);
        csvReader.close();
        return allData;
    }
}
