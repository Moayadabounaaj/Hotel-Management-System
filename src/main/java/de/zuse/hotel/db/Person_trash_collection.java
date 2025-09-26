package de.zuse.hotel.db;
import de.zuse.hotel.core.Address;

import java.time.LocalDate;
import java.util.Date;
import javax.persistence.*;

@Entity
@Table(name = "Person_trash_collection")
public class Person_trash_collection {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Person_id")
    private int id;
    @Column(name = "Firstname", nullable = false)
    private String firstName;
    @Column(name = "Lastname", nullable = false)
    private String lastName;
    @Column(name = "Birthday", nullable = false)
    private LocalDate birthday;
    @Column(name = "Email")
    private String email;
    @Column(name = "Phone_Number", length = 12)
    private String telNumber;
    @OneToOne(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name = "Address_id", nullable = false)
    private Address address;
}