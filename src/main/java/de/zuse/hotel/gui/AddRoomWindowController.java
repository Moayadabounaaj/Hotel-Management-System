package de.zuse.hotel.gui;

import de.zuse.hotel.core.Floor;
import de.zuse.hotel.core.HotelCore;
import de.zuse.hotel.core.Room;
import de.zuse.hotel.core.RoomSpecification;
import javafx.event.ActionEvent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class AddRoomWindowController implements ControllerApi
{
    public TextField roomprice;
    public AnchorPane Window;


    public TextField roomnumber;


    public ChoiceBox<Integer> floorChoiceBox;


    public ChoiceBox<RoomSpecification.Types> roomType;


    @Override
    public void onStart()
    {
        Window.getStylesheets().clear();
        Window.getStylesheets().add(SettingsController.getCorrectStylePath("BookingWindow.css"));

        List<RoomSpecification.Types> roomTypes = Arrays.stream(RoomSpecification.Types.values())
                .collect(Collectors.toList());
        roomType.getItems().addAll(roomTypes);
        roomType.setValue(RoomSpecification.Types.SINGLE);

        List<Integer> floorNumbers = HotelCore.get().getFloors().stream()
                .map(Floor::getFloorNr)
                .collect(Collectors.toList());
        floorChoiceBox.getItems().addAll(floorNumbers);
        if (floorNumbers.size() > 0)
            floorChoiceBox.setValue(floorChoiceBox.getItems().get(0));

        JavaFxUtil.makeFieldOnlyNumbers(roomnumber);
        JavaFxUtil.makeFieldOnlyNumbers(roomprice);
    }

    @Override
    public void onUpdate()
    {
    }

    public void addingRoom(ActionEvent actionEvent) throws Exception
    {
        String roomNr = roomnumber.getText();
        String price = roomprice.getText();

        if (roomNr.strip().isEmpty() || price.strip().isEmpty() || roomType.getValue() == null)
        {
            InfoController.showMessage(InfoController.LogLevel.Error, "Room Number", "fill all information about room");
            return;
        }

        Room room = new Room(HotelCore.get().getFloorByFloorNr(floorChoiceBox.getValue()), Integer.parseInt(roomNr),
                Double.parseDouble(price), roomType.getValue());

        HotelCore.get().addNewRoomToHotel(room.getFloorNr(), room);
        closeWindow();
    }


    public void closeWindow()
    {
        Stage stage = (Stage) roomnumber.getScene().getWindow();
        stage.close();
    }

}
