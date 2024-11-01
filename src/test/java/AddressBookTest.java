import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.openqa.selenium.*;

import java.time.*;
import java.util.List;

public class AddressBookTest {
    WebDriver driver;
    WebDriverWait wait;

    @BeforeEach
    void setUp(){
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        driver.get("http://localhost/");
    }
    @AfterEach
    void cleanUp(){
        driver.close();
    }

    @Test @Disabled
    void testAddressBookTitle(){
        String actualTitle = driver.getTitle();
        assertEquals("Address Book", actualTitle);
    }
    @Test @Disabled
    void testListEntriesLink(){
        WebElement listLink = driver.findElement(By.xpath("/html/body/doctype/ul/li[1]/a"));
        listLink.click();

        String actualTitle = driver.getTitle();
        assertEquals("Address Book", actualTitle);

        String actualHeader = driver.findElement(By.xpath("/html/body/div[1]/h1")).getText();
        assertEquals("Address Book - All Entries", actualHeader);
    }
    @Test @Disabled
    void testAddEntryLink(){
        WebElement addLink = driver.findElement(By.xpath("/html/body/doctype/ul/li[2]/a"));
        addLink.click();
        
        String actualTitle = driver.getTitle();
        assertEquals("Address Book", actualTitle);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/ValidData.csv", numLinesToSkip = 1)
    void testValidAddAddresses(String testID, String EntryType, String FirstName, String LastName, String BusinessName, String Address1, String Address2, String Address3, String City, String Province, String Country, String PostalCode, String Email1, String Email2, String Email3, String Phone1Type, String Phone1Num, String Phone2Type, String Phone2Num, String Phone3Type, String Phone3Num, String Website1, String Website2, String Website3) throws NoSuchElementException {
        // Go to the new entry page
        driver.findElement(By.linkText("Add New Entry")).click();

        // Ensure we are on the correct page
        assertEquals("http://localhost/newEntry.php", driver.getCurrentUrl());
        
        // Create array of all text fields
        String[] textData = {FirstName, LastName, BusinessName, Address1, Address2, Address3, City, Province, Country, PostalCode, Email1, Email2, Email3, Phone1Num, Phone2Num, Phone3Num, Website1, Website2, Website3};
        // Create array of all dropdown fields
        String[] dropdownData = {EntryType, Phone1Type, Phone2Type, Phone3Type};
        
        // Call the helper method which will populate the form with the test data
        enterTestDataIntoAddressForm(testID, textData, dropdownData);
        
        // Ensure the entry was added properly by checking if the success element is present.
        // TODO: maybe we should verify by checking the entry list
        assertDoesNotThrow(() -> {
            WebElement successElement = driver.findElement(By.xpath("/html/body/form/div/h2"));
            assertEquals("The new address book entry was added successfully", successElement.getText());
        });
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/ValidData.csv", numLinesToSkip = 1)
    void testValidEditAddresses(String testID, String EntryType, String FirstName, String LastName, String BusinessName, String Address1, String Address2, String Address3, String City, String Province, String Country, String PostalCode, String Email1, String Email2, String Email3, String Phone1Type, String Phone1Num, String Phone2Type, String Phone2Num, String Phone3Type, String Phone3Num, String Website1, String Website2, String Website3) throws NoSuchElementException {
        // Go to the new entry page
        driver.findElement(By.linkText("List All Entries")).click();
        driver.findElement(By.xpath("/html/body/table/tbody/tr[2]/td[4]/form[2]/input[3]")).click(); // TODO: this should be a relevent edit button
        
        // Ensure we are on the correct page
        assertEquals("http://localhost/editEntry.php", driver.getCurrentUrl());
        
        // Create array of all text fields
        String[] textData = {FirstName, LastName, BusinessName, Address1, Address2, Address3, City, Province, Country, PostalCode, Email1, Email2, Email3, Phone1Num, Phone2Num, Phone3Num, Website1, Website2, Website3};
        // Create array of all dropdown fields
        String[] dropdownData = {EntryType, Phone1Type, Phone2Type, Phone3Type};
        
        // Call the helper method which will populate the form with the test data
        enterTestDataIntoAddressForm(testID, textData, dropdownData);

        // Ensure the entry was edited properly by checking if the success element is present.
        // TODO: maybe we should verify by checking the entry list
        assertDoesNotThrow(() -> {
            WebElement successElement = driver.findElement(By.xpath("/html/body/form/div/h2"));
            assertEquals("The address book entry was updated successfully", successElement.getText());
        });
    }


    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ HELPER METHODS ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    /*
    ----------------------------------------------------------
    Name: enterTestDataIntoAddressForm
    ----------------------------------------------------------
    Description: Takes in test data and inputs the data into the addressbook form.
    Use: enterTestDataIntoAddressForm(testID, textData, dropdownData);
    ----------------------------------------------------------
    Parameters:
        String testID - The test ID for the current test. Usually in the form: TC-XX
        String[] textData - The data for all the text inputs for the form. This will be retrieved from a CSV file
        String[] dropdownData - The data for all the dropdown forms. This will be retrieved from a CSV file
    ----------------------------------------------------------
     */
    void enterTestDataIntoAddressForm(String testID, String[] textData, String[] dropdownData){
        // Find all the input elements on the page
        List<WebElement> inputElements = driver.findElements(By.tagName("input"));
        // Find all the select elements on the page
        List<WebElement> dropdownElements = driver.findElements(By.tagName("select"));

        for (int i = 0; i < textData.length; i++) {
            // Clear any data that is currently there
            inputElements.get(i).clear();
            
            // Check if the textData has a value to send (null means no data) 
            if(textData[i] != null){
                inputElements.get(i).sendKeys(textData[i]);
            }
        }
        for (int i = 0; i < dropdownElements.size(); i++) { // Select the proper dropdowns
            Select dropdown = new Select(dropdownElements.get(i));  
            try{
                // Check if the dropdownData has a value to send (null means no data) 
                if(dropdownData[i] != null){
                    // Select the dropdown element based on the value of the text data
                    dropdown.selectByValue(dropdownData[i]);
                }
            } catch (NoSuchElementException ex){ // If the dropdown selection could not be found this is likely an error in the test data.
                throw new NoSuchElementException(testID + " LIKELY DROPDOWN TEST DATA ERROR: \"" + dropdownData[i] + "\" | ERROR MESSAGE: " + ex.getMessage());
            }
        }
        // Submit the entry
        driver.findElement(By.id("submit_button")).submit();
    }
}



