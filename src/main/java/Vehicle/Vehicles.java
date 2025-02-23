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
    private int makeYear;
    private String licensePlate;
    private String type;
    private int capacity;
    private String status;

    // ✅ Default Constructor (Needed for JSON parsing)
    public Vehicles() {}

    // ✅ Constructor with All Fields
    public Vehicles(int id, String model, int makeYear, String licensePlate, String type, int capacity, String status) {
        this.id = id;
        this.model = model;
        this.makeYear = makeYear;
        this.licensePlate = licensePlate;
        this.type = type;
        this.capacity = capacity;
        this.status = status;
    }

    // ✅ Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getModel() { return model; }
    public void setModel(String model) { this.model = model; }

    public int getMakeYear() { return makeYear; }
    public void setMakeYear(int makeYear) { this.makeYear = makeYear; }

    public String getLicensePlate() { return licensePlate; }
    public void setLicensePlate(String licensePlate) { this.licensePlate = licensePlate; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public int getCapacity() { return capacity; }
    public void setCapacity(int capacity) { this.capacity = capacity; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    // ✅ Override toString() for Debugging
    @Override
    public String toString() {
        return "Vehicles{" +
                "id=" + id +
                ", model='" + model + '\'' +
                ", makeYear=" + makeYear +
                ", licensePlate='" + licensePlate + '\'' +
                ", type='" + type + '\'' +
                ", capacity=" + capacity +
                ", status='" + status + '\'' +
                '}';
    }
}
