package de.zuse.hotel.util;

/**
 * This Exception is safe to throw during runtime, it is our way to transfer error messages
 * and edge cases from and between the app layers (core, db, gui)
 * the exception gets caught from the javafx application loop
 */
public class BreakPointException extends RuntimeException
{
    public BreakPointException(String message)
    {
        super(message);
    }
}
