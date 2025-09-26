package de.zuse.hotel.db;

import de.zuse.hotel.core.Booking;
import de.zuse.hotel.core.Person;
import de.zuse.hotel.util.ZuseCore;

import java.time.LocalDate;
import java.util.List;

/**
 *The HotelDatabaseApiImpl class provides an implementation for the HotelDatabaseApi interface, using a database for data storage and retrieval.
 */
public class HotelDatabaseApiImpl implements HotelDatabaseApi
{
    /**
     * Constructs a new instance of HotelDatabaseApiImpl and initializes the database connection.
     */
    public HotelDatabaseApiImpl()
    {
        try
        {
            JDBCConnecter.getConnection();
            JDBCConnecter.getEntityManagerFactory();
        } catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    /**
     * Adds a guest to the database.
     *
     * @param guest The guest to be added.
     * @return true if the guest was added successfully, false otherwise.
     */
    @Override
    public boolean addGuest(Person guest)
    {
        PersonConnecter personConnecter = new PersonConnecter();
        personConnecter.dbCreate(guest);
        return true;
    }

    /**
     *  Make sure by deleting a Guest to delete/cancel all his Booking(s) first!!! other way it will fail
     * @param guestId The ID of the guest to be removed.
     * @return true if the guest was removed successfully, false otherwise.
     */
    @Override
    public boolean removeGuest(int guestId)
    {
        PersonConnecter personConnecter = new PersonConnecter();
        personConnecter.dbRemoveById(guestId);
        return true;
    }

    /**
     * Updates the details of a guest in the database.
     *
     * @param updatedGuest The updated guest object.
     * @return true if the guest was updated successfully, false otherwise.
     */
    @Override
    public boolean updateGuest(Person updatedGuest)
    {
        PersonConnecter personConnecter = new PersonConnecter();
        personConnecter.dbUpdate(updatedGuest);
        // hir is the ID needed
        return true;
    }

    /**
     * Gets a guest from the database using their ID.
     *
     * @param guestID The ID of the guest to be retrieved.
     * @return The guest object if found, null otherwise.
     */
    @Override
    public Person getGuest(int guestID)
    {
        PersonConnecter personConnecter = new PersonConnecter();
        return personConnecter.dbsearchById(guestID);
    }

    /**
     * Gets a list of all guests in the database.
     *
     * @return A list of all guests in the database.
     */
    @Override
    public List<Person> getAllGuest()
    {
        PersonConnecter personConnecter = new PersonConnecter();
        personConnecter.dbsearchAll();
        return (List<Person>) personConnecter.dbsearchAll();
    }

    /**
     * Adds a booking to the database.
     *
     * @param booking The booking to be added.
     * @return true if the booking was added successfully, false otherwise.
     */
    @Override
    public boolean addBooking(Booking booking)
    {
        BookingConnector bookingConnector = new BookingConnector();
        bookingConnector.dbCreate(booking);
        return true;
    }

    /**
     * Removes a booking from the database.
     *
     * @param bookingID The ID of the booking to be removed.
     * @return true if the booking was removed successfully, false otherwise.
     */
    @Override
    public boolean removeBooking(int bookingID)
    {
        BookingConnector bookingConnector = new BookingConnector();
        bookingConnector.dbRemoveById(bookingID);
        return true;
    }

    /**
     * Updates the given booking in the database.
     *
     * @param updatedBooking the updated booking to store in the database
     * @return true if the booking was updated successfully, false otherwise
     */
    @Override
    public boolean updateBooking(Booking updatedBooking)
    {
        BookingConnector bookingConnector = new BookingConnector();
        bookingConnector.dbUpdate(updatedBooking);
        return true;
    }

    /**
     * Retrieves the booking with the given ID from the database.
     *
     * @param bookingID the ID of the booking to retrieve
     * @return the booking with the given ID, or null if no booking exists with that ID
     */
    @Override
    public Booking getBooking(int bookingID)
    {
        BookingConnector bookingConnector = new BookingConnector();
        return bookingConnector.dbsearchById(bookingID);
    }

    /**
     * Retrieves all bookings from the database.
     *
     * @return a list of all bookings stored in the database
     */
    @Override
    public List<Booking> getAllBooking()
    {
        BookingConnector bookingConnector = new BookingConnector();
        bookingConnector.dbsearchAll();
        return (List<Booking>) bookingConnector.dbsearchAll();
    }

    /**
     * Retrieves bookings from the database that match the given search filter.
     *
     * @param bookingSearchFilter the search filter to apply to the database query
     * @return a list of bookings that match the search filter
     */
    @Override
    public List<Booking> getBookingsByFilter(BookingSearchFilter bookingSearchFilter)
    {
        ZuseCore.coreAssert(bookingSearchFilter != null, "Not valid bookingSearchFilter Object");

        BookingConnector bookingConnector = new BookingConnector();
        return bookingConnector.dbSerschforanythinhg(bookingSearchFilter);
    }

    /**
     * Retrieves all bookings from the database that start between the given start and end dates (inclusive).
     *
     * @param start the start date of the date range to search
     * @param end the end date of the date range to search
     * @return a list of bookings that start between the given start and end dates
     */
    @Override
    public List<Booking> getAllBookingBetweenStartAndEnd(LocalDate start, LocalDate end)
    {
        BookingConnector bookingConnector = new BookingConnector();

        return bookingConnector.dbSearchBookingBetween(start,end);
    }

    /**
    * Retrieves persons from the database that match the given search filter.
    *
    * @param personSearchFilter the search filter to apply to the database query
    * @return a list of persons that match the search filter
    */
    @Override
    public List<Person> getPersonsByFilter(PersonSearchFilter personSearchFilter)
    {

        PersonConnecter personConnector = new PersonConnecter();
        return personConnector.dbSerschforanythinhg(personSearchFilter);
    }

    /**
     * Shuts down the Database .
     *
     * @throws Exception if an error occurs during shutdown
     */
    @Override
    public void shutdown() throws Exception
    {
        JDBCConnecter.shutdown();
    }
}
 // :)
