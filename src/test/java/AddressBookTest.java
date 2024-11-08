import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.openqa.selenium.*;

import java.time.*;
import java.util.List;

@ExtendWith(FailureLogger.class)
@TestMethodOrder(OrderAnnotation.class)
public class AddressBookTest {

    static final String URL = "http://localhost/addressbook";// Address Book URL

    WebDriver driver;
    WebDriverWait wait;

    @BeforeEach
    void setUp() {
        driver = new ChromeDriver();
        driver.manage().deleteAllCookies();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(2));
        driver.get(URL + "/index.php");
    }

    @AfterEach
    void cleanUp() {
        // See FailureLogger.java to see AfterEach logic
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ ADD ADDRESS
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Order(1)
    @ParameterizedTest(name = "testValidAddAddresses [{0}]")
    @CsvFileSource(resources = "/ValidData.csv", numLinesToSkip = 1)
    void testValidAddAddresses(String testID, String EntryType, String FirstName, String LastName, String BusinessName,
            String Address1, String Address2, String Address3, String City, String Province, String Country,
            String PostalCode, String Email1, String Email2, String Email3, String Phone1Type, String Phone1Num,
            String Phone2Type, String Phone2Num, String Phone3Type, String Phone3Num, String Website1, String Website2,
            String Website3) throws NoSuchElementException {

        // Test that we are on the landing page
        String expectedUrl = URL + "/index.php";
        String actualUrl = driver.getCurrentUrl();
        assertEquals(expectedUrl, actualUrl);

        // Go to the new entry page
        driver.findElement(By.linkText("Add New Entry")).click();

        // Ensure we are on the correct page
        assertEquals(URL + "/newEntry.php", driver.getCurrentUrl());

        // Create array of all text fields
        String[] textData = { FirstName, LastName, BusinessName, Address1, Address2, Address3, City, Province, Country,
                PostalCode, Email1, Email2, Email3, Phone1Num, Phone2Num, Phone3Num, Website1, Website2, Website3 };
        // Create array of all dropdown fields
        String[] dropdownData = { EntryType, Phone1Type, Phone2Type, Phone3Type };

        // Call the helper method which will populate the form with the test data
        enterTestDataIntoAddressForm(testID, textData, dropdownData);

        // Ensure the entry was added properly by checking if the success element is
        // present and holds the correct message.
        assertDoesNotThrow(() -> {
            WebElement successElement = driver.findElement(By.xpath("/html/body/form/div/h2"));
            assertEquals("The new address book entry was added successfully", successElement.getText());
        });

    }

    @Order(2)
    @ParameterizedTest(name = "testInvalidAddAddresses [{0}]")
    @CsvFileSource(resources = "/InvalidData.csv", numLinesToSkip = 1)
    void testInvalidAddAddresses(String testID, String EntryType, String FirstName, String LastName,
            String BusinessName, String Address1, String Address2, String Address3, String City, String Province,
            String Country, String PostalCode, String Email1, String Email2, String Email3, String Phone1Type,
            String Phone1Num, String Phone2Type, String Phone2Num, String Phone3Type, String Phone3Num, String Website1,
            String Website2, String Website3) throws NoSuchElementException {

        // Test that we are on the landing page
        String expectedUrl = URL + "/index.php";
        String actualUrl = driver.getCurrentUrl();
        assertEquals(expectedUrl, actualUrl);

        // Go to the new entry page
        driver.findElement(By.linkText("Add New Entry")).click();

        // Ensure we are on the correct page
        assertEquals(URL + "/newEntry.php", driver.getCurrentUrl());

        // Create array of all text fields
        String[] textData = { FirstName, LastName, BusinessName, Address1, Address2, Address3, City, Province, Country,
                PostalCode, Email1, Email2, Email3, Phone1Num, Phone2Num, Phone3Num, Website1, Website2, Website3 };
        // Create array of all dropdown fields
        String[] dropdownData = { EntryType, Phone1Type, Phone2Type, Phone3Type };

        // Call the helper method which will populate the form with the test data
        enterTestDataIntoAddressForm(testID, textData, dropdownData);

        // Ensure the entry was not added by checking if the failure element is present.
        assertDoesNotThrow(() -> {
            driver.findElement(By.xpath("/html/body/p"));
        });

    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ LIST ADDRESSES
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Order(3)
    @Test
    void testListAllEntries() {

        String expectedUrl = URL + "/index.php";
        String actualUrl = driver.getCurrentUrl();
        assertEquals(expectedUrl, actualUrl);// Test that we are still on the correct URL

        // Go to the list all entries page
        driver.findElement(By.linkText("List All Entries")).click();
        String expectedListUrl = URL + "/allList.php";
        String actualListUrl = driver.getCurrentUrl();
        assertEquals(expectedListUrl, actualListUrl);// Test that we on the list all entries page
    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ EDIT ADDRESS
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Order(4)
    @ParameterizedTest(name = "testValidEditAddresses [{0}]")
    @CsvFileSource(resources = "/ValidData.csv", numLinesToSkip = 1)
    void testValidEditAddresses(String testID, String EntryType, String FirstName, String LastName, String BusinessName,
            String Address1, String Address2, String Address3, String City, String Province, String Country,
            String PostalCode, String Email1, String Email2, String Email3, String Phone1Type, String Phone1Num,
            String Phone2Type, String Phone2Num, String Phone3Type, String Phone3Num, String Website1, String Website2,
            String Website3) throws NoSuchElementException {

        // Test that we are on the landing page
        String expectedUrl = URL + "/index.php";
        String actualUrl = driver.getCurrentUrl();
        assertEquals(expectedUrl, actualUrl);

        // Go to the new entry page
        driver.findElement(By.linkText("List All Entries")).click();

        // Find the first edit entry button and click it
        driver.findElement(By.xpath("/html/body/table/tbody/tr[2]/td[4]/form[2]/input[3]")).click();

        // Ensure we are on the edit entry page
        assertEquals(URL + "/editEntry.php", driver.getCurrentUrl());

        // Create array of all text fields
        String[] textData = { FirstName, LastName, BusinessName, Address1, Address2, Address3, City, Province, Country,
                PostalCode, Email1, Email2, Email3, Phone1Num, Phone2Num, Phone3Num, Website1, Website2, Website3 };
        // Create array of all dropdown fields
        String[] dropdownData = { EntryType, Phone1Type, Phone2Type, Phone3Type };

        // Call the helper method which will populate the form with the test data
        enterTestDataIntoAddressForm(testID, textData, dropdownData);

        // Ensure the entry was edited properly by checking if the success element is
        // present and holds the correct message.
        assertDoesNotThrow(() -> {
            WebElement successElement = driver.findElement(By.xpath("/html/body/form/div/h2"));
            assertEquals("The address book entry was updated successfully", successElement.getText());
        });

    }

    @Order(5)
    @ParameterizedTest(name = "testInvalidEditAddresses [{0}]")
    @CsvFileSource(resources = "/InvalidData.csv", numLinesToSkip = 1)
    void testInvalidEditAddresses(String testID, String EntryType, String FirstName, String LastName,
            String BusinessName, String Address1, String Address2, String Address3, String City, String Province,
            String Country, String PostalCode, String Email1, String Email2, String Email3, String Phone1Type,
            String Phone1Num, String Phone2Type, String Phone2Num, String Phone3Type, String Phone3Num, String Website1,
            String Website2, String Website3) throws NoSuchElementException {
        // Test that we are on the landing page
        String expectedUrl = URL + "/index.php";
        String actualUrl = driver.getCurrentUrl();
        assertEquals(expectedUrl, actualUrl);

        // Go to the new entry page
        driver.findElement(By.linkText("List All Entries")).click();

        // Find the first edit entry button and click it
        driver.findElement(By.xpath("/html/body/table/tbody/tr[2]/td[4]/form[2]/input[3]")).click();

        // Ensure we are on the edit entry page
        assertEquals(URL + "/editEntry.php", driver.getCurrentUrl());

        // Create array of all text fields
        String[] textData = { FirstName, LastName, BusinessName, Address1, Address2, Address3, City, Province, Country,
                PostalCode, Email1, Email2, Email3, Phone1Num, Phone2Num, Phone3Num, Website1, Website2, Website3 };
        // Create array of all dropdown fields
        String[] dropdownData = { EntryType, Phone1Type, Phone2Type, Phone3Type };

        // Call the helper method which will populate the form with the test data
        enterTestDataIntoAddressForm(testID, textData, dropdownData);

        // Ensure the edit did not go through by checking if the failure element is
        // present.
        assertDoesNotThrow(() -> {
            driver.findElement(By.xpath("/html/body/p"));
        });

    }

    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ VIEW ADDRESS
    // ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    @Order(6)
    @Test
    void testViewAddresses() {
        driver.findElement(By.linkText("List All Entries")).click();
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(1000)); // Set a 10-second timeout

        int index = 0;
        while (true) {
            // Find all "View Details" buttons on the page
            List<WebElement> viewDetails = driver.findElements(By.xpath("//input[@value='View Details']"));

            // Exit loop if no "View Details" buttons are found
            if (viewDetails.isEmpty() || index >= viewDetails.size()) {
                break;
            }

            // Click the next "View Details" button based on the index
            viewDetails.get(index).click();

            // Wait for the "Return" button to be clickable
            wait.until(ExpectedConditions.elementToBeClickable(By.xpath("/html/body/div[2]/a")));

            // Click the "Return" button to go back to the list
            driver.findElement(By.xpath("/html/body/div[2]/a")).click();

            // Wait for the "View Details" buttons to be present again
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//input[@value='View Details']")));

            // Increment the index to click the next button in the next iteration
            index++;
        }
    }

    // ==================================== HELPER METHODS
    // ====================================

    /**
     * Takes in test data and inputs the data into the addressbook form.
     * 
     * @param testID       - The test ID for the current test. Usually in the form:
     *                     TC-XX
     * @param textData     - The data for all the text inputs for the form. This
     *                     will be retrieved from a CSV file
     * @param dropdownData - The data for all the dropdown forms. This will be
     *                     retrieved from a CSV file
     * @throws NoSuchElementException - If the driver cannot find an element this
     *                                will be thrown, likely cause is an issue in
     *                                the test data.
     */
    void enterTestDataIntoAddressForm(String testID, String[] textData, String[] dropdownData)
            throws NoSuchElementException {
        // Find all the input elements on the page
        List<WebElement> inputElements = driver.findElements(By.tagName("input"));
        // Find all the select elements on the page
        List<WebElement> dropdownElements = driver.findElements(By.tagName("select"));

        for (int i = 0; i < textData.length; i++) {
            // Clear any data that is currently there
            inputElements.get(i).clear();

            // Check if the textData has a value to send (null means no data)
            if (textData[i] != null) {
                inputElements.get(i).sendKeys(textData[i]);
            }
        }
        for (int i = 0; i < dropdownElements.size(); i++) { // Select the proper dropdowns
            Select dropdown = new Select(dropdownElements.get(i));
            try {
                // Check if the dropdownData has a value to send (null means no data)
                if (dropdownData[i] != null) {
                    // Select the dropdown element based on the value of the text data
                    dropdown.selectByValue(dropdownData[i]);
                }
            } catch (NoSuchElementException ex) { // If the dropdown selection could not be found this is likely an
                                                  // error in the test data.
                throw new NoSuchElementException(testID + " LIKELY DROPDOWN TEST DATA ERROR: \"" + dropdownData[i]
                        + "\" | ERROR MESSAGE: " + ex.getMessage());
            }
        }
        // Submit the entry
        driver.findElement(By.id("submit_button")).submit();
    }

}
