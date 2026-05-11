package com.example.library.management.entity;

import jakarta.persistence.*;


import java.util.List;

@Entity
@Table(name="users")
public class User
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public Long id;

    @Column(name="name")
    public String name;

    @Column(name="email")
    public String email;

    @Column(name="phone_number")
    public String phoneNumber;

    @Column(name="address")
    public String address;

    @Column(nullable = false)
    public String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    public List<Loan> loans;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    public List<Reservation> reservations;

    public User(){}
}
