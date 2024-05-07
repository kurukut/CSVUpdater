package com.exam.csvApp.csvUpdater.Helpers.fileReadersAndWriters;

import java.io.IOException;
import java.util.List;

public interface RecordReader {
    public List<String[]> readAllLines() throws IOException;
}
