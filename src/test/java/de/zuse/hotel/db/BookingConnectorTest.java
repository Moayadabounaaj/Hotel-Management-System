package de.zuse.hotel.db;

import de.zuse.hotel.core.Address;
import de.zuse.hotel.core.Booking;
import de.zuse.hotel.core.Person;
import de.zuse.hotel.util.ZuseCore;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.TestInstance.Lifecycle;

import javax.persistence.EntityManager;
import java.sql.Connection;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(Lifecycle.PER_CLASS)
public class BookingConnectorTest
{
    private static Connection conn;
    private static BookingConnector bookingConnector;
    private static PersonConnecter personConnecter;
    private Person person1;
    private Person person2;

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


        person1 = new Person("A", "A", LocalDate.of(1990, 1, 1)
                , "@gmail.com", "123456789111",
                new Address("de", "vk", "st", "66333", 42));

        person2 = new Person("B", "B", LocalDate.of(1990, 1, 1)
                , "@gmail.com", "123456789111",
                new Address("de", "vk", "st", "66333", 42));

        removeAll();
        bookingConnector = new BookingConnector();
        personConnecter = new PersonConnecter();
        personConnecter.dbCreate(person1);
        personConnecter.dbCreate(person2);
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
    @DisplayName("Test creating a booking")
    public void testDbCreate()
    {
        Booking booking = new Booking();
        booking.setStartDate(LocalDate.of(2023, 4, 5));
        booking.setEndDate(LocalDate.of(2023, 4, 10));
        booking.setGuest(person1);
        booking.setGuestsNum(2);

        bookingConnector.dbCreate(booking);
        Booking booking2 = bookingConnector.dbsearchById(booking.getBookingID());
        assertNotNull(booking2, "Booking ID should not be null after creation");
    }

    @Test
    @DisplayName("Test searching for all bookings")
    public void testDbSearchAll()
    {
        Booking booking1 = new Booking();
        booking1.setStartDate(LocalDate.of(2023, 4, 5));
        booking1.setEndDate(LocalDate.of(2023, 4, 10));
        booking1.setGuest(person1);
        booking1.setGuestsNum(2);

        Booking booking2 = new Booking();
        booking2.setStartDate(LocalDate.of(2023, 4, 5));
        booking2.setEndDate(LocalDate.of(2023, 4, 10));
        booking2.setGuest(person2);
        booking2.setGuestsNum(2);

        bookingConnector.dbCreate(booking1);
        bookingConnector.dbCreate(booking2);

        List<Booking> allBookings = bookingConnector.dbsearchAll();
        List<Booking> notCancelledBooking = allBookings.stream().filter(o -> !o.isCanceled()).toList();

        assertEquals(notCancelledBooking.get(0), booking1, "");
        assertEquals(notCancelledBooking.get(1), booking2, "");
    }

    @Test
    @DisplayName("Test searching for a booking by ID")
    public void testDbSearchById()
    {
        Booking booking = new Booking();
        booking.setStartDate(LocalDate.of(2023, 4, 5));
        booking.setEndDate(LocalDate.of(2023, 4, 10));
        booking.setGuest(person1);
        booking.setGuestsNum(2);

        bookingConnector.dbCreate(booking);

        Booking result = bookingConnector.dbsearchById(booking.getBookingID());
        assertNotNull(result, "Result should not be null");
    }

    @Test
    @DisplayName("Test removing all bookings")
    public void testDbRemoveAll()
    {
        Booking booking1 = new Booking();
        booking1.setStartDate(LocalDate.of(2023, 4, 5));
        booking1.setEndDate(LocalDate.of(2023, 4, 10));
        booking1.setGuest(person1);
        booking1.setGuestsNum(2);

        Booking booking2 = new Booking();
        booking2.setStartDate(LocalDate.of(2023, 4, 11));
        booking2.setEndDate(LocalDate.of(2023, 4, 15));
        booking2.setGuest(person2);
        booking2.setGuestsNum(3);

        bookingConnector.dbCreate(booking1);
        bookingConnector.dbCreate(booking2);
        bookingConnector.dbRemoveAll();

        List<Booking> allBookings = bookingConnector.dbsearchAll();
        allBookings.forEach(new Consumer<Booking>()
        {
            @Override
            public void accept(Booking booking)
            {
                assertTrue(booking.isCanceled(), "Booking must be canceled");
            }
        });
    }

    @Test
    @DisplayName("Test removing a booking by ID")
    public void testDbRemoveById()
    {
        Booking booking = new Booking();
        booking.setStartDate(LocalDate.of(2023, 4, 5));
        booking.setEndDate(LocalDate.of(2023, 4, 10));
        booking.setGuest(person1);
        booking.setGuestsNum(2);

        bookingConnector.dbCreate(booking);
        List<Booking> bookings = bookingConnector.dbsearchAll();
        bookingConnector.dbRemoveById(bookings.get(0).getBookingID());
        Booking result = bookingConnector.dbsearchById(bookings.get(0).getBookingID());

        assertTrue(result.isCanceled(), "Booking should be canceled after removing");
    }

    @Test
    @DisplayName("Test updating a booking")
    public void testDbUpdate()
    {
        // Create a booking
        Booking booking = new Booking();
        booking.setStartDate(LocalDate.of(2023, 4, 5));
        booking.setEndDate(LocalDate.of(2023, 4, 10));
        booking.setGuest(person1);
        booking.setGuestsNum(2);

        bookingConnector.dbCreate(booking);

        // Update the booking with new values
        booking.setStartDate(LocalDate.of(2023, 4, 11));
        booking.setEndDate(LocalDate.of(2023, 4, 15));
        booking.setGuest(person2);
        booking.setGuestsNum(3);

        bookingConnector.dbUpdate(booking);

        // Retrieve the updated booking and check if it has the new values
        Booking result = bookingConnector.dbsearchById(booking.getBookingID());
        assertEquals(booking.getStartDate(), result.getStartDate(), "Start date should be updated");
        assertEquals(booking.getEndDate(), result.getEndDate(), "End date should be updated");
        assertEquals(booking.getGuest().getFirstName(), result.getGuest().getFirstName(), "Guest first name should be updated");
        assertEquals(booking.getGuest().getLastName(), result.getGuest().getLastName(), "Guest last name should be updated");
        assertEquals(booking.getGuest().getBirthday(), result.getGuest().getBirthday(), "Guest birthdate should be updated");
        assertEquals(booking.getGuestsNum(), result.getGuestsNum(), "Guests number should be updated");
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

