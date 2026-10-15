package server;

import com.sun.net.httpserver.HttpExchange;
import model.AttendanceRecord;
import service.AttendanceService;
import util.JsonUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Routes handled here:
 *   POST   /api/attendance                -> mark attendance
 *   GET    /api/attendance/student/{id}   -> a student's records + percentage
 *   DELETE /api/attendance/{id}           -> delete one record
 */
public class AttendanceHandler {

    private final AttendanceService service;

    public AttendanceHandler(AttendanceService service) {
        this.service = service;
    }

    public void handle(HttpExchange exchange) {
        try {
            String suffix = exchange.getRequestURI().getPath().replaceFirst("^/api/attendance/?", "");
            String method = exchange.getRequestMethod();

            if (method.equals("POST") && suffix.isEmpty()) {
                create(exchange);
            } else if (method.equals("GET") && suffix.startsWith("student/")) {
                getForStudent(exchange, suffix.substring("student/".length()));
            } else if (method.equals("DELETE") && !suffix.isEmpty()) {
                delete(exchange, suffix);
            } else {
                ResponseUtil.sendJson(exchange, 404, ResponseUtil.errorJson("Not found."));
            }
        } catch (Exception e) {
            try {
                ResponseUtil.sendJson(exchange, 500, ResponseUtil.errorJson("Server error: " + e.getMessage()));
            } catch (IOException ignored) {
            }
        }
    }

    private void create(HttpExchange exchange) throws IOException {
        Map<String, String> body = JsonUtil.parseObject(ResponseUtil.readBody(exchange));
        try {
            int studentId = Integer.parseInt(body.getOrDefault("studentId", ""));
            String subject = body.getOrDefault("subject", "");
            String date = body.getOrDefault("date", "");
            String status = body.getOrDefault("status", "");

            String result = service.markAttendance(studentId, subject, date, status);
            if (!result.equals("OK")) {
                ResponseUtil.sendJson(exchange, 400, ResponseUtil.errorJson(result));
                return;
            }
            ResponseUtil.sendJson(exchange, 201, "{\"message\":\"Attendance marked.\"}");
        } catch (NumberFormatException e) {
            ResponseUtil.sendJson(exchange, 400, ResponseUtil.errorJson("studentId must be a number."));
        }
    }

    private void getForStudent(HttpExchange exchange, String idStr) throws IOException {
        try {
            int studentId = Integer.parseInt(idStr);
            List<AttendanceRecord> records = service.getRecords(studentId);
            double percentage = service.getPercentage(studentId);
            long present = records.stream().filter(AttendanceRecord::isPresent).count();

            String recordsJson = records.stream().map(AttendanceRecord::toJson).collect(Collectors.joining(","));
            String json = String.format(
                    "{\"records\":[%s],\"totalClasses\":%d,\"present\":%d,\"percentage\":%.2f}",
                    recordsJson, records.size(), present, percentage
            );
            ResponseUtil.sendJson(exchange, 200, json);
        } catch (NumberFormatException e) {
            ResponseUtil.sendJson(exchange, 400, ResponseUtil.errorJson("Invalid student id."));
        }
    }

    private void delete(HttpExchange exchange, String idStr) throws IOException {
        try {
            int id = Integer.parseInt(idStr);
            if (service.delete(id)) {
                ResponseUtil.sendJson(exchange, 200, "{\"message\":\"Record deleted.\"}");
            } else {
                ResponseUtil.sendJson(exchange, 404, ResponseUtil.errorJson("Record not found."));
            }
        } catch (NumberFormatException e) {
            ResponseUtil.sendJson(exchange, 400, ResponseUtil.errorJson("Invalid record id."));
        }
    }
}
