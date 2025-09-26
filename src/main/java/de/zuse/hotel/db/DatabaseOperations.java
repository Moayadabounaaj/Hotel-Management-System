package de.zuse.hotel.db;

import de.zuse.hotel.core.Booking;

import java.util.List;

public interface DatabaseOperations
{
    public void dbCreate (Object object);
    public List<?> dbsearchAll();
    public <T> T dbsearchById  (int id);

    public void dbRemoveAll();
    public void dbRemoveById(int id);

    public void dbUpdate(Object object);

    public List<?> dbSerschforanythinhg(Object searchFilter);
}
