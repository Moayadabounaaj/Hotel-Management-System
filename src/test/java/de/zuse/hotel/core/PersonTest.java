package de.zuse.hotel.core;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.*;

class PersonTest {
    private Address address;

    @BeforeEach
    public void init() {
        address = new Address("Syria", "Damascus", "burkan", "66534", 30);
    }

    @Test
    void testFirstname() {
        final String expectedName = "WoW";
        Person person = new Person();
        person.setFirstName("WoW");
        final String actualName = person.getFirstName();
        assertEquals(expectedName, actualName);

        Assertions.assertThrows(Exception.class, () ->
        {
            person.setLastName(null);
        });

        Assertions.assertThrows(Exception.class, () ->
        {
            person.setLastName("");
        });

        Assertions.assertThrows(Exception.class, () ->
        {
            person.setLastName("123");
        });

    }

    @Test
    void testtLastname() {
        final String expectedName = "WoW";
        Person person = new Person();
        person.setLastName(expectedName);
        final String actualName = person.getLastName();
        assertEquals(expectedName, actualName);

        Assertions.assertThrows(Exception.class, () ->
        {
            person.setLastName(null);
        });

        Assertions.assertThrows(Exception.class, () ->
        {
            person.setLastName("");
        });

        Assertions.assertThrows(Exception.class, () ->
        {
            person.setLastName("123");
        });
    }

    @Test
    void testId() {
        final int expectedId = 12348;
        Person person = new Person();
        person.setId(expectedId);
        final int actualId = person.getId();
        assertEquals(expectedId, actualId);

        Assertions.assertThrows(Exception.class, () ->
        {
            person.setId(-10);
        });

    }



    @Test
    void testBirthday() {
        final LocalDate expectedBirthday = LocalDate.of(1998, 2, 3);
        Person person = new Person();
        person.setBirthday(expectedBirthday);
        final LocalDate actualBirthday = person.getBirthday();
        assertEquals(expectedBirthday, actualBirthday);

        Assertions.assertThrows(Exception.class, () ->
        {
            person.setBirthday(LocalDate.of(2010, 1, 1));
        });

    }

    @Test
    void testEmail() {
        final String expectedEmail = "zeus@gmail.com";
        Person person = new Person();
        person.setEmail(expectedEmail);
        final String actualEmail = person.getEmail();
        assertEquals(expectedEmail, actualEmail);

        Assertions.assertThrows(Exception.class, () ->
        {
            person.setEmail(null);
        });
    }


    @Test
    void testTelNumber() {
        final String expectedTelNumber = "015214874571";
        Person person = new Person();
        person.setTelNumber(expectedTelNumber);
        final String actualTelNumber = person.getTelNumber();
        assertEquals(expectedTelNumber, actualTelNumber);

        Assertions.assertThrows(Exception.class, () ->
        {
            person.setTelNumber("0152178541");
        });
    }


    @Test
    void testAddress() {
        final Address expectedAddress = new Address("germany", "Saarbruecken", "Zweibruecker", "66534", 23);
        Person person = new Person();
        person.setAddress(expectedAddress);
        final Address actualAddress = person.getAddress();
        assertEquals(expectedAddress, actualAddress);

        Assertions.assertThrows(Exception.class, () ->
        {
            person.setAddress(null);
        });
    }

}

