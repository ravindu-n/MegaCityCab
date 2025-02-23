/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Vehicle;

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
public class VehicleOperations {
    // ✅ Add a New Vehicle
    public static int addVehicle(Vehicles vehicle) {
        String query = "INSERT INTO vehicles (model, make_year, license_plate, vType, capacity, vStatus) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseOperation.connect();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, vehicle.getModel());
            stmt.setInt(2, vehicle.getMakeYear());
            stmt.setString(3, vehicle.getLicensePlate());
            stmt.setString(4, vehicle.getType());
            stmt.setInt(5, vehicle.getCapacity());
            stmt.setString(6, vehicle.getStatus());

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

    // ✅ Retrieve All Vehicles
    public static List<Vehicles> getAllVehicles() {
        List<Vehicles> vehicleList = new ArrayList<>();
        String query = "SELECT * FROM vehicles";
        try (Connection conn = DatabaseOperation.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                vehicleList.add(new Vehicles(
                    rs.getInt("Id"),
                    rs.getString("model"),
                    rs.getInt("make_year"),
                    rs.getString("license_plate"),
                    rs.getString("vType"),
                    rs.getInt("capacity"),
                    rs.getString("vStatus")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return vehicleList;
    }

    // ✅ Delete a Vehicle by ID
    public static int deleteVehicle(int id) {
        String query = "DELETE FROM vehicles WHERE Id=?";
        try (Connection conn = DatabaseOperation.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Error case
    }

    // ✅ Update Vehicle Status (Available, Booked, Maintenance)
    public static boolean updateVehicleStatus(int id, String status) {
        String query = "UPDATE vehicles SET status=? WHERE Id=?";
        try (Connection conn = DatabaseOperation.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, status);
            stmt.setInt(2, id);
            int updatedRows = stmt.executeUpdate();
            return updatedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false; // Error case
    }
}
