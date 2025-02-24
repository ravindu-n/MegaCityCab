/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Booking;

/**
 *
 * @author ravin
 */
public class Bookings {
    private int id;
    private int customerId;
    private Integer driverId;  // Nullable
    private Integer vehicleId; // Nullable
    private String pickupLocation;
    private String dropoffLocation;
    private String bookingDate;
    private String pickupTime;
    private double fare;
    private String bStatus;

    // ✅ Default Constructor (Needed for JSON parsing)
    public Bookings() {}

    // ✅ Constructor with All Fields
    public Bookings(int id, int customerId, Integer driverId, Integer vehicleId, String pickupLocation, 
                    String dropoffLocation, String bookingDate, String pickupTime, double fare, String bStatus) {
        this.id = id;
        this.customerId = customerId;
        this.driverId = driverId;
        this.vehicleId = vehicleId;
        this.pickupLocation = pickupLocation;
        this.dropoffLocation = dropoffLocation;
        this.bookingDate = bookingDate;
        this.pickupTime = pickupTime;
        this.fare = fare;
        this.bStatus = bStatus;
    }

    // ✅ Getters and Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public int getCustomerId() { return customerId; }
    public void setCustomerId(int customerId) { this.customerId = customerId; }

    public Integer getDriverId() { return driverId; }
    public void setDriverId(Integer driverId) { this.driverId = driverId; }

    public Integer getVehicleId() { return vehicleId; }
    public void setVehicleId(Integer vehicleId) { this.vehicleId = vehicleId; }

    public String getPickupLocation() { return pickupLocation; }
    public void setPickupLocation(String pickupLocation) { this.pickupLocation = pickupLocation; }

    public String getDropoffLocation() { return dropoffLocation; }
    public void setDropoffLocation(String dropoffLocation) { this.dropoffLocation = dropoffLocation; }

    public String getBookingDate() { return bookingDate; }
    public void setBookingDate(String bookingDate) { this.bookingDate = bookingDate; }

    public String getPickupTime() { return pickupTime; }
    public void setPickupTime(String pickupTime) { this.pickupTime = pickupTime; }

    public double getFare() { return fare; }
    public void setFare(double fare) { this.fare = fare; }

    public String getbStatus() { return bStatus; }
    public void setbStatus(String bStatus) { this.bStatus = bStatus; }

    // ✅ Override toString() for Debugging
    @Override
    public String toString() {
        return "Bookings{" +
                "id=" + id +
                ", customerId=" + customerId +
                ", driverId=" + driverId +
                ", vehicleId=" + vehicleId +
                ", pickupLocation='" + pickupLocation + '\'' +
                ", dropoffLocation='" + dropoffLocation + '\'' +
                ", bookingDate='" + bookingDate + '\'' +
                ", pickupTime='" + pickupTime + '\'' +
                ", fare=" + fare +
                ", bStatus='" + bStatus + '\'' +
                '}';
    }
}
