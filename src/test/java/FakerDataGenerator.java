import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.opencsv.CSVWriter;
import com.github.javafaker.Faker;

public class FakerDataGenerator {

    static final String FAKER_FILE_PATH = "src\\test\\resources\\FakerData.csv";
    public static void main(String[] args) {
        // Generate fake data
        generateFakeCSVData(25);
    }

    /**
     * Generates fake data using java-faker and writes it to a CSV file.
     * @param numRows The number of rows that the method will generate.
      */
    public static void generateFakeCSVData(int numRows){
        
        List<String[]> csvData = new ArrayList<>();

        Faker faker = new Faker();
        
        // Start with the header row
        String[] currentRow = {"Test ID","EntryType","FirstName","LastName","BusinessName","Address1","Address2","Address3","City","Province","Country","PostalCode","Email1","Email2","Email3","Phone1Type","Phone1Num","Phone2Type","Phone2Num","Phone3Type","Phone3Num","Website1","Website2","Website3"}; 
        csvData.add(currentRow);
                
        // Add and Generate given number of rows to the CSV data List. 
        for (int i = 0; i < numRows; i++) {
            currentRow = new String[24];
            currentRow[0] = "TC-"+(i+1); // Test ID
            currentRow[1] = "Family"; // Entry Type (Family)
            currentRow[2] = faker.name().firstName(); // First Name
            currentRow[3] = faker.name().lastName(); // Last Name
            currentRow[4] = faker.company().name(); // Business Name
            currentRow[5] = faker.address().streetAddress(); // Address1
            currentRow[6] = faker.address().streetAddress(); // Address2
            currentRow[7] = faker.address().streetAddress(); // Address3
            currentRow[8] = faker.address().city(); // City
            currentRow[9] = faker.address().state(); // Province
            currentRow[10] = faker.address().country(); // Country
            currentRow[11] = faker.address().zipCode(); // Postal Code
            currentRow[12] = faker.internet().emailAddress(); // Email1
            currentRow[13] = faker.internet().emailAddress(); // Email2
            currentRow[14] = faker.internet().emailAddress(); // Email3
            currentRow[15] = "Mobile"; // PhoneType1
            currentRow[16] = faker.phoneNumber().cellPhone(); // PhoneNumber1
            currentRow[17] = "Mobile"; // PhoneType2
            currentRow[18] = faker.phoneNumber().cellPhone(); // PhoneNumber2
            currentRow[19] = "Mobile"; // PhoneType3
            currentRow[20] = faker.phoneNumber().cellPhone(); // PhoneNumber3
            currentRow[21] = faker.internet().domainName(); // Website1
            currentRow[22] = faker.internet().domainName(); // Website2
            currentRow[23] = faker.internet().domainName(); // Website3
            csvData.add(currentRow);
        }
        
        writeToCSV(csvData);
    }

    /**
     * Writes the given data to a CSV file.
     * @param csvData A list of rows / String arrays filled with the data to be writen to csv.
     */
    private static void writeToCSV(List<String[]> csvData){
        // Create Resources directory if it does not exist
        File destFile = new File(FAKER_FILE_PATH);
        if(!destFile.getParentFile().exists()){
            destFile.getParentFile().mkdirs();
        }
        try (CSVWriter writer = new CSVWriter(new FileWriter(FAKER_FILE_PATH))){
            writer.writeAll(csvData);
        } catch (IOException e){
            e.printStackTrace();
        }
    }
}
