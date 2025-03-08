/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package User;

/**
 *
 * @author ravin
 */
public class Users {
    private int id;
    private String username;
    private String email;
    private String phone;
    private String nic;
    private String pword; 
    private String urole;
    private String address;
    private String licenseNumber;  // ✅ Added
    private String dStatus;        // ✅ Added
    private Integer vehicleId;     // ✅ Added (nullable)

    // ✅ Default Constructor (Needed for JSON parsing & frameworks)
    public Users() {}

    // ✅ Constructor with All Fields
    public Users(int id, String username, String email, String phone, String nic, String pword, String urole, String address, String licenseNumber, String dStatus, Integer vehicleId) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.nic = nic;
        this.pword = pword;
        this.urole = urole;
        this.address = address;
        this.licenseNumber = licenseNumber;
        this.dStatus = dStatus;
        this.vehicleId = vehicleId;
    }

    // ✅ Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getNic() { return nic; }
    public void setNic(String nic) { this.nic = nic; }

    public String getPword() { return pword; }  // Ensure this matches DB column
    public void setPword(String pword) { this.pword = pword; }  

    public String getUrole() { return urole; }  // Ensure this matches DB column
    public void setUrole(String urole) { this.urole = urole; } 
    
    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
    
    public String getLicenseNumber() { return licenseNumber; }
    public void setLicenseNumber(String licenseNumber) { this.licenseNumber = licenseNumber; }

    public String getDStatus() { return dStatus; }
    public void setDStatus(String dStatus) { this.dStatus = dStatus; }

    public Integer getVehicleId() { return vehicleId; }
    public void setVehicleId(Integer vehicleId) { this.vehicleId = vehicleId; }
    
    // ✅ Override toString() for debugging
    @Override
    public String toString() {
        return "Users{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", nic='" + nic + '\'' +
                ", urole='" + urole + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
