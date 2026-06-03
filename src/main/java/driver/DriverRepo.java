package driver;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;

public class DriverRepository {

    private final String filePath;

    
    //Constructor
    public DriverRepository(String filePath) {
        this.filePath = filePath;
        ensureFileExists();
    }

    // Add
    public void add(Driver driver) {
        if (retrieve(driver.getDriverID()) != null) {
            throw new IllegalArgumentException(
                "D1: Duplicate driverID: " + driver.getDriverID());
        }
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(filePath, true))) {
            writer.write(toFileString(driver));
            writer.newLine();
        } catch (IOException e) {
            throw new RuntimeException(
                "Failed to write driver to file: " + e.getMessage(), e);
        }
    }

    // Retrieve
    public Driver retrieve(String driverID) {
        for (Driver d : loadAll()) {
            if (d.getDriverID().equals(driverID)) return d;
        }
        return null;
    }

    // Update
    public void update(Driver updatedDriver) {
        List<Driver> all = loadAll();
        boolean found = false;

        for (int i = 0; i < all.size(); i++) {
            Driver existing = all.get(i);
            if (existing.getDriverID().equals(updatedDriver.getDriverID())) {
                found = true;
                existing.setExperienceYears(updatedDriver.getExperienceYears());
                existing.setLicenseType(updatedDriver.getLicenseType());
                existing.setAddress(updatedDriver.getAddress());
                existing.setBirthdate(updatedDriver.getBirthdate());

                all.set(i, existing);
                break;
            }
        }

        if (!found) {
            throw new IllegalArgumentException(
                "Driver not found with ID: " + updatedDriver.getDriverID());
        }

        saveAll(all);
    }

    // Count
    public int count() {
        return loadAll().size();
    }

    // Serialisation helpers
    private String toFileString(Driver driver) {
        String encodedAddress = driver.getAddress().replace("|", "§");
        return driver.getDriverID()
            + "|" + driver.getDriverName()
            + "|" + driver.getExperienceYears()
            + "|" + driver.getLicenseType().name()
            + "|" + encodedAddress
            + "|" + driver.getBirthdate()
                         .format(java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

    private Driver fromFileString(String line) {
        String[] parts = line.split("\\|", 6);
        if (parts.length != 6) {
            throw new IllegalArgumentException("Invalid driver record: " + line);
        }
        String decodedAddress = parts[4].replace("§", "|");
        LICENSE_TYPES licenseType = LICENSE_TYPES.valueOf(parts[3]);
        return new Driver(
            parts[0],           // driverID
            parts[1],           // driverName
            parts[2],           // experienceYears (String constructor)
            licenseType,
            decodedAddress,
            parts[5]            // birthdate dd-MM-yyyy
        );
    }

    // File I/O helpers
    private List<Driver> loadAll() {
        List<Driver> drivers = new ArrayList<>();
        File file = new File(filePath);
        if (!file.exists()) return drivers;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (!line.isEmpty()) {
                    drivers.add(fromFileString(line));
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(
                "Failed to read drivers file: " + e.getMessage(), e);
        }
        return drivers;
    }

    /** Overwrite the TXT file with the provided list of drivers. */
    private void saveAll(List<Driver> drivers) {
        try (BufferedWriter writer = new BufferedWriter(
                new FileWriter(filePath, false))) {
            for (Driver d : drivers) {
                writer.write(toFileString(d));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new RuntimeException(
                "Failed to save drivers file: " + e.getMessage(), e);
        }
    }

    /** Create the TXT file (and parent directories) if it does not yet exist. */
    private void ensureFileExists() {
        File file = new File(filePath);
        if (!file.exists()) {
            try {
                File parent = file.getParentFile();
                if (parent != null) parent.mkdirs();
                file.createNewFile();
            } catch (IOException e) {
                throw new RuntimeException(
                    "Could not create drivers file: " + e.getMessage(), e);
            }
        }
    }
}
