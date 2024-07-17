import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public class LoginManager {
    private static final String USER_DB_FILE = "users.txt";
    private PasswordHasher passwordHasher = new PasswordHasher();

    public void registerUser(String username, String password, String role) {
        String hashedPassword = passwordHasher.hashPassword(password);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USER_DB_FILE, true))) {
            writer.write(username + ":" + hashedPassword + ":" + role + "\n");
        } catch (IOException e) {
            System.err.println("Error registering user: " + e.getMessage());
        }
    }

    public boolean login(String username, String password) {
        try (BufferedReader reader = new BufferedReader(new FileReader(USER_DB_FILE))) {
            String line;
            while ((line = reader.readLine())!= null) {
                String[] parts = line.split(":");
                if (parts.length == 3 && parts[0].equals(username)) {
                    String storedHash = parts[1];
                    String salt = storedHash.split(":")[1];
                    String hashedPassword = passwordHasher.hashPassword(password, salt);
                    if (hashedPassword.equals(storedHash)) {
                        return true; // Login successful
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error logging in: " + e.getMessage());
        }
        return false; // Login failed
    }
}

class PasswordHasher {
    private MessageDigest getMessageDigest() {
        try {
            return MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    public String hashPassword(String password) {
        String salt = generateSalt();
        return hashPassword(password, salt);
    }

    public String hashPassword(String password, String salt) {
        String passwordWithSalt = password + salt;
        MessageDigest digest = getMessageDigest();
        byte[] passwordBytes = passwordWithSalt.getBytes();
        byte[] hashBytes = digest.digest(passwordBytes);

        String hashBase64 = Base64.getEncoder().encodeToString(hashBytes);

        return hashBase64 + ":" + salt;
    }

    private String generateSalt() {
        // Generate a random salt (e.g., using SecureRandom)
        // For simplicity, we'll use a fixed salt for now
        return "mySalt";
    }
}