package bus;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.*;

public class BusTest {

    // A "base" driver who passes the age and experience conditions ~26 years old, 10 years
    // experience, HEAVY licence. 
    private Driver bDriver() {
        return new Driver("23232###RR", "John", 10, LICENSE_TYPES.HEAVY,
                "123 | Main St | Metropolis | State | Country", "01-01-2000");
    }

    // ===== B1: busID must be exactly 8 digits =====

    //1. Normal: a valid 8-digit ID is accepted
    @Test
    @DisplayName("B1, Part 1 valid 8-digit bus ID is accepted")
    void BusIdAccepted() {
        Bus bus = new Bus("12345678", 40, 80.0, "Diesel");
        assertEquals("12345678", bus.getBusID());
    }

    //2. Invalid/edge: wrong length, letters, empty, spaces are all rejected
    @ParameterizedTest
    @DisplayName("B1, Part 2: invalid bus IDs are rejected")
    @ValueSource(strings = {"1234567", "123456789", "1234567A", "abcdefgh", "", "12 34567"})
    void invalidBusIdRejected(String busID) {
        try {
            new Bus(busID, 40, 80.0, "Diesel");
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("Invalid bus details", ex.getMessage());
        }
    }

    // ===== B2: capacity can DECREASE but not INCREASE on update =====

    //3. Normal: can decrease capacity
    @Test
    @DisplayName("B2: Part 1: capacity can be decreased")
    void canDecreaseCapacity() {
        Bus bus = new Bus("12345678", 60, 75.0, "Diesel");
        assertTrue(bus.updateCapacity(40));
        assertEquals(40, bus.getCapacity());
    }

    //4. Invalid: capacity cannot be increased 
    @Test
    @DisplayName("B2: Part 2: capacity cannot be increased")
    void capacityCannotIncrease() {
        Bus bus = new Bus("12345678", 60, 75.0, "Diesel");
        assertFalse(bus.updateCapacity(70));
        assertEquals(60, bus.getCapacity());
    }

    //5. Edge: capacitySameValue is not either an increase or decrease. Can be allowed
    @Test
    @DisplayName("B2: Part 3: setting the same capacity is allowed")
    void capacitySameValueAllowed() {
        Bus bus = new Bus("12345678", 60, 75.0, "Diesel");
        assertTrue(bus.updateCapacity(60));
        assertEquals(60, bus.getCapacity());
    }

    // ===== B3: driver older than 50 cannot drive a bus with capacity >= 50 =====

    //6. Invalid: a old Driver Cannot Drive Large bus. 
    @Test
    @DisplayName("B3: Part 1: driver over 50 cannot drive a large bus (capacity >= 50)")
    void oldDriverCannotDriveLargeBus() {
        Driver oldDriver = new Driver("23232###RR", "Old", 10, LICENSE_TYPES.HEAVY,
                "123 | Main St | Metropolis | State | Country", "01-01-1970");
        Bus largeBus = new Bus("12345678", 60, 80.0, "Diesel");
        assertFalse(largeBus.permittedToDrive(oldDriver));
    }

    //7. Normal: a young driver can drive a large bus
    @Test
    @DisplayName("B3: Part 2: young driver can drive a large bus")
    void youngDriverCanDriveLargeBus() {
        Bus largeBus = new Bus("12345678", 60, 80.0, "Diesel");
        assertTrue(largeBus.permittedToDrive(bDriver()));
    }

    //8. Edge: an old driver CAN drive a small bus (capacity < 50)
    @Test
    @DisplayName("B3: Part 3: old driver can drive a small bus (capacity < 50)")
    void oldDriverCanDriveSmallBus() {
        Driver oldDriver = new Driver("23232###RR", "Old", 10, LICENSE_TYPES.HEAVY,
                "123 | Main St | Metropolis | State | Country", "01-01-1970");
        Bus smallBus = new Bus("12345678", 40, 80.0, "Diesel");
        assertTrue(smallBus.permittedToDrive(oldDriver));
    }

