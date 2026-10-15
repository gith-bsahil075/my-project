package server;

import com.sun.net.httpserver.HttpExchange;
import model.MarkRecord;
import service.MarksService;
import util.JsonUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Routes handled here:
 *   POST   /api/marks                -> add a subject's marks
 *   GET    /api/marks/student/{id}   -> a student's records + average + grade
 *   DELETE /api/marks/{id}           -> delete one record
 */
public class MarksHandler {

    private final MarksService service;

    public MarksHandler(MarksService service) {
        this.service = service;
    }

    public void handle(HttpExchange exchange) {
        try {
            String suffix = exchange.getRequestURI().getPath().replaceFirst("^/api/marks/?", "");
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
            double marksObtained = Double.parseDouble(body.getOrDefault("marksObtained", ""));
            double maxMarks = Double.parseDouble(body.getOrDefault("maxMarks", "100"));

            String result = service.addMarks(studentId, subject, marksObtained, maxMarks);
            if (!result.equals("OK")) {
                ResponseUtil.sendJson(exchange, 400, ResponseUtil.errorJson(result));
                return;
            }
            ResponseUtil.sendJson(exchange, 201, "{\"message\":\"Marks added.\"}");
        } catch (NumberFormatException e) {
            ResponseUtil.sendJson(exchange, 400, ResponseUtil.errorJson("studentId, marksObtained and maxMarks must be numbers."));
        }
    }

    private void getForStudent(HttpExchange exchange, String idStr) throws IOException {
        try {
            int studentId = Integer.parseInt(idStr);
            List<MarkRecord> records = service.getRecords(studentId);
            double avg = service.getAveragePercentage(studentId);
            String grade = service.getGrade(avg);

            String recordsJson = records.stream().map(MarkRecord::toJson).collect(Collectors.joining(","));
            String json = String.format(
                    "{\"records\":[%s],\"average\":%.2f,\"grade\":%s}",
                    recordsJson, avg, ResponseUtil.quote(grade)
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
