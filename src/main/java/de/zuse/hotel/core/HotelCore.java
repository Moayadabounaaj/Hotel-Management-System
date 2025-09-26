package de.zuse.hotel.core;

import de.zuse.hotel.db.BookingSearchFilter;
import de.zuse.hotel.db.HotelDatabaseApi;
import de.zuse.hotel.db.HotelDatabaseApiImpl;
import de.zuse.hotel.db.PersonSearchFilter;
import de.zuse.hotel.util.BreakPointException;
import de.zuse.hotel.util.HotelSerializer;
import de.zuse.hotel.util.ZuseCore;
import de.zuse.hotel.util.pdf.InvoicePdf;
import de.zuse.hotel.util.pdf.PdfFile;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * The HotelCore class implements the HotelCoreApi interface and represents the core functionality
 * of a hotel management system.
 */
public class HotelCore implements HotelCoreApi
{
    private static HotelCore instance = null;
    private HotelDatabaseApi hotelDatabaseApi;
    private HotelConfiguration hotelConfiguration;
    private Runnable updateCallback;

    /**
     * Returns the single instance of the HotelCore class. This method should be called after the `init()`
     * method has been called to initialize the instance. If `init()` has not been called, this method will
     * throw an assertion error.
     *
     * @return The single instance of the HotelCore class.
     * @throws BreakPointException if the `init()` method has not been called before calling this method.
     */
    public static HotelCore get()
    {
        ZuseCore.coreAssert(instance != null, "you should call HotelCore.init() on start of app!");

        return instance;
    }

    /**
     * Initializes the HotelCore class by creating a new instance of it if one does not already exist. If
     * an instance already exists, this method does nothing.
     */
    public static void init()
    {
        if (instance != null)
            return;

        instance = new HotelCore();
    }

    /**
     * Shuts down the HotelCore class by serializing the hotel configuration and settings, shutting down the
     * hotel database API, and setting the instance variable to null.
     *
     * @throws BreakPointException if there is an error while serializing the hotel configuration or settings or
     *                             if there is an error while shutting down the hotel database API.
     */
    public static void shutdown()
    {
        HotelSerializer hotelSerializer = new HotelSerializer();

        try
        {
            hotelSerializer.serializeHotel(instance.hotelConfiguration);
            hotelSerializer.serializeSettings();
            instance.hotelDatabaseApi.shutdown();
        } catch (Exception e)
        {
            ZuseCore.coreAssert(false, e.getMessage());
            if (ZuseCore.DEBUG_MODE)
                e.printStackTrace();
        }

        instance = null;
    }

    private HotelCore()
    {
        hotelDatabaseApi = new HotelDatabaseApiImpl();

        HotelSerializer hotelSerializer = new HotelSerializer();
        try
        {
            hotelConfiguration = hotelSerializer.deserializeHotel();
            hotelSerializer.deserializeSettings();
        } catch (Exception e)
        {
            if (ZuseCore.DEBUG_MODE)
                e.printStackTrace();
        }
    }

    /**
     * Sets the name of the hotel to the specified name.
     *
     * @param name The new name for the hotel.
     */
    @Override
    public void setHotelName(String name)
    {
        ZuseCore.coreAssert(name != null && name.strip().isEmpty(), "Name is empty!!");

        hotelConfiguration.setHotelName(name);
    }

    @Override
    public String getHotelName()
    {
        return hotelConfiguration.getHotelName();
    }

    /**
     * Adds a guest to the hotel using database API and returns true if the guest was successfully added. If an
     * update callback has been registered, it will be executed after the guest has been added.
     *
     * @param guest The guest to add to the hotel database API.
     * @return True if the guest was successfully added, false otherwise.
     */
    @Override
    public boolean addGuest(Person guest)
    {
        // this is here because it's an app-logic details not db-impl details
        {
            PersonSearchFilter personSearchFilter = new PersonSearchFilter();
            personSearchFilter.email = guest.getEmail();
            // should return a list with 0 size to add the guest to db
            List<Person> personList = hotelDatabaseApi.getPersonsByFilter(personSearchFilter);
            ZuseCore.check(personList.size() == 0, "please try another email, this email already exist");
        }

        boolean state = hotelDatabaseApi.addGuest(guest);
        if (updateCallback != null)
            updateCallback.run();

        return state;
    }

