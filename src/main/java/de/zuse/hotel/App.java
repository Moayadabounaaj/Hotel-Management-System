package de.zuse.hotel;

import de.zuse.hotel.gui.Gui;
import de.zuse.hotel.util.ConsoleDialogLayer;

public class App
{
    public static void main(String[] args)
    {
        Layer layer = new Gui();
        //Layer layer = new ConsoleDialogLayer();
        layer.onStart();
        layer.run(args);
        layer.onClose();
    }
}


