package de.zuse.hotel.gui;

import de.zuse.hotel.core.Address;
import de.zuse.hotel.core.HotelCore;
import de.zuse.hotel.core.Person;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class GuestController implements ControllerApi
{
    public AnchorPane root;
    public Text guestTitleId;
    public Text searchTextid;
    public TableView<Person> guestTable;

    public TableColumn<Person, Integer> guestIdCln;


    public TableColumn<Person, String> firstNameCln;

    public TableColumn<Person, String> lastNameCln;

    public TableColumn<Person, LocalDate> birthdayCln;
    public TableColumn<Person, String> emailCln;

    public TableColumn<Person, String> telefonCln;

    public TableColumn<Person, Address> addressCln;

    private Person selectedUser = null;


    public TextField seachBarID;

    @Override
    public void onStart()
    {
        setupStyling();
        guestIdCln.setCellValueFactory(new PropertyValueFactory<>("id"));
        firstNameCln.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameCln.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        birthdayCln.setCellValueFactory(new PropertyValueFactory<>("birthday"));
        emailCln.setCellValueFactory(new PropertyValueFactory<>("email"));
        telefonCln.setCellValueFactory(new PropertyValueFactory<>("telNumber"));
        addressCln.setCellValueFactory(new PropertyValueFactory<>("address"));
        //refresh the database and load the data from it on the table
        onRefresh();

        guestTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue observableValue, Object oldValue, Object newValue)
            {
                if (guestTable.getSelectionModel().getSelectedItem() != null)
                    selectedUser = guestTable.getSelectionModel().getSelectedItem();
                else
                    selectedUser = null;
            }
        });

        seachBarID.textProperty().addListener((observable, oldValue, newValue) ->
        { //listener on the table, that calls the seachGuests method when it is triggered by changing the textfield of it and seach for the changed text on the textfield(new value)
            searchGuests(newValue);
        });
    }

    @Override
    public void onUpdate()
    {
        onRefresh();
    }

    public void onRefresh()
    {
        guestTable.getItems().clear();
        List<Person> personList = HotelCore.get().getAllGuest();
        ObservableList<Person> pList = FXCollections.observableArrayList(personList);
        guestTable.setItems(pList);
    }

    public void addGuestBtn(ActionEvent event) throws Exception
    {
        createFXMLoader("addGuest.fxml", "Add a Guest");
    }

    public void deleteGuestBtn(ActionEvent event) throws Exception
    {
        createFXMLoader("deleteGuest.fxml", "Delete a Guest");
    }

    public void updateGuestBtn(ActionEvent event) throws Exception
    {
        if (selectedUser != null)
            createFXMLoader("editGuest.fxml", "Update a Guest");
        else
            InfoController.showMessage(InfoController.LogLevel.Error, "Update Guest", "No Guest was selected on the table! Please select a Guest.");
    }

    public void createFXMLoader(String string, String description) throws Exception
    {
        Consumer<Object> consumer = (Object obj) ->
        {
            if (obj instanceof EditGuestController)
            {
                ((EditGuestController) obj).setSelectedUser(selectedUser);
            }
        };

        JavaFxUtil.loadPopUpWindow(getClass().getResource(string), description, consumer);
    }

    public void searchGuests(String string)
    {
        List<Person> filteredGuests = HotelCore.get().getAllGuest().stream()
                .filter(guest -> guest.getFirstName().toLowerCase().contains(string.toLowerCase())
                        || guest.getLastName().toLowerCase().contains(string.toLowerCase())
                        || guest.getEmail().toLowerCase().contains(string.toLowerCase())
                        || guest.getTelNumber().toLowerCase().contains(string.toLowerCase())
                        || String.valueOf(guest.getId()).contains(string)) // search for ID as a string
                .collect(Collectors.toList()); //stream filter search

        ObservableList<Person> GuestList = FXCollections.observableArrayList(filteredGuests); //refresh the table
        guestTable.setItems(GuestList);
    }

    public void setupStyling()
    {
        root.getStylesheets().clear();
        if (SettingsController.currentMode == SettingsController.SystemMode.LIGHT)
            guestTable.setStyle("");

        root.getStylesheets().add(SettingsController.getCorrectStylePath("background.css"));
        root.getStylesheets().add(SettingsController.getCorrectStylePath("NavMenu.css"));
        guestTable.getStylesheets().add(SettingsController.getCorrectStylePath("Tableview.css"));
        seachBarID.getStylesheets().add(SettingsController.getCorrectStylePath("BookingWindow.css"));
    }

}
