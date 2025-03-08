/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Booking;

import DB.DatabaseOperation;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ravin
 */
public class BookingOperations {

    // ✅ Updated Add Booking Method (Without driver_id)
    public static int addBooking(Bookings booking) {
        String query = "INSERT INTO bookings (customer_id, vehicle_id, pickup_location, dropoff_location, pickup_time, fare, bStatus) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseOperation.connect(); PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, booking.getCustomerId());

            if (booking.getVehicleId() != null) {
                stmt.setInt(2, booking.getVehicleId());
            } else {
                stmt.setNull(2, java.sql.Types.INTEGER);
            }

            stmt.setString(3, booking.getPickupLocation());
            stmt.setString(4, booking.getDropoffLocation());
            stmt.setString(5, booking.getPickupTime());
            stmt.setDouble(6, booking.getFare());
            stmt.setString(7, booking.getbStatus());

            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Error case
    }

    // ✅ Get All Bookings
    public static List<Bookings> getAllBookings() {
        List<Bookings> bookingList = new ArrayList<>();
        String query = "SELECT b.*, u.username AS customerName FROM bookings b "
                + "JOIN users u ON b.customer_id = u.Id ORDER BY b.booking_date DESC";
        try (Connection conn = DatabaseOperation.connect(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                bookingList.add(new Bookings(
                        rs.getInt("Id"),
                        rs.getInt("customer_id"),
                        rs.getObject("vehicle_id") != null ? rs.getInt("vehicle_id") : null,
                        rs.getString("pickup_location"),
                        rs.getString("dropoff_location"),
                        rs.getString("booking_date"),
                        rs.getString("pickup_time"),
                        rs.getDouble("fare"),
                        rs.getString("bStatus"),
                        rs.getString("customerName")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookingList;
    }

    // ✅ Get Bookings by Customer ID
    public static List<Bookings> getBookingsByCustomer(int customerId) {
        List<Bookings> bookingList = new ArrayList<>();
        String query = "SELECT b.*, u.username AS customerName FROM bookings b "
                + "JOIN users u ON b.customer_id = u.Id WHERE b.customer_id = ? ORDER BY b.booking_date DESC";

        try (Connection conn = DatabaseOperation.connect(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                bookingList.add(new Bookings(
                        rs.getInt("Id"),
                        rs.getInt("customer_id"),
                        rs.getObject("vehicle_id") != null ? rs.getInt("vehicle_id") : null,
                        rs.getString("pickup_location"),
                        rs.getString("dropoff_location"),
                        rs.getString("booking_date"),
                        rs.getString("pickup_time"),
                        rs.getDouble("fare"),
                        rs.getString("bStatus"),
                        rs.getString("customerName")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookingList;
    }

    // ✅ Delete a Booking by ID
    public static int deleteBooking(int id) {
        String query = "DELETE FROM bookings WHERE Id=?";
        try (Connection conn = DatabaseOperation.connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Error case
    }

    // ✅ Update Booking Status
    public static boolean updateBookingStatus(int id, String newStatus) {
        String query = "UPDATE bookings SET bStatus=? WHERE Id=?";
        try (Connection conn = DatabaseOperation.connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, newStatus);
            stmt.setInt(2, id);
            int updatedRows = stmt.executeUpdate();
            return updatedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Error case
    }

    // ✅ Get Driver's Bookings
    public static List<Bookings> getBookingsByDriver(int driverId) {
        List<Bookings> bookingList = new ArrayList<>();
        String query = "SELECT b.*, u.username AS customerName FROM bookings b "
                + "JOIN users u ON b.customer_id = u.Id WHERE b.driver_id = ?";

        try (Connection conn = DatabaseOperation.connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, driverId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                bookingList.add(new Bookings(
                        rs.getInt("Id"),
                        rs.getInt("customer_id"),
                        rs.getInt("vehicle_id"),
                        rs.getString("pickup_location"),
                        rs.getString("dropoff_location"),
                        rs.getString("booking_date"),
                        rs.getString("pickup_time"),
                        rs.getDouble("fare"),
                        rs.getString("bStatus"),
                        rs.getString("customerName") // Adding customer name for frontend display
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookingList;
    }
}
