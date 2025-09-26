package de.zuse.hotel.gui;

import de.zuse.hotel.core.Booking;
import de.zuse.hotel.core.HotelCore;
import de.zuse.hotel.db.BookingSearchFilter;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

import java.util.List;
import java.util.function.Consumer;

public class DeleteGuestController implements ControllerApi
{
    public AnchorPane root;
    public TextField guestId;

    public void deleteGuest(ActionEvent actionEvent) throws Exception
    {
        if (guestId.getText() == null || guestId.getText().strip().isEmpty())
        {
            InfoController.showMessage(InfoController.LogLevel.Error,"Delete Guest","No Guest-ID was entered!");
            return;
        }

        int id = Integer.parseInt(guestId.getText());

        //Cancel Booking with guest
        {
            BookingSearchFilter bookingSearchFilter = new BookingSearchFilter();
            bookingSearchFilter.guest = HotelCore.get().getGuest(id);

            if (bookingSearchFilter.guest == null)
            {
                InfoController.showMessage(InfoController.LogLevel.Error,"Delete Guest"
                        ,"The Guest id you entered is not valid");

                return;
            }

            List<Booking> bookings = HotelCore.get().getBookingByFilter(bookingSearchFilter);
            if (bookings.size() > 0)
            {
                boolean state = InfoController.showConfirmMessage(InfoController.LogLevel.Warn,"Cancel Booking"
                        , "The guest you try to delete has booking" +
                        ",delete the guest will cancel all his booking/s ?");

                if (state)
                {
                    bookings.forEach(new Consumer<Booking>()
                    {
                        @Override
                        public void accept(Booking booking)
                        {
                            HotelCore.get().removeBooking(booking.getBookingID());
                        }
                    });

                    if (HotelCore.get().removeGuest(id))
                        InfoController.showMessage(InfoController.LogLevel.Info,"Delete Guest","Guest deleted Successfully");
                }

                JavaFxUtil.closeCurrentStage();
                return;
            }
        }

        if (InfoController.showConfirmMessage(InfoController.LogLevel.Info,"Delete Guest","Are you sure?"))
        {
            boolean state = HotelCore.get().removeGuest(id);
            if (state)
                InfoController.showMessage(InfoController.LogLevel.Info,"Delete Guest","Guest deleted Successfully");
        }

        JavaFxUtil.closeCurrentStage();
    }

    @Override
    public void onStart()
    {
        JavaFxUtil.makeFieldOnlyNumbers(guestId);
        root.getStylesheets().add(SettingsController.getCorrectStylePath("BookingWindow.css"));
    }

    @Override
    public void onUpdate(){}

}