    /**
     * Removes the guest with the specified ID from the hotel database and returns true if the guest was
     * successfully removed. If an update callback has been registered, it will be executed after the guest has
     * been removed.
     *
     * @param guestID The ID of the guest to remove from the hotel database API.
     * @return True if the guest was successfully removed, false otherwise.
     */
    @Override
    public boolean removeGuest(int guestID)
    {
        boolean state = hotelDatabaseApi.removeGuest(guestID);
        if (updateCallback != null)
            updateCallback.run();

        return state;
    }

    /**
     * Returns the guest with the specified ID from the hotel database as a Person object.
     *
     * @param personID The ID of the guest to retrieve from the hotel database API.
     * @return The guest with the specified ID as a Person object.
     */
    @Override
    public Person getGuest(int personID)
    {
        return hotelDatabaseApi.getGuest(personID);
    }

    /**
     * Returns a list of all guests in the hotel database as a List of Person objects.
     *
     * @return A list of all guests in the hotel database API.
     */
    @Override
    public List<Person> getAllGuest()
    {
        return hotelDatabaseApi.getAllGuest();
    }

    /**
     * Returns a list of guests in the hotel database that match the specified search filter as a List of
     * Person objects.
     *
     * @param personSearchFilter The search filter to apply to the guests in the hotel database API.
     * @return A list of guests in the hotel database API that match the specified search filter.
     */
    @Override
    public List<Person> getPersonsByFilter(PersonSearchFilter personSearchFilter)
    {
        return hotelDatabaseApi.getPersonsByFilter(personSearchFilter);
    }

    /**
     * Updates the information for the specified guest in the hotel database and returns true if the
     * update was successful. If an update callback has been registered, it will be executed after the guest
     * has been updated.
     *
     * @param guest The guest to update in the hotel database API.
     * @return True if the guest was successfully updated, false otherwise.
     */
    @Override
    public boolean updateGuest(Person guest)
    {
        // this is here because it's an app-logic details not db-impl details
        {
            PersonSearchFilter personSearchFilter = new PersonSearchFilter();
            personSearchFilter.email = guest.getEmail();
            // should return a list with 0 size to add the guest to db
            List<Person> personList = hotelDatabaseApi.getPersonsByFilter(personSearchFilter);

            if (personList.size() > 0 && personList.get(0).getId() != guest.getId())
                ZuseCore.check(false, "please try another email, this email already exist");
        }

        boolean state = hotelDatabaseApi.updateGuest(guest);
        if (updateCallback != null)
            updateCallback.run();

        return state;
    }

    /**
     * Adds a new booking to the database.
     *
     * @param booking The booking to be added to the database.
     * @return True if the booking was successfully added to the database, false otherwise.
     */
    @Override
    public boolean addBooking(Booking booking)
    {
        boolean state = hotelDatabaseApi.addBooking(booking);
        if (updateCallback != null)
            updateCallback.run();

        return state;
    }

    /**
     * Removes a booking from the database using the booking ID.
     *
     * @param bookingID The ID of the booking to be removed.
     * @return True if the booking was successfully removed from the database, false otherwise.
     */
    @Override
    public boolean removeBooking(int bookingID)
    {
        boolean state = hotelDatabaseApi.removeBooking(bookingID);
        if (updateCallback != null)
            updateCallback.run();

        return state;
    }

    /**
     * Removes a booking from the database using a Booking object.
     *
     * @param booking The booking to be removed from the database.
     * @return True if the booking was successfully removed from the database, false otherwise.
     */
    @Override
    public boolean removeBooking(Booking booking)
    {
        return removeBooking(booking.getBookingID());
    }

    /**
     * Retrieves a booking from the database using the booking ID.
     *
     * @param bookingID The ID of the booking to be retrieved.
     * @return The Booking object corresponding to the booking ID, null if the booking does not exist.
     */
    @Override
    public Booking getBooking(int bookingID)
    {
        return hotelDatabaseApi.getBooking(bookingID);
    }

