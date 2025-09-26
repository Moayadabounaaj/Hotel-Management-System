package de.zuse.hotel.gui;

import de.zuse.hotel.core.Address;
import de.zuse.hotel.core.HotelCore;
import de.zuse.hotel.core.Person;
import javafx.event.ActionEvent;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;


public class EditGuestController implements ControllerApi
{
    public AnchorPane root;
    public DatePicker bDateID;

    public TextField cityID;

    public TextField countreyID;

    public TextField emailID;

    public TextField firstNameID;


    public TextField houseNrID;

    public TextField lastNameID;

    public TextField plzID;

    public TextField streetID;

    public TextField telefonID;

    private Person selectedUser;

    public EditGuestController()
    {
    }

    public void saveChanges(ActionEvent actionEvent)
    {
        if (InfoController.showConfirmMessage(InfoController.LogLevel.Info, "Save Changes", "Are you sure?"))
        {
            selectedUser.setFirstName(firstNameID.getText());
            selectedUser.setLastName(lastNameID.getText());
            selectedUser.setEmail(emailID.getText());
            selectedUser.setTelNumber(telefonID.getText());
            selectedUser.setBirthday(bDateID.getValue());

            Address newAddress = new Address(countreyID.getText(), cityID.getText(), streetID.getText(),
                    plzID.getText(), Integer.parseInt(houseNrID.getText()));

            selectedUser.setAddress(newAddress);
            HotelCore.get().updateGuest(selectedUser); //update guest data
            JavaFxUtil.closeCurrentStage();
        }
    }

    private void getSelected(Person guest)
    {
        if (guest != null)
        {
            firstNameID.setText(guest.getFirstName());
            lastNameID.setText(guest.getLastName());
            emailID.setText(guest.getEmail());
            telefonID.setText(guest.getTelNumber());
            bDateID.setValue(guest.getBirthday());
            countreyID.setText(guest.getAddress().getCountry());
            cityID.setText(guest.getAddress().getCity());
            streetID.setText(guest.getAddress().getStreet());
            plzID.setText(guest.getAddress().getPlz());
            houseNrID.setText(Integer.toString(guest.getAddress().getHouseNr()));
        }
    }

    @Override
    public void onStart()
    {
        root.getStylesheets().add(SettingsController.getCorrectStylePath("BookingWindow.css"));
        bDateID.getStylesheets().add(SettingsController.getCorrectStylePath("datePickerStyle.css"));

        getSelected(selectedUser); //view the current guest  data

        JavaFxUtil.makeFieldOnlyNumbers(houseNrID);
        JavaFxUtil.makeFieldOnlyNumbers(plzID);
        JavaFxUtil.makeFieldOnlyNumbers(telefonID);

        JavaFxUtil.makeFieldOnlyChars(firstNameID);
        JavaFxUtil.makeFieldOnlyChars(lastNameID);
        JavaFxUtil.makeFieldOnlyChars(cityID);
        JavaFxUtil.makeFieldOnlyChars(countreyID);
        JavaFxUtil.makeFieldOnlyChars(streetID);
    }

    @Override
    public void onUpdate()
    {
    }

    public void cancelChanges(ActionEvent actionEvent)
    {
        JavaFxUtil.closeCurrentStage();
    }

    public void setSelectedUser(Person selectedUser)
    {
        this.selectedUser = selectedUser;
    }
}