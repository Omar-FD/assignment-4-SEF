package driver;

import java.time.LocalDate;
import java.util.Arrays;

enum LICENSE_TYPES {
        LIGHT,
        MEDIUM,
        HEAVY,
        PUBLIC_TRANSPORT,
    }
public class Driver {

    private final String driverID;
    private final String driverName;
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
            this.birthdate = LocalDate.parse(birthdate, java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        } else {
            throw new IllegalArgumentException("Invalid driver details");
        }
    }
    public Driver(String driverID, String driverName, String experienceYears, LICENSE_TYPES licenseType, String address, String birthdate) {
        if (validFields(driverID, driverName, experienceYears, licenseType, address, birthdate)) {
            this.driverID = driverID;
            this.driverName = driverName;
            this.experienceYears = Integer.parseInt(experienceYears);
            this.licenseType = licenseType;
            this.address = address;
            this.birthdate = LocalDate.parse(birthdate, java.time.format.DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        } else {
            throw new IllegalArgumentException("Invalid driver details");
        }
    }

    public String getDriverID() {
        return driverID;
    }

    public String getDriverName() {
        return driverName;
    }

    public int getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(int experienceYears) {
        this.experienceYears = experienceYears;
    }

    public LICENSE_TYPES getLicenseType() {
        return licenseType;
    }

    public void setLicenseType(LICENSE_TYPES licenseType) {
        if (this.experienceYears > 10) return;
        this.licenseType = licenseType;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        if (!validAddress(address)) return;
        this.address = address;
    }

    public LocalDate getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(LocalDate birthdate) {
        if (!validBirthdate(birthdate.toString())) return;
        this.birthdate = birthdate;
    }


    //---------------HELPER METHODS-----------------------------


    //checking all the fields
    private Boolean validFields(String driverID, String driverName, int experienceYears, LICENSE_TYPES licenseType, String address, String birthdate) {
        if (!validName(driverName) || experienceYears < 0 || licenseType == null) return false;
        return validID(driverID) && validBirthdate(birthdate) && validAddress(address);
    }

    private Boolean validFields(String driverID, String driverName, String experienceYears, LICENSE_TYPES licenseType, String address, String birthdate) {
        if (licenseType == null) {
            return  false;
        } else {
            System.out.println("valid name");
        };
        return validID(driverID) && validBirthdate(birthdate) && validAddress(address) && validExperience(experienceYears) && validName(driverName);
    }

    public Boolean validName(String driverName) {
        if (driverName.isEmpty()) return false;
        if (driverName.trim().isEmpty()) return false;
        for (int i = 0; i < driverName.length(); i++) {
            if (!Character.isLetter(driverName.charAt(i))) {
                return false;
            }
        }
        return true;
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
        String middleFive = driverID.substring(2,8);
        long specialCount = middleFive.chars().filter(c -> !Character.isLetterOrDigit(c)).count();
        if (specialCount < 2) {
            return false;
        }
        String lastTwo = driverID.substring(8,10);
        if (!lastTwo.matches("[A-Z]{2}")) {
            return false;
        }
        return true;
    }

    //Address validation
    public Boolean validAddress(String address) {
        if (address.trim().isEmpty()) return false;
        String[] parts = address.split("\\|");
        if (parts.length != 5) {
            return false;
        }
        //Checking if it starts with a street number
        int streetNumber = parts[0].trim().length();
        if (!parts[0].trim().matches("[0-9]{" + streetNumber + "}")) {
            return false;
        }
        String restOfAddress = parts[1] + parts[2] + parts[3] + parts[4];
        return restOfAddress.matches("[A-Za-z0-9\\s]+");
    }

    public Boolean validExperience(String experience) {
        int experienceYears = Integer.parseInt(experience);
        return validExperience(experienceYears);
    }
    public Boolean validExperience(int experience) {
        if (experience < 0) return false;
        if (experience > 60) return false;
        return true;
    }

    //Birthday Validation
    public Boolean validBirthdate(String date) {
        String[] parts = date.split("-");
        if (parts.length != 3) {
            return false;
        }
        boolean allDigits = Arrays.stream(parts).allMatch(part -> !part.isEmpty() && part.chars().allMatch(Character::isDigit));
        if (!allDigits) { return false;}

        int day = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int year = Integer.parseInt(parts[2]);
        if (day < 1 || day > 31 || month < 1 || month > 12 || year < 1900 || year > 2021) {
            return false;
        }
        return month != 2 || day <= 29;
    }
    public Boolean validBirthdate(LocalDate date) {
        String stringDate = date.toString();
        String[] parts = stringDate.split("-");
        if (parts.length != 3) {
            return false;
        }
        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int day = Integer.parseInt(parts[2]);
        if (day < 1 || day > 31 || month < 1 || month > 12 || year < 1900 || year > 2021) {
            return false;
        }
        return month != 2 || day <= 29;
    }
}
