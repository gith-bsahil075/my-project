package dao;

import util.PasswordUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Stores a single admin account in data/admin.txt as: username,salt,hash
 * On first run (no file yet) a default admin is created:
 *   username: admin   password: admin123
 * (Documented in the README -- change it by deleting data/admin.txt and
 * restarting the server, which recreates the default account.)
 */
public class AdminDAO {

    private static final String FILE_PATH = "data" + File.separator + "admin.txt";
    private static final String DEFAULT_USERNAME = "admin";
    private static final String DEFAULT_PASSWORD = "admin123";

    private String username;
    private String salt;
    private String hash;

    public AdminDAO() {
        load();
    }

    /** Verifies a login attempt against the stored admin account. */
    public boolean verify(String usernameAttempt, String passwordAttempt) {
        if (usernameAttempt == null || passwordAttempt == null) {
            return false;
        }
        return usernameAttempt.equals(username) && PasswordUtil.matches(passwordAttempt, salt, hash);
    }

    private void load() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            createDefaultAdmin();
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line = reader.readLine();
            if (line == null || line.isBlank()) {
                createDefaultAdmin();
                return;
            }
            String[] parts = line.split(",", 3);
            username = parts[0];
            salt = parts[1];
            hash = parts[2];
        } catch (IOException e) {
            System.out.println("Could not read admin credentials, recreating default admin: " + e.getMessage());
            createDefaultAdmin();
        }
    }

    private void createDefaultAdmin() {
        username = DEFAULT_USERNAME;
        salt = PasswordUtil.generateSaltBase64();
        hash = PasswordUtil.hash(DEFAULT_PASSWORD, salt);
        save();
        System.out.println("No admin account found -> created default login: "
                + DEFAULT_USERNAME + " / " + DEFAULT_PASSWORD + " (change this later).");
    }

    private void save() {
        File dir = new File("data");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try (FileWriter writer = new FileWriter(FILE_PATH)) {
            writer.write(username + "," + salt + "," + hash);
        } catch (IOException e) {
            System.out.println("Could not save admin credentials: " + e.getMessage());
        }
    }
}
