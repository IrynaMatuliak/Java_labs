package com.example;

import org.junit.jupiter.api.Test;
import java.sql.Connection;
import static org.junit.jupiter.api.Assertions.*;

public class DatabaseConnectionTest {

    @Test
    public void testGetInstance() {
        DatabaseConnection instance1 = DatabaseConnection.getInstance();
        DatabaseConnection instance2 = DatabaseConnection.getInstance();

        assertNotNull(instance1);
        assertNotNull(instance2);
        assertSame(instance1, instance2, "Must return the same instance");
    }

    @Test
    public void testGetConnection() {
        DatabaseConnection dbConnection = DatabaseConnection.getInstance();
        Connection connection = dbConnection.getConnection();

        assertNotNull(connection, "The connection must not be null.");
        try {
            assertFalse(connection.isClosed(), "The connection must be open.");
        } catch (Exception e) {
            fail("Error checking connection status: " + e.getMessage());
        }
    }
}