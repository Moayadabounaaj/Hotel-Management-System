package de.zuse.hotel.core;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;


class AddressTest {
    private Address address;

    @BeforeEach
    public void init() {
        address = new Address("Syria", "Damascus", "aboromane", "66534", 30);
    }
    @Test
    void testCountry() {
        final String expectedCountry= "Syria";
        Address address = new Address();
        address.setCountry(expectedCountry);
        final String actualCountry = address.getCountry();
        assertEquals(expectedCountry, actualCountry);

        Assertions.assertThrows(Exception.class, () ->
        {
            address.setCountry(null);

            Assertions.assertThrows(Exception.class, () ->
            {
                address.setCountry("");
            });

        });
    }

    @Test
    void testStreet() {
        final String expectedStreet= "aboromane";
        Address address = new Address();
        address.setStreet(expectedStreet);
        final String actualStreet = address.getStreet();
        assertEquals(expectedStreet, actualStreet);

        Assertions.assertThrows(Exception.class, () ->
        {
            address.setStreet(null);

            Assertions.assertThrows(Exception.class, () ->
            {
                address.setStreet("");
            });

        });
    }

    @Test
    void testCity() {
        final String expectedCity= "Damascus";
        Address address = new Address();
        address.setCity(expectedCity);
        final String actualCity = address.getCity();
        assertEquals(expectedCity, expectedCity);

        Assertions.assertThrows(Exception.class, () ->
        {
            address.setCity(null);

            Assertions.assertThrows(Exception.class, () ->
            {
                address.setCity("");
            });

        });
    }

    @Test
    void testPlz() {
        final String expectedPlz = "01545";
        Address address = new Address();
        address.setPlz(expectedPlz);
        assertEquals(expectedPlz, address.getPlz());

        Assertions.assertThrows(Exception.class, () ->
        {
            address.setPlz("545");
        });
    }

    @Test
    void testHouseNr() {
        final int expectedHouseNr = 30;
        Address address = new Address();
        address.setHouseNr(expectedHouseNr);
        final int actualHouseNr = address.getHouseNr();
        assertEquals(expectedHouseNr, actualHouseNr);

        Assertions.assertThrows(Exception.class, () ->
        {
            address.setHouseNr(-2);
        });
    }
}