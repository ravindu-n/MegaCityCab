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
    
    // ✅ Add a New Booking
    public static int addBooking(Bookings booking) {
        String query = "INSERT INTO bookings (customer_id, driver_id, vehicle_id, pickup_location, dropoff_location, pickup_time, fare, bStatus) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseOperation.connect();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, booking.getCustomerId());
            if (booking.getDriverId() != null) {
                stmt.setInt(2, booking.getDriverId());
            } else {
                stmt.setNull(2, java.sql.Types.INTEGER);
            }
            if (booking.getVehicleId() != null) {
                stmt.setInt(3, booking.getVehicleId());
            } else {
                stmt.setNull(3, java.sql.Types.INTEGER);
            }
            stmt.setString(4, booking.getPickupLocation());
            stmt.setString(5, booking.getDropoffLocation());
            stmt.setString(6, booking.getPickupTime());
            stmt.setDouble(7, booking.getFare());
            stmt.setString(8, booking.getbStatus());

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

    // ✅ Retrieve All Bookings
    public static List<Bookings> getAllBookings() {
        List<Bookings> bookingList = new ArrayList<>();
        String query = "SELECT * FROM bookings";
        try (Connection conn = DatabaseOperation.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                bookingList.add(new Bookings(
                    rs.getInt("Id"),
                    rs.getInt("customer_id"),
                    rs.getObject("driver_id") != null ? rs.getInt("driver_id") : null,
                    rs.getObject("vehicle_id") != null ? rs.getInt("vehicle_id") : null,
                    rs.getString("pickup_location"),
                    rs.getString("dropoff_location"),
                    rs.getString("booking_date"),
                    rs.getString("pickup_time"),
                    rs.getDouble("fare"),
                    rs.getString("bStatus")
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
        try (Connection conn = DatabaseOperation.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
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
        try (Connection conn = DatabaseOperation.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, newStatus);
            stmt.setInt(2, id);
            int updatedRows = stmt.executeUpdate();
            return updatedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Error case
    }
}
