package com.example;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static EmployeeDAO employeeDAO;
    private static TaskDAO taskDAO;
    private static DepartmentDAO departmentDAO;
    private static Scanner scanner;

    public static void main(String[] args) {
        employeeDAO = new EmployeeDAO();
        taskDAO = new TaskDAO();
        departmentDAO = new DepartmentDAO();
        scanner = new Scanner(System.in);

        while (true) {
            printMenu();
            int choice = getIntInput("Choose an option: ");
            switch (choice) {
                case 1:
                    showAllEmployees();
                    break;
                case 2:
                    showAllTasks();
                    break;
                case 3:
                    showEmployeesByDepartment();
                    break;
                case 4:
                    addTaskForEmployee();
                    break;
                case 5:
                    showTasksByEmployee();
                    break;
                case 6:
                    deleteEmployee();
                    break;
                case 7:
                    showAllDepartments();
                    break;
                case 0:
                    DatabaseConnection.getInstance().closeConnection();
                    scanner.close();
                    return;
                default:
                    System.out.println("Incorrect choice. Try again.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\n--- Menu ---");
        System.out.println("1. Get a list of all employees");
        System.out.println("2. Get a list of all tasks");
        System.out.println("3. Get a list of employees in the specified department");
        System.out.println("4. Add a task for an employee");
        System.out.println("5. Get a list of employee tasks");
        System.out.println("6. Delete employee");
        System.out.println("7. Show all departments");
        System.out.println("0. Exit");
    }

    private static void showAllEmployees() {
        System.out.println("\n--- All employees ---");
        List<Employee> employees = employeeDAO.getAllEmployees();
        if (employees.isEmpty()) {
            System.out.println("No employees found.");
        } else {
            employees.forEach(System.out::println);
        }
    }

    private static void showAllTasks() {
        System.out.println("\n--- All tasks ---");
        List<Task> tasks = taskDAO.getAllTasks();
        if (tasks.isEmpty()) {
            System.out.println("No tasks found.");
        } else {
            tasks.forEach(System.out::println);
        }
    }

    private static void showEmployeesByDepartment() {
        System.out.println("\n--- Employees by department ---");
        showAllDepartments();
        int departmentId = getIntInput("Enter department ID: ");

        List<Employee> employees = employeeDAO.getEmployeesByDepartment(departmentId);
        if (employees.isEmpty()) {
            System.out.println("No employees found in this department.");
        } else {
            employees.forEach(System.out::println);
        }
    }

    private static void addTaskForEmployee() {
        System.out.println("\n--- Add a task ---");
        showAllEmployees();

        int employeeId = getIntInput("Enter employee ID: ");
        System.out.print("Enter a task description: ");
        scanner.nextLine();
        String description = scanner.nextLine();

        Task task = new Task(0, description, employeeId);
        boolean success = taskDAO.addTask(task);

        if (success) {
            System.out.println("Task successfully added!");
        } else {
            System.out.println("Error adding task.");
        }
    }

    private static void showTasksByEmployee() {
        System.out.println("\n--- Employee tasks ---");
        showAllEmployees();

        int employeeId = getIntInput("Enter employee ID: ");
        List<Task> tasks = taskDAO.getTasksByEmployee(employeeId);

        if (tasks.isEmpty()) {
            System.out.println("No tasks found for this employee.");
        } else {
            tasks.forEach(System.out::println);
        }
    }

    private static void deleteEmployee() {
        System.out.println("\n--- Delete employee ---");
        showAllEmployees();

        int employeeId = getIntInput("Enter the employee ID to delete: ");
        boolean success = employeeDAO.deleteEmployee(employeeId);

        if (success) {
            System.out.println("Employee successfully deleted!");
        } else {
            System.out.println("Error while deleting employee.");
        }
    }

    private static void showAllDepartments() {
        System.out.println("\n--- All departments ---");
        List<Department> departments = departmentDAO.getAllDepartments();
        if (departments.isEmpty()) {
            System.out.println("No departments found.");
        } else {
            departments.forEach(System.out::println);
        }
    }

    private static int getIntInput(String message) {
        System.out.print(message);
        while (!scanner.hasNextInt()) {
            System.out.print("Please enter a number: ");
            scanner.next();
        }
        int input = scanner.nextInt();
        return input;
    }
}