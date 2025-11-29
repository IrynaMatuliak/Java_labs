package com.example;

public class Employee {
    private int employeeId;
    private String lastName;
    private String firstName;
    private String position;
    private int departmentId;

    public Employee() {}

    public Employee(int employeeId, String lastName, String firstName, String position, int departmentId) {
        this.employeeId = employeeId;
        this.lastName = lastName;
        this.firstName = firstName;
        this.position = position;
        this.departmentId = departmentId;
    }

    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }

    public String getLastName() { return lastName; }
    public void setLastName(String lastName) { this.lastName = lastName; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }

    public int getDepartmentId() { return departmentId; }
    public void setDepartmentId(int departmentId) { this.departmentId = departmentId; }

    @Override
    public String toString() {
        return String.format("Employee ID: %d, %s %s, Position: %s, Department ID: %d", employeeId, lastName, firstName, position, departmentId);
    }
}