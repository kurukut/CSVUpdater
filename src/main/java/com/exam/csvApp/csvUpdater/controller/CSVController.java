package com.exam.csvApp.csvUpdater.controller;

import com.exam.csvApp.csvUpdater.Helpers.StringHelpers;
import com.exam.csvApp.csvUpdater.Helpers.fileReadersAndWriters.CSVRecordReader;
import com.exam.csvApp.csvUpdater.Helpers.fileReadersAndWriters.CSVResultGenerator;
import com.exam.csvApp.csvUpdater.Helpers.fileReadersAndWriters.RecordReader;
import com.exam.csvApp.csvUpdater.Helpers.fileReadersAndWriters.ResultGenerator;
import com.exam.csvApp.csvUpdater.entity.RequestRecord;
import com.exam.csvApp.csvUpdater.entity.ResponseRecord;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

@RestController
public class CSVController {

    @PostMapping(path = "/csv/{id}", consumes = "application/json")
    public ResponseEntity<Object> processCSV(@PathVariable("id") String fileName, @RequestBody RequestRecord requestRecord){
        try {
            int counter = 0;

            //read from CSV file
            RecordReader csvRecordReader = new CSVRecordReader(fileName);
            List<String[]> allData = csvRecordReader.readAllLines();
            if (allData!=null && !allData.isEmpty() ) {
                String column = requestRecord.getColumnHeader();
                String oldValue = requestRecord.getOldValue();
                String newValue = requestRecord.getNewValue();
                String regex = "\\b"+oldValue+"\\b";
                Pattern pattern = Pattern.compile(regex,Pattern.CASE_INSENSITIVE);

                String[] headers = allData.get(0);

                //find out the column that needs to be updated and counted
                int index = IntStream.range(0, headers.length).filter(i -> column.equals(headers[i].trim())).findAny().orElse(-1);

                for (int i = 1; i < allData.size(); i++) {
                    for (int j=0;j<allData.get(i).length;j++){
                        String current = allData.get(i)[j];
                        Matcher matcher = pattern.matcher(current);
                        //find and replace old value
                        if(matcher.find()){
                            if (j==index){
                                //update the number of rows
                                counter++;
                            }
                            allData.get(i)[j]=allData.get(i)[j].replaceAll("\\b+"+"(?i)"+oldValue+"\\b",newValue);

                        }
                    }
                }
            }
            //update CSV
            ResultGenerator csvResultGenerator = new CSVResultGenerator(fileName);
            csvResultGenerator.writeAllLines(allData);
            return new ResponseEntity<Object>(new ResponseRecord(counter, StringHelpers.removeFileExtension(fileName)), HttpStatus.OK);
        }
        catch (Exception e) {
            return new ResponseEntity<Object>(e.getMessage(),HttpStatus.BAD_REQUEST);
        }
    }
}
