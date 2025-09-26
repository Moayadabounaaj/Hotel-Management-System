package de.zuse.hotel.gui;

import de.zuse.hotel.util.ZuseCore;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class SceneController implements ControllerApi
{
    private static final String BUTTON_SELECTED_STYLE_NAME = "on_button_selected";
    public AnchorPane background;
    public VBox menuBar;

    public Button guestBtnId;

    public Button roomsBtnId;

    public Button settingsBtnId;

    public Button dashboardBtnId;

    public BorderPane borderPane;

    public SceneController()
    {
    }

    public void onClickDashboardBtn(ActionEvent event) throws IOException
    {
        borderPane.setCenter(JavaFxUtil.loadFxmlAsNode(getClass().getResource("Dashboard.fxml")));
        onSwitchPanel(dashboardBtnId);
    }

    public void onClickRoomBtn(ActionEvent event) throws IOException
    {
        borderPane.setCenter(JavaFxUtil.loadFxmlAsNode(getClass().getResource("Rooms.fxml")));
        onSwitchPanel(roomsBtnId);
    }

    public void onClickGuestBtn(ActionEvent event) throws IOException
    {
        borderPane.setCenter(JavaFxUtil.loadFxmlAsNode(getClass().getResource("Guest.fxml")));
        onSwitchPanel(guestBtnId);
    }

    public void onClickSettingsBtn(ActionEvent event) throws IOException
    {
        borderPane.setCenter(JavaFxUtil.loadFxmlAsNode(getClass().getResource("Settings.fxml")));
        onSwitchPanel(settingsBtnId);
    }

    public void resetButtonsStyle()
    {
        //reset
        dashboardBtnId.getStyleClass().removeAll(BUTTON_SELECTED_STYLE_NAME);
        settingsBtnId.getStyleClass().removeAll(BUTTON_SELECTED_STYLE_NAME);
        roomsBtnId.getStyleClass().removeAll(BUTTON_SELECTED_STYLE_NAME);
        guestBtnId.getStyleClass().removeAll(BUTTON_SELECTED_STYLE_NAME);

        dashboardBtnId.setStyle("");
        settingsBtnId.setStyle("");
        roomsBtnId.setStyle("");
        guestBtnId.setStyle("");
    }

    private void onSwitchPanel(Button selectedBtn)
    {
        resetButtonsStyle();
        selectedBtn.getStyleClass().add(BUTTON_SELECTED_STYLE_NAME);
        selectedBtn.setStyle("-fx-cursor: hand; -fx-text-fill: #ffffff;"); //disable hovor effect
    }

    public void handleBookRoomButtonAction(ActionEvent event) throws Exception
    {
        JavaFxUtil.loadPopUpWindow(getClass().getResource("BookingWindow.fxml"), "Book a room", null);
    }

    @Override
    public void onStart()
    {
        background.getStylesheets().add(SettingsController.getCorrectStylePath("background.css"));
        menuBar.getStylesheets().add(SettingsController.getCorrectStylePath("NavMenu.css"));

        try
        {
            borderPane.setCenter(JavaFxUtil.loadFxmlAsNode(getClass().getResource("Dashboard.fxml")));
            onSwitchPanel(dashboardBtnId);
        } catch (Exception e)
        {
            if (ZuseCore.DEBUG_MODE)
                e.printStackTrace();
        }
    }

    @Override
    public void onUpdate()
    {
    }

}
