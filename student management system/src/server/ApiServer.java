package server;

import com.sun.net.httpserver.HttpContext;
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.SimpleFileServer;
import dao.PhotoDAO;
import dao.StudentDAOImpl;
import service.AttendanceService;
import service.AuthService;
import service.DashboardService;
import service.FeeService;
import service.MarksService;
import service.StudentService;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.file.Path;

/**
 * Wires together the HTTP server: static file hosting for the browser
 * frontend (/public) plus a REST API, all using classes built into the
 * JDK (com.sun.net.httpserver) -- no Spring Boot / Maven / external
 * dependency needed to run this project.
 *
 * One StudentDAOImpl is created here and shared across every service,
 * so a student added/edited/deleted through one module is immediately
 * visible to the others (e.g. Attendance checking a student still exists).
 *
 * Two session roles exist:
 *   ADMIN   -> full access, via AuthFilter        (/api/students, /api/attendance, ...)
 *   STUDENT -> read-only access to their own data, via StudentAuthFilter (/api/me/*)
 */
public class ApiServer {

    private final int port;

    public ApiServer(int port) {
        this.port = port;
    }

    public void start(Path publicDir) throws IOException {
        StudentDAOImpl studentDAO = new StudentDAOImpl();

        StudentService studentService = new StudentService(studentDAO);
        AttendanceService attendanceService = new AttendanceService(studentDAO);
        MarksService marksService = new MarksService(studentDAO);
        FeeService feeService = new FeeService(studentDAO);
        AuthService authService = new AuthService(studentDAO);
        PhotoDAO photoDAO = new PhotoDAO();

        AuthHandler authHandler = new AuthHandler(authService);
        StudentHandler studentHandler = new StudentHandler(studentService, photoDAO, authService);
        AttendanceHandler attendanceHandler = new AttendanceHandler(attendanceService);
        MarksHandler marksHandler = new MarksHandler(marksService);
        FeeHandler feeHandler = new FeeHandler(feeService, studentService);
        StudentPortalHandler portalHandler = new StudentPortalHandler(
                studentService, attendanceService, marksService, feeService, photoDAO);
        DashboardService dashboardService = new DashboardService(studentService, attendanceService, marksService, feeService);
        DashboardHandler dashboardHandler = new DashboardHandler(dashboardService);

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        // Frontend (HTML/CSS/JS) -- public, no login required to load the files themselves.
        server.createContext("/", SimpleFileServer.createFileHandler(publicDir));

        // Auth endpoints -- public (you need to be able to log in while logged out).
        server.createContext("/api/login", authHandler::handleLogin);
        server.createContext("/api/student-login", authHandler::handleStudentLogin);
        server.createContext("/api/logout", authHandler::handleLogout);

        // Admin-only routes.
        protect(server.createContext("/api/session", authHandler::handleSessionCheck), new AuthFilter());
        protect(server.createContext("/api/students", studentHandler::handle), new AuthFilter());
        protect(server.createContext("/api/attendance", attendanceHandler::handle), new AuthFilter());
        protect(server.createContext("/api/marks", marksHandler::handle), new AuthFilter());
        protect(server.createContext("/api/fees", feeHandler::handle), new AuthFilter());
        protect(server.createContext("/api/dashboard/stats", dashboardHandler::handle), new AuthFilter());

        // Student-only routes (a student can only ever see their own data).
        protect(server.createContext("/api/me", portalHandler::handle), new StudentAuthFilter());

        server.setExecutor(null);
        server.start();

        System.out.println("Server running -> http://localhost:" + port);
        System.out.println("Press Ctrl+C in this terminal to stop the server.");
    }

    private void protect(HttpContext context, com.sun.net.httpserver.Filter filter) {
        context.getFilters().add(filter);
    }
}
