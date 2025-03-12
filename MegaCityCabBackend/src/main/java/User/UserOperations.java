/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package User;

import DB.DatabaseOperation;
import DB.PasswordHasher;
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

    // ✅ Register User (Admin & Customer)
    public static boolean registerUser(Users user) {
        String sql = "INSERT INTO users (username, email, phone, nic, pword, urole, address) VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conn = DatabaseOperation.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPhone());
            stmt.setString(4, user.getNic());
            stmt.setString(5, PasswordHasher.hashPassword(user.getPword())); // ✅ HASH here
            stmt.setString(6, user.getUrole());
            stmt.setString(7, user.getAddress());

            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // ✅ User Login (Admin & Customer)
    public static Users loginUser(String email, String password) {
        String sql = "SELECT * FROM users WHERE email = ?";

        try (Connection conn = DatabaseOperation.connect(); PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, email);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String hashedPassword = rs.getString("pword");
                if (PasswordHasher.checkPassword(password, hashedPassword)) { // ✅ Check password
                    return new Users(
                            rs.getInt("Id"),
                            rs.getString("username"),
                            rs.getString("email"),
                            rs.getString("phone"),
                            rs.getString("nic"),
                            null, // Do not return password
                            rs.getString("urole"),
                            rs.getString("address")
                    );
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ✅ Fetch All Users
    public static List<Users> getAllUsers() {
        List<Users> userList = new ArrayList<>();
        String query = "SELECT * FROM users";

        try (Connection conn = DatabaseOperation.connect(); Statement stmt = conn.createStatement(); ResultSet rs = stmt.executeQuery(query)) {
            while (rs.next()) {
                Users user = new Users(
                        rs.getInt("Id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("nic"),
                        rs.getString("pword"),
                        rs.getString("urole"),
                        rs.getString("address")
                );
                userList.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return userList;
    }

    // ✅ Get User by ID
    public static Users getUserById(int id) {
        String query = "SELECT * FROM users WHERE Id = ?";
        try (Connection conn = DatabaseOperation.connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Users(
                        rs.getInt("Id"),
                        rs.getString("username"),
                        rs.getString("email"),
                        rs.getString("phone"),
                        rs.getString("nic"),
                        rs.getString("pword"),
                        rs.getString("urole"),
                        rs.getString("address")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    // ✅ Delete User by ID
    public static boolean deleteUser(int id) {
        String query = "DELETE FROM users WHERE Id = ?";
        try (Connection conn = DatabaseOperation.connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static boolean updateUser(int id, Users user) {
        String query = "UPDATE users SET username=?, email=?, phone=?, nic=?, address=? WHERE Id=?";
        try (Connection conn = DatabaseOperation.connect(); PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getEmail());
            stmt.setString(3, user.getPhone());
            stmt.setString(4, user.getNic());
            stmt.setString(5, user.getAddress());
            stmt.setInt(6, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
