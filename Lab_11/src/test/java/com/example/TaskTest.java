package com.example;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TaskTest {

    @Test
    public void testTaskCreation() {
        Task task = new Task(1, "Develop a new module", 1);

        assertEquals(1, task.getTaskId());
        assertEquals("Develop a new module", task.getTaskDescription());
        assertEquals(1, task.getEmployeeId());
    }

    @Test
    public void testTaskSetters() {
        Task task = new Task();
        task.setTaskId(2);
        task.setTaskDescription("Prepare a report");
        task.setEmployeeId(2);

        assertEquals(2, task.getTaskId());
        assertEquals("Prepare a report", task.getTaskDescription());
        assertEquals(2, task.getEmployeeId());
    }

    @Test
    public void testTaskToString() {
        Task task = new Task(1, "Develop a new module", 1);
        String expected = "Task ID: 1, Description: Develop a new module, Employee ID: 1";

        assertEquals(expected, task.toString());
    }
}