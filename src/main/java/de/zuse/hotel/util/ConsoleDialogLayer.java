package de.zuse.hotel.util;

import de.zuse.hotel.Layer;
import de.zuse.hotel.core.*;
import de.zuse.hotel.db.BookingSearchFilter;
import de.zuse.hotel.db.PersonSearchFilter;


import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class ConsoleDialogLayer implements Layer
{
    //-------------------Const fields--------------------------------//
    private static final int ADD_GUEST = 1;
    private static final int REMOVE_GUEST = 2;
    private static final int UPDATE_GUEST = 3;
    private static final int GET_GUEST = 4;
    private static final int GET_ALL_GUEST = 5;

    private static final int ADD_BOOKING = 6;
    private static final int REMOVE_BOOKING = 7;
    private static final int UPDATE_BOOKING = 8;
    private static final int GET_BOOKING = 9;
    private static final int GET_ALL_BOOKING = 10;

    private static final int AUTO_TEST_SEARCH_BY_FILTER = 11;
    private static final int END = 0;
    //--------------------------------------------------------------//

    private Scanner input;

    @Override
    public void onStart()
    {
        System.out.println("Start Loading Database..");
        input = new Scanner(System.in);
        HotelCore.init();
    }

    @Override
    public void run(String[] args)
    {
        int currentInput = -1;
        while (currentInput != END)
        {
            printInputInformation();
            currentInput = readInteger();
            handelInput(currentInput);
        }
    }

    @Override
    public void onClose()
    {
        System.out.println("Close Hotel App ...");
        HotelCore.shutdown();
    }


    private void printInputInformation()
    {
        System.out.println("Add Guest: " + ADD_GUEST);
        System.out.println("Remove Guest: " + REMOVE_GUEST);
        System.out.println("Update Guest: " + UPDATE_GUEST);
        System.out.println("Get Guest: " + GET_GUEST);
        System.out.println("Get All Guests: " + GET_ALL_GUEST);

        System.out.println("Add Booking: " + ADD_BOOKING);
        System.out.println("Remove Booking: " + REMOVE_BOOKING);
        System.out.println("Update Booking: " + UPDATE_BOOKING);
        System.out.println("Get Booking: " + GET_BOOKING);
        System.out.println("Get All Booking: " + GET_ALL_BOOKING);
        System.out.println("Auto test by filter: " + AUTO_TEST_SEARCH_BY_FILTER);
        System.out.println("Close App: " + END);
    }

    private void handelInput(int inputParam)
    {
        switch (inputParam)
        {
            case ADD_GUEST:                     addGuest();             break;
            case REMOVE_GUEST:                  removeGuest();          break;
            case UPDATE_GUEST:                  updateGuest();          break;
            case GET_GUEST:                     getGuest();             break;
            case GET_ALL_GUEST:                 getAllGuests();         break;
            case ADD_BOOKING:                   addBooking();           break;
            case REMOVE_BOOKING:                removeBooking();        break;
            case UPDATE_BOOKING:                updateBooking();        break;
            case GET_BOOKING:                   getBooking();           break;
            case GET_ALL_BOOKING:               getAllBooking();        break;
            case AUTO_TEST_SEARCH_BY_FILTER:    autoTestByFilter();     break;
            default:                            return;
        }
    }

    private void addGuest()
    {
        Person guest = readGuestInfo();

        HotelCore.get().addGuest(guest);
    }

    private void removeGuest()
    {
        System.out.print("Enter Guest ID: ");
        int id = readInteger();

        HotelCore.get().removeGuest(id);
    }

    private void updateGuest()
    {
        Person guest = readGuestInfo();
        HotelCore.get().updateGuest(guest);
    }

    private void getGuest()
    {
        System.out.print("Enter Guest ID: ");
        int id = readInteger();
        Person guest = HotelCore.get().getGuest(id);

        System.out.println(guest.toString());
    }

    private void getAllGuests()
    {
        List<Person> guests = HotelCore.get().getAllGuest();
        //guests.forEach(System.out::println);

        if (guests != null)
        {
            for (Person guest : guests)
                if (guest != null)
                    System.out.println(guest);
        }
    }

    private void addBooking()
    {
        Booking booking = readBookingInfo();
        HotelCore.get().addBooking(booking);
    }

    private void removeBooking()
    {
        System.out.println("Booking ID: ");
        int bookID = readInteger();

        HotelCore.get().removeBooking(bookID);
    }

    private void updateBooking()
    {
        System.out.println("\n\nIn ConsoleDialog you have to enter the information again!\n\n");
        Booking updatedBooking = readBookingInfo();

        HotelCore.get().updateBooking(updatedBooking);
    }

    private void getBooking()
    {
        System.out.println("Booking ID: ");
        int bookID = readInteger();

        Booking booking = HotelCore.get().getBooking(bookID);

        System.out.println(booking);
    }

    private void getAllBooking()
    {
        List<Booking> bookings = HotelCore.get().getAllBooking();
        //bookings.forEach(System.out::println);
        if (bookings != null)
        {
            for (Booking booking : bookings)
                if (booking != null )
                    System.out.println(bookings);
        }

    }

    private int readInteger()
    {
        System.out.print("--> ");
        int in = input.nextInt();
        input.nextLine();
        return in;
    }

    private float readFloat()
    {
        System.out.print("--> ");
        float in = input.nextFloat();
        input.nextLine();
        return in;
    }

    private String readString()
    {
        System.out.print("--> ");
        return input.nextLine();
    }

    private Address readAddressInfo()
    {
        System.out.println("---(Address)----\n");
        System.out.print("Country: ");
        String country = readString();

        System.out.print("City: ");
        String city = readString();

        System.out.print("Street: ");
        String street = readString();

        System.out.print("plz(Integer): ");
        String plz = readString();

        System.out.print("houseNr(Integer): ");
        int houseNr = readInteger();

        return new Address(country, city, street, plz, houseNr);
    }

    private Person readGuestInfo()
    {
        System.out.print("FirstName: ");
        String firstName = readString();

        System.out.print("LastName: ");
        String lastName = readString();

        LocalDate birthday = readDate("Birthday");

        System.out.print("Email: ");
        String email = readString();

        System.out.print("TelephoneNr: ");
        String telNr = readString();

        Address address = readAddressInfo();
        return new Person(firstName, lastName, birthday, email, telNr, address);
    }

    public Booking readBookingInfo()
    {
        System.out.print("roomNumber: ");
        int roomNr = readInteger();

        System.out.print("floorNumber: ");
        int floorNr = readInteger();

        LocalDate startDate = readDate("StartDate_Booking");
        LocalDate endDate = readDate("EndDate_Booking");

        System.out.println("Enter Guest Id: ");
        int guestID = readInteger();

        Person guest = HotelCore.get().getGuest(guestID);
        Booking booking = new Booking(roomNr, floorNr, startDate, endDate, guest);

        System.out.println("is it Paid?: ");
        System.out.println("Paid: " + Payment.Status.PAID.ordinal());
        System.out.println("Not Paid: " + Payment.Status.NOT_PAID.ordinal());
        int paymentStatus = readInteger();

        if (paymentStatus == Payment.Status.PAID.ordinal())
        {
            LocalDate payDate = readDate("Payment_Date");
            System.out.println("\n----PaymentType---\n");
            System.out.println("CASH: " + Payment.Type.CASH.ordinal());
            System.out.println("CREDIT_CARD: " + Payment.Type.CREDIT_CARD.ordinal());
            System.out.println("DEBIT_CARD: " + Payment.Type.DEBIT_CARD.ordinal());
            System.out.println("MOBILE_PAYMENT: " + Payment.Type.MOBILE_PAYMENT.ordinal());
            int paymentTypeInput = readInteger();

            System.out.println("Enter Price(float): ");
            float price = readFloat();

            booking.pay(payDate, Payment.Type.values()[paymentTypeInput], price);
        }

        return booking;
    }

    private LocalDate readDate(String date)
    {
        System.out.print(date + "(Year): ");
        int year = readInteger();

        System.out.print(date + "(Month): ");
        int month = readInteger();

        System.out.print(date + "(Day): ");
        int day = readInteger();

        return LocalDate.of(year, month, day);
    }

    private void autoTestByFilter()
    {
        Person person = new Person("bs", "sd", LocalDate.of(1999, 5, 22), "2002@gmail"
                , "123456789111", new Address("de", "vk", "st", "66333", 24));

        Booking booking = new Booking(2, 1, LocalDate.of(2023, 5, 1)
                , LocalDate.of(2023, 6, 1), person);

        HotelCore.get().addGuest(person);
        HotelCore.get().addBooking(booking);

        BookingSearchFilter bookingSearchFilter = new BookingSearchFilter();
        bookingSearchFilter.startDate = LocalDate.of(2023,5,1);

        PersonSearchFilter personSearchFilter = new PersonSearchFilter();
        personSearchFilter.email = "2002@gmail";

        List<Booking> bookings = HotelCore.get().getBookingByFilter(bookingSearchFilter);
        List<Person> personList = HotelCore.get().getPersonsByFilter(personSearchFilter);

        bookings.forEach(System.out::println);
        personList.forEach(System.out::println);
    }

}
