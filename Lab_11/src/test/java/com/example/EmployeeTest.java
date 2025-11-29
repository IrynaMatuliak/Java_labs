package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class EmployeeTest {

    @Test
    public void testEmployeeCreation() {
        Employee employee = new Employee(1, "Smith", "John", "Developer", 1);

        assertEquals(1, employee.getEmployeeId());
        assertEquals("Smith", employee.getLastName());
        assertEquals("John", employee.getFirstName());
        assertEquals("Developer", employee.getPosition());
        assertEquals(1, employee.getDepartmentId());
    }

    @Test
    public void testEmployeeSetters() {
        Employee employee = new Employee();
        employee.setEmployeeId(2);
        employee.setLastName("Johnson");
        employee.setFirstName("Mary");
        employee.setPosition("Manager");
        employee.setDepartmentId(2);

        assertEquals(2, employee.getEmployeeId());
        assertEquals("Johnson", employee.getLastName());
        assertEquals("Mary", employee.getFirstName());
        assertEquals("Manager", employee.getPosition());
        assertEquals(2, employee.getDepartmentId());
    }

    @Test
    public void testEmployeeToString() {
        Employee employee = new Employee(1, "Smith", "John", "Developer", 1);
        String expected = "Employee ID: 1, Smith John, Position: Developer, Department ID: 1";

        assertEquals(expected, employee.toString());
    }
}