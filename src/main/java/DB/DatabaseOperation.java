/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author ravin
 */
public class DatabaseOperation {
    private static final String URL = "jdbc:mysql://localhost:3306/mega_city_cabs?useSSL=false&allowPublicKeyRetrieval=true";
    private static final String USER = "root"; // Change as needed
    private static final String PASSWORD = "12345678"; // Change as needed
    private static Connection connection;

    // Establishing Database Connection
    public static Connection connect() {
        try {
            if (connection == null || connection.isClosed()) {
                Class.forName("com.mysql.cj.jdbc.Driver");
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✅ Database Connected Successfully!");
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            System.err.println("❌ Database Connection Failed!");
        }
        return connection;
    }

    // Close Connection
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println("🔌 Database Connection Closed.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Execute Insert, Update, Delete Queries
    public static boolean executeUpdate(String query, Object... params) {
        try (PreparedStatement pstmt = connect().prepareStatement(query)) {
            setParameters(pstmt, params);
            int affectedRows = pstmt.executeUpdate();
            return affectedRows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Execute Select Queries
    public static ResultSet executeQuery(String query, Object... params) {
        try {
            PreparedStatement pstmt = connect().prepareStatement(query);
            setParameters(pstmt, params);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    // Set Query Parameters Dynamically
    private static void setParameters(PreparedStatement pstmt, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            pstmt.setObject(i + 1, params[i]);
        }
    }

    // Test Connection
    public static void main(String[] args) {
        connect(); // Check if connection works
        closeConnection(); // Close after testing
    }
}
