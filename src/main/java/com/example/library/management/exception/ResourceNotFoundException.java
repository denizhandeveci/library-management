package com.example.library.management.exception;

public class ResourceNotFoundException extends RuntimeException
{
    public ResourceNotFoundException(String message) {
        super(message);
    }

    public static ResourceNotFoundException forId(String resourceName, Long id) {
        return new ResourceNotFoundException((resourceName + " with id=" + id + " was not found"));
    }
}
