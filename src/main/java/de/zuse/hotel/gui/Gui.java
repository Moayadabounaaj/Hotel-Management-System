package de.zuse.hotel.gui;


import de.zuse.hotel.Layer;
import de.zuse.hotel.core.HotelCore;
import de.zuse.hotel.util.ZuseCore;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.image.Image;
import javafx.stage.Stage;



public class Gui extends Application implements Layer
{
    private static Gui instance = null;
    private Image image;

    public static Gui getInstance()
    {
        if (instance == null)
            instance = new Gui();

        return instance;
    }

    @Override
    public void onStart()
    {
        System.out.println("On Starting The Hotel App...");
        ZuseCore.bindCallbackErrorAction(this::handleErrorMessages);

    }

    @Override
    public void run(String[] args)
    {
        launch(args);
    }

    @Override
    public void onClose()
    {
        System.out.println("Closing the App");
        HotelCore.shutdown();
    }

    @Override
    public void start(Stage stage) throws Exception
    {
        JavaFxUtil.loadNewWindow(getClass().getResource("LoadingPage.fxml"), "Hotel v1.0",
                getClass().getResource("images/Untitled.jpg").toExternalForm(), stage);
    }

    public void startLoading()
    {
        HotelCore.init();
        HotelCore.get().bindOnUpdateAction(JavaFxUtil::onUpdate);

        // this takes so much ram, we could not know why
        // it allocates more than 1 GB on heap, it might be a bug in Image library
        //image = new Image(getClass().getResource("images/settingsBackground.gif").toExternalForm());
    }

    public void handleErrorMessages(String msg)
    {
        InfoController.showMessage(InfoController.LogLevel.Error, "Error", msg);
    }

    public void restartApp()
    {
        Platform.runLater(() ->
        {
            try
            {
                JavaFxUtil.closeAllStages();
                this.start(new Stage());
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        });
    }

    public Image getSettingsImage()
    {
        return image;
    }

}