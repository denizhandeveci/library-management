package com.example.library.management.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;


@Entity
@Table(name = "admins")
public class Admin extends BaseEntity
{
    @Override
    public String getIdPrefix() {
        return "ADM-";
    }

    @Column(name = "name", nullable = false)
    public String name;

    @Column(name = "email", nullable = false, unique = true)
    public String email;

    @Column(name = "phone_number")
    public String phoneNumber;

    @Column(name = "address")
    public String address;

    @Column(name = "password_hash", nullable = false)
    public String passwordHash;

    public Admin() {}
}
