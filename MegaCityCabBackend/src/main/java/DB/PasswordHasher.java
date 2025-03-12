/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DB;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author ravin
 */
public class PasswordHasher {

//    public static void main(String[] args) {
//        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
//        String adminPassword = encoder.encode("admin123");
//        String userPassword = encoder.encode("customer123");
//
//        System.out.println("Admin Hashed Password: " + adminPassword);
//        System.out.println("Customer Hashed Password: " + userPassword);
//    }
    // Singleton encoder instance (best practice)
    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // ✅ Hash the password
    public static String hashPassword(String password) {
        return encoder.encode(password);
    }

    // ✅ Check password match
    public static boolean checkPassword(String rawPassword, String hashedPassword) {
        return encoder.matches(rawPassword, hashedPassword);
    }
}
