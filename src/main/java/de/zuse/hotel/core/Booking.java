package de.zuse.hotel.core;

import de.zuse.hotel.util.ZuseCore;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.time.temporal.ChronoUnit.DAYS;

/**
 * Represents a booking made by a guest for a room in a hotel.
 * Each booking has a unique ID, a room number, a floor number, a start date, an end date, a guest, a payment,
 * a guest name, a cancellation status, the number of guests, and a list of extra services.
 */
@Entity
@Table(name = "Bookings")
public class Booking
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Booking_id")
    private int bookingID;
    @Column(name = "Room_Number", nullable = false)
    private int roomNumber;
    @Column(name = "Floor_Number", nullable = false)
    private int floorNumber;
    @Column(name = "Start_Date", nullable = false)
    private LocalDate startDate;
    @Column(name = "End_Date", nullable = false)
    private LocalDate endDate;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "Person_id", nullable = true) //nullable because we want to delete person and cancel booking
    private Person guest;//to avoid EAGER loading maybe save person id and load it manually in db

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "Payment_Id", nullable = false)
    private Payment payment;

    @Column(name = "Guest_Name", nullable = false)
    private String guestName; // when guest get deleted, we still have his name

    @Column(name = "canceled")
    @Type(type = "org.hibernate.type.BooleanType")
    private boolean canceled = false;

    @Column(name = "Guests_Num", nullable = false)
    private int guestsNum;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> extraServices = new ArrayList<>();

    public Booking(int roomNumber, int floorNumber, LocalDate startDate, LocalDate endDate, Person guest)
    {
        ZuseCore.check(roomNumber >= 0, "Number of Room should be greater than zero!!");
        ZuseCore.check(floorNumber >= 0, "Number of Room should be greater than zero!!");
        ZuseCore.check(HotelCore.get().isFloorInHotel(floorNumber), "Floor " + floorNumber + " is not in Hotel!!");
        ZuseCore.check(HotelCore.get().isRoomInHotel(floorNumber, roomNumber), "Room " + roomNumber + " is not in Hotel!!");
        ZuseCore.check(startDate != null, "please enter StartDate!");
        ZuseCore.check(endDate != null, "please enter End Date!");
        ZuseCore.check(guest != null, "Guest is null!!");
        ZuseCore.check(startDate.isBefore(endDate), "Start Date should be before you end date");

        ZuseCore.isValidDate(startDate, "not Valid startDate!!");
        ZuseCore.isValidDate(endDate, "not Valid endDate!!");

        this.roomNumber = roomNumber;
        this.startDate = startDate;
        this.endDate = endDate;
        this.guest = guest;
        this.guestName = guest.getFirstName()+ " " + guest.getLastName();
        this.floorNumber = floorNumber;
        payment = new Payment();
    }

    public Booking()
    {
        payment = new Payment();
    }

    public int createInvoice()
    {
        return 0;
    }

    @Override
    public String toString()
    {
        String toString = "roomNumber=" + roomNumber +
                ", floorNumber=" + floorNumber +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", payment=" + payment +
                ", guestsNum=" + guestsNum +
                ", Services= " + extraServices.toString() +
                ", canceled=" + canceled;

        if (guest != null)
            toString += ", guest= " + guest.getFirstName() + " " + guest.getLastName();

        return toString;
    }

    /**
     * Creates a new payment for this booking with the given payment date, payment type, and price.
     *
     * @param paymentDate the date of the payment
     * @param type the type of payment (e.g., credit card, cash)
     * @param price the price of the payment
     * @throws de.zuse.hotel.util.BreakPointException if paymentDate is null or not a valid date
     */
    public void pay(LocalDate paymentDate, Payment.Type type, float price)
    {
        ZuseCore.coreAssert(paymentDate != null, "paymentDate is null!!");
        ZuseCore.isValidDate(paymentDate, "not Valid Date!!");

        payment = new Payment(paymentDate, Payment.Status.PAID, type, price);
    }

    public int getBookingID()
    {
        return bookingID;
    }

    public int getRoomNumber()
    {
        return roomNumber;
    }

    public LocalDate getStartDate()
    {
        return startDate;
    }

    public LocalDate getEndDate()
    {
        return endDate;
    }

    public String getGuestName()
    {
        return guestName;
    }

    public void setGuest(Person guest)
    {
        ZuseCore.check(guest != null, "Guest is null!!");
        this.guest = guest;
    }

    public boolean isPaid()
    {

        return payment.status == Payment.Status.PAID;
    }

    public void setRoomNumber(int roomNumber)
    {
        ZuseCore.check(roomNumber >= 0, "Number of Room should be greater than zero!!");
        this.roomNumber = roomNumber;
    }

    public void setStartDate(LocalDate startDate)
    {
        ZuseCore.check(startDate != null, "StartDate is null!!");
        ZuseCore.isValidDate(startDate, "not Valid startDate!!");

        this.startDate = startDate;
    }

    public void setEndDate(LocalDate endDate)
    {
        ZuseCore.check(endDate != null, "EndDate is null!!");
        ZuseCore.isValidDate(endDate, "not Valid startDate!!");

        this.endDate = endDate;
    }

    public Person getGuest()
    {
        return guest;
    }

    /**
     * Adds an extra service to this booking.
     *
     * @param serviceName the name of the extra service to add
     * @throws de.zuse.hotel.util.BreakPointException if serviceName is not a valid service or if the service is already added
     */
    public void addExtraService(String serviceName)
    {
        //extraServices will not contain duplicate or not valid services
        ZuseCore.check(HotelCore.get().hasRoomService(serviceName), "Service Name is not valid!");
        ZuseCore.check(extraServices.contains(serviceName) == false, "Room has already this service");

        extraServices.add(serviceName);
    }

    public List<String> getBookedServices()
    {
        return extraServices;
    }

    public int getFloorNumber()
    {
        return floorNumber;
    }

    public void setFloorNumber(int floorNumber)
    {
        ZuseCore.check(floorNumber >= 0, "Number of Room should be greater than zero!!");
        this.floorNumber = floorNumber;
    }

    public void cancelBooking()
    {
        canceled = true;
        guest = null;
    }

    public boolean isCanceled()
    {
        return guest == null || canceled == true;//if there is no valid Guest, then it is canceled
    }

    public int getGuestsNum()
    {
        return guestsNum;
    }

    public void setGuestsNum(int guestsNum)
    {
        ZuseCore.check(guestsNum >= 0 , "Gust number can not be negative !!!!" );
        this.guestsNum = guestsNum;
    }

    public Payment getPayment()
    {
        return payment;
    }

    /**
     * Calculates the total cost per night for this booking, based on the given room price and any additional services.
     *
     * @param roomPrice the price per night for the room
     * @return the total cost per night for this booking, including any additional services
     */
    public double coastPerNight(double roomPrice)
    {
        double total = 0.0;
        long daysBetween = DAYS.between(startDate, endDate);
        double totalServicevalue = 0.0;

        for (String string : extraServices)
        {
            totalServicevalue += HotelCore.get().getRoomServicePrice(string);
        }

        total = roomPrice * daysBetween + totalServicevalue;
        return total;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Booking booking = (Booking) o;
        return bookingID == ((Booking) o).bookingID;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(bookingID, roomNumber, floorNumber, startDate, endDate, guest, payment, canceled, guestsNum, extraServices);
    }

}

