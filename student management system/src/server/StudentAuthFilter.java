package server;

import com.sun.net.httpserver.Filter;
import com.sun.net.httpserver.HttpExchange;
import util.SessionManager;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

/**
 * Blocks any request that doesn't carry a valid STUDENT session cookie.
 * On success, stashes the session's studentId as an exchange attribute
 * so StudentPortalHandler can read it with:
 *   (Integer) exchange.getAttribute("studentId")
 */
public class StudentAuthFilter extends Filter {

    @Override
    public String description() {
        return "Requires a valid student login session";
    }

    @Override
    public void doFilter(HttpExchange exchange, Chain chain) throws IOException {
        String token = AuthFilter.extractToken(exchange);
        SessionManager.Session session = SessionManager.getSession(token);
        if (session != null && session.role() == SessionManager.Role.STUDENT) {
            exchange.setAttribute("studentId", session.studentId());
            chain.doFilter(exchange);
            return;
        }
        String json = "{\"error\":\"Not logged in as a student. Please log in again.\"}";
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(401, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
}
