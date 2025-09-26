package de.zuse.hotel.gui;

import de.zuse.hotel.core.HotelCore;
import javafx.event.ActionEvent;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class AddServicesWindowController implements ControllerApi{


    public AnchorPane addServiceRoot;

    public TextField serviceTextField;

    public TextField priceTextField1;



    public void addService (ActionEvent actionEvent)
    {
        if (serviceTextField.getText().isEmpty() || priceTextField1.getText() == null || priceTextField1.getText().strip().isEmpty())
        {
            InfoController.showMessage(InfoController.LogLevel.Error, "Add Service", "Please enter Service Name and Price!");
        } else
        {

            String sername = serviceTextField.getText();
            double price =  Math.round(Double.parseDouble(priceTextField1.getText()) * 100) / 100;;
            HotelCore.get().addNewRoomService(sername , price);
            HotelCore.get().getAllRoomServices().forEach(System.out::println);
            InfoController.showMessage(InfoController.LogLevel.Info, "Success", "Service added Successfuly!");
            JavaFxUtil.closeCurrentStage();
        }
    }

    @Override
    public void onStart()
    {
        addServiceRoot.getStylesheets().add(SettingsController.getCorrectStylePath("BookingWindow.css"));

        JavaFxUtil.makeFieldOnlyChars(serviceTextField);
        JavaFxUtil.makeFieldOnlyNumericWithDecimal(priceTextField1);

    }

    @Override
    public void onUpdate() {}
}
