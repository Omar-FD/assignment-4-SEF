package driver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.CsvSource;

public class DriverTest {
    //Tests for verifying details
    @Test
    @DisplayName("normal detail verification")
    void verifyNormalDetails() {
        Driver testDriver = new Driver("32##6785BR", "John", 10, Driver.LICENSE_TYPES.LIGHT, "123 | Main St | Metropolis Downtown | Metropolis | Metropolitan Country", "04-07-2002");
        final Boolean result = testDriver.verifyLogin(testDriver.getDriverID(), testDriver.getBirthdate().toString());

        assert result;
    }

    //Test Edge Cases
}
