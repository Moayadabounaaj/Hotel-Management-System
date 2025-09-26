package de.zuse.hotel.core;

import de.zuse.hotel.db.BookingSearchFilter;
import de.zuse.hotel.db.PersonSearchFilter;
import de.zuse.hotel.gui.ControllerApi;
import de.zuse.hotel.util.pdf.PdfFile;

import java.time.LocalDate;
import java.util.List;

public interface HotelCoreApi {

    // Hotel name
    void setHotelName(String name);
    String getHotelName();

    // Guest related methods
    boolean addGuest(Person guest);
    boolean removeGuest(int guestID);
    Person getGuest(int personID);
    List<Person> getAllGuest();
    List<Person> getPersonsByFilter(PersonSearchFilter personSearchFilter);
    boolean updateGuest(Person guest);

    // Booking related methods
    boolean addBooking(Booking booking);
    boolean removeBooking(int bookingID);
    boolean removeBooking(Booking booking);
    Booking getBooking(int bookingID);
    List<Booking> getAllBooking();
    List<Booking> getBookingByFilter(BookingSearchFilter bookingSearchFilter);
    List<Booking> getAllBookingBetweenStartAndEnd(LocalDate start, LocalDate end);
    boolean updateBooking(Booking booking);
    PdfFile getBookingAsPdfFile(int bookingID);

    // Room related methods
    List<Floor> getFloors();
    List<Room> getRooms(int floorNr);
    Room getRoom(int floorNr, int roomNr);
    boolean isFloorInHotel(int floorNr);
    boolean isRoomInHotel(int floorNr, int roomNr);
    void removeRoomFromHotel(int floorNr, int roomNr);
    void addNewFloorToHotel(Floor floor);
    void addNewRoomToHotel(int floorNr, Room room);
    Floor getFloorByFloorNr(int floorNr);
    Room getRoomByRoomNr(int floorNr, int roomNr);

    // Room Services related methods
    void addNewRoomService(String serviceName, double price);
    double getRoomServicePrice(String serviceName);
    boolean hasRoomService(String serviceName);
    List<String> getAllRoomServices();

    // Serialization
    void serializeHotel();

    // Others
    void bindOnUpdateAction(Runnable action);
}
