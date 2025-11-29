package com.example;

import org.junit.jupiter.api.*;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TaskDAOTest {

    private TaskDAO taskDAO;
    private EmployeeDAO employeeDAO;

    @BeforeAll
    public void setUp() {
        taskDAO = new TaskDAO();
        employeeDAO = new EmployeeDAO();
    }

    @Test
    public void testGetAllTasks() {
        List<Task> tasks = taskDAO.getAllTasks();

        assertNotNull(tasks, "Task list must not be null");
    }

    @Test
    public void testGetTasksByEmployee() {
        List<Task> tasks = taskDAO.getTasksByEmployee(1);
        assertNotNull(tasks, "Task list must not be null");
    }

    @Test
    public void testGetTasksByNonExistentEmployee() {
        List<Task> tasks = taskDAO.getTasksByEmployee(999);
        assertNotNull(tasks, "Task list must not be null");
        assertTrue(tasks.isEmpty(), "For a non-existent employee, the list should be empty.");
    }

    @Test
    public void testAddTask() {
        Task task = new Task(0, "Тестове завдання", 1);
        boolean result = taskDAO.addTask(task);
        assertTrue(result, "Завдання повинно бути успішно додане");
    }

    @AfterAll
    public void tearDown() {
        DatabaseConnection.getInstance().closeConnection();
    }
}