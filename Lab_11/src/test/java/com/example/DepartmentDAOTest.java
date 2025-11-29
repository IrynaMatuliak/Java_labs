package com.example;

import org.junit.jupiter.api.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class DepartmentDAOTest {

    private DepartmentDAO departmentDAO;

    @BeforeAll
    public void setUp() {
        departmentDAO = new DepartmentDAO();
    }

    @Test
    public void testGetAllDepartments() {
        List<Department> departments = departmentDAO.getAllDepartments();

        assertNotNull(departments, "The list of departments must not be null");
        assertFalse(departments.isEmpty(), "The list of departments must not be empty");

        for (Department department : departments) {
            assertTrue(department.getDepartmentId() > 0, "Department ID must be greater than 0");
            assertNotNull(department.getName(), "Department name must not be null");
            assertFalse(department.getName().isEmpty(), "Department name must not be empty");
        }
    }

    @AfterAll
    public void tearDown() {
        DatabaseConnection.getInstance().closeConnection();
    }
}
