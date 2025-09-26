package de.zuse.hotel.gui;

import de.zuse.hotel.core.*;
import de.zuse.hotel.db.PersonSearchFilter;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import org.controlsfx.control.CheckComboBox;


import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class BookingWindowController implements ControllerApi
{
    public Button closeBtnId;
    public TextField guestsNumber;
    public TextField priceField;
    public ChoiceBox<Floor> floorChoiceBox;
    public AnchorPane root;

    public CheckComboBox<String> extraServices;
    public ChoiceBox<Room> roomChoiceBox;
    public CheckBox paidCheckBox;
    public TextField guestOrEmailID; // small letter pls jan
    public DatePicker arrivalDate;
    public DatePicker depatureDate;
    public ChoiceBox<Payment.Type> paymentChoiceBox;

    public void closeWindow()
    {
        Stage stage = (Stage) closeBtnId.getScene().getWindow();
        stage.close();
    }

    public void addBooking(ActionEvent event) throws Exception
    {
        if (guestOrEmailID.getText() == null || guestOrEmailID.getText().strip().isEmpty()
                || guestsNumber.getText() == null || guestsNumber.getText().strip().isEmpty()
                || roomChoiceBox.getValue() == null || paymentChoiceBox.getValue() == null)
        {
            InfoController.showMessage(InfoController.LogLevel.Warn, "Add Booking", "can not add Booking" +
                    ", please fill all information!");

            return;
        }


        int floorNr = floorChoiceBox.getValue().getFloorNr();
        int roomNr = roomChoiceBox.getValue().getRoomNr();
        int guestNum = Integer.parseInt(guestsNumber.getText());
        Payment.Type paymentType = paymentChoiceBox.getValue();

        Person guest = null;
        Pattern pattern = Pattern.compile(Person.EMAIL_REGEX);
        Matcher matcher = pattern.matcher(guestOrEmailID.getText());
        if (matcher.matches())
        {
            PersonSearchFilter personSearchFilter = new PersonSearchFilter();
            personSearchFilter.email = guestOrEmailID.getText().trim().toLowerCase();
            List<Person> pList = HotelCore.get().getPersonsByFilter(personSearchFilter);
            if (pList.size() > 0)
            {
                Person filteredGuest = pList.get(0);
                guest = HotelCore.get().getGuest(filteredGuest.getId());
            }
        } else
        {
            try
            {
                int id = Integer.parseInt(guestOrEmailID.getText());
                guest = HotelCore.get().getGuest(id);
            } catch (Exception e)
            {
                // Ignore
            }
        }

        if (guest == null)
        {
            InfoController.showMessage(InfoController.LogLevel.Warn, "Add Booking", "could not find guest, check your id or email");
            return;
        }

        Booking booking = new Booking(roomNr, floorNr, arrivalDate.getValue(), depatureDate.getValue(), guest);
        booking.setGuestsNum(guestNum);

        extraServices.getCheckModel().getCheckedItems().forEach(new Consumer<String>()
        {
            @Override
            public void accept(String s)
            {
                booking.addExtraService(s);
            }
        });

        booking.pay(LocalDate.now(), paymentType, Float.parseFloat(priceField.getText()));

        boolean state = HotelCore.get().addBooking(booking);
        if (state)
            InfoController.showMessage(InfoController.LogLevel.Info, "Add Booking", "Booking added successfully");

        closeWindow();
    }

    @Override
    public void onStart()
    {
        root.getStylesheets().add(SettingsController.getCorrectStylePath("BookingWindow.css"));
        extraServices.getStylesheets().add(SettingsController.getCorrectStylePath("comboCheckbox.css"));
        arrivalDate.getStylesheets().add(SettingsController.getCorrectStylePath("datePickerStyle.css"));
        depatureDate.getStylesheets().add(SettingsController.getCorrectStylePath("datePickerStyle.css"));



        // Set payment ChoiceBox values and default value
        List<Payment.Type> typeList = Arrays.stream(Payment.Type.values()).collect(Collectors.toList());
        paymentChoiceBox.getItems().addAll(typeList);
        paymentChoiceBox.setValue(Payment.Type.CASH);

        JavaFxUtil.makeFieldOnlyNumbers(guestsNumber); //guestsNumber Take only numbers
        JavaFxUtil.makeFieldOnlyNumericWithDecimal(priceField);

        priceField.setEditable(false);

        setChoiceBoxStringConverter();
        floorChoiceBox.getItems().addAll(HotelCore.get().getFloors());

        //callbacks
        floorChoiceBox.setOnAction(this::onDateChangeListener);
        arrivalDate.setOnAction(this::onDateChangeListener);
        depatureDate.setOnAction(this::onDateChangeListener);
        roomChoiceBox.setOnAction(this::changePriceField);
        extraServices.getCheckModel().getCheckedItems().addListener(new InvalidationListener()
        {
            @Override
            public void invalidated(Observable observable)
            {
                changePriceField(new ActionEvent());
            }
        });

        if (floorChoiceBox.getItems().size() > 0) //default value
            floorChoiceBox.setValue(floorChoiceBox.getItems().get(0));


        extraServices.getItems().setAll(FXCollections.observableArrayList(HotelCore.get().getAllRoomServices()));
    }

    @Override
    public void onUpdate()
    {
    }

    /**
     * This is a very expensive method (performance), we have to optimize it in feature, Maybe :)
     */
    public void showAvailableRooms(ObservableValue<? extends Floor> observable, Floor oldValue, Floor newValue)
    {
        roomChoiceBox.getItems().clear();

        // we do not show rooms or floor until the client set the start and end date of booking
        if (arrivalDate.getValue() == null || depatureDate.getValue() == null || floorChoiceBox.getValue() == null) //Problem when user enter chars!!
            return;

        List<Booking> bookingList = HotelCore.get().getAllBookingBetweenStartAndEnd(arrivalDate.getValue()
                , depatureDate.getValue());

        //Booked roomNr(s) between start and end
        List<Integer> bookedRooms = bookingList.stream()
                .filter(booking -> !booking.isCanceled()
                        && floorChoiceBox.getValue().getFloorNr() == booking.getFloorNumber())
                .map(Booking::getRoomNumber).toList();

        roomChoiceBox.getItems().addAll(newValue.getRooms()
                .stream()
                .filter(room -> !bookedRooms.contains(room.getRoomNr()))
                .toList());

        if (roomChoiceBox.getItems().size() > 0) //default value
            roomChoiceBox.setValue(roomChoiceBox.getItems().get(0));
    }

    public void onDateChangeListener(ActionEvent event)
    {
        showAvailableRooms(floorChoiceBox.getSelectionModel().selectedItemProperty()
                , floorChoiceBox.getValue(), floorChoiceBox.getValue());
    }

    public void changePriceField(ActionEvent event)
    {
        if (roomChoiceBox.getValue() != null)
        {
            priceField.setText(String.valueOf(roomChoiceBox.getValue().getPrice()));
        } else
        {
            priceField.setText("0.0");
        }

        double currentServicesPrice = 0.0;
        if (extraServices.getCheckModel() != null)
        {
            for (String service : extraServices.getCheckModel().getCheckedItems())
            {
                currentServicesPrice += HotelCore.get().getRoomServicePrice(service);
            }
        }

        double prevPrice = 0.0;
        if (priceField.getText() != null && !priceField.getText().trim().isEmpty())
            prevPrice = Double.parseDouble(priceField.getText());

        priceField.setText(String.valueOf(prevPrice + currentServicesPrice));
    }

    private void setChoiceBoxStringConverter()
    {
        roomChoiceBox.setConverter(new StringConverter<Room>()
        {
            @Override
            public String toString(Room room)
            {
                String toString = "";
                if (room != null)
                    toString = String.valueOf(room.getRoomNr());

                return toString;
            }

            @Override
            public Room fromString(String string)
            {
                return null;
            }
        });

        floorChoiceBox.setConverter(new StringConverter<Floor>()
        {
            @Override
            public String toString(Floor floor)
            {
                String toString = "";
                if (floor != null)
                    toString = String.valueOf(floor.getFloorNr());

                return toString;
            }

            @Override
            public Floor fromString(String string)
            {
                return null;
            }
        });
    }

}
