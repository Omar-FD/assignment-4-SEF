package driver;

import java.time.LocalDate;

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
        this.driverID = driverID;
    }
}
