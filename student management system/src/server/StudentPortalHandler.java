package server;

import com.sun.net.httpserver.HttpExchange;
import model.AttendanceRecord;
import model.Fee;
import model.MarkRecord;
import model.Student;
import service.AttendanceService;
import service.FeeService;
import service.MarksService;
import service.StudentService;
import dao.PhotoDAO;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Self-service routes for a logged-in STUDENT session (protected by
 * StudentAuthFilter, which stashes the session's studentId on the
 * exchange -- every method here reads that instead of trusting anything
 * in the URL, so a student can only ever see their own data.
 *
 *   GET /api/me            -> own profile
 *   GET /api/me/photo      -> own photo
 *   GET /api/me/attendance -> own attendance records + percentage
 *   GET /api/me/marks      -> own marks records + average + grade
 *   GET /api/me/fees       -> own fee status
 */
public class StudentPortalHandler {

    private final StudentService studentService;
    private final AttendanceService attendanceService;
    private final MarksService marksService;
    private final FeeService feeService;
    private final PhotoDAO photoDAO;

    public StudentPortalHandler(StudentService studentService, AttendanceService attendanceService,
                                 MarksService marksService, FeeService feeService, PhotoDAO photoDAO) {
        this.studentService = studentService;
        this.attendanceService = attendanceService;
        this.marksService = marksService;
        this.feeService = feeService;
        this.photoDAO = photoDAO;
    }

    public void handle(HttpExchange exchange) {
        try {
            int studentId = (Integer) exchange.getAttribute("studentId");
            String suffix = exchange.getRequestURI().getPath().replaceFirst("^/api/me/?", "");

            if (!"GET".equals(exchange.getRequestMethod())) {
                ResponseUtil.sendJson(exchange, 405, ResponseUtil.errorJson("Method not allowed."));
                return;
            }

            switch (suffix) {
                case "" -> getProfile(exchange, studentId);
                case "photo" -> servePhoto(exchange, studentId);
                case "attendance" -> getAttendance(exchange, studentId);
                case "marks" -> getMarks(exchange, studentId);
                case "fees" -> getFees(exchange, studentId);
                default -> ResponseUtil.sendJson(exchange, 404, ResponseUtil.errorJson("Not found."));
            }
        } catch (Exception e) {
            try {
                ResponseUtil.sendJson(exchange, 500, ResponseUtil.errorJson("Server error: " + e.getMessage()));
            } catch (IOException ignored) {
            }
        }
    }

    private void getProfile(HttpExchange exchange, int studentId) throws IOException {
        Student s = studentService.searchStudent(studentId);
        if (s == null) {
            ResponseUtil.sendJson(exchange, 404, ResponseUtil.errorJson("Student record not found."));
            return;
        }
        String base = s.toJson();
        String json = base.substring(0, base.length() - 1) + ",\"hasPhoto\":" + photoDAO.hasPhoto(studentId) + "}";
        ResponseUtil.sendJson(exchange, 200, json);
    }

    private void servePhoto(HttpExchange exchange, int studentId) throws IOException {
        byte[] bytes = photoDAO.getPhotoBytes(studentId);
        if (bytes == null) {
            ResponseUtil.sendJson(exchange, 404, ResponseUtil.errorJson("No photo uploaded."));
            return;
        }
        exchange.getResponseHeaders().set("Content-Type", photoDAO.getMimeType(studentId));
        exchange.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private void getAttendance(HttpExchange exchange, int studentId) throws IOException {
        List<AttendanceRecord> records = attendanceService.getRecords(studentId);
        double percentage = attendanceService.getPercentage(studentId);
        long present = records.stream().filter(AttendanceRecord::isPresent).count();
        String recordsJson = records.stream().map(AttendanceRecord::toJson).collect(Collectors.joining(","));
        String json = String.format(
                "{\"records\":[%s],\"totalClasses\":%d,\"present\":%d,\"percentage\":%.2f}",
                recordsJson, records.size(), present, percentage
        );
        ResponseUtil.sendJson(exchange, 200, json);
    }

    private void getMarks(HttpExchange exchange, int studentId) throws IOException {
        List<MarkRecord> records = marksService.getRecords(studentId);
        double avg = marksService.getAveragePercentage(studentId);
        String grade = marksService.getGrade(avg);
        String recordsJson = records.stream().map(MarkRecord::toJson).collect(Collectors.joining(","));
        String json = String.format(
                "{\"records\":[%s],\"average\":%.2f,\"grade\":%s}",
                recordsJson, avg, ResponseUtil.quote(grade)
        );
        ResponseUtil.sendJson(exchange, 200, json);
    }

    private void getFees(HttpExchange exchange, int studentId) throws IOException {
        Fee fee = feeService.getFee(studentId);
        String json = "{" + fee.toJsonFields() + "}";
        ResponseUtil.sendJson(exchange, 200, json);
    }
}
