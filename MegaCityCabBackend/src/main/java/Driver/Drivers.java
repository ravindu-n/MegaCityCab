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
    private String dName;
    private String licenseNumber;
    private String phone;
    private String nic;
    private String dStatus;
    private Integer vehicleId; // Nullable

    // ✅ Default Constructor (Needed for JSON parsing)
    public Drivers() {}

    // ✅ Constructor with All Fields
    public Drivers(int id, String dName, String licenseNumber, String phone, String nic, String dStatus, Integer vehicleId) {
        this.id = id;
        this.dName = dName;
        this.licenseNumber = licenseNumber;
        this.phone = phone;
        this.nic = nic;
        this.dStatus = dStatus;
        this.vehicleId = vehicleId;
    }

    // ✅ Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getdName() { return dName; }
    public void setdName(String dName) { this.dName = dName; }

    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getNic() { return nic; }
    public void setNic(String nic) { this.nic = nic; }

    public String getdStatus() { return dStatus; }
    public void setdStatus(String dStatus) { this.dStatus = dStatus; }

    public Integer getVehicleId() { return vehicleId; }
    public void setVehicleId(Integer vehicleId) { this.vehicleId = vehicleId; }

    // ✅ Override toString() for Debugging
    @Override
    public String toString() {
        return "Drivers{" +
                "id=" + id +
                ", dName='" + dName + '\'' +
                ", licenseNumber='" + licenseNumber + '\'' +
                ", phone='" + phone + '\'' +
                ", nic='" + nic + '\'' +
                ", dStatus='" + dStatus + '\'' +
                ", vehicleId=" + vehicleId +
                '}';
    }
}
