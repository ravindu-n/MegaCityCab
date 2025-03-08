/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Driver;

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
public class DriverOperations {

    // ✅ Add a New Driver
    public static int addDriver(Drivers driver, int userId) {
        String query = "INSERT INTO drivers (user_id, dName, license_number, phone, nic, dStatus, vehicle_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseOperation.connect(); PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setInt(1, userId);
            stmt.setString(2, driver.getdName());
            stmt.setString(3, driver.getLicenseNumber());
            stmt.setString(4, driver.getPhone());
            stmt.setString(5, driver.getNic());
            stmt.setString(6, driver.getdStatus());
            if (driver.getVehicleId() != null) {
                stmt.setInt(7, driver.getVehicleId());
            } else {
                stmt.setNull(7, java.sql.Types.INTEGER);
            }

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

    // ✅ Retrieve All Drivers
    public static List<Drivers> getAllDrivers() {
        List<Drivers> driverList = new ArrayList<>();
        String query = "SELECT * FROM users WHERE urole = 'Driver'";

        try (Connection conn = DatabaseOperation.connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {

            System.out.println("🔹 Fetching drivers...");

            while (rs.next()) {
                System.out.println("✅ Driver found: " + rs.getString("username"));

                driverList.add(new Drivers(
                        rs.getInt("Id"),
                        rs.getString("username"),
                        rs.getString("license_number"),
                        rs.getString("phone"),
                        rs.getString("nic"),
                        rs.getString("dStatus"),
                        rs.getObject("vehicle_id") != null ? rs.getInt("vehicle_id") : null
                ));
            }

            if (driverList.isEmpty()) {
                System.out.println("❌ No drivers found in database.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return driverList;
    }

    // ✅ Delete a Driver by ID
    public static int deleteDriver(int id) {
        String query = "DELETE FROM drivers WHERE Id=?";
        try (Connection conn = DatabaseOperation.connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Error case
    }

    // ✅ Update Driver Status (Available, On Trip, Inactive)
    public static boolean updateDriverStatus(int id, String status) {
        String query = "UPDATE drivers SET dStatus=? WHERE Id=?";
        try (Connection conn = DatabaseOperation.connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, status);
            stmt.setInt(2, id);
            int updatedRows = stmt.executeUpdate();
            return updatedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Error case
    }

    // ✅ Assign a Driver to a Vehicle
    public static boolean assignVehicleToDriver(int driverId, int vehicleId) {
        String query = "UPDATE drivers SET vehicle_id=? WHERE Id=?";
        try (Connection conn = DatabaseOperation.connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, vehicleId);
            stmt.setInt(2, driverId);
            int updatedRows = stmt.executeUpdate();
            return updatedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Error case
    }

    public static Drivers getDriverById(int id) {
        String query = "SELECT * FROM drivers WHERE Id = ?";
        try (Connection conn = DatabaseOperation.connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Drivers(
                        rs.getInt("Id"),
                        rs.getString("dName"),
                        rs.getString("license_number"),
                        rs.getString("phone"),
                        rs.getString("nic"),
                        rs.getString("dStatus"),
                        rs.getObject("vehicle_id") != null ? rs.getInt("vehicle_id") : null
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Driver not found
    }

    public static Drivers getDriverByUserId(int userId) {
        String query = "SELECT * FROM drivers WHERE user_id = ?";
        try (Connection conn = DatabaseOperation.connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Drivers(
                        rs.getInt("Id"),
                        rs.getString("dName"),
                        rs.getString("license_number"),
                        rs.getString("phone"),
                        rs.getString("nic"),
                        rs.getString("dStatus"),
                        rs.getObject("vehicle_id") != null ? rs.getInt("vehicle_id") : null
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Driver not found
    }
}
