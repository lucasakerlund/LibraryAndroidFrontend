package com.example.library.models;

public class Book {

    private String title;
    private String isbn;
    private String published;
    private String image;

    public Book(String title, String isbn, String published, String image) {
        this.title = title;
        this.isbn = isbn;
        this.published = published;
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getPublished() {
        return published;
    }

    public void setPublished(String published) {
        this.published = published;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
