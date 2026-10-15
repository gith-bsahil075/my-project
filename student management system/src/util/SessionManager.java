package util;

import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * In-memory session store, now role-aware:
 *   - ADMIN sessions can manage every student.
 *   - STUDENT sessions are tied to exactly one studentId and can only
 *     view that student's own data (enforced by StudentAuthFilter).
 * Sessions live only as long as the server process runs.
 */
public final class SessionManager {

    public enum Role { ADMIN, STUDENT }

    public record Session(Role role, Integer studentId, Instant expiry) {
        public boolean isExpired() {
            return Instant.now().isAfter(expiry);
        }
    }

    private static final Duration SESSION_DURATION = Duration.ofHours(4);
    private static final Map<String, Session> sessions = new ConcurrentHashMap<>();

    private SessionManager() {
    }

    public static String createAdminSession() {
        String token = UUID.randomUUID().toString();
        sessions.put(token, new Session(Role.ADMIN, null, Instant.now().plus(SESSION_DURATION)));
        return token;
    }

    public static String createStudentSession(int studentId) {
        String token = UUID.randomUUID().toString();
        sessions.put(token, new Session(Role.STUDENT, studentId, Instant.now().plus(SESSION_DURATION)));
        return token;
    }

    /** Returns the session for a token, or null if missing/expired. */
    public static Session getSession(String token) {
        if (token == null) {
            return null;
        }
        Session session = sessions.get(token);
        if (session == null || session.isExpired()) {
            return null;
        }
        return session;
    }

    public static boolean isValid(String token) {
        return getSession(token) != null;
    }

    public static void invalidate(String token) {
        if (token != null) {
            sessions.remove(token);
        }
    }
}
