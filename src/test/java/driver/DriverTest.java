package driver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DriverTest {
    //1. Testing driver creation
    @Test
    @DisplayName("Testing driver creation")
    void testDriverCreation() {
        Driver testDriver = new Driver("32##6785BR", "John", 10, LICENSE_TYPES.LIGHT, "123 | Main St | Metropolis Downtown | Metropolis | Metropolitan Country", "04-07-2002");
        assertEquals("32##6785BR", testDriver.getDriverID());
        assertEquals("John", testDriver.getDriverName());
        assertEquals(10, testDriver.getExperienceYears());
        assertEquals(LICENSE_TYPES.LIGHT, testDriver.getLicenseType());
        assertEquals("123 | Main St | Metropolis Downtown | Metropolis | Metropolitan Country", testDriver.getAddress());
    }

    //2. Testing missing parameters during instantiation
    @ParameterizedTest()
    @DisplayName("Testing missing parameters during instantiation")
    @CsvSource(textBlock = """
            "","",10,LIGHT,123 | Main St | Metropolis Downtown | Metropolis | Metropolitan Country,04-07-2002,
            24#43@54FT,Jane,"",,"",04-08-2003,
            "","","",,"","",
            """)
    void testMissingParameters(String id, String name, String experienceYears, LICENSE_TYPES licenseType, String address, String birthdate) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Driver(id, name, experienceYears, licenseType, address, birthdate);
        });
        assertEquals("Invalid driver details", exception.getMessage());
    }


    //3. Testing driver creation with incorrect ID's including edge cases
    @ParameterizedTest()
    @DisplayName("Testing invalid ID's during instantiation")
    @ValueSource(strings = {"1234567890", "abcdefghij", "a1b2c3d4e5", "3#1@23325W", "----------", "123111231112311"})
    void testInvalidId(String id) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Driver(id, "John", 10, LICENSE_TYPES.LIGHT, "123 | Main St | Metropolis Downtown | Metropolis | Metropolitan Country", "04-07-2002");
        });
        assertEquals("Invalid driver details", exception.getMessage());
    }

    //4. Testing driver creation with incorrect names including edge cases
    @ParameterizedTest()
    @DisplayName("Testing names during instantiation including edge cases")
    @ValueSource(strings = {"", "   ", "Jake1", "John", "123123", "-----"})
    void testInvalidNames(String name) {
        if (!name.equals("John")) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                new Driver("32##6785BR", name, 10, LICENSE_TYPES.HEAVY, "123 | Main St | Metropolis Downtown | Metropolis | Metropolit Country", "04-07-2002");
            });
            assertEquals("Invalid driver details", exception.getMessage());
        } else {
            Driver testDriver = new Driver("32##6785BR", name, 10, LICENSE_TYPES.HEAVY, "123 | Main St | Metropolis Downtown | Metropolis | Metropolit Country", "04-07-2002");
            assertEquals(testDriver.getDriverName(), name);
        }
    }

    //5. Testing driver creation with incorrect experience years including edge cases
    @ParameterizedTest()
    @DisplayName("Testing different experience years")
    @ValueSource(ints = {0, 11, 100, 24, -1})
    void testInvalidExperienceYears(int experienceYears) {

        if (experienceYears < 0) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                new Driver("32##6785BR", "John", experienceYears, LICENSE_TYPES.LIGHT, "123 | Main St | Metropolis Downtown | Metropolis | Metropolitan Country", "04-07-2002");
            });
            assertEquals("Experience cannot be negative", exception.getMessage());
        } else if (experienceYears > 60) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                new Driver("32##6785BR", "John", experienceYears, LICENSE_TYPES.LIGHT, "123 | Main St | Metropolis Downtown | Metropolis | Metropolitan Country", "04-07-2002");
            });
            assertEquals("Experience cannot exceed 60 years", exception.getMessage());
        } else {
            Driver testDriver = new Driver("32##6785BR", "John", experienceYears, LICENSE_TYPES.LIGHT, "123 | Main St | Metropolis Downtown | Metropolis | Metropolitan Country", "04-07-2002");
            assertEquals(testDriver.getExperienceYears(), experienceYears);
        }
    }

    //6. Testing driver license types including edge cases
    @ParameterizedTest()
    @DisplayName("Testing different License Types")
    @ValueSource(strings = {"LIGHT", "HEAVY", "MEDIUM", "PUBLIC_TRANSPORT", "NONE", ""})
    void testInvalidLicenseTypes(String licenses) {
        if (!licenses.equals("LIGHT") && !licenses.equals("HEAVY") && !licenses.equals("MEDIUM") && !licenses.equals("PUBLIC_TRANSPORT")) {
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
                new Driver("32##6785BR", "Mark", 10, LICENSE_TYPES.valueOf(licenses), "123 | Main St | Metropolis Downtown | Metropolis | Metropolitan Country", "04-07-2002");
            });
            assertEquals("No enum constant driver.LICENSE_TYPES." + licenses, exception.getMessage());
        } else {
            Driver testDriver = new Driver("32##6785BR", "John", 10, LICENSE_TYPES.valueOf(licenses), "123 | Main St | Metropolis Downtown | Metropolis | Metropolitan Country", "04-07-2002");
            assertEquals(testDriver.getLicenseType(), LICENSE_TYPES.valueOf(licenses));
        }
    }

    //7. Testing invalid addresses including edge cases
    @ParameterizedTest()
    @DisplayName("Testing valid && invalid Addresses including edge cases")
    @ValueSource(strings = {"", "                 ", "123123123123", "123 Main St", "123 | Main", "||||||||", "@@@@@@@@|#######", "123 | Main St | Metropolis Downtown | Metropolis | Metropolitan Country"})
    void testInvalidAddresses(String address) {
        if (!address.equals("123 | Main St | Metropolis Downtown | Metropolis | Metropolitan Country")) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
           new Driver("32##6785BR", "John", 10, LICENSE_TYPES.LIGHT, address, "04-07-2002");
        });
        assertEquals("Invalid driver details", exception.getMessage());
        } else {
            Driver testDriver = new Driver("32##6785BR", "John", 10, LICENSE_TYPES.LIGHT, address, "04-07-2002");
            assertEquals(testDriver.getAddress(), address);
        }
    }

    //8. Testing valid & invalid birth dates
    @ParameterizedTest()
    @DisplayName("Testing valid and invalid Birth dates including edge cases")
    @ValueSource(strings = {"04-04-2004", "000000", "abcd12", "123456", "12/31/2024", "2024/12/13", "abcdefgh", "@@-##-()()", ""})
    void testInvalidBirthdates(String birthdate) {
        if (!birthdate.equals("04-04-2004")) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            new Driver("32##6785BR", "John", 10, LICENSE_TYPES.LIGHT, "123 | Main St | Metropolis Downtown | Metropolis | Metropolitan Country", birthdate);
        });
        assertEquals("Invalid driver details", exception.getMessage());
        } else {
            Driver testDriver = new Driver("32##6785BR", "John", 10, LICENSE_TYPES.LIGHT, "123 | Main St | Metropolis Downtown | Metropolis | Metropolitan Country", birthdate);
            assertEquals(testDriver.getBirthdate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), birthdate);
        }
    }
    //9. Tests for verifying details
    @Test
    @DisplayName("normal detail verification")
    void verifyNormalDetails() {
        Driver testDriver = new Driver("32##6785BR", "John", 10, LICENSE_TYPES.LIGHT, "123 | Main St | Metropolis Downtown | Metropolis | Metropolitan Country", "04-07-2002");
        final boolean result = testDriver.verifyLogin(testDriver.getDriverID(), testDriver.getBirthdate().toString());

        assert result;
    }
    //10. Testing for invalid detail verification
    @ParameterizedTest
    @DisplayName("invalid detail verification")
    @CsvSource(textBlock =
            """
            " ", " "
            "1234567890", "04-07-2002"
            "abcdefgh", "1231123"
            
            """
    )
    void verifyInvalidDetails(String id, String birthdate) {
        Driver testDriver = new Driver("32##6785BR", "John", 10, LICENSE_TYPES.LIGHT, "123 | Main St | Metropolis Downtown | Metropolis | Metropolitan Country", "04-07-2002");
        final Boolean result = testDriver.verifyLogin("1234567890", "04-07-2002");
        assert !result;
    }
    //11. Testing the address setter
    @Test
    @DisplayName("Testing the address setter and getter")
    void verifySetAddress() {
        Driver testDriver = new Driver("32##6785BR", "John", 10, LICENSE_TYPES.LIGHT, "123 | Main St | Metropolis Downtown | Metropolis | Metropolitan Country", "04-07-2002");
        Driver replacementDriver = new Driver("44@#6905TT", "James", 5, LICENSE_TYPES.HEAVY, "244 | High St | Metropolis Suburb | Metropolian | Metropolian Country", "12-10-2004");
        testDriver.setAddress(replacementDriver.getAddress());
        assertEquals("244 | High St | Metropolis Suburb | Metropolian | Metropolian Country", testDriver.getAddress());
    }

    //12. Testing the birthdate setter
    @Test
    @DisplayName("Testing the birthdate setter and getter")
    void verifySetBirthdate() {
        Driver testDriver = new Driver("32##6785BR", "John", 10, LICENSE_TYPES.LIGHT, "123 | Main St | Metropolis Downtown | Metropolis | Metropolitan Country", "04-07-2002");
        Driver replacementDriver = new Driver("44@#6905TT", "James", 5, LICENSE_TYPES.HEAVY, "244 | High St | Metropolis Suburb | Metropolian | Metropolian Country", "12-10-2004");
        testDriver.setBirthdate(LocalDate.parse(replacementDriver.getBirthdate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")), DateTimeFormatter.ofPattern("dd-MM-yyyy")));
        assertEquals("12-10-2004", testDriver.getBirthdate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
    }

    //13. Testing the experience years setter
    @Test
    @DisplayName("Testing the experience years setter")
    void verifySetExperienceYears() {
        Driver testDriver = new Driver("32##6785BR", "John", 10, LICENSE_TYPES.LIGHT, "123 | Main St | Metropolis Downtown | Metropolis | Metropolitan Country", "04-07-2002");
        Driver replacementDriver = new Driver("44@#6905TT", "James", 5, LICENSE_TYPES.HEAVY, "244 | High St | Metropolis Suburb | Metropolian | Metropolian Country", "12-10-2004");
        testDriver.setExperienceYears(replacementDriver.getExperienceYears());
        assertEquals(5, testDriver.getExperienceYears());
    }

    //14. Testing the license type setter
    @Test
    @DisplayName("Testing the license type setter")
    void verifySetLicenseType() {
        Driver testDriver = new Driver("32##6785BR", "John", 10, LICENSE_TYPES.LIGHT, "123 | Main St | Metropolis Downtown | Metropolis | Metropolitan Country", "04-07-2002");
        Driver replacementDriver = new Driver("44@#6905TT", "James", 5, LICENSE_TYPES.HEAVY, "244 | High St | Metropolis Suburb | Metropolian | Metropolian Country", "12-10-2004");
        testDriver.setLicenseType(replacementDriver.getLicenseType());
        assertEquals(LICENSE_TYPES.HEAVY, testDriver.getLicenseType());
    }

    //15. Testing the incorrect experience years
    @Test
    @DisplayName("Testing the incorrect experience years")
    void verifySetIncorrectExperienceYears() {
        Driver testDriver = new Driver("32##6785BR", "John", 70, LICENSE_TYPES.LIGHT, "123 | Main St | Metropolis Downtown | Metropolis | Metropolitan Country", "04-07-2002");
        assertThrows(IllegalArgumentException.class, () -> {
            testDriver.setExperienceYears(70);
        });
    }
}
