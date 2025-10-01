package org.example;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class JournalEntry {
    private String lastName;
    private String firstName;
    private LocalDate birthDate;
    private String phone;
    private String street;
    private String house;
    private String apartment;

    public JournalEntry(String lastName, String firstName, LocalDate birthDate,
                        String phone, String street, String house, String apartment) {
        this.lastName = lastName;
        this.firstName = firstName;
        this.birthDate = birthDate;
        this.phone = phone;
        this.street = street;
        this.house = house;
        this.apartment = apartment;
    }

    // Getters
    public String getLastName() { return lastName; }
    public String getFirstName() { return firstName; }
    public LocalDate getBirthDate() { return birthDate; }
    public String getPhone() { return phone; }
    public String getStreet() { return street; }
    public String getHouse() { return house; }
    public String getApartment() { return apartment; }

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return String.format("Student: %s %s, Birth Date: %s, Phone: %s, Address: %s St., Building %s, Apt. %s",
                lastName, firstName, birthDate.format(formatter), phone, street, house, apartment);
    }

    @Override
    public boolean equals(Object o) {
        // If the compared object is the same object (by reference), returns true
        if (this == o) return true;
        // If the object is null or has another type, returns false
        if (o == null || getClass() != o.getClass()) return false;
        JournalEntry that = (JournalEntry) o;
        return Objects.equals(lastName, that.lastName) &&
                Objects.equals(firstName, that.firstName) &&
                Objects.equals(birthDate, that.birthDate) &&
                Objects.equals(phone, that.phone) &&
                Objects.equals(street, that.street) &&
                Objects.equals(house, that.house) &&
                Objects.equals(apartment, that.apartment);
    }

    @Override
    public int hashCode() {
        return Objects.hash(lastName, firstName, birthDate, phone, street, house, apartment);
    }
}