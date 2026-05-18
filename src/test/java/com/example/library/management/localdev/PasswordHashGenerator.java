package com.example.library.management.localdev;

import org.springframework.security.crypto.factory.PasswordEncoderFactories;

public class PasswordHashGenerator
{
    public static void main(String[] args) {
        var encoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

        System.out.println("admin123: " + encoder.encode("admin123"));
        System.out.println("password123: " + encoder.encode("password123"));
    }
}
