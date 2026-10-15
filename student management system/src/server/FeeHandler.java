package server;

import com.sun.net.httpserver.HttpExchange;
import model.Fee;
import model.Student;
import service.FeeService;
import service.StudentService;
import util.JsonUtil;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Routes handled here:
 *   GET /api/fees            -> every student's fee status (joined with name/course)
 *   PUT /api/fees/{studentId}-> set totalFee / paidAmount for one student
 */
public class FeeHandler {

    private final FeeService feeService;
    private final StudentService studentService;

    public FeeHandler(FeeService feeService, StudentService studentService) {
        this.feeService = feeService;
        this.studentService = studentService;
    }

    public void handle(HttpExchange exchange) {
        try {
            String suffix = exchange.getRequestURI().getPath().replaceFirst("^/api/fees/?", "");
            String method = exchange.getRequestMethod();

            if (method.equals("GET") && suffix.isEmpty()) {
                getAll(exchange);
            } else if (method.equals("PUT") && !suffix.isEmpty()) {
                update(exchange, suffix);
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

    private void getAll(HttpExchange exchange) throws IOException {
        List<Student> students = studentService.getAllStudents();
        String json = students.stream()
                .map(s -> {
                    Fee fee = feeService.getFee(s.getId());
                    return String.format(
                            "{\"studentId\":%d,\"name\":%s,\"course\":%s,%s}",
                            s.getId(), ResponseUtil.quote(s.getName()), ResponseUtil.quote(s.getCourse()), fee.toJsonFields()
                    );
                })
                .collect(Collectors.joining(",", "[", "]"));
        ResponseUtil.sendJson(exchange, 200, json);
    }

    private void update(HttpExchange exchange, String idStr) throws IOException {
        Map<String, String> body = JsonUtil.parseObject(ResponseUtil.readBody(exchange));
        try {
            int studentId = Integer.parseInt(idStr);
            double totalFee = Double.parseDouble(body.getOrDefault("totalFee", ""));
            double paidAmount = Double.parseDouble(body.getOrDefault("paidAmount", "0"));

            String result = feeService.setFee(studentId, totalFee, paidAmount);
            if (!result.equals("OK")) {
                int status = result.contains("No student found") ? 404 : 400;
                ResponseUtil.sendJson(exchange, status, ResponseUtil.errorJson(result));
                return;
            }
            ResponseUtil.sendJson(exchange, 200, "{\"message\":\"Fee updated.\"}");
        } catch (NumberFormatException e) {
            ResponseUtil.sendJson(exchange, 400, ResponseUtil.errorJson("totalFee and paidAmount must be numbers."));
        }
    }
}
