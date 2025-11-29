package com.example;

public class Department {
    private int departmentId;
    private String name;
    private String phone;

    public Department() {}

    public Department(int departmentId, String name, String phone) {
        this.departmentId = departmentId;
        this.name = name;
        this.phone = phone;
    }

    public int getDepartmentId() { return departmentId; }
    public void setDepartmentId(int departmentId) { this.departmentId = departmentId; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    @Override
    public String toString() {
        return String.format("Department ID: %d, Name: %s, Phone: %s", departmentId, name, phone);
    }
}
