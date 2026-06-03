package driver;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.nio.file.*;

/**
 * Integration tests for Driver-related operations
 *   IT1: Valid drivers are stored correctly
 *   IT2: Invalid drivers are rejected
 *   IT3: Updates are persisted correctly
 *   IT4: Record counts are updated correctly
 */
@TestMethodOrder(MethodOrderer.DisplayName.class)
public class DriverIntegrationTest {

    /** Temporary TXT file used for each individual test. */
    private Path tempFile;

    /** Repository under test — points at the temp file. */
    private DriverRepository repo;

    // JUnit lifecycle — fresh file before each test, cleanup after
    @BeforeEach
    void setUp() throws IOException {
        tempFile = Files.createTempFile("driver_integration_", ".txt");
        repo = new DriverRepository(tempFile.toString());
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(tempFile);
    }

    // Shared helper
    private Driver validDriver() {
        return new Driver(
            "24@#1609AB",
            "John",
            5,
            LICENSE_TYPES.HEAVY,
            "12|Main Street|Melbourne|VIC|Australia",
            "18-02-1998"
        );
    }

    /** A second valid driver with a different ID. */
    private Driver secondValidDriver() {
        return new Driver(
            "47#@0204CD",
            "Bob",
            3,
            LICENSE_TYPES.LIGHT,
            "5|Park Road|Sydney|NSW|Australia",
            "22-11-1995"
        );
    }

    // Valid drivers are stored correctly
    @Test
    @DisplayName("IT-D1 - Valid driver is stored and retrievable from TXT file")
    void itD1_validDriverStoredAndRetrievable() {
        // Arrange
        Driver driver = validDriver();

        // Act — persists to real TXT file
        repo.add(driver);

        // Assert — reads back from real TXT file
        Driver retrieved = repo.retrieve("24@#1609AB");

        assertNotNull(retrieved,
            "Driver should be retrievable after being added");
        assertEquals("24@#1609AB",    retrieved.getDriverID(),
            "driverID should match");
        assertEquals("John",          retrieved.getDriverName(),
            "driverName should match");
        assertEquals(5,                retrieved.getExperienceYears(),
            "experienceYears should match");
        assertEquals(LICENSE_TYPES.HEAVY, retrieved.getLicenseType(),
            "licenseType should match");
        assertEquals("12|Main Street|Melbourne|VIC|Australia",
            retrieved.getAddress(), "address should match");
    }

    // Invalid drivers are rejected
    @Test
    @DisplayName("IT-D2 - Driver with invalid driverID is rejected, file stays empty")
    void itD2_invalidDriverRejected() throws IOException {
        // Arrange — invalid driverID: first two chars are not digits 2-9
        assertThrows(IllegalArgumentException.class, () -> {
            new Driver(
                "BADINPUT!!",        
                "Invalid",
                2,
                LICENSE_TYPES.LIGHT,
                "1|Some St|Brisbane|QLD|Australia",
                "01-01-2000"
            );
        }, "Driver constructor should throw for invalid driverID");

        // Assert — nothing was written to the TXT file
        String fileContent = Files.readString(tempFile);
        assertTrue(fileContent.isBlank(),
            "TXT file should be empty after a rejected driver");
    }

    // Updates are persisted correctly
    @Test
    @DisplayName("IT-D3 - Updated driver fields are persisted to TXT file")
    void itD3_updatePersistedCorrectly() {
        // Arrange — add a driver with 5 years experience
        repo.add(validDriver());

        // Build updated driver with new experience and address
        Driver updated = new Driver(
            "24@#1609AB",     // same ID 
            "John",          // same name 
            7,                // updated experience years
            LICENSE_TYPES.HEAVY,
            "99|New Ave|Hobart|TAS|Australia",  // updated address
            "18-02-1998"
        );

        // Act
        repo.update(updated);

        // Assert — read back from TXT file
        Driver retrieved = repo.retrieve("24@#1609AB");
        assertNotNull(retrieved);
        assertEquals(7, retrieved.getExperienceYears(),
            "Updated experienceYears should be persisted");
        assertEquals("99|New Ave|Hobart|TAS|Australia", retrieved.getAddress(),
            "Updated address should be persisted");

        assertEquals("24@#1609AB", retrieved.getDriverID(),
            "D5: driverID must remain unchanged");
        assertEquals("John", retrieved.getDriverName(),
            "D5: driverName must remain unchanged");
    }

    // Record counts are updated correctly
    @Test
    @DisplayName("IT-D4 - Count updates correctly as drivers are added")
    void itD4_countUpdatesCorrectly() throws IOException {
        // Empty repo starts at 0
        assertEquals(0, repo.count(),
            "Empty repository should return count 0");

        // Add first driver — count becomes 1
        repo.add(validDriver());
        assertEquals(1, repo.count(),
            "Count should be 1 after first valid add");

        // Add second driver — count becomes 2
        repo.add(secondValidDriver());
        assertEquals(2, repo.count(),
            "Count should be 2 after second valid add");

        // Verify count matches actual lines in TXT file
        long fileLineCount = Files.lines(tempFile)
            .filter(line -> !line.isBlank())
            .count();
        assertEquals(fileLineCount, repo.count(),
            "repo.count() should match number of non-blank lines in TXT file");
    }
}