package com.example.library.management.entity;

import jakarta.persistence.*;


@Entity
@Table(name="admins")
public class Admin
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    public Long id;

    @Column(name="name")
    public String name;

    @Column(name="email")
    public String email;

    @Column(name="phone_number")
    public String phoneNumber;

    @Column(name="address")
    public String address;

    @Column(unique = true, nullable = false)
    public String password;

    public Admin(){}
}
