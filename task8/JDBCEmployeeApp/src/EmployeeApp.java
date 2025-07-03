package src;
import java.sql.*;
import java.util.Scanner;

public class EmployeeApp {
    static final String DB_URL = "jdbc:mysql://localhost:3306/employee_db";
    static final String USER = "root";
    static final String PASS = "password"; 

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS)) {
            System.out.println("Connected to database!");

            while (true) {
                System.out.println("\n=== Employee Database Menu ===");
                System.out.println("1. Add Employee");
                System.out.println("2. View Employees");
                System.out.println("3. Update Employee");
                System.out.println("4. Delete Employee");
                System.out.println("5. Exit");
                System.out.print("Choose an option: ");
                int choice = scanner.nextInt();
                scanner.nextLine();

                switch (choice) {
                    case 1:
                        addEmployee(conn, scanner);
                        break;
                    case 2:
                        viewEmployees(conn);
                        break;
                    case 3:
                        updateEmployee(conn, scanner);
                        break;
                    case 4:
                        deleteEmployee(conn, scanner);
                        break;
                    case 5:
                        System.out.println("Exiting...");
                        return;
                    default:
                        System.out.println("Invalid option.");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    static void addEmployee(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter name: ");
        String name = scanner.nextLine();
        System.out.print("Enter department: ");
        String dept = scanner.nextLine();
        System.out.print("Enter salary: ");
        double salary = scanner.nextDouble();

        String sql = "INSERT INTO employees (name, department, salary) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, dept);
            stmt.setDouble(3, salary);
            stmt.executeUpdate();
            System.out.println("Employee added!");
        }
    }

    static void viewEmployees(Connection conn) throws SQLException {
        String sql = "SELECT * FROM employees";
        try (Statement stmt = conn.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            System.out.println("\n--- Employee List ---");
            while (rs.next()) {
                System.out.printf("ID: %d | Name: %s | Dept: %s | Salary: %.2f\n",
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getString("department"),
                        rs.getDouble("salary"));
            }
        }
    }

    static void updateEmployee(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter employee ID to update: ");
        int id = scanner.nextInt();
        scanner.nextLine(); 
        System.out.print("Enter new name: ");
        String name = scanner.nextLine();
        System.out.print("Enter new department: ");
        String dept = scanner.nextLine();
        System.out.print("Enter new salary: ");
        double salary = scanner.nextDouble();

        String sql = "UPDATE employees SET name=?, department=?, salary=? WHERE id=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setString(2, dept);
            stmt.setDouble(3, salary);
            stmt.setInt(4, id);
            int rowsUpdated = stmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Employee updated!");
            } else {
                System.out.println("Employee ID not found.");
            }
        }
    }

    static void deleteEmployee(Connection conn, Scanner scanner) throws SQLException {
        System.out.print("Enter employee ID to delete: ");
        int id = scanner.nextInt();

        String sql = "DELETE FROM employees WHERE id=?";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, id);
            int rowsDeleted = stmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Employee deleted.");
            } else {
                System.out.println("Employee ID not found.");
            }
        }
    }
}
