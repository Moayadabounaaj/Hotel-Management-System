package de.zuse.hotel.db;

import de.zuse.hotel.core.Payment;
import de.zuse.hotel.core.Person;

import java.time.LocalDate;
import java.util.List;

/* struct of data to search */
public class BookingSearchFilter
{
    // what is null, going to be ignored by searching
    public Integer roomNumber = null;
    public Integer floorNumber = null;
    public LocalDate startDate = null;
    public LocalDate endDate = null;
    public Person guest = null;
    public Payment payment = null;
    public Boolean canceled = null;
    public Long guestsNum = null;
    public List<String> extraServices = null;

    public BookingSearchFilter(){}
}
