package de.zuse.hotel.gui;

import de.zuse.hotel.core.Person;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Stack;
import java.util.function.Consumer;

public class JavaFxUtil
{
    private static Stack<Stage> stageLayers = new Stack<>();
    private static ControllerApi currentController = null; // pointer to current Window for refresh data


    public static void makeFieldOnlyNumbers(TextField textField)
    {
        if (textField == null)
            return;

        textField.textProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {
                if (!newValue.matches("\\d*"))
                {
                    textField.setText(newValue.replaceAll("[^\\d]", ""));
                }
            }
        });
    }

    public static void makeFieldOnlyChars(TextField textField)
    {
        if (textField == null)
            return;

        textField.textProperty().addListener(new ChangeListener<String>()
        {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue)
            {
                if (!newValue.matches(Person.NAME_REGEX))
                {
                    textField.setText(newValue.replaceAll(Person.NAME_REGEX, ""));
                }
            }
        });
    }

    public static void makeFieldOnlyNumericWithDecimal(TextField textField)
    {
        if (textField == null)
            return;

        textField.textProperty().addListener((observable, oldValue, newValue) ->
        {
            if (!newValue.matches("\\d*\\.?\\d*"))
            {
                textField.setText(oldValue);
            }
        });
    }

    public static Parent loadFxmlAsNode(URL fxmlName) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlName);
        Parent node = fxmlLoader.load();

        if (fxmlLoader.getController() instanceof ControllerApi)
        {
            currentController = fxmlLoader.getController();
            currentController.onStart();
        }

        return node;
    }

    public static void loadNewWindow(URL fxmlName, String windowTitle, Stage stage) throws IOException
    {
        if (stage == null)
            stage = new Stage();

        stageLayers.push(stage);
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlName);
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);


        if (windowTitle != null)
            stage.setTitle(windowTitle);

        stage.setScene(scene);
        stage.show();

        if (fxmlLoader.getController() instanceof ControllerApi)
        {
            currentController = fxmlLoader.getController();
            currentController.onStart();
        }
    }

    public static void loadNewWindow(URL fxmlName, String windowTitle, String iconUrl, Stage stage) throws IOException
    {
        if (stage == null)
            stage = new Stage();

        stageLayers.push(stage);
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlName);
        Scene scene = new Scene(fxmlLoader.load(), 1280, 720);
        Image icon = new Image(iconUrl);


        if (windowTitle != null)
            stage.setTitle(windowTitle);
        stage.getIcons().add(icon);
        stage.setScene(scene);
        stage.show();

        if (fxmlLoader.getController() instanceof ControllerApi)
        {
            currentController = fxmlLoader.getController();
            currentController.onStart();
        }
    }

    public static void loadPopUpWindow(URL fxmlName, String windowTitle, Consumer<Object> beforeShowing) throws IOException
    {
        FXMLLoader fxmlLoader = new FXMLLoader(fxmlName);
        Scene scene = new Scene(fxmlLoader.load());
        Stage stage = new Stage();
        stageLayers.push(stage);
        stage.setTitle(windowTitle);
        stage.setScene(scene);
        stage.initModality(Modality.APPLICATION_MODAL); //default, for closing th pop up window
        stage.resizableProperty().setValue(false);

        if (beforeShowing != null)
            beforeShowing.accept(fxmlLoader.getController());

        stage.show();

        if (fxmlLoader.getController() instanceof ControllerApi)
        {
            ((ControllerApi) fxmlLoader.getController()).onStart();
        }

        fxmlLoader.getController();
    }

    public static void onUpdate()
    {
        currentController.onUpdate();
    }

    public static void closeCurrentStage()
    {
        Stage currentStage = stageLayers.pop();
        currentStage.close();
    }

    public static void closeAllStages()
    {
        while (stageLayers.size() > 0)
        {
            closeCurrentStage();
        }
    }


}
