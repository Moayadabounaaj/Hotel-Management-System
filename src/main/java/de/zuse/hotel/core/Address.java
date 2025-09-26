package de.zuse.hotel.core;

import de.zuse.hotel.util.ZuseCore;

import javax.persistence.*;
import java.util.Objects;


/**
 * The Address class represents a physical address, including the country, city, street, postal code,
 * and house number.
 */
@Entity
@Table(name = "Address")
public class Address
{
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "Address_id", nullable = false)
    private int id;
    @Column(name = "Country", nullable = false)
    private String country;
    @Column(name = "City", nullable = false)
    private String city;
    @Column(name = "Street", nullable = false)
    private String street;
    @Column(name = "PostCode")
    private String plz;
    @Column(name = "House_Number")
    private int houseNr;

    public Address(String country, String city, String street, String plz, int houseNr)
    {
        ZuseCore.check(country != null && !country.strip().isEmpty(), "country can not be null");
        ZuseCore.check(city != null && !city.strip().isEmpty(), "city can not be null");
        ZuseCore.check(street != null && !street.strip().isEmpty(), "street can not be null");

        ZuseCore.check(plz.length() == 5, "The plz must contains 5 Nummbers");
        ZuseCore.check(houseNr >= 0, "The houseNr must be >= 0");

        this.country = country;
        this.city = city;
        this.street = street;
        this.plz = plz;
        this.houseNr = houseNr;
    }

    public Address()
    {
    }

    public int getId()
    {
        return id;
    }

    public String getCountry()
    {
        return country;
    }

    public String getStreet()
    {
        return street;
    }

    public String getCity()
    {
        return city;
    }

    public String getPlz()
    {
        return plz;
    }

    public int getHouseNr()
    {
        return houseNr;
    }

    public void setCountry(String country)
    {
        ZuseCore.check(country != null && !country.strip().isEmpty(), "country can not be null");
        this.country = country;
    }

    public void setCity(String city)
    {
        ZuseCore.check(city != null && !city.strip().isEmpty(), "city can not be null");
        this.city = city;
    }

    public void setStreet(String street)
    {
        ZuseCore.check(street != null && !street.strip().isEmpty(), "street can not be null");
        this.street = street;
    }

    public void setPlz(String newPlz)
    {
        ZuseCore.check(newPlz.length() == 5, "The plz must contains 5 Nummbers");
        this.plz = newPlz;
    }

    public void setHouseNr(int houseNr)
    {
        ZuseCore.check(houseNr >= 0, "The house-number must be >= 0");
        this.houseNr = houseNr;
    }


    @Override
    public String toString()
    {
        return country + ", " +  plz+ ", " + city + ", " +  street + ", " + houseNr;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Address address = (Address) o;
        return id == address.id && houseNr == address.houseNr && Objects.equals(country, address.country) && Objects.equals(city, address.city) && Objects.equals(street, address.street) && Objects.equals(plz, address.plz);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, country, city, street, plz, houseNr);
    }
}