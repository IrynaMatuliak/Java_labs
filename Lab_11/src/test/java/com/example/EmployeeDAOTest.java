package com.example;

import org.junit.jupiter.api.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class EmployeeDAOTest {

    private EmployeeDAO employeeDAO;
    private DepartmentDAO departmentDAO;

    @BeforeAll
    public void setUp() {
        employeeDAO = new EmployeeDAO();
        departmentDAO = new DepartmentDAO();
    }

    @Test
    public void testGetAllEmployees() {
        List<Employee> employees = employeeDAO.getAllEmployees();

        assertNotNull(employees, "The employee list must not be null");
    }

    @Test
    public void testGetEmployeesByDepartment() {
        List<Employee> employees = employeeDAO.getEmployeesByDepartment(1);
        assertNotNull(employees, "The employee list must not be null");
    }

    @Test
    public void testGetEmployeesByNonExistentDepartment() {
        List<Employee> employees = employeeDAO.getEmployeesByDepartment(999);
        assertNotNull(employees, "The employee list must not be null");
        assertTrue(employees.isEmpty(), "For a non-existent department, the list should be empty");
    }

    @AfterAll
    public void tearDown() {
        DatabaseConnection.getInstance().closeConnection();
    }
}