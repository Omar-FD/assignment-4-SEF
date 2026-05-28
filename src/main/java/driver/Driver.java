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
}
