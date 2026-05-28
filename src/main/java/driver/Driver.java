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

    public Driver() {}

    public Boolean verifyDetails(String driverID, String date) {
        return driverID.equals(this.driverID) && this.birthdate.equals(LocalDate.parse(date));
    }
    public void addID(String driverID) {
        //checking length
        if (driverID.length() < 10) {
            return;
        }
        //checking structure
        String firstTwo = driverID.substring(0,2);
        if (!firstTwo.matches("[2-9]{2}")) {
            return;
        }
        String middleFive = driverID.substring(2,9);
        long specialCount = middleFive.chars().filter(c -> !Character.isLetterOrDigit(c)).count();
        if (specialCount != 2) {
            return;
        }
        String lastTwo = driverID.substring(9,11);
        if (!lastTwo.matches("[A-Z]{2}")) {
            return;
        }
        this.driverID = driverID;
    }

    //Address addition && validation
    public void addAddress(String address) {
        String[] parts = address.split("\\|");
        if (parts.length != 5) {
            return;
        }
        //Checking if it starts with street number
        int streetNumber = parts[0].length();
        if (!parts[0].matches("[0-9]{" + streetNumber + "}")) {
            return;
        }
        String restOfAddress = parts[1] + " " + parts[2] + " " + parts[3] + " " + parts[4];
        if (!restOfAddress.matches("[A-Za-z0-9\\s]+")) {
            return;
        }
        this.address = restOfAddress;
    }

    public void addBirthdate(String date) {
        String[] parts = date.split("-");
        if (parts.length != 3) {
            return;
        }
        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);
        if (day < 1 || day > 31 || month < 1 || month > 12 || year < 1900 || year > 2021) {
            return;
        }
        this.birthdate = LocalDate.of(year, month, day);
    }
}
