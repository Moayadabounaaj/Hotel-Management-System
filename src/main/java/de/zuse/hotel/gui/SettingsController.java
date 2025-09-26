package de.zuse.hotel.gui;

import java.net.URL;
import java.util.ResourceBundle;


import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;


public class SettingsController implements Initializable
{
    public ImageView backgroundImage_btn;

    public enum SystemMode
    {
        LIGHT, DARK;
    }

    public AnchorPane anchor;
    public Button imageD;
    public Button imageL;

    public static SystemMode currentMode = SystemMode.DARK;
    //relative path to project.dir

    private static class Wrapper
    {
    }

    public void changeToDarkmood()
    {
        imageD.setOnMouseClicked(e ->
        {
            if (currentMode == SystemMode.DARK)
                return;

            currentMode = SystemMode.DARK;
            Gui.getInstance().restartApp();
        });
    }

    public void changeToLightmood()
    {
        imageL.setOnMouseClicked(e ->
        {
            if (currentMode == SystemMode.LIGHT)
                return;

            currentMode = SystemMode.LIGHT;
            Gui.getInstance().restartApp();
        });
    }

    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        changeToDarkmood();
        changeToLightmood();

        Image image = Gui.getInstance().getSettingsImage();
        if (image != null)
            backgroundImage_btn.setImage(image);
    }

    public static String getCorrectStylePath(String fileName)
    {
        Wrapper wrapper = new Wrapper();

        switch (currentMode)
        {
            case DARK:
                return wrapper.getClass().getResource("styling/darkMode/" + fileName).toExternalForm();
            case LIGHT:
                return wrapper.getClass().getResource("styling/lightMode/" + fileName).toExternalForm();
        }

        return wrapper.getClass().getResource("styling/darkMode/" + fileName).toExternalForm();
    }

    public static SystemMode getMode()
    {
        return currentMode;
    }

    public static void setMode(SystemMode mode)
    {
        currentMode = mode;
    }
}
