package com.exam.csvApp.csvUpdater;

import com.exam.csvApp.csvUpdater.Helpers.fileReadersAndWriters.CSVRecordReader;
import com.exam.csvApp.csvUpdater.Helpers.fileReadersAndWriters.RecordReader;
import com.exam.csvApp.csvUpdater.entity.RequestRecord;
import com.opencsv.CSVWriter;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CSVControllerTest {
    @Autowired
    private MockMvc mockMvc;

    private static final String testCSVFile = "testCSV.csv";

    String testJSON ="{\"columnHeader\":\"origin\",\"oldValue\":\"Londom\",\"newValue\":\"London\"}";
    static List<String[]> expectedCSVOutput=new ArrayList<>();

    @BeforeAll
    static void setUp() {
        expectedCSVOutput.add(new String[]
                {"filename", "origin", "metadata", "hash"});
        expectedCSVOutput.add(new String[]
                {"file1", "London", "a file about London", "e737a6b0734308a08b8586720b3c299548ff77b846e3c9c89db88b63c7ea69b6"});

        expectedCSVOutput.add(new String[]
                {"file2", "Surrey", "a file about NA", "e737a6b0734308a08b8586720b3c299548ff77b846e3c9c89db88b63c7ea69b6"});

        expectedCSVOutput.add(new String[]
                {"file3", "London", "a file about London", "e737a6b0734308a08b8586720b3c299548ff77b846e3c9c89db88b63c7ea69b6"});

        expectedCSVOutput.add(new String[]
                {"file4", "Penrith", "Londoma fiLondomle about NAPLondom", "e737a6b0734308a08b8586720b3c299548ff77b846e3c9c89db88b63c7ea69b6"});

        expectedCSVOutput.add(new String[]
                {"file5", "LondomPenLondomrithLondom", "a file about Londo", "e737a6b0734308a08b8586720b3c299548ff77b846e3c9c89db88b63c7ea69b6"});

        expectedCSVOutput.add(new String[]
                {"file6", "LondomPenLondomrithLondom", "London a file London about London ", "e737a6b0734308a08b8586720b3c299548ff77b846e3c9c89db88b63c7ea69b6"});

        expectedCSVOutput.add(new String[]
                {"file7", "London London", "London a file London about London ", "e737a6b0734308a08b8586720b3c299548ff77b846e3c9c89db88b63c7ea69b6"});

        expectedCSVOutput.add(new String[]
                {"file8", "London London", "London a file London about London London London", "e737a6b0734308a08b8586720b3c299548ff77b846e3c9c89db88b63c7ea69b6"});


        List<String[]> dataLines = new ArrayList<>();
        dataLines.add(new String[]
                {"filename", "origin", "metadata", "hash"});
        //replace metadata
        dataLines.add(new String[]
                {"file1", "London", "a file about Londom", "e737a6b0734308a08b8586720b3c299548ff77b846e3c9c89db88b63c7ea69b6"});
        //replace nothing
        dataLines.add(new String[]
                {"file2", "Surrey", "a file about NA", "e737a6b0734308a08b8586720b3c299548ff77b846e3c9c89db88b63c7ea69b6"});
        //replace origin and metadata
        dataLines.add(new String[]
                {"file3", "Londom", "a file about Londom", "e737a6b0734308a08b8586720b3c299548ff77b846e3c9c89db88b63c7ea69b6"});
        /*
        replace nothing as Londom is a substring and not a whole word
        This includes 3 cases substring appearing in the beginning,
        substring appearing in the middle of a sentence and
        substring appearing at the end of a sentence
         */
        dataLines.add(new String[]
                {"file4", "Penrith", "Londoma fiLondomle about NAPLondom", "e737a6b0734308a08b8586720b3c299548ff77b846e3c9c89db88b63c7ea69b6"});

        /*
        replace nothing as Londom is a substring and not a whole word
         */
        dataLines.add(new String[]
                {"file5", "LondomPenLondomrithLondom", "a file about Londo", "e737a6b0734308a08b8586720b3c299548ff77b846e3c9c89db88b63c7ea69b6"});

        /*
        replace twice but do not increment counter
         */
        dataLines.add(new String[]
                {"file6", "LondomPenLondomrithLondom", "Londom a file Londom about Londom ", "e737a6b0734308a08b8586720b3c299548ff77b846e3c9c89db88b63c7ea69b6"});

        /*
        replace twice but increment counter once
         */
        dataLines.add(new String[]
                {"file7", "Londom Londom", "London a file London about London ", "e737a6b0734308a08b8586720b3c299548ff77b846e3c9c89db88b63c7ea69b6"});

         /*
        replace twice but increment counter once
         */
        dataLines.add(new String[]
                {"file8", "Londom londom", "London a file London about London londom Londom", "e737a6b0734308a08b8586720b3c299548ff77b846e3c9c89db88b63c7ea69b6"});


        try  {
            CSVWriter writer = new CSVWriter(new FileWriter(testCSVFile));
            writer.writeAll(dataLines);
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @AfterAll
    static void tearDown(){
        File csvFile = new File(testCSVFile);
        assertTrue(csvFile.delete());
    }

    @Test
    void shouldReturn200OKPostRequest() throws Exception{
        String expectedResponse = "{\"rowsUpdated\":3,\"csvId\":\"testCSV\"}";

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/csv/"+testCSVFile)
                .accept(MediaType.APPLICATION_JSON).content(testJSON)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();
        System.out.println(response.getContentAsString());
        assertEquals(HttpStatus.OK.value(),response.getStatus());
        assertEquals(expectedResponse,response.getContentAsString());

        RecordReader csvRecordReader = new CSVRecordReader(testCSVFile);
        assertDoesNotThrow(csvRecordReader::readAllLines);
        List<String[]> allData = csvRecordReader.readAllLines();
        compareCSVLists(allData);

    }

    @Test
    void shouldReturn400BadFileNamePostRequest() throws Exception{
        String expectedResponse = "abc (No such file or directory)";

        RequestRecord exampleRequestRecord = new RequestRecord("origin","Londom","London");
        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/csv/abc")
                .accept(MediaType.APPLICATION_JSON).content(testJSON)
                .contentType(MediaType.APPLICATION_JSON);
        MvcResult result = mockMvc.perform(requestBuilder).andReturn();
        MockHttpServletResponse response = result.getResponse();

        assertEquals(HttpStatus.BAD_REQUEST.value(),response.getStatus());
        assertEquals(expectedResponse,response.getContentAsString());

    }

    private void compareCSVLists(List<String[]> actualCSVOutput){

        assertEquals(actualCSVOutput.size(), expectedCSVOutput.size());
        IntStream.range(0, actualCSVOutput.size())
                        .forEach(i -> assertArrayEquals(actualCSVOutput.get(i), expectedCSVOutput.get(i)));
    }
}