    /**
     * Retrieves all bookings from the database.
     *
     * @return A List of all Booking objects in the database, empty if there are no bookings.
     */
    @Override
    public List<Booking> getAllBooking()
    {
        return hotelDatabaseApi.getAllBooking();
    }

    /**
     * Retrieves a List of Booking objects from the database based on the specified search filter.
     *
     * @param bookingSearchFilter The filter to be applied to the database query.
     * @return A List of Booking objects that match the search filter criteria, empty if there are no matches.
     */
    @Override
    public List<Booking> getBookingByFilter(BookingSearchFilter bookingSearchFilter)
    {
        return hotelDatabaseApi.getBookingsByFilter(bookingSearchFilter);
    }

    /**
     * Retrieves a List of all Booking objects in the database that have a start date between the specified start and end dates.
     *
     * @param start The start date of the range.
     * @param end   The end date of the range.
     * @return A List of Booking objects that have a start date between the specified start and end dates, empty if there are no matches.
     */
    @Override
    public List<Booking> getAllBookingBetweenStartAndEnd(LocalDate start, LocalDate end)
    {
        return hotelDatabaseApi.getAllBookingBetweenStartAndEnd(start, end);
    }

    /**
     * Updates an existing booking in the database.
     *
     * @param booking The updated Booking object.
     * @return True if the booking was successfully updated in the database, false otherwise.
     */
    @Override
    public boolean updateBooking(Booking booking)
    {
        boolean state = hotelDatabaseApi.updateBooking(booking);
        if (updateCallback != null)
            updateCallback.run();

        return state;
    }

    /**
     * Generates a PDF file of the booking invoice.
     *
     * @param bookingID The ID of the booking for which to generate the invoice PDF.
     * @return The generated PdfFile object, null if the booking is cancelled or does not exist.
     */
    @Override
    public PdfFile getBookingAsPdfFile(int bookingID)
    {
        Booking booking = hotelDatabaseApi.getBooking(bookingID);
        ZuseCore.check(booking != null && !booking.isCanceled(), "You can not save canceled Booking as Pdf");

        return new InvoicePdf(booking);
    }

    /**
     * Returns a list of all floors in the hotel
     *
     * @return a list of all floors in the hotel
     */
    @Override
    public List<Floor> getFloors()
    {
        return hotelConfiguration.getHotelFloors();
    }

    /**
     * Returns a list of all rooms on the specified floor
     *
     * @param floorNr the floor number to get rooms from
     * @return a list of all rooms on the specified floor
     * @throws BreakPointException if floorNr is greater than or equal to the number of floors in the hotel
     */
    @Override
    public List<Room> getRooms(int floorNr)
    {
        ZuseCore.coreAssert(floorNr < hotelConfiguration.getHotelFloors().size(), "FloorNr > size, floorNr is the index!");

        return hotelConfiguration.getHotelFloors().get(floorNr).getRooms();
    }

    /**
     * Returns a room object given the floor number and room number
     *
     * @param floorNr the floor number of the room
     * @param roomNr  the room number
     * @return the room object
     * @throws BreakPointException if the floor or room does not exist in the hotel
     */
    @Override
    public Room getRoom(int floorNr, int roomNr)
    {
        Floor floor = hotelConfiguration.getHotelFloors().get(floorNr);
        ZuseCore.check(floor != null, "Floor " + floorNr + " is not in Hotel!");

        Room room = floor.getRooms().get(roomNr);
        ZuseCore.check(room != null, "Room " + roomNr + " is not in Hotel!");

        return room;
    }

    /**
     * Returns whether or not a floor with the specified floor number exists in the hotel
     *
     * @param floorNr the floor number to check
     * @return true if the floor exists, false otherwise
     */
    @Override
    public boolean isFloorInHotel(int floorNr)
    {
        //TODO: Optimization
        for (Floor floor : hotelConfiguration.getHotelFloors())
            if (floor.getFloorNr() == floorNr)
                return true;

        return false;
    }

