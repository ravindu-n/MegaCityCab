/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Booking;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author ravin
 */
public class Bookings {

    private int Id;
    private int customer_id;
    private Integer driver_id;
    private Integer vehicle_id;
    private String pickup_location;
    private String dropoff_location;
    private String booking_date;
    private String pickup_time;
    private BigDecimal fare;
    private BigDecimal distance; // ✅ Updated to BigDecimal
    private String bStatus;
    private String customerName;

    // ✅ Default Constructor
    public Bookings() {
    }

    // ✅ Full Constructor
    public Bookings(int Id, int customer_id, Integer driver_id, Integer vehicle_id, String pickup_location,
            String dropoff_location, String booking_date, String pickup_time,
            BigDecimal fare, BigDecimal distance, String bStatus, String customerName) {
        this.Id = Id;
        this.customer_id = customer_id;
        this.driver_id = driver_id;
        this.vehicle_id = vehicle_id;
        this.pickup_location = pickup_location;
        this.dropoff_location = dropoff_location;
        this.booking_date = booking_date;
        this.pickup_time = pickup_time;
        this.fare = fare;
        this.distance = distance;
        this.bStatus = bStatus;
        this.customerName = customerName;
    }

    // ✅ ResultSet Constructor
    public Bookings(ResultSet rs) throws SQLException {
        this.Id = rs.getInt("Id");
        this.customer_id = rs.getInt("customer_id");
        this.driver_id = (Integer) rs.getObject("driver_id");
        this.vehicle_id = (Integer) rs.getObject("vehicle_id");
        this.pickup_location = rs.getString("pickup_location");
        this.dropoff_location = rs.getString("dropoff_location");
        this.booking_date = rs.getString("booking_date");
        this.pickup_time = rs.getString("pickup_time");
        this.fare = rs.getBigDecimal("fare");
        this.distance = rs.getBigDecimal("distance");
        this.bStatus = rs.getString("bStatus");
        this.customerName = rs.getString("customerName");
    }

    // Getters & Setters
    // ✅ Include distance
    public int getId() {
        return Id;
    }

    public void setId(int Id) {
        this.Id = Id;
    }

    public int getCustomerId() {
        return customer_id;
    }

    public void setCustomerId(int customer_id) {
        this.customer_id = customer_id;
    }

    public Integer getDriverId() {
        return driver_id;
    }

    public void setDriverId(Integer driver_id) {
        this.driver_id = driver_id;
    }

    public Integer getVehicleId() {
        return vehicle_id;
    }

    public void setVehicleId(Integer vehicle_id) {
        this.vehicle_id = vehicle_id;
    }

    public String getPickupLocation() {
        return pickup_location;
    }

    public void setPickupLocation(String pickup_location) {
        this.pickup_location = pickup_location;
    }

    public String getDropoffLocation() {
        return dropoff_location;
    }

    public void setDropoffLocation(String dropoff_location) {
        this.dropoff_location = dropoff_location;
    }

    public String getBookingDate() {
        return booking_date;
    }

    public void setBookingDate(String booking_date) {
        this.booking_date = booking_date;
    }

    public String getPickupTime() {
        return pickup_time;
    }

    public void setPickupTime(String pickup_time) {
        this.pickup_time = pickup_time;
    }

    public BigDecimal getFare() {
        return fare;
    }

    public void setFare(BigDecimal fare) {
        this.fare = fare;
    }

    public BigDecimal getDistance() {
        return distance;
    }

    public void setDistance(BigDecimal distance) {
        this.distance = distance;
    }

    public String getbStatus() {
        return bStatus;
    }

    public void setbStatus(String bStatus) {
        this.bStatus = bStatus;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    // ✅ toString
    @Override
    public String toString() {
        return "Bookings{" + "id=" + Id + ", customerId=" + customer_id + ", driverId=" + driver_id
                + ", vehicleId=" + vehicle_id + ", pickupLocation=" + pickup_location + ", dropoffLocation=" + dropoff_location
                + ", bookingDate=" + booking_date + ", pickupTime=" + pickup_time + ", fare=" + fare
                + ", distance=" + distance + ", bStatus=" + bStatus + ", customerName=" + customerName + '}';
    }
}
