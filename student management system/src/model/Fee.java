package model;

/** One student's fee record. Due amount and status are derived, not stored. */
public class Fee {

    private int studentId;
    private double totalFee;
    private double paidAmount;

    public Fee(int studentId, double totalFee, double paidAmount) {
        this.studentId = studentId;
        this.totalFee = totalFee;
        this.paidAmount = paidAmount;
    }

    public int getStudentId() {
        return studentId;
    }

    public double getTotalFee() {
        return totalFee;
    }

    public double getPaidAmount() {
        return paidAmount;
    }

    public double getDueAmount() {
        return Math.max(0, totalFee - paidAmount);
    }

    public String getStatus() {
        if (totalFee <= 0) return "Not Set";
        if (paidAmount >= totalFee) return "Paid";
        if (paidAmount > 0) return "Partial";
        return "Due";
    }

    public String toJsonFields() {
        // Fields only (no braces) -- the API layer wraps this together with student name/course.
        return String.format(
                "\"totalFee\":%.2f,\"paidAmount\":%.2f,\"dueAmount\":%.2f,\"status\":%s",
                totalFee, paidAmount, getDueAmount(), JsonSupport.quote(getStatus())
        );
    }

    public String toFileString() {
        return studentId + "," + totalFee + "," + paidAmount;
    }

    public static Fee fromFileString(String line) {
        String[] p = line.split(",", -1);
        return new Fee(Integer.parseInt(p[0].trim()), Double.parseDouble(p[1].trim()), Double.parseDouble(p[2].trim()));
    }
}
