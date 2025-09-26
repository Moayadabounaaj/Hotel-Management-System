package de.zuse.hotel.db;

import de.zuse.hotel.core.Booking;
import de.zuse.hotel.core.Person;

import java.time.LocalDate;
import java.util.List;

public interface HotelDatabaseApi
{
    //Guest
    public boolean addGuest(Person guest);
    public boolean removeGuest(int guestId);
    public boolean updateGuest( Person updatedGuest);
    public Person getGuest(int guestID);
    public List<Person> getAllGuest();

    //Booking
    public boolean addBooking(Booking booking);
    public boolean removeBooking(int bookingID);
    public boolean updateBooking(Booking updatedBooking);
    public Booking getBooking(int bookingID);
    public List<Booking> getAllBooking();
    public List<Booking> getBookingsByFilter(BookingSearchFilter bookingSearchFilter);
    public List<Booking> getAllBookingBetweenStartAndEnd(LocalDate start, LocalDate end);
    public List<Person> getPersonsByFilter(PersonSearchFilter personSearchFilter);

    /**
     * Optional
     */
    public void shutdown() throws Exception;
}