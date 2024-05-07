package com.exam.csvApp.csvUpdater.Helpers.fileReadersAndWriters;

import java.io.IOException;
import java.util.List;

public interface ResultGenerator {
    public void writeAllLines( List<String[]> allData) throws IOException ;
}
