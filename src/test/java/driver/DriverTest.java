package driver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class DriverTest {
    //1. Tests for verifying details
    @Test
    @DisplayName("normal detail verification")
    void verifyNormalDetails() {
        Driver testDriver = new Driver("32##6785BR", "John", 10, LICENSE_TYPES.LIGHT, "123 | Main St | Metropolis Downtown | Metropolis | Metropolitan Country", "04-07-2002");
        final Boolean result = testDriver.verifyLogin(testDriver.getDriverID(), testDriver.getBirthdate().toString());

        assert result;
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
            Driver testDriver = new Driver(id, name, experienceYears, licenseType, address, birthdate);
        });
        assertEquals("Invalid driver details", exception.getMessage());
    }


    //3. Testing driver creation with incorrect ID's including edge cases
    @ParameterizedTest()
    @DisplayName("Testing invalid ID's during instantiation")
    @ValueSource(strings = {"1234567890", "abcdefghij", "a1b2c3d4e5", "3#1@23325W", "----------", ""})
    void testInvalidId(String id) {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            Driver testDriver = new Driver(id, "John", 10, LICENSE_TYPES.LIGHT, "123 | Main St | Metropolis Downtown | Metropolis | Metropolitan Country", "04-07-2002");
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
                Driver testDriver = new Driver("32##6785BR", name, 10, LICENSE_TYPES.HEAVY, "123 | Main St | Metropolis Downtown | Metropolis | Metropolit Country", "04-07-2002");
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
            Driver testDriver = new Driver("32##6785BR", "John", experienceYears, LICENSE_TYPES.LIGHT, "123 | Main St | Metropolis Downtown | Metropolis | Metropolitan Country", "04-07-2002");
        });
        assertEquals("Invalid driver details", exception.getMessage());
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
                Driver testDriver = new Driver("32##6785BR", "John", 10, LICENSE_TYPES.valueOf(licenses), "123 | Main St | Metropolis Downtown | Metropolis | Metropolitan Country", "04-07-2002");
            });
            assertEquals("No enum constant driver.LICENSE_TYPES." + licenses, exception.getMessage());
        } else {
            Driver testDriver = new Driver("32##6785BR", "John", 10, LICENSE_TYPES.valueOf(licenses), "123 | Main St | Metropolis Downtown | Metropolis | Metropolitan Country", "04-07-2002");
            assertEquals(testDriver.getLicenseType(), LICENSE_TYPES.valueOf(licenses));
        }
    }
}
