import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVWriter;
import com.github.javafaker.Faker;

public class FakerData {
    public static void main(String[] args) {

        // Generate fake data
        List<String[]> fakeData = generateFakeCSVData(10);

        // Write fake data to CSV
        writeToCSV("src\\test\\resources\\FakerCSV.csv", fakeData);

    }

    /**
     * Generates fake data using java-faker to write to a CSV file.
     * @param numRows The number of rows that the method will generate.
     * @return A list of rows / String arrays populated with fake data. Includes the header.
      */
    private static List<String[]> generateFakeCSVData(int numRows){
        
        List<String[]> csvData = new ArrayList<>();
        
        // Start with the header row
        String[] currentRow = {"Test ID","EntryType","FirstName","LastName","BusinessName","Address1","Address2","Address3","City","Province","Country","PostalCode","Email1","Email2","Email3","Phone1Type","Phone1Num","Phone2Type","Phone2Num","Phone3Type","Phone3Num","Website1","Website2","Website3"}; 
        csvData.add(currentRow);
        
        // Add and Generate given number of rows to the CSV data List. 
        for (int i = 0; i < numRows; i++) {
            // TODO: Generate relevant fake data using java-faker library here (Currently just populates it with the header row 'numRow' times)
            
            csvData.add(currentRow);
        }
        
        return csvData;
    }

    /**
     * Writes the given data to a CSV file in the given file path.
     * @param filePath Path to the CSV file, e.g. "src\\test\\resources\\FakerCSV.csv"
     * @param csvData A list of rows / String arrays filled with the data to be writen to csv.
     */
    private static void writeToCSV(String filePath, List<String[]> csvData){
        try (CSVWriter writer = new CSVWriter(new FileWriter(filePath))){
            writer.writeAll(csvData);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
