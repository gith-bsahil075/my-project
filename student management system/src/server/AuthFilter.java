package server;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import util.SessionManager;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.List;

/**
 * Blocks any request that doesn't carry a valid ADMIN session cookie.
 * (Student portal routes use StudentAuthFilter instead.)
 */
public class AuthFilter extends Filter {

    public static final String COOKIE_NAME = "session";

    @Override
    public String description() {
        return "Requires a valid admin login session";
    }

    @Override
    public void doFilter(HttpExchange exchange, Chain chain) throws IOException {
        String token = extractToken(exchange);
        SessionManager.Session session = SessionManager.getSession(token);
        if (session != null && session.role() == SessionManager.Role.ADMIN) {
            chain.doFilter(exchange);
            return;
        }
        String json = "{\"error\":\"Not logged in as admin. Please log in again.\"}";
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(401, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    public static String extractToken(HttpExchange exchange) {
        List<String> cookieHeaders = exchange.getRequestHeaders().get("Cookie");
        if (cookieHeaders == null) {
            return null;
        }
        for (String headerLine : cookieHeaders) {
            for (String pair : headerLine.split(";")) {
                String[] kv = pair.trim().split("=", 2);
                if (kv.length == 2 && kv[0].equals(COOKIE_NAME)) {
                    return kv[1];
                }
            }
        }
        return null;
    }
}
