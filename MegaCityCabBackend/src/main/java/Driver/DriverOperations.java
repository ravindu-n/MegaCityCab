/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Driver;

import DB.DatabaseOperation;
import DB.PasswordHasher;
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
public class DriverOperations {

    // ✅ Register Driver
    public static boolean registerDriver(Drivers driver) {
        // ✅ Check if vehicle is already assigned (if vehicle is selected)
        if (driver.getVehicleId() != null && isVehicleAssigned(driver.getVehicleId())) {
            System.out.println("❌ Vehicle already assigned to another driver.");
            return false;
        }

        String sql = "INSERT INTO drivers (dName, license_number, phone, nic, pword, dStatus, vehicle_id) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseOperation.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, driver.getdName());
            stmt.setString(2, driver.getLicenseNumber());
            stmt.setString(3, driver.getPhone());
            stmt.setString(4, driver.getNic());
            stmt.setString(5, PasswordHasher.hashPassword(driver.getPword())); // ✅ Hashed password
            stmt.setString(6, "Available"); // ✅ Set default status to 'Available'
            if (driver.getVehicleId() != null) {
                stmt.setInt(7, driver.getVehicleId());
            } else {
                stmt.setNull(7, Types.INTEGER);
            }

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /// ✅ Corrected Driver Login by Driver Name and Password
    public static Drivers loginDriver(String dName, String password) {
        String sql = "SELECT * FROM drivers WHERE dName = ?";

        try (Connection conn = DatabaseOperation.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, dName);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String hashedPassword = rs.getString("pword");
                if (PasswordHasher.checkPassword(password, hashedPassword)) {
                    return new Drivers(
                            rs.getInt("Id"),
                            rs.getString("dName"),
                            rs.getString("license_number"),
                            rs.getString("phone"),
                            rs.getString("nic"),
                            null, // Don't return password
                            rs.getString("dStatus"),
                            rs.getObject("vehicle_id") != null ? rs.getInt("vehicle_id") : null
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Keep this for debugging in console
        }
        return null; // Failed login
    }

    // ✅ Get All Drivers
    public static List<Drivers> getAllDrivers() {
        List<Drivers> driverList = new ArrayList<>();
        String sql = "SELECT * FROM drivers";

        try (Connection conn = DatabaseOperation.connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                driverList.add(new Drivers(
                        rs.getInt("Id"),
                        rs.getString("dName"),
                        rs.getString("license_number"),
                        rs.getString("phone"),
                        rs.getString("nic"),
                        null, // Don't return password
                        rs.getString("dStatus"),
                        rs.getObject("vehicle_id") != null ? rs.getInt("vehicle_id") : null
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return driverList;
    }

    // ✅ Get Driver by ID
    public static Drivers getDriverById(int id) {
        String sql = "SELECT * FROM drivers WHERE Id = ?";
        try (Connection conn = DatabaseOperation.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Drivers(
                        rs.getInt("Id"),
                        rs.getString("dName"),
                        rs.getString("license_number"),
                        rs.getString("phone"),
                        rs.getString("nic"),
                        null,
                        rs.getString("dStatus"),
                        rs.getObject("vehicle_id") != null ? rs.getInt("vehicle_id") : null
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ✅ Delete Driver
    public static boolean deleteDriver(int id) {
        String sql = "DELETE FROM drivers WHERE Id = ?";
        try (Connection conn = DatabaseOperation.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ Check if vehicle is already assigned to any driver
    public static boolean isVehicleAssigned(int vehicleId) {
        String sql = "SELECT COUNT(*) FROM drivers WHERE vehicle_id = ?";

        try (Connection conn = DatabaseOperation.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, vehicleId);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                int count = rs.getInt(1);
                return count > 0; // ✅ Return true if assigned
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // ✅ Not assigned
    }

}
