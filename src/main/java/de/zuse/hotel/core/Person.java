package de.zuse.hotel.core;

import de.zuse.hotel.util.ZuseCore;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * representing a person with a unique ID, first name, last name,
 * birthday, email, telephone number, and address.
 */
@Entity
@Table(name = "Person")
public class Person {
    private static final int TELEPHONE_NUMBER_COUNT = 12;
    public static final String EMAIL_REGEX = "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-äöüÄÖÜß]+\\.)+[a-zA-Z]{2,7}$";
    public static final String NAME_REGEX = "[A-Za-zÄäÖöÜüß\\s]+";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Person_id", nullable = false, updatable = false)
    private int id;
    @Column(name = "Firstname", nullable = false)
    private String firstName;
    @Column(name = "Lastname", nullable = false)
    private String lastName;
    @Column(name = "Birthday", nullable = false)
    private LocalDate birthday;
    @Column(name = "Email")
    private String email;
    @Column(name = "Phone_Number", length = TELEPHONE_NUMBER_COUNT)
    private String telNumber;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "Address_id", nullable = false)
    private Address address;

    //private ArrayList<Integer> bookingID; //TODO

    public Person(String firstName, String lastName, LocalDate birthday, String email, String telNumber, Address address)
    {
        ZuseCore.check(firstName != null && !firstName.strip().isEmpty(), "The FirstName can not be null!");
        ZuseCore.check(firstName.matches(NAME_REGEX), "First Name should be only with chars!");
        ZuseCore.check(lastName != null && !lastName.strip().isEmpty(), "The LastName can not be null!");
        ZuseCore.check(lastName.matches(NAME_REGEX), "Last Name should be only with chars!");
        ZuseCore.check(email != null && !email.strip().isEmpty(), "The Email can not be null");
        ZuseCore.check(telNumber.length() == TELEPHONE_NUMBER_COUNT, "The Telefonnr must contains" + TELEPHONE_NUMBER_COUNT + " nummbers");
        ZuseCore.check(address != null, "address can not be null!!");
        ZuseCore.check(is18OrOlder(birthday), "The Person must be 18 years old!!");

        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        ZuseCore.check(matcher.matches(), "The provided email address is not valid");

        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.email = email.trim().toLowerCase();
        this.telNumber = telNumber;
        this.address = address;
    }

    public Person() {}

    public String getFirstName()
    {
        return firstName;
    }

    public void setFirstName(String firstName)
    {
        ZuseCore.check(firstName != null && !firstName.strip().isEmpty(), "The FirstName can not be null!");
        ZuseCore.check(firstName.matches(NAME_REGEX), "First Name should be only with chars!");

        this.firstName = firstName;
    }

    public String getLastName()
    {
        return lastName;
    }

    public void setLastName(String lastName)
    {
        ZuseCore.check(lastName != null && !lastName.strip().isEmpty(), "The LastName can not be null!");
        ZuseCore.check(lastName.matches(NAME_REGEX), "Last Name should be only with chars!");

        this.lastName = lastName;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        ZuseCore.check(id >= 0, "The Id must be >= 0!");
        this.id = id;
    }

    public LocalDate getBirthday()
    {
        return birthday;
    }

    public void setBirthday(LocalDate birthday)
    {
        ZuseCore.check(is18OrOlder(birthday), "The Person must be 18 years old!!");

        this.birthday = birthday;
    }

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        ZuseCore.check(email != null && !email.trim().isEmpty(), "The Email can not be null");
        email = email.trim().toLowerCase();

        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);

        ZuseCore.check(matcher.matches(), "The provided email address is not valid");

        this.email = email;
    }

    public String getTelNumber()
    {
        return telNumber;
    }

    public void setTelNumber(String telNumber)
    {
        ZuseCore.check(String.valueOf(telNumber).length() == 12, "The Telefonnr must contains 12 nummbers");
        this.telNumber = telNumber;
    }

    public Address getAddress()
    {
        return address;
    }

    public void setAddress(Address address)
    {
        ZuseCore.check(address != null, "address can not be null!!");
        this.address = address;
    }

    @Override
    public String toString()
    {
        return "{" +
                "firstname='" + firstName + "," +
                ", lastname='" + lastName + "," +
                ", id=" + id +
                ", birthday=" + birthday +
                ", email='" + email + "," +
                ", telephone Number=" + telNumber + "," +
                ", address=" + address + "\n" +
                '}';
    }

    /**
     * A utility function related to age calculation and validation.
     */
    public static boolean is18OrOlder(LocalDate birthDate)
    {
        LocalDate now = LocalDate.now();
        LocalDate eighteenYearsAgo = now.minusYears(18);
        return !birthDate.isAfter(eighteenYearsAgo);
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return id == person.id && Objects.equals(firstName, person.firstName) && Objects.equals(lastName, person.lastName) && Objects.equals(birthday, person.birthday) && Objects.equals(email, person.email) && Objects.equals(telNumber, person.telNumber) && Objects.equals(address, person.address);
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(id, firstName, lastName, birthday, email, telNumber, address);
    }
}
