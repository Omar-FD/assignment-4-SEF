package bus;

import driver.Driver;
import driver.LICENSE_TYPES;

import java.time.LocalDate;
import java.time.Period;

/**
 * Represents a bus in the Intelligent Bus Driver Guidance System.
 * Enforces the bus conditions B1-B5.
 */
public class Bus {

    private final String busID;     // immutable, like driverID
    private int capacity;
    private double fuelLevel;
    private String fuelType;        // Diesel, Hybrid, Electricity

    // The constructor validates the fields and throws if they are invalid,
    // matching the style of your teammate's Driver class.
    public Bus(String busID, int capacity, double fuelLevel, String fuelType) {
        if (!isValidBusID(busID) || !isValidFuelType(fuelType) || capacity <= 0) {
            throw new IllegalArgumentException("Invalid bus details");
        }
        this.busID = busID;
        this.capacity = capacity;
        this.fuelLevel = fuelLevel;
        this.fuelType = fuelType;
    }

    // ---- Getters ----
    public String getBusID() { return busID; }
    public int getCapacity() { return capacity; }
    public double getFuelLevel() { return fuelLevel; }
    public String getFuelType() { return fuelType; }

    // B1: busID must be exactly 8 characters, all digits.
    // (Uniqueness is enforced later by BusRepository, not here.)
    public static boolean isValidBusID(String busID) {
        return busID != null && busID.matches("\\d{8}");
    }

    // Helper: a bus must be Diesel, Hybrid, or Electricity.
    private static boolean isValidFuelType(String fuelType) {
        return fuelType != null &&
               (fuelType.equals("Diesel")
                || fuelType.equals("Hybrid")
                || fuelType.equals("Electricity"));
    }

    // B2: capacity can DECREASE during an update, but never INCREASE.
    // Returns true if the change was allowed and applied, false otherwise.
    public boolean updateCapacity(int newCapacity) {
        if (newCapacity > this.capacity) {
            return false; // increase not allowed
        }
        this.capacity = newCapacity;
        return true;
    }

    // B3, B4, B5: whether a given driver is permitted to drive THIS bus.
    public boolean canBeDrivenBy(Driver driver) {
        int age = Period.between(driver.getBirthdate(), LocalDate.now()).getYears();

        // B3: a driver older than 50 cannot drive a bus with capacity >= 50.
        if (age > 50 && this.capacity >= 50) {
            return false;
        }

        // B4: only drivers with at least 5 years experience can drive
        //     electric buses.
        if (this.fuelType.equals("Electricity") && driver.getExperienceYears() < 5) {
            return false;
        }

        // B5: only Heavy or Public Transport licence holders can operate
        //     electric OR hybrid buses.
        if (this.fuelType.equals("Electricity") || this.fuelType.equals("Hybrid")) {
            LICENSE_TYPES lt = driver.getLicenseType();
            if (lt != LICENSE_TYPES.HEAVY && lt != LICENSE_TYPES.PUBLIC_TRANSPORT) {
                return false;
            }
        }

        return true; // passed all applicable rules
    }
    public boolean permittedToDrive(Driver driver) {
        return canBeDrivenBy(driver);
    }
}