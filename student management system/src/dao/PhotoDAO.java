package dao;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

/**
 * Stores each student's profile photo as two small files:
 *   data/photos/{id}.bin   -> the raw image bytes
 *   data/photos/{id}.type  -> the MIME type (e.g. "image/jpeg")
 * The frontend uploads photos as base64 data URLs (e.g.
 * "data:image/jpeg;base64,...") inside a normal JSON request, so no
 * multipart/form-data parser is needed -- it reuses the same JSON
 * plumbing as the rest of the API.
 */
public class PhotoDAO {

    private static final String PHOTOS_DIR = "data" + File.separator + "photos";
    private static final int MAX_BYTES = 3 * 1024 * 1024; // 3 MB safety cap

    public void savePhoto(int studentId, String dataUrl) throws IOException {
        if (dataUrl == null || !dataUrl.startsWith("data:")) {
            throw new IllegalArgumentException("Photo must be a data URL (data:image/...;base64,...).");
        }
        int commaIndex = dataUrl.indexOf(',');
        if (commaIndex < 0) {
            throw new IllegalArgumentException("Malformed image data.");
        }

        String header = dataUrl.substring(5, commaIndex); // strips "data:", keeps "image/jpeg;base64"
        String mimeType = header.contains(";") ? header.substring(0, header.indexOf(';')) : header;
        if (!mimeType.startsWith("image/")) {
            throw new IllegalArgumentException("Only image files are allowed.");
        }

        byte[] bytes;
        try {
            bytes = Base64.getDecoder().decode(dataUrl.substring(commaIndex + 1));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Could not decode image data.");
        }
        if (bytes.length > MAX_BYTES) {
            throw new IllegalArgumentException("Photo is too large (max 3 MB).");
        }

        File dir = new File(PHOTOS_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        Files.write(Path.of(PHOTOS_DIR, studentId + ".bin"), bytes);
        Files.writeString(Path.of(PHOTOS_DIR, studentId + ".type"), mimeType);
    }

    public byte[] getPhotoBytes(int studentId) throws IOException {
        Path path = Path.of(PHOTOS_DIR, studentId + ".bin");
        if (!Files.exists(path)) {
            return null;
        }
        return Files.readAllBytes(path);
    }

    public String getMimeType(int studentId) throws IOException {
        Path path = Path.of(PHOTOS_DIR, studentId + ".type");
        if (!Files.exists(path)) {
            return "application/octet-stream";
        }
        return Files.readString(path).trim();
    }

    public boolean hasPhoto(int studentId) {
        return Files.exists(Path.of(PHOTOS_DIR, studentId + ".bin"));
    }

    public void deletePhoto(int studentId) {
        try {
            Files.deleteIfExists(Path.of(PHOTOS_DIR, studentId + ".bin"));
            Files.deleteIfExists(Path.of(PHOTOS_DIR, studentId + ".type"));
        } catch (IOException ignored) {
            // best-effort cleanup
        }
    }
}
