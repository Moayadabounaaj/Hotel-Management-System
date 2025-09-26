package de.zuse.hotel;

public interface Layer
{
    /**
     * Loading DB etc..
     */
    public void onStart();

    public void run(String[] args);

    /**
     * Unload DB and Cleanup
     */
    public void onClose();
}
