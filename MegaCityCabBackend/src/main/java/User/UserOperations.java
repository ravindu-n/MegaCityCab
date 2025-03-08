/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package User;

import DB.DatabaseOperation;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author ravin
 */
public class UserOperations {

    private static final BCryptPasswordEncoder encoder;

    // Static Block for Initializing BCryptPasswordEncoder
    static {
        BCryptPasswordEncoder tempEncoder = null;
        try {
            tempEncoder = new BCryptPasswordEncoder();
            System.out.println("✅ BCryptPasswordEncoder initialized successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
        encoder = tempEncoder;
    }

    // ✅ Create - Add New User (Supports Customer, Admin, Driver)
    public static int addAccount(Users user) {
        String query = "INSERT INTO users (email, username, pword, urole, address, phone, nic, license_number, dStatus, vehicle_id) " +
                       "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseOperation.connect();
             PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getUsername());
            stmt.setString(3, encoder.encode(user.getPword())); // ✅ Encrypt Password
            stmt.setString(4, user.getUrole());
            stmt.setString(5, user.getAddress());
            stmt.setString(6, user.getPhone());
            stmt.setString(7, user.getNic());

            // ✅ Driver-Specific Fields
            if ("Driver".equalsIgnoreCase(user.getUrole())) {
                stmt.setString(8, user.getLicenseNumber()); 
                stmt.setString(9, user.getDStatus());  
                if (user.getVehicleId() != null) {
                    stmt.setInt(10, user.getVehicleId());
                } else {
                    stmt.setNull(10, java.sql.Types.INTEGER);
                }
            } else {
                stmt.setNull(8, java.sql.Types.VARCHAR);
                stmt.setNull(9, java.sql.Types.VARCHAR);
                stmt.setNull(10, java.sql.Types.INTEGER);
            }

            stmt.executeUpdate();

            // ✅ Get Generated User ID
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Error case
    }

    // ✅ Read - Get All Users (Including Drivers)
    public static List<Users> getAllAccounts() {
        List<Users> userList = new ArrayList<>();
        String query = "SELECT * FROM users";

        try (Connection conn = DatabaseOperation.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                userList.add(new Users(
                        rs.getInt("Id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("nic"),
                        null, // Don't return password for security
                        rs.getString("urole"),
                        rs.getString("address"),
                        rs.getString("license_number"),
                        rs.getString("dStatus"),
                        rs.getObject("vehicle_id") != null ? rs.getInt("vehicle_id") : null
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }

    // ✅ Get User By ID
    public static Users getUserById(int id) {
        String query = "SELECT * FROM users WHERE Id = ?";

        try (Connection conn = DatabaseOperation.connect();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Users(
                        rs.getInt("Id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("nic"),
                        null, // Don't return password for security
                        rs.getString("urole"),
                        rs.getString("address"),
                        rs.getString("license_number"),
                        rs.getString("dStatus"),
                        rs.getObject("vehicle_id") != null ? rs.getInt("vehicle_id") : null
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // User not found
    }

    // ✅ Delete - Remove User
    public static int deleteAccount(int id) {
        String query = "DELETE FROM users WHERE Id=?";
        try (Connection conn = DatabaseOperation.connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Error case
    }

    // ✅ Validate Login (Now supports Admin, Customer, and Driver)
    public static Users validateLogin(String email, String rawPassword) {
        String query = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = DatabaseOperation.connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String storedPassword = rs.getString("pword");
                if (storedPassword != null && encoder.matches(rawPassword, storedPassword)) {
                    return new Users(
                            rs.getInt("Id"),
                            rs.getString("username"),
                            rs.getString("email"),
                            rs.getString("phone"),
                            rs.getString("nic"),
                            rs.getString("pword"),
                            rs.getString("urole"),
                            rs.getString("address"),
                            null, // License Number (new field, but missing in constructor)
                            rs.getString("dStatus"), // Driver status (new field, but missing)
                            rs.getInt("vehicle_id") // Vehicle ID (new field, but missing)
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}