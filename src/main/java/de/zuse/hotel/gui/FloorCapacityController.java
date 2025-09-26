package de.zuse.hotel.gui;

import de.zuse.hotel.core.Floor;
import de.zuse.hotel.core.HotelCore;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class FloorCapacityController implements ControllerApi
{

    public AnchorPane floorCapacityRoot;
    public TextField capacityTextField;

    public void addCapacity(ActionEvent actionEvent)
    {
        if (capacityTextField.getText().isEmpty())
        {
            InfoController.showMessage(InfoController.LogLevel.Error, "Floor capacity", "Please enter floor capacity!");
        } else
        {
            Floor newFloor = new Floor(HotelCore.get().getFloors().size() + 1, Integer.parseInt(capacityTextField.getText()));

            HotelCore.get().addNewFloorToHotel(newFloor);
            InfoController.showMessage(InfoController.LogLevel.Info, "Success", "Floor added Successfuly!");
            JavaFxUtil.closeCurrentStage();
        }
    }


    @Override
    public void onStart()
    {
        floorCapacityRoot.getStylesheets().add(SettingsController.getCorrectStylePath("BookingWindow.css"));
    }

    @Override
    public void onUpdate()
    {

    }
}
