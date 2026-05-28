package driver;

import java.time.LocalDate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Driver {
    enum LICENSE_TYPES {
        LIGHT,
        MEDIUM,
        HEAVY,
        PUBLIC_TRANSPORT,
    }
    private String driverID;
    private String driverName;
    private int experienceYears;
    private LICENSE_TYPES licenseType;
    private String address;
    private LocalDate birthdate; //as dd-mm-yyyy

    public Driver(String driverID, String driverName, int experienceYears, LICENSE_TYPES licenseType, String address, String birthdate) {
        if (validFields(driverID, driverName, experienceYears, licenseType, address, birthdate)) {
            this.driverID = driverID;
            this.driverName = driverName;
            this.experienceYears = experienceYears;
            this.licenseType = licenseType;
            this.address = address;
            this.birthdate = LocalDate.parse(birthdate);
        }
    }

    //checking all the fields
    private Boolean validFields(String driverID, String driverName, int experienceYears, LICENSE_TYPES licenseType, String address, String birthdate) {
        return validID(driverID) && validBirthdate(birthdate) && checkAddress(address);
    }

    //checking the fields required for login
    public Boolean verifyLogin(String driverID, String date) {
        return driverID.equals(this.driverID) && this.birthdate.equals(LocalDate.parse(date));
    }

    //Verifying the ID field
    public Boolean validID(String driverID) {
        //checking length
        if (driverID.length() < 10) {
            return false;
        }
        //checking structure
        String firstTwo = driverID.substring(0,2);
        if (!firstTwo.matches("[2-9]{2}")) {
            return false;
        }
        String middleFive = driverID.substring(2,9);
        long specialCount = middleFive.chars().filter(c -> !Character.isLetterOrDigit(c)).count();
        if (specialCount != 2) {
            return false;
        }
        String lastTwo = driverID.substring(9,11);
        if (!lastTwo.matches("[A-Z]{2}")) {
            return false;
        }
        return true;
    }

    //Address validation
    public Boolean checkAddress(String address) {
        String[] parts = address.split("\\|");
        if (parts.length != 5) {
            return false;
        }
        //Checking if it starts with street number
        int streetNumber = parts[0].length();
        if (!parts[0].matches("[0-9]{" + streetNumber + "}")) {
            return false;
        }
        String restOfAddress = parts[1] + " " + parts[2] + " " + parts[3] + " " + parts[4];
        return restOfAddress.matches("[A-Za-z0-9\\s]+");
    }
    //Birthday Validation
    public Boolean validBirthdate(String date) {
        String[] parts = date.split("-");
        if (parts.length != 3) {
            return false;
        }
        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);
        if (day < 1 || day > 31 || month < 1 || month > 12 || year < 1900 || year > 2021) {
            return false;
        }
        return month != 2 || day <= 29;
    }
}
