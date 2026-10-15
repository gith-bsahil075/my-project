import server.ApiServer;

import java.io.IOException;
import java.nio.file.Path;

/**
 * Entry point for the WEB version of the Student Management System.
 * Starts the built-in HTTP server (backend) which also serves the
 * browser UI (frontend) from the /public folder.
 *
 * Run this class, then open http://localhost:8080 in a browser.
 *
 * (Main.java still exists separately as the console version — both
 * share the same model/dao layer underneath, just through different
 * Service instances.)
 */
public class ServerMain {

    // Cloud hosts (Render, Railway, etc.) inject a PORT env var and expect
    // the app to listen on it. Locally, nothing sets PORT, so it falls
    // back to 8080 -> http://localhost:8080 keeps working exactly as before.
    private static final int PORT = Integer.parseInt(
            System.getenv().getOrDefault("PORT", "8080"));

    public static void main(String[] args) throws IOException {
        Path publicDir = Path.of("public").toAbsolutePath();

        if (!publicDir.toFile().exists()) {
            System.out.println("Could not find the 'public' folder at: " + publicDir);
            System.out.println("Make sure you run this from the project's root folder.");
            return;
        }

        new ApiServer(PORT).start(publicDir);
    }
}
