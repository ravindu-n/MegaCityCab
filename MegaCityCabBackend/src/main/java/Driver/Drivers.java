/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Driver;

/**
 *
 * @author ravin
 */
public class Drivers {

    private int id;
    private String dName;            // Driver Name
    private String licenseNumber;   // License Number
    private String phone;           // Phone Number
    private String nic;             // NIC Number
    private String pword;           // Password (Hashed in backend)
    private String dStatus;         // Driver Status (Default 'Available')
    private Integer vehicleId;      // Vehicle ID (Optional, can be null)

    // ✅ Default Constructor (Important for JSON Parsing)
    public Drivers() {
    }

    // ✅ Full Constructor with All Fields
    public Drivers(int id, String dName, String licenseNumber, String phone, String nic, String pword, String dStatus, Integer vehicleId) {
        this.id = id;
        this.dName = dName;
        this.licenseNumber = licenseNumber;
        this.phone = phone;
        this.nic = nic;
        this.pword = pword;
        this.dStatus = dStatus;
        this.vehicleId = vehicleId;
    }

    // ✅ Getters and Setters (Camel Case - Correct for JSON Mapping)
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getdName() {
        return dName;
    }

    public void setdName(String dName) {
        this.dName = dName;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getPword() {
        return pword;
    }

    public void setPword(String pword) {
        this.pword = pword;
    }

    public String getdStatus() {
        return dStatus;
    }

    public void setdStatus(String dStatus) {
        this.dStatus = dStatus;
    }

    public Integer getVehicleId() {
        return vehicleId;
    }

    public void setVehicleId(Integer vehicleId) {
        this.vehicleId = vehicleId;
    }
}
