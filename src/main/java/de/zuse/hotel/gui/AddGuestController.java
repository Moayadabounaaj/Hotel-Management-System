package de.zuse.hotel.gui;

import de.zuse.hotel.core.Address;
import de.zuse.hotel.core.HotelCore;
import de.zuse.hotel.core.Person;
import javafx.event.ActionEvent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;


public class AddGuestController implements ControllerApi
{
    public TextField country;
    public TextField city;
    public TextField street;
    public TextField plz;
    public TextField houseNr;
    public AnchorPane window;

    public TextField lastName;

    public TextField firstName;

    public TextField email;

    public TextField telNum;

    public DatePicker birthDate;


    public void addGuest(ActionEvent event) throws Exception
    {
        if (houseNr.getText().strip().isEmpty() || plz.getText().strip().isEmpty())
        {
            InfoController.showMessage(InfoController.LogLevel.Error, "Room Number", "fill all information about Guest");
            return;
        }

        Address address = new Address(country.getText(), city.getText(), street.getText()
                , plz.getText(), Integer.parseInt(houseNr.getText()));

        Person guest = new Person(firstName.getText(), lastName.getText(), birthDate.getValue(), email.getText(),
                telNum.getText(), address);

        //add guest to database
        boolean info = HotelCore.get().addGuest(guest);
        if (info)
        {
            InfoController.showMessage(InfoController.LogLevel.Info,
                    "Information", "Guest Added Successfully!");
        }

        JavaFxUtil.closeCurrentStage();
    }

    @Override
    public void onStart()
    {
        window.getStylesheets().add(SettingsController.getCorrectStylePath("BookingWindow.css"));
        birthDate.getStylesheets().add(SettingsController.getCorrectStylePath("datePickerStyle.css"));

        JavaFxUtil.makeFieldOnlyNumbers(houseNr);
        JavaFxUtil.makeFieldOnlyNumbers(plz);
        JavaFxUtil.makeFieldOnlyNumbers(telNum);

        JavaFxUtil.makeFieldOnlyChars(firstName);
        JavaFxUtil.makeFieldOnlyChars(lastName);
        JavaFxUtil.makeFieldOnlyChars(city);
        JavaFxUtil.makeFieldOnlyChars(country);
        JavaFxUtil.makeFieldOnlyChars(street);
    }

    @Override
    public void onUpdate(){}

}
