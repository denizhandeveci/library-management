package com.example.library.management.dto.book;

public enum BookSortField
{
    TITLE("title"),
    AUTHOR("author"),
    GENRE("genre"),
    ISBN("isbn");

    private final String propertyName;

    BookSortField(String propertyName) {
        this.propertyName = propertyName;
    }

    public String propertyName() {
        return propertyName;
    }
}
