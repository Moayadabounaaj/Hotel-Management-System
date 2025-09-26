package de.zuse.hotel.util;

import java.time.LocalDate;
import java.util.function.Consumer;

public class ZuseCore
{
    public static final boolean DEBUG_MODE = true;
    private static Consumer<String> callback;

    public static void bindCallbackErrorAction(Consumer<String> func)
    {
        coreAssert(func != null, "You have to set a valid callback error function!!");

        callback = func;
    }

    /**
     * If the condition fails, We throw the exception to stop app from executing the next statement.
     * We want the application to stop at this point, similar to an empty return statement
     */
    public static void check(boolean condition, String msg)
    {
        if (!condition)
        {
            if (callback != null)
                callback.accept(msg);

            throw new BreakPointException(msg);
        }
    }

    /**
     * This is only for debugging, and it works only in debug mode.
     * If the condition fails, we want the application to crash.
     */
    public static void coreAssert(boolean condition, String msg)
    {
        if (DEBUG_MODE)
        {
            try
            {
                if (!condition)
                    throw new BreakPointException(msg);
            } catch (BreakPointException e)
            {
                e.printStackTrace();
                System.exit(1);
            }
        }
    }

    public static void isValidDate(LocalDate date, String msg)
    {
        if (date.isBefore(LocalDate.now()))
        {
            check(false, msg);
        }
    }


}
