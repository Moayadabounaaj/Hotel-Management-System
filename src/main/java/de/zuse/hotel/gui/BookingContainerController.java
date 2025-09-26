package de.zuse.hotel.gui;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

public class BookingContainerController
{

    public AnchorPane bookingContainer;

    public Label arrivalDate;
    public Label departureDate;
    public Label guestName;
    public Label roomAndFloor;
    public Button canceledBooking;
    private int bookingID;

    public BookingContainerController()
    {

    }

    public void setBookingID(int id)
    {
        bookingID = id;
    }

    public int getBookingID()
    {
        return bookingID;
    }

    public Node getContent()
    {
        return bookingContainer;
    }

    public void setStyle(boolean canceld)
    {
        bookingContainer.getStylesheets().add(SettingsController.getCorrectStylePath("background.css"));
        if (canceld)
            canceledBooking.setStyle("-fx-background-color:  #f54e4e");
        else
            canceledBooking.setStyle("-fx-background-color:  #1ef2d9");
    }
}


