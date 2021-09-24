package com.bionix.models;

public class Book {
    public int id;
    public String title;
    public int publishYear;
    public int authorId;

    public Book(int id, String title, int publishYear, int authorId) {
        this.id = id;
        this.title = title;
        this.publishYear = publishYear;
        this.authorId = authorId;
    }
}