    /**
     * Returns whether or not a room with the specified floor number and room number exists in the hotel
     *
     * @param floorNr the floor number of the room
     * @param roomNr  the room number
     * @return true if the room exists, false otherwise
     * @throws BreakPointException if the specified floor does not exist in the hotel
     */
    @Override
    public boolean isRoomInHotel(int floorNr, int roomNr)
    {
        //TODO: Optimization
        ZuseCore.check(isFloorInHotel(floorNr), "Floor" + floorNr + " is not in Hotel!!");

        for (Room room : getFloorByFloorNr(floorNr).getRooms())
        {
            if (room.getRoomNr() == roomNr)
                return true;
        }

        return false;
    }

    /**
     * Removes a room from the hotel configuration given the floor and room numbers
     *
     * @param floorNr the floor number of the room
     * @param roomNr  the room number
     */
    @Override
    public void removeRoomFromHotel(int floorNr, int roomNr)
    {
        hotelConfiguration.removeRoom(floorNr, roomNr);
        // in case the app crash, we do not lose any changes
        serializeHotel();
    }

    /**
     * Adds a new floor to the hotel configuration
     *
     * @param floor the floor to add
     */
    @Override
    public void addNewFloorToHotel(Floor floor)
    {
        hotelConfiguration.addNewFloor(floor);
        // in case the app crash, we do not lose any changes
        serializeHotel();
    }

    /**
     * Adds a new room to the hotel configuration on the specified
     */
    @Override
    public void addNewRoomToHotel(int floorNr, Room room)
    {
        hotelConfiguration.addNewRoom(floorNr, room);
        // in case the app crash, we do not lose any changes
        serializeHotel();
    }

    /**
     * Returns the floor with the specified floor number.
     *
     * @param floorNr The floor number of the floor to be returned.
     * @return The floor with the specified floor number.
     */
    @Override
    public Floor getFloorByFloorNr(int floorNr)
    {
        return hotelConfiguration.getFloorByFloorNr(floorNr);
    }

    /**
     * Returns the room with the specified floor number and room number.
     *
     * @param floorNr The floor number of the room.
     * @param roomNr  The room number of the room.
     * @return The room with the specified floor number and room number.
     */
    @Override
    public Room getRoomByRoomNr(int floorNr, int roomNr)
    {
        return hotelConfiguration.getRoomByRoomNr(floorNr, roomNr);
    }

    /**
     * Adds a new room service with the specified name and price to the hotel configuration.
     *
     * @param serviceName The name of the new room service.
     * @param price       The price of the new room service.
     */
    @Override
    public void addNewRoomService(String serviceName, double price)
    {
        hotelConfiguration.addNewRoomService(serviceName, price);
        serializeHotel();
    }

    /**
     * Returns the price of the room service with the specified name.
     *
     * @param serviceName The name of the room service.
     * @return The price of the room service with the specified name.
     */
    @Override
    public double getRoomServicePrice(String serviceName)
    {
        return hotelConfiguration.getRoomServicePrice(serviceName);
    }

    /**
     * Checks if the hotel configuration contains a room service with the specified name.
     *
     * @param serviceName The name of the room service to check.
     * @return True if the hotel configuration contains a room service with the specified name, false otherwise.
     */
    @Override
    public boolean hasRoomService(String serviceName)
    {
        return hotelConfiguration.hasServiceName(serviceName);
    }

    /**
     * Returns a list of all the room services in the hotel configuration.
     *
     * @return A list of all the room services in the hotel configuration.
     */
    @Override
    public List<String> getAllRoomServices()
    {
        return new ArrayList<>(hotelConfiguration.getRoomServices().keySet());
    }

    /**
     * Serializes the hotel configuration and settings to disk.
     */
    @Override
    public void serializeHotel()
    {
        HotelSerializer hotelSerializer = new HotelSerializer();
        try
        {
            hotelSerializer.serializeHotel(hotelConfiguration);
            hotelSerializer.serializeSettings();
        } catch (IOException e)
        {
            throw new RuntimeException(e);
        }

        if (updateCallback != null)
            updateCallback.run();
    }

    /**
     * Binds a callback function to be called whenever the hotel configuration or database is updated.
     *
     * @param action The callback function to be called.
     */
    @Override
    public void bindOnUpdateAction(Runnable action)
    {
        updateCallback = action;
    }
}
