package dao;

import util.PasswordUtil;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.SecureRandom;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Student portal logins: one row per student in data/student_logins.txt
 * as: studentId,salt,hash
 * A student's username IS their student ID -- there's no separate field
 * to manage. Passwords are generated (not chosen), so admins hand them
 * out; a student can't sign themselves up.
 */
public class StudentCredentialDAO {

    private record Credential(String salt, String hash) {
    }

    private static final String FILE_PATH = "data" + File.separator + "student_logins.txt";
    private static final String PASSWORD_CHARS = "ABCDEFGHJKLMNPQRSTUVWXYZabcdefghjkmnpqrstuvwxyz23456789";
    private static final int PASSWORD_LENGTH = 8;

    private final Map<Integer, Credential> credentials;

    public StudentCredentialDAO() {
        credentials = new LinkedHashMap<>();
        load();
    }

    /** Creates (or resets) a student's login and returns the new PLAINTEXT password to show the admin once. */
    public String generatePassword(int studentId) {
        String plainPassword = randomPassword();
        String salt = PasswordUtil.generateSaltBase64();
        String hash = PasswordUtil.hash(plainPassword, salt);
        credentials.put(studentId, new Credential(salt, hash));
        save();
        return plainPassword;
    }

    public boolean verify(int studentId, String password) {
        Credential c = credentials.get(studentId);
        if (c == null || password == null) {
            return false;
        }
        return PasswordUtil.matches(password, c.salt(), c.hash());
    }

    public boolean hasLogin(int studentId) {
        return credentials.containsKey(studentId);
    }

    public void deleteLogin(int studentId) {
        if (credentials.remove(studentId) != null) {
            save();
        }
    }

    private String randomPassword() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(PASSWORD_LENGTH);
        for (int i = 0; i < PASSWORD_LENGTH; i++) {
            sb.append(PASSWORD_CHARS.charAt(random.nextInt(PASSWORD_CHARS.length())));
        }
        return sb.toString();
    }

    private void save() {
        File dir = new File("data");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        try (PrintWriter writer = new PrintWriter(new FileWriter(FILE_PATH))) {
            for (Map.Entry<Integer, Credential> entry : credentials.entrySet()) {
                writer.println(entry.getKey() + "," + entry.getValue().salt() + "," + entry.getValue().hash());
            }
        } catch (IOException e) {
            System.out.println("Could not save student login data: " + e.getMessage());
        }
    }

    private void load() {
        File file = new File(FILE_PATH);
        if (!file.exists()) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isBlank()) {
                    continue;
                }
                String[] parts = line.split(",", 3);
                credentials.put(Integer.parseInt(parts[0].trim()), new Credential(parts[1].trim(), parts[2].trim()));
            }
        } catch (IOException | NumberFormatException e) {
            System.out.println("Could not load student login data: " + e.getMessage());
        }
    }
}
