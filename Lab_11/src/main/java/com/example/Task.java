package com.example;

public class Task {
    private int taskId;
    private String taskDescription;
    private int employeeId;

    public Task() {}

    public Task(int taskId, String taskDescription, int employeeId) {
        this.taskId = taskId;
        this.taskDescription = taskDescription;
        this.employeeId = employeeId;
    }

    public int getTaskId() { return taskId; }
    public void setTaskId(int taskId) { this.taskId = taskId; }

    public String getTaskDescription() { return taskDescription; }
    public void setTaskDescription(String taskDescription) { this.taskDescription = taskDescription; }

    public int getEmployeeId() { return employeeId; }
    public void setEmployeeId(int employeeId) { this.employeeId = employeeId; }

    @Override
    public String toString() {
        return String.format("Task ID: %d, Description: %s, Employee ID: %d", taskId, taskDescription, employeeId);
    }
}
