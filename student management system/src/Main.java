import model.Student;
import service.StudentService;

import java.util.List;
import java.util.Scanner;

/**
 * Console entry point for the Student Management System.
 * Pure Java SE 21, no external libraries -> just Run in VS Code.
 */
public class Main {

    private static final StudentService service = new StudentService();
    private static final Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        System.out.println("=== Student Management System ===");
        boolean running = true;

        while (running) {
            printMenu();
            int choice = readInt("Enter choice: ");

            switch (choice) {
                case 1 -> addStudent();
                case 2 -> viewAllStudents();
                case 3 -> searchStudent();
                case 4 -> updateStudent();
                case 5 -> deleteStudent();
                case 6 -> {
                    System.out.println("Data saved. Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid choice, please enter 1-6.");
            }
        }
        sc.close();
    }

    private static void printMenu() {
        String menu = """

                ------------------------------
                 1. Add Student
                 2. View All Students
                 3. Search Student by ID
                 4. Update Student
                 5. Delete Student
                 6. Exit
                ------------------------------""";
        System.out.println(menu);
    }

    private static void addStudent() {
        System.out.println("-- Add Student --");
        int id = readInt("Enter ID: ");
        String name = readLine("Enter Name: ");
        int age = readInt("Enter Age: ");
        String course = readLine("Enter Course: ");
        double cgpa = readDouble("Enter CGPA (0-10): ");

        System.out.println(service.addStudent(id, name, age, course, cgpa));
    }

    private static void viewAllStudents() {
        List<Student> students = service.getAllStudents();
        if (students.isEmpty()) {
            System.out.println("No students found.");
            return;
        }
        printTableHeader();
        for (Student s : students) {
            System.out.println(s);
        }
        System.out.println("Total: " + students.size() + " student(s).");
    }

    private static void searchStudent() {
        int id = readInt("Enter ID to search: ");
        Student s = service.searchStudent(id);
        if (s == null) {
            System.out.println("No student found with ID " + id + ".");
            return;
        }
        printTableHeader();
        System.out.println(s);
    }

    private static void updateStudent() {
        int id = readInt("Enter ID to update: ");
        if (service.searchStudent(id) == null) {
            System.out.println("No student found with ID " + id + ".");
            return;
        }
        System.out.println("Enter new details (existing record will be overwritten):");
        String name = readLine("Enter Name: ");
        int age = readInt("Enter Age: ");
        String course = readLine("Enter Course: ");
        double cgpa = readDouble("Enter CGPA (0-10): ");

        System.out.println(service.updateStudent(id, name, age, course, cgpa));
    }

    private static void deleteStudent() {
        int id = readInt("Enter ID to delete: ");
        System.out.println(service.deleteStudent(id));
    }

    private static void printTableHeader() {
        System.out.printf("%-6s %-20s %-5s %-15s %-5s%n", "ID", "Name", "Age", "Course", "CGPA");
    }

    // ---------- Small input helpers so bad input never crashes the app ----------

    private static String readLine(String prompt) {
        System.out.print(prompt);
        return sc.nextLine().trim();
    }

    private static int readInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int value = Integer.parseInt(sc.nextLine().trim());
                return value;
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid whole number.");
            }
        }
    }

    private static double readDouble(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                return Double.parseDouble(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number (e.g. 8.5).");
            }
        }
    }
}
