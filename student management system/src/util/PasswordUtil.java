package util;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

/**
 * Password hashing using PBKDF2WithHmacSHA256 (built into the JDK's
 * javax.crypto package -- no external library needed).
 * Each password gets its own random salt, so two identical passwords
 * never produce the same stored hash.
 */
public final class PasswordUtil {

    private static final int ITERATIONS = 65536;
    private static final int KEY_LENGTH_BITS = 256;
    private static final int SALT_LENGTH_BYTES = 16;

    private PasswordUtil() {
    }

    public static String generateSaltBase64() {
        byte[] salt = new byte[SALT_LENGTH_BYTES];
        new SecureRandom().nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    public static String hash(String password, String saltBase64) {
        byte[] salt = Base64.getDecoder().decode(saltBase64);
        try {
            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_LENGTH_BITS);
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hashBytes = factory.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
            throw new RuntimeException("Password hashing failed", e);
        }
    }

    /** Constant-time comparison so verification time can't leak the correct hash. */
    public static boolean matches(String password, String saltBase64, String expectedHashBase64) {
        String actualHash = hash(password, saltBase64);
        return MessageDigest.isEqual(
                actualHash.getBytes(),
                expectedHashBase64.getBytes()
        );
    }
}
