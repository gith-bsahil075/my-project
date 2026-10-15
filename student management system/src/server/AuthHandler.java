package server;

import com.sun.net.httpserver.HttpExchange;
import service.AuthService;
import util.JsonUtil;

import java.io.IOException;
import java.util.Map;

public class AuthHandler {

    private final AuthService authService;

    public AuthHandler(AuthService authService) {
        this.authService = authService;
    }

    public void handleLogin(HttpExchange exchange) throws IOException {
        if (!"POST".equals(exchange.getRequestMethod())) {
            ResponseUtil.sendJson(exchange, 405, ResponseUtil.errorJson("Method not allowed."));
            return;
        }
        Map<String, String> body = JsonUtil.parseObject(ResponseUtil.readBody(exchange));
        String username = body.getOrDefault("username", "");
        String password = body.getOrDefault("password", "");

        String token = authService.login(username, password);
        if (token == null) {
            ResponseUtil.sendJson(exchange, 401, ResponseUtil.errorJson("Invalid username or password."));
            return;
        }

        // HttpOnly so client-side JS can't read/steal it; Path=/ so it's sent on every request.
        // (No "Secure" flag since this runs on plain http://localhost for local dev.)
        exchange.getResponseHeaders().add("Set-Cookie",
                AuthFilter.COOKIE_NAME + "=" + token + "; HttpOnly; Path=/; Max-Age=14400; SameSite=Lax");
        ResponseUtil.sendJson(exchange, 200, "{\"message\":\"Logged in successfully.\"}");
    }

    public void handleStudentLogin(HttpExchange exchange) throws IOException {
        if (!"POST".equals(exchange.getRequestMethod())) {
            ResponseUtil.sendJson(exchange, 405, ResponseUtil.errorJson("Method not allowed."));
            return;
        }
        Map<String, String> body = JsonUtil.parseObject(ResponseUtil.readBody(exchange));
        try {
            int studentId = Integer.parseInt(body.getOrDefault("studentId", ""));
            String password = body.getOrDefault("password", "");

            String token = authService.studentLogin(studentId, password);
            if (token == null) {
                ResponseUtil.sendJson(exchange, 401, ResponseUtil.errorJson("Invalid student ID or password."));
                return;
            }
            exchange.getResponseHeaders().add("Set-Cookie",
                    AuthFilter.COOKIE_NAME + "=" + token + "; HttpOnly; Path=/; Max-Age=14400; SameSite=Lax");
            ResponseUtil.sendJson(exchange, 200, "{\"message\":\"Logged in successfully.\"}");
        } catch (NumberFormatException e) {
            ResponseUtil.sendJson(exchange, 400, ResponseUtil.errorJson("Student ID must be a number."));
        }
    }

    public void handleLogout(HttpExchange exchange) throws IOException {
        String token = AuthFilter.extractToken(exchange);
        authService.logout(token);
        exchange.getResponseHeaders().add("Set-Cookie",
                AuthFilter.COOKIE_NAME + "=; HttpOnly; Path=/; Max-Age=0");
        ResponseUtil.sendJson(exchange, 200, "{\"message\":\"Logged out.\"}");
    }

    /** Reaching this handler at all means AuthFilter already validated the session. */
    public void handleSessionCheck(HttpExchange exchange) throws IOException {
        ResponseUtil.sendJson(exchange, 200, "{\"authenticated\":true}");
    }
}
