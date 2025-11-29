package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DepartmentTest {

    @Test
    public void testDepartmentCreation() {
        Department department = new Department(1, "IT department", "+380501234567");

        assertEquals(1, department.getDepartmentId());
        assertEquals("IT department", department.getName());
        assertEquals("+380501234567", department.getPhone());
    }

    @Test
    public void testDepartmentSetters() {
        Department department = new Department();
        department.setDepartmentId(2);
        department.setName("Sales department");
        department.setPhone("+380501234568");

        assertEquals(2, department.getDepartmentId());
        assertEquals("Sales department", department.getName());
        assertEquals("+380501234568", department.getPhone());
    }

    @Test
    public void testDepartmentToString() {
        Department department = new Department(1, "IT department", "+380501234567");
        String expected = "Department ID: 1, Name: IT department, Phone: +380501234567";

        assertEquals(expected, department.toString());
    }
}