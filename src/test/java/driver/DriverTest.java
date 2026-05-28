package driver;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.provider.CsvSource;

public class DriverTest {
    //Tests for verifying details
    @Test
    @DisplayName("normal detail verification")
    void verifyNormalDetails() {
        Driver driver = new Driver("13579", "John", 2, Driver.LICENSE_TYPES.LIGHT, "123 Main St", null);
        Boolean result = driver.verifyDetails("13579", "12-01-1990");
        assert result;
    }

    //Test Edge Cases
    @Test
    @CsvSource(textBlock = """

""")
}
