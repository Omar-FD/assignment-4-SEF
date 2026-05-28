package driver;

import java.time.LocalDate;

public class Driver {
    private String driverID;
    private String driverName;
    private int experienceYears;
    private LICENSE_TYPES licenseType;
    private String address;
    private LocalDate birthdate; //as dd-mm-yyyy


    enum LICENSE_TYPES {
        LIGHT,
        MEDIUM,
        HEAVY,
        PUBLIC_TRANSPORT,
    }
}
