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

    // ✅ Create - Add New User
    public static int addAccount(Users user) {
        String query = "INSERT INTO users (email, username, pword, urole, name, address, phone, nic) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseOperation.connect(); // Fix: Changed from DBConnection to DatabaseOperation
                 PreparedStatement stmt = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getEmail());
            stmt.setString(2, user.getUsername());
            stmt.setString(3, encoder.encode(user.getPword()));  // Encrypt Password
            stmt.setString(4, user.getUrole());
            stmt.setString(7, user.getPhone());
            stmt.setString(8, user.getNic());
            stmt.executeUpdate();

            // Get Generated User ID
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1; // Error case
    }

    // ✅ Read - Get All Users (Fixed Column Mapping)
    public static List<Users> getAllAccounts() {
        List<Users> userList = new ArrayList<>();
        String query = "SELECT * FROM users";
        try (Connection conn = DatabaseOperation.connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                userList.add(new Users(
                        rs.getInt("Id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("nic"),
                        rs.getString("pword"),
                        rs.getString("urole"),
                        rs.getString("address")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
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

    // ✅ Validate Login
    public static Users validateLogin(String email, String rawPassword) { // Fix: Changed return type from User to Users
        String query = "SELECT * FROM users WHERE email = ?";
        try (Connection conn = DatabaseOperation.connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String storedPassword = rs.getString("pword");  // Fix: Ensure correct column name
                if (storedPassword != null && encoder.matches(rawPassword, storedPassword)) {
                    return new Users(
                            rs.getInt("Id"),
                            rs.getString("username"),
                            rs.getString("email"),
                            rs.getString("phone"),
                            rs.getString("nic"), // Correct field placement
                            null, // Do not return password for security
                            rs.getString("urole"), // ✅ Fetch correct role
                            rs.getString("address")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null; // Login failed
    }
}
