package driver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
//import org.junit.jupiter.params.provider.CsvSource;

public class DriverTest {
    //Tests for verifying details
    @Test
    @DisplayName("normal detail verification")
    void verifyNormalDetails() {
        Driver testDriver = new Driver("32##6785BR", "John", 10, LICENSE_TYPES.LIGHT, "123 | Main St | Metropolis Downtown | Metropolis | Metropolitan Country", "04-07-2002");
        final Boolean result = testDriver.verifyLogin(testDriver.getDriverID(), testDriver.getBirthdate().toString());

        assert result;
    }

    //Testing missing parameters during instantiation
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


    //Testing driver creation with incorrect ID's including edge cases
    @ParameterizedTest()
    @DisplayName("Testing invalid ID's during instantiation")
    @ValueSource(strings = {"1234567890", "abcdefghij", "a1b2c3d4e5", "3#1@23325W", "----------", ""})
    void testInvalidId(String id) {
       IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
        Driver testDriver = new Driver(id, "John", 10, LICENSE_TYPES.LIGHT, "123 | Main St | Metropolis Downtown | Metropolis | Metropolitan Country", "04-07-2002");
        });
        assertEquals("Invalid driver details", exception.getMessage());
    }

}
