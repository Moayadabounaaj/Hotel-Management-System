package de.zuse.hotel.db;

import de.zuse.hotel.core.Address;
import de.zuse.hotel.core.Person;
import de.zuse.hotel.util.ZuseCore;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
/*
public class AddressConnecterTest
{
    private static AddressConnecter addressConnecter;
    private static Connection conn;

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

        BookingConnector bookingConnector = new BookingConnector();
        bookingConnector.dbRemoveAll();
        PersonConnecter personConnecter = new PersonConnecter();
        personConnecter.dbRemoveAll();
        addressConnecter = new AddressConnecter();
        addressConnecter.dbRemoveAll();
    }

    @AfterAll
    public static void tearDown()
    {
        BookingConnector bookingConnector = new BookingConnector();
        bookingConnector.dbRemoveAll();
        PersonConnecter personConnecter = new PersonConnecter();
        personConnecter.dbRemoveAll();
        addressConnecter.dbRemoveAll();

        try
        {
            conn.close();
        } catch (Exception e)
        {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testDbCreate()
    {
        Address address = new Address("de", "saarbruecken", "stadionStrasse", "66155", 42);
        addressConnecter.dbCreate(address);
        List<Address> addresses = addressConnecter.dbsearchAll();
        assertTrue(addresses.contains(address));
    }

    @Test
    public void testDbsearchAll()
    {
        Address address = new Address("de", "saarbruecken", "stadionStrasse", "66155", 42);
        Address address2 = new Address("de", "saarbruecken", "stadionStrasse", "66155", 42);
        addressConnecter.dbCreate(address);
        addressConnecter.dbCreate(address2);
        List<Address> addresses = addressConnecter.dbsearchAll();

        assertNotNull(addresses);
        assertTrue(addresses.contains(address));
        assertTrue(addresses.contains(address2));
    }

    @Test
    public void testDbsearchById()
    {
        Address address = new Address("de", "saarbruecken", "stadionStrasse", "66155", 42);
        addressConnecter.dbCreate(address);
        Address result = addressConnecter.dbsearchById(address.getId());
        assertEquals(address, result);
    }

    @Test
    public void testDbRemoveAll()
    {
        //addressConnecter.dbRemoveAll();
        //List<Address> addresses = addressConnecter.dbsearchAll();
        //assertTrue(addresses.isEmpty());
    }

    @Test
    public void testDbRemoveById()
    {
        //Address address = new Address("de", "saarbruecken", "stadionStrasse", "66155", 42);
        //addressConnecter.dbCreate(address);
        //int id = address.getId();
        //addressConnecter.dbRemoveById(id);
        //Address result = addressConnecter.dbsearchById(id);
        //assertNull(result);
    }

    @Test
    public void testDbUpdate()
    {
        Address address = new Address("de", "saarbruecken", "stadionStrasse", "66155", 42);
        addressConnecter.dbCreate(address);
        address.setCity("New York"); // modify the address
        addressConnecter.dbUpdate(address);
        Address result = addressConnecter.dbsearchById(address.getId());
        assertEquals(address, result);
    }
}
*/