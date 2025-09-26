package de.zuse.hotel.gui;


import de.zuse.hotel.core.*;
import de.zuse.hotel.db.BookingSearchFilter;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;


import java.io.IOException;
import java.util.List;
import java.util.function.Consumer;

public class RoomController implements ControllerApi
{
    public TableView<Room> roomTable;
    public AnchorPane rooms;
    public Text windowTitel;
    public ChoiceBox<Integer> floorChoiceBox;

    public TableColumn<Room, Integer> roomNrCln;

    public TableColumn<Room, RoomSpecification.Types> roomTypeCln;

    public TableColumn<Room, Double> priceCln;

    private Room currentSelectedRoom = null;

    public void viewRoomData()
    {
        if (floorChoiceBox.getValue() == null || floorChoiceBox.getItems().size() == 0)
            return;

        List<Room> rooms = HotelCore.get().getFloorByFloorNr(floorChoiceBox.getValue()).getRooms();

        if (rooms != null)
        {
            roomTable.getItems().clear();
            roomTable.setItems(FXCollections.observableArrayList(rooms));
        }
    }

    public void refreschFloorData()
    {
        if (HotelCore.get().getFloors().size() != floorChoiceBox.getItems().size())
        {
            floorChoiceBox.getItems().clear();
            floorChoiceBox.getItems().addAll(HotelCore.get().getFloors().stream().map(Floor::getFloorNr).toList());
            if (floorChoiceBox.getItems().size() > 0)
                floorChoiceBox.setValue(floorChoiceBox.getItems().get(0));
        }
    }

    @Override
    public void onUpdate()
    {
        refreschFloorData();
        viewRoomData();
    }

    public void onStart()
    {
        setupStyling();

        roomNrCln.setCellValueFactory(new PropertyValueFactory<>("roomNr"));
        priceCln.setCellValueFactory(new PropertyValueFactory<>("price"));
        roomTypeCln.setCellValueFactory(new PropertyValueFactory<>("roomType"));

        //roomTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        roomTable.getSelectionModel().selectedItemProperty().addListener(new ChangeListener()
        {
            @Override
            public void changed(ObservableValue observableValue, Object oldValue, Object newValue)
            {
                if (roomTable.getSelectionModel().getSelectedItem() != null)
                    currentSelectedRoom = roomTable.getSelectionModel().getSelectedItem();
                else
                    currentSelectedRoom = null;
            }
        });

        // set a default Floor 1
        refreschFloorData();
        viewRoomData();

        floorChoiceBox.setOnAction(this::onFloorChoiceChanged);
        if (floorChoiceBox.getItems().size() > 0)
            floorChoiceBox.setValue(floorChoiceBox.getItems().get(0));
    }


    public void onFloorChoiceChanged(ActionEvent actionEvent)
    {
        viewRoomData();
    }

    @FXML
    void handleAddRoomButtonAction(ActionEvent event) throws Exception
    {
        JavaFxUtil.loadPopUpWindow(getClass().getResource("addRoom.fxml"), "Add a room", null);
    }


    public void handelRemoveRoomButton(ActionEvent event)
    {
        if (currentSelectedRoom == null)
        {
            InfoController.showMessage(InfoController.LogLevel.Info, "Delete Room", "Select Room to be deleted");
            return;
        }

        BookingSearchFilter bookingSearchFilter = new BookingSearchFilter();
        bookingSearchFilter.roomNumber = currentSelectedRoom.getRoomNr();
        bookingSearchFilter.canceled = false;
        List<Booking> bookings = HotelCore.get().getBookingByFilter(bookingSearchFilter);
        if (bookings.size() > 0)
        {
            // Show message to confirm deleting
            boolean state = InfoController.showConfirmMessage(InfoController.LogLevel.Warn, "Removing Room Warning"
                    , "There is/are " + bookings.size() + " booking(s) with the room " + currentSelectedRoom.getRoomNr() +
                            " in Floor " + currentSelectedRoom.getFloorNr() +
                            " ,deleting the room will cancel all the bookings with it");

            if (state)
            {
                HotelCore.get().removeRoomFromHotel(currentSelectedRoom.getFloorNr(), currentSelectedRoom.getRoomNr());
                bookings.forEach(new Consumer<Booking>()
                {
                    @Override
                    public void accept(Booking booking)
                    {
                        HotelCore.get().removeBooking(booking.getBookingID());//cancel all the bookings
                    }
                });
            }

            return;
        }


        if (InfoController.showConfirmMessage(InfoController.LogLevel.Warn, "Delete Room", "Are you sure?"))
        {
            HotelCore.get().removeRoomFromHotel(currentSelectedRoom.getFloorNr(), currentSelectedRoom.getRoomNr());
        }
    }

    public void addFloor(ActionEvent actionEvent) throws IOException
    {
        JavaFxUtil.loadPopUpWindow(getClass().getResource("floorCapacity.fxml"), "Add a floor", null);
    }

    public void addServices(ActionEvent actionEvent)throws IOException
    {
        JavaFxUtil.loadPopUpWindow(getClass().getResource("addServices.fxml"), "Add a Services", null);
    }
    public void setupStyling()
    {
        rooms.getStylesheets().clear();
        if (SettingsController.currentMode == SettingsController.SystemMode.LIGHT)
            roomTable.setStyle("");

        roomTable.getStylesheets().add(SettingsController.getCorrectStylePath("Tableview.css"));
        floorChoiceBox.getStylesheets().add(SettingsController.getCorrectStylePath("BookingWindow.css"));
        rooms.getStylesheets().addAll(SettingsController.getCorrectStylePath("NavMenu.css")
                , SettingsController.getCorrectStylePath("background.css"));
    }

}