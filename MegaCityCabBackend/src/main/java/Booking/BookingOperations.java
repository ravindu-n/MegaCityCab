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
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author ravin
 */
public class BookingOperations {

    // ✅ Add Booking (Auto assign driver)
    public static int addBooking(Bookings booking) {
        String getDriverQuery = "SELECT Id FROM drivers WHERE dStatus = 'Available' AND vehicle_id = ?";
        String insertBookingQuery = "INSERT INTO bookings (customer_id, driver_id, vehicle_id, pickup_location, dropoff_location, pickup_time, fare, distance, bStatus) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseOperation.connect(); PreparedStatement driverStmt = conn.prepareStatement(getDriverQuery); PreparedStatement bookingStmt = conn.prepareStatement(insertBookingQuery, Statement.RETURN_GENERATED_KEYS)) {

            driverStmt.setInt(1, booking.getVehicleId());
            ResultSet driverResult = driverStmt.executeQuery();

            Integer driverId = driverResult.next() ? driverResult.getInt("Id") : null;

            if (booking.getCustomerId() == 0 || booking.getVehicleId() == 0 || booking.getPickupLocation().isEmpty() || booking.getDropoffLocation().isEmpty()) {
                return -1; // Validation failed
            }

            bookingStmt.setInt(1, booking.getCustomerId());
            if (driverId != null) {
                bookingStmt.setInt(2, driverId);
            } else {
                bookingStmt.setNull(2, Types.INTEGER);
            }
            bookingStmt.setInt(3, booking.getVehicleId());
            bookingStmt.setString(4, booking.getPickupLocation());
            bookingStmt.setString(5, booking.getDropoffLocation());
            bookingStmt.setString(6, booking.getPickupTime());
            bookingStmt.setBigDecimal(7, booking.getFare());
            bookingStmt.setBigDecimal(8, booking.getDistance());
            bookingStmt.setString(9, "Pending");

            if (bookingStmt.executeUpdate() > 0) {
                ResultSet rs = bookingStmt.getGeneratedKeys();
                return rs.next() ? rs.getInt(1) : -1;
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    // ✅ Get All Bookings (For Admin view)
    public static List<Bookings> getAllBookings() {
        List<Bookings> bookingList = new ArrayList<>();
        String query = "SELECT b.*, u.username AS customerName FROM bookings b "
                + "JOIN users u ON b.customer_id = u.Id "
                + "ORDER BY b.booking_date DESC";

        try (Connection conn = DatabaseOperation.connect(); PreparedStatement stmt = conn.prepareStatement(query); ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                bookingList.add(new Bookings(rs)); // Add each booking to list
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookingList;
    }

    // ✅ Get Bookings by Customer ID (For Customer View)
    public static List<Bookings> getBookingsByCustomer(int customerId) {
        List<Bookings> bookingList = new ArrayList<>();
        String query = "SELECT b.*, u.username AS customerName FROM bookings b "
                + "JOIN users u ON b.customer_id = u.Id "
                + "WHERE b.customer_id = ? "
                + "ORDER BY b.booking_date DESC";

        try (Connection conn = DatabaseOperation.connect(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, customerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                bookingList.add(new Bookings(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookingList;
    }

    // ✅ Get Bookings by Driver ID (For Driver View)
    public static List<Bookings> getBookingsByDriver(int driverId) {
        List<Bookings> bookingList = new ArrayList<>();
        String query = "SELECT b.*, u.username AS customerName FROM bookings b "
                + "JOIN users u ON b.customer_id = u.Id "
                + "WHERE b.driver_id = ? "
                + "ORDER BY b.booking_date DESC";

        try (Connection conn = DatabaseOperation.connect(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, driverId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                bookingList.add(new Bookings(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return bookingList;
    }

    // ✅ Update Booking Status (For Driver to Confirm or Cancel)
    public static boolean updateBookingStatus(int bookingId, String newStatus) {
        String query = "UPDATE bookings SET bStatus = ? WHERE Id = ?";
        try (Connection conn = DatabaseOperation.connect(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setString(1, newStatus);
            stmt.setInt(2, bookingId);

            return stmt.executeUpdate() > 0; // Return true if updated successfully
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Failed
    }

    // ✅ Delete a Booking (Admin Action)
    public static int deleteBooking(int id) {
        String query = "DELETE FROM bookings WHERE Id = ?";
        try (Connection conn = DatabaseOperation.connect(); PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            return stmt.executeUpdate(); // Return number of rows affected
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Error
    }

    // ✅ (Optional) Private Helper to Find Any Available Driver
    private static Integer getAvailableDriver() {
        String query = "SELECT Id FROM drivers WHERE dStatus = 'Available' LIMIT 1";
        try (Connection conn = DatabaseOperation.connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                return rs.getInt("Id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // No available driver
    }
}
