package de.zuse.hotel.db;

import java.time.LocalDate;

/* struct of data to search */
public class PersonSearchFilter
{
    // what is null, going to be ignored by searching
    public String firstName = null;
    public String lastName = null;
    public LocalDate birthday = null;
    public String email = null;
    public String telNumber = null;
    //public Address address = null; not supported (von Herr Basel und Herr Jandost) ;)
}
