package com.example.library.management.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "users")
public class User extends BaseEntity
{
    @Override
    public String getIdPrefix() {
        return "USR-";
    }

    @Column(name = "name")
    public String name;

    @Column(name = "email")
    public String email;

    @Column(name = "phone_number")
    public String phoneNumber;

    @Column(name = "address")
    public String address;

    @Column(nullable = false)
    public String password;

    public User() {}
}
