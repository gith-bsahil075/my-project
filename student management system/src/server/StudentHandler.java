package server;

import com.sun.net.httpserver.HttpExchange;
import dao.PhotoDAO;
import model.Student;
import service.AuthService;
import service.StudentService;
import util.JsonUtil;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Routes handled here:
 *   GET/POST   /api/students                    -> list / create
 *   GET/PUT/DELETE /api/students/{id}            -> one student
 *   GET/POST   /api/students/{id}/photo          -> profile photo (view / upload)
 *   POST       /api/students/{id}/generate-login -> (re)issue a student portal password
 */
public class StudentHandler {

    private final StudentService service;
    private final PhotoDAO photoDAO;
    private final AuthService authService;

    public StudentHandler(StudentService service, PhotoDAO photoDAO, AuthService authService) {
        this.service = service;
        this.photoDAO = photoDAO;
        this.authService = authService;
    }

    public void handle(HttpExchange exchange) {
        try {
            String path = exchange.getRequestURI().getPath();
            String suffix = path.replaceFirst("^/api/students/?", "");

            if (suffix.endsWith("/photo")) {
                String idStr = suffix.substring(0, suffix.length() - "/photo".length());
                handlePhoto(exchange, idStr);
                return;
            }

            if (suffix.endsWith("/generate-login")) {
                String idStr = suffix.substring(0, suffix.length() - "/generate-login".length());
                handleGenerateLogin(exchange, idStr);
                return;
            }

            Integer id = null;
            if (!suffix.isBlank()) {
                try {
                    id = Integer.parseInt(suffix);
                } catch (NumberFormatException e) {
                    ResponseUtil.sendJson(exchange, 400, ResponseUtil.errorJson("Invalid student id in URL: " + suffix));
                    return;
                }
            }

            switch (exchange.getRequestMethod()) {
                case "GET" -> {
                    if (id == null) getAll(exchange);
                    else getOne(exchange, id);
                }
                case "POST" -> create(exchange);
                case "PUT" -> {
                    if (id == null) ResponseUtil.sendJson(exchange, 400, ResponseUtil.errorJson("Student id required in URL for update."));
                    else update(exchange, id);
                }
                case "DELETE" -> {
                    if (id == null) ResponseUtil.sendJson(exchange, 400, ResponseUtil.errorJson("Student id required in URL for delete."));
                    else delete(exchange, id);
                }
                default -> ResponseUtil.sendJson(exchange, 405, ResponseUtil.errorJson("Method not allowed."));
            }
        } catch (Exception e) {
            try {
                ResponseUtil.sendJson(exchange, 500, ResponseUtil.errorJson("Server error: " + e.getMessage()));
            } catch (IOException ignored) {
            }
        }
    }

    private void getAll(HttpExchange exchange) throws IOException {
        List<Student> students = service.getAllStudents();
        String json = students.stream().map(this::withPhotoFlag).collect(Collectors.joining(",", "[", "]"));
        ResponseUtil.sendJson(exchange, 200, json);
    }

    private void getOne(HttpExchange exchange, int id) throws IOException {
        Student s = service.searchStudent(id);
        if (s == null) {
            ResponseUtil.sendJson(exchange, 404, ResponseUtil.errorJson("No student found with id " + id + "."));
        } else {
            ResponseUtil.sendJson(exchange, 200, withPhotoFlag(s));
        }
    }

    private void create(HttpExchange exchange) throws IOException {
        Map<String, String> body = JsonUtil.parseObject(ResponseUtil.readBody(exchange));
        try {
            String name = body.getOrDefault("name", "");
            int age = Integer.parseInt(body.getOrDefault("age", ""));
            String course = body.getOrDefault("course", "");
            double cgpa = Double.parseDouble(body.getOrDefault("cgpa", ""));

            int newId = service.nextAvailableId();
            String result = service.addStudent(newId, name, age, course, cgpa);
            if (result.startsWith("Error")) {
                ResponseUtil.sendJson(exchange, 400, ResponseUtil.errorJson(result));
                return;
            }
            ResponseUtil.sendJson(exchange, 201, withPhotoFlag(service.searchStudent(newId)));
        } catch (NumberFormatException e) {
            ResponseUtil.sendJson(exchange, 400, ResponseUtil.errorJson("Age and CGPA must be valid numbers."));
        }
    }

