/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DB;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 *
 * @author ravin
 */
public class PasswordHasher {

    private static final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    // Method to hash a password
    public static String hashPassword(String password) {
        return encoder.encode(password);
    }

    // Method to verify a password
    public static boolean verifyPassword(String rawPassword, String hashedPassword) {
        return encoder.matches(rawPassword, hashedPassword);
    }

    public static void main(String[] args) {
        // Example: Hashing a password
        String plainPassword = "password123";
        String hashedPassword = hashPassword(plainPassword);

        System.out.println("Original: " + plainPassword);
        System.out.println("Hashed: " + hashedPassword);

        // Example: Verifying password
        System.out.println("Password Match: " + verifyPassword(plainPassword, hashedPassword));
        
        System.out.println(hashPassword("admin123"));

    }
}
