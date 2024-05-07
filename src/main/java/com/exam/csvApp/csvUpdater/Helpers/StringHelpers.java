package com.exam.csvApp.csvUpdater.Helpers;

import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.InputStream;

public class StringHelpers {
    public static String removeFileExtension(String fileName){
        int length = fileName.length();
        return length>4?fileName.substring(0,length-4):"";
    }

}