    private void update(HttpExchange exchange, int id) throws IOException {
        Map<String, String> body = JsonUtil.parseObject(ResponseUtil.readBody(exchange));
        try {
            String name = body.getOrDefault("name", "");
            int age = Integer.parseInt(body.getOrDefault("age", ""));
            String course = body.getOrDefault("course", "");
            double cgpa = Double.parseDouble(body.getOrDefault("cgpa", ""));

            String result = service.updateStudent(id, name, age, course, cgpa);
            if (result.startsWith("Error")) {
                int status = result.contains("No student found") ? 404 : 400;
                ResponseUtil.sendJson(exchange, status, ResponseUtil.errorJson(result));
                return;
            }
            ResponseUtil.sendJson(exchange, 200, withPhotoFlag(service.searchStudent(id)));
        } catch (NumberFormatException e) {
            ResponseUtil.sendJson(exchange, 400, ResponseUtil.errorJson("Age and CGPA must be valid numbers."));
        }
    }

    private void delete(HttpExchange exchange, int id) throws IOException {
        String result = service.deleteStudent(id);
        if (result.startsWith("Error")) {
            ResponseUtil.sendJson(exchange, 404, ResponseUtil.errorJson(result));
        } else {
            photoDAO.deletePhoto(id); // clean up their photo along with the record
            authService.deleteStudentLogin(id); // and their portal login, if any
            ResponseUtil.sendJson(exchange, 200, "{\"message\":\"Student deleted successfully.\"}");
        }
    }

    private void handleGenerateLogin(HttpExchange exchange, String idStr) throws IOException {
        if (!"POST".equals(exchange.getRequestMethod())) {
            ResponseUtil.sendJson(exchange, 405, ResponseUtil.errorJson("Method not allowed."));
            return;
        }
        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            ResponseUtil.sendJson(exchange, 400, ResponseUtil.errorJson("Invalid student id."));
            return;
        }
        String password = authService.generateStudentLogin(id);
        if (password == null) {
            ResponseUtil.sendJson(exchange, 404, ResponseUtil.errorJson("No student found with id " + id + "."));
            return;
        }
        String json = String.format(
                "{\"studentId\":%d,\"password\":%s,\"message\":\"Login created. Share these credentials with the student -- they won't be shown again.\"}",
                id, ResponseUtil.quote(password)
        );
        ResponseUtil.sendJson(exchange, 200, json);
    }

    // ---------- Profile photo ----------

    private void handlePhoto(HttpExchange exchange, String idStr) throws IOException {
        int id;
        try {
            id = Integer.parseInt(idStr);
        } catch (NumberFormatException e) {
            ResponseUtil.sendJson(exchange, 400, ResponseUtil.errorJson("Invalid student id."));
            return;
        }
        if (service.searchStudent(id) == null) {
            ResponseUtil.sendJson(exchange, 404, ResponseUtil.errorJson("No student found with id " + id + "."));
            return;
        }

        switch (exchange.getRequestMethod()) {
            case "GET" -> servePhoto(exchange, id);
            case "POST" -> uploadPhoto(exchange, id);
            default -> ResponseUtil.sendJson(exchange, 405, ResponseUtil.errorJson("Method not allowed."));
        }
    }

    private void servePhoto(HttpExchange exchange, int id) throws IOException {
        byte[] bytes = photoDAO.getPhotoBytes(id);
        if (bytes == null) {
            ResponseUtil.sendJson(exchange, 404, ResponseUtil.errorJson("No photo uploaded for this student."));
            return;
        }
        exchange.getResponseHeaders().set("Content-Type", photoDAO.getMimeType(id));
        exchange.getResponseHeaders().set("Cache-Control", "no-cache");
        exchange.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }

    private void uploadPhoto(HttpExchange exchange, int id) throws IOException {
        Map<String, String> body = JsonUtil.parseObject(ResponseUtil.readBody(exchange));
        String dataUrl = body.get("photo");
        try {
            photoDAO.savePhoto(id, dataUrl);
            ResponseUtil.sendJson(exchange, 200, "{\"message\":\"Photo uploaded.\"}");
        } catch (IllegalArgumentException e) {
            ResponseUtil.sendJson(exchange, 400, ResponseUtil.errorJson(e.getMessage()));
        }
    }

    private String withPhotoFlag(Student s) {
        String base = s.toJson(); // ends with "}"
        boolean hasPhoto = photoDAO.hasPhoto(s.getId());
        boolean hasLogin = authService.studentHasLogin(s.getId());
        return base.substring(0, base.length() - 1)
                + ",\"hasPhoto\":" + hasPhoto
                + ",\"hasLogin\":" + hasLogin + "}";
    }
}