    // ===== B4: only drivers with >= 5 years experience can drive electric =====.

    //9. Invalid: low experience driver cannot drive an electric bus
    @Test
    @DisplayName("B4: Part 1: under 5 years experience cannot drive electric")
    void lowExperienceCannotDriveElectricBus() {
        Driver newDriver = new Driver("23232###RR", "New", 3, LICENSE_TYPES.HEAVY,
                "123 | Main St | Metropolis | State | Country", "01-01-2000");
        Bus electricBus = new Bus("12345678", 60, 80.0, "Electricity");
        assertFalse(electricBus.permittedToDrive(newDriver));
    }

    //10. Edge: exactly 5 years experience CAN drive an electric bus
    @Test
    @DisplayName("B4: Part 2: exactly 5 years experience can drive electric")
    void exactlyFiveYearsCanDriveElectricBus() {
        Driver driver = new Driver("23232###RR", "Five", 5, LICENSE_TYPES.HEAVY,
                "123 | Main St | Metropolis | State | Country", "01-01-2000");
        Bus electricBus = new Bus("12345678", 60, 80.0, "Electricity");
        assertTrue(electricBus.permittedToDrive(driver));
    }

    //11. Normal: an experienced driver can drive an electric bus
    @Test
    @DisplayName("B4: Part 3: experienced driver can drive electric")
    void experiencedDriverCanDriveElectricBus() {
        Bus electricBus = new Bus("12345678", 60, 80.0, "Electricity");
        assertTrue(electricBus.permittedToDrive(bDriver()));
    }

    // ===== B5: only Heavy or Public Transport licence can operate electric/hybrid =====

    //12. Invalid: a LIGHT licence cannot operate a hybrid bus
    @Test
    @DisplayName("B5: Part 1: LIGHT licence cannot operate a hybrid bus")
    void lightLicenceCannotDriveHybridBus() {
        Driver lightDriver = new Driver("23232###RR", "Light", 10, LICENSE_TYPES.LIGHT,
                "123 | Main St | Metropolis | State | Country", "01-01-2000");
        Bus hybridBus = new Bus("12345678", 60, 80.0, "Hybrid");
        assertFalse(hybridBus.permittedToDrive(lightDriver));
    }

    //13. Invalid: a MEDIUM licence cannot operate an electric bus
    @Test
    @DisplayName("B5: Part 2: MEDIUM licence cannot operate an electric bus")
    void mediumLicenceCannotDriveElectricBus() {
        Driver mediumDriver = new Driver("23232###RR", "Med", 10, LICENSE_TYPES.MEDIUM,
                "123 | Main St | Metropolis | State | Country", "01-01-2000");
        Bus electricBus = new Bus("12345678", 60, 80.0, "Electricity");
        assertFalse(electricBus.permittedToDrive(mediumDriver));
    }

    //14. Normal: a public transport driver (ptDriver) can operate a hybrid bus
    @Test
    @DisplayName("B5: Part 3: Public Transport licence can operate a hybrid bus")
    void publicTransportCanDriveHybridBus() {
        Driver ptDriver = new Driver("23232###RR", "PT", 10, LICENSE_TYPES.PUBLIC_TRANSPORT,
                "123 | Main St | Metropolis | State | Country", "01-01-2000");
        Bus hybridBus = new Bus("12345678", 60, 80.0, "Hybrid");
        assertTrue(hybridBus.permittedToDrive(ptDriver));
    }

    //15. Edge: a HEAVY licence can operate an electric bus
    @Test
    @DisplayName("B5: Part 4: Heavy licence can operate an electric bus")
    void heavyLicenceCanDriveElectricBus() {
        Bus electricBus = new Bus("12345678", 60, 80.0, "Electricity");
        assertTrue(electricBus.permittedToDrive(bDriver()));
    }
}