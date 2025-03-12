/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vehicle;

/**
 *
 * @author ravin
 */
public class Vehicles {

    private int id;
    private String model;
    private int make_year;
    private String license_plate;
    private String vType;
    private int capacity;
    private String vStatus;

    // ✅ Default Constructor (Needed for JSON parsing)
    public Vehicles() {
    }

    // ✅ Constructor with All Fields
    public Vehicles(int id, String model, int make_year, String license_plate, String vType, int capacity, String vStatus) {
        this.id = id;
        this.model = model;
        this.make_year = make_year;  // Ensure this is included
        this.license_plate = license_plate;
        this.vType = vType;
        this.capacity = capacity;
        this.vStatus = vStatus;
    }

    // ✅ Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public int getMakeYear() {
        return make_year;
    }

    public void setMakeYear(int makeYear) {
        this.make_year = makeYear;
    }

    public String getLicensePlate() {
        return license_plate;
    }

    public void setLicensePlate(String licensePlate) {
        this.license_plate = licensePlate;
    }

    public String getType() {
        return vType;
    }

    public void setType(String type) {
        this.vType = type;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getStatus() {
        return vStatus;
    }

    public void setStatus(String status) {
        this.vStatus = status;
    }

    // ✅ Override toString() for Debugging
    @Override
    public String toString() {
        return "Vehicles{"
                + "id=" + id
                + ", model='" + model + '\''
                + ", makeYear=" + make_year
                + ", licensePlate='" + license_plate + '\''
                + ", type='" + vType + '\''
                + ", capacity=" + capacity
                + ", status='" + vStatus + '\''
                + '}';
    }
}
