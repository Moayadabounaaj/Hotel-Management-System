package de.zuse.hotel.db;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;
import de.zuse.hotel.core.Address;
import de.zuse.hotel.core.Person;
import de.zuse.hotel.util.ZuseCore;
import org.junit.jupiter.api.*;

import javax.persistence.EntityManager;

import static org.junit.jupiter.api.Assertions.*;

public class PersonConnecterTest
{
    private static Connection conn;
    private static PersonConnecter personConnecter;
    private Address address1;
    private Address address2;

    @BeforeEach
    public void setUp()
    {
        if (!ZuseCore.DEBUG_MODE)
        {
            ZuseCore.coreAssert(false, "Can not run the test in realease mode");
        }

        try
        {
            conn = JDBCConnecter.getConnection();
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }

        address1 = new Address("de", "vk", "stadionStrasse", "66333", 42);
        address2 = new Address("de", "saarbruecken", "stadionStrasse", "66155", 42);

        personConnecter = new PersonConnecter();
        removeAll();
    }

    @AfterAll
    public static void tearDown()
    {
        try
        {
            JDBCConnecter.shutdown();
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testDbCreate()
    {
        // create a new person

        Person person = new Person("Monzer", "Doe", LocalDate.of(1999, 5, 22),
                "b@gmail.com", "123456789111", address1);

        personConnecter.dbCreate(person);

        List<?> allPersons = personConnecter.dbsearchAll();
        assertEquals(person, allPersons.get(0));
    }

    @Test
    public void testDbsearchAll()
    {
        // create some persons and add them to the database
        Person person1 = new Person("Mohammed", "Doe", LocalDate.of(1999, 5, 22),
                "b@gmail.com", "123456789111", address1);

        Person person2 = new Person("B", "Doe", LocalDate.of(1999, 5, 22)
                , "jane.doe@example.com", "987654321111", address2);

        personConnecter.dbCreate(person1);
        personConnecter.dbCreate(person2);

        List<?> allPersons = personConnecter.dbsearchAll();

        // check that the correct number of persons was returned
        assertEquals(2, allPersons.size());
        // check that the returned list contains the added persons
        assertTrue(allPersons.contains(person1));
        assertTrue(allPersons.contains(person2));
    }

    @Test
    public void testDbsearchById()
    {
        // create a new person and add them to the database
        Person person = new Person("Basel", "Doe", LocalDate.of(1999, 5, 22),
                "b@gmail.com", "123456789111", address1);

        personConnecter.dbCreate(person);

        // get the person by ID from the database
        Person retrievedPerson = personConnecter.dbsearchById(person.getId());

        // check that the correct person was retrieved
        assertEquals(person, retrievedPerson);
    }

    @Test
    public void testDbRemoveAll()
    {
        // create some persons and add them to the database
        Address address1 = new Address("de", "vk", "stadionStrasse", "66333", 42);
        Person person1 = new Person("Jan", "Doe", LocalDate.of(1999, 5, 22),
                "b@gmail.com", "123456789111", address1);

        Person person2 = new Person("Moayed", "Doe", LocalDate.of(1999, 5, 22),
                "b@gmail.com", "123456789111", address2);

        personConnecter.dbCreate(person1);
        personConnecter.dbCreate(person2);

        // remove all persons from the database
        personConnecter.dbRemoveAll();

        // check that there are no persons left in the database
        List<?> allPersons = personConnecter.dbsearchAll();
        assertTrue(allPersons.isEmpty());
    }

    @Test
    public void testDbRemoveById()
    {
        // Create a new Person and insert it into the database

        Person person = new Person("Anas", "Doe", LocalDate.of(1999, 5, 22),
                "b@gmail.com", "123456789111", address1);

        personConnecter.dbCreate(person);
        personConnecter.dbRemoveById(person.getId());

        Person person1 = personConnecter.dbsearchById(person.getId());

        assertNull(person1);
    }

    @AfterEach
    public void removeAll()
    {
        EntityManager manager = JDBCConnecter.getEntityManagerFactory().createEntityManager();
        manager.getTransaction().begin();
        manager.createNativeQuery("DELETE FROM Bookings").executeUpdate();
        manager.createNativeQuery("DELETE FROM Person").executeUpdate();
        manager.getTransaction().commit();
        manager.close();
    }
}