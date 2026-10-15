package server;

import com.sun.net.httpserver.HttpExchange;
import service.DashboardService;

import java.io.IOException;
import java.util.Map;
import java.util.stream.Collectors;

public class DashboardHandler {

    private final DashboardService service;

    public DashboardHandler(DashboardService service) {
        this.service = service;
    }

    public void handle(HttpExchange exchange) {
        try {
            if (!"GET".equals(exchange.getRequestMethod())) {
                ResponseUtil.sendJson(exchange, 405, ResponseUtil.errorJson("Method not allowed."));
                return;
            }

            String growthJson = mapToJson(service.studentGrowthByMonth());
            String attendanceJson = mapToJson(service.attendanceOverview());
            String subjectJson = mapToJsonDouble(service.subjectWiseAverage());

            String json = String.format(
                    "{\"totalStudents\":%d,\"presentToday\":%d,\"feesCollected\":%.2f,\"feesDue\":%.2f," +
                            "\"averageMarks\":%.2f,\"studentGrowth\":%s,\"attendanceOverview\":%s,\"subjectMarks\":%s}",
                    service.totalStudents(), service.presentToday(), service.feesCollected(), service.feesDue(),
                    service.averageMarks(), growthJson, attendanceJson, subjectJson
            );
            ResponseUtil.sendJson(exchange, 200, json);
        } catch (Exception e) {
            try {
                ResponseUtil.sendJson(exchange, 500, ResponseUtil.errorJson("Server error: " + e.getMessage()));
            } catch (IOException ignored) {
            }
        }
    }

    private String mapToJson(Map<String, Long> map) {
        return map.entrySet().stream()
                .map(e -> ResponseUtil.quote(e.getKey()) + ":" + e.getValue())
                .collect(Collectors.joining(",", "{", "}"));
    }

    private String mapToJsonDouble(Map<String, Double> map) {
        return map.entrySet().stream()
                .map(e -> ResponseUtil.quote(e.getKey()) + ":" + e.getValue())
                .collect(Collectors.joining(",", "{", "}"));
    }
}
