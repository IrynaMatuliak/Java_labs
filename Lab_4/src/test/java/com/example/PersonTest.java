package com.example;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

public class PersonTest {
    @Test
    void testPersonCreation() {
        Person person = new Person("John") {};
        assertEquals("John", person.getName());
    }

    @Test
    void testPersonToString() {
        Person person = new Person("John") {};
        assertTrue(person.toString().contains("John"));
    }

    @Test
    void testFirefighterCreation() {
        Firefighter firefighter = new Firefighter("Mike");
        assertEquals("Mike", firefighter.getName());
        assertTrue(firefighter instanceof Person);
    }

    @Test
    void testPoliceOfficerCreation() {
        PoliceOfficer officer = new PoliceOfficer("Arthur");
        assertEquals("Arthur", officer.getName());
        assertTrue(officer instanceof Person);
    }
}
