package service;

import dao.FeeDAO;
import model.AttendanceRecord;
import model.Fee;
import model.MarkRecord;
import model.Student;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Pulls numbers together from the existing services for the dashboard --
 * it doesn't own any storage itself, just aggregates what's already there.
 */
public class DashboardService {

    private final StudentService studentService;
    private final AttendanceService attendanceService;
    private final MarksService marksService;
    private final FeeService feeService;

    public DashboardService(StudentService studentService, AttendanceService attendanceService,
                             MarksService marksService, FeeService feeService) {
        this.studentService = studentService;
        this.attendanceService = attendanceService;
        this.marksService = marksService;
        this.feeService = feeService;
    }

    public int totalStudents() {
        return studentService.getAllStudents().size();
    }

    /** Distinct students with at least one "Present" record dated today. */
    public long presentToday() {
        String today = LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE);
        return attendanceService.getAllRecords().stream()
                .filter(r -> r.isPresent() && today.equals(r.getDate()))
                .map(AttendanceRecord::getStudentId)
                .distinct()
                .count();
    }

    public double feesCollected() {
        return feeService.getAllFees().values().stream().mapToDouble(Fee::getPaidAmount).sum();
    }

    public double feesDue() {
        return feeService.getAllFees().values().stream().mapToDouble(Fee::getDueAmount).sum();
    }

    /** Weighted average percentage across every marks record ever entered. */
    public double averageMarks() {
        List<MarkRecord> all = marksService.getAllRecords();
        double totalObtained = all.stream().mapToDouble(MarkRecord::getMarksObtained).sum();
        double totalMax = all.stream().mapToDouble(MarkRecord::getMaxMarks).sum();
        return totalMax == 0 ? 0.0 : Math.round((totalObtained / totalMax) * 10000.0) / 100.0;
    }

    /** Students registered per month (yyyy-MM), oldest first. Students from before this
     *  feature existed (createdAt == 0) are grouped under "Earlier". */
    public Map<String, Long> studentGrowthByMonth() {
        DateTimeFormatter monthFormat = DateTimeFormatter.ofPattern("yyyy-MM");
        Map<String, Long> result = new TreeMap<>();
        for (Student s : studentService.getAllStudents()) {
            String bucket;
            if (s.getCreatedAt() <= 0) {
                bucket = "Earlier";
            } else {
                bucket = Instant.ofEpochMilli(s.getCreatedAt()).atZone(ZoneId.systemDefault()).format(monthFormat);
            }
            result.merge(bucket, 1L, Long::sum);
        }
        return result;
    }

    public Map<String, Long> attendanceOverview() {
        List<AttendanceRecord> all = attendanceService.getAllRecords();
        long present = all.stream().filter(AttendanceRecord::isPresent).count();
        long absent = all.size() - present;
        Map<String, Long> result = new LinkedHashMap<>();
        result.put("Present", present);
        result.put("Absent", absent);
        return result;
    }

    /** Average percentage per subject, across all students who have marks for it. */
    public Map<String, Double> subjectWiseAverage() {
        Map<String, double[]> totals = new LinkedHashMap<>(); // subject -> [obtainedSum, maxSum]
        for (MarkRecord r : marksService.getAllRecords()) {
            totals.computeIfAbsent(r.getSubject(), k -> new double[2]);
            double[] pair = totals.get(r.getSubject());
            pair[0] += r.getMarksObtained();
            pair[1] += r.getMaxMarks();
        }
        Map<String, Double> result = new LinkedHashMap<>();
        for (Map.Entry<String, double[]> entry : totals.entrySet()) {
            double[] pair = entry.getValue();
            double pct = pair[1] == 0 ? 0.0 : Math.round((pair[0] / pair[1]) * 10000.0) / 100.0;
            result.put(entry.getKey(), pct);
        }
        return result;
    }
}
