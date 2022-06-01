package com.example.library.models;

public class Book {

    private int bookId = 0;
    private int libraryId = 0;
    private String title;
    private String description;
    private String[] authors;
    private String[] genre;
    private String isbn;
    private String published;
    private int pages;
    private String language;
    private String imageSrc;

    public Book(int bookId, int libraryId, String title, String description, String[] authors, String[] genre, String isbn, String published, int pages, String language, String imageSrc) {
        this.bookId = bookId;
        this.libraryId = libraryId;
        this.title = title;
        this.description = description;
        this.authors = authors;
        this.genre = genre;
        this.isbn = isbn;
        this.published = published;
        this.pages = pages;
        this.language = language;
        this.imageSrc = imageSrc;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public int getLibraryId() {
        return libraryId;
    }

    public void setLibraryId(int libraryId) {
        this.libraryId = libraryId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String[] getAuthors() {
        return authors;
    }

    public void setAuthors(String[] authors) {
        this.authors = authors;
    }

    public String[] getGenre() {
        return genre;
    }

    public void setGenre(String[] genre) {
        this.genre = genre;
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

    public int getPages() {
        return pages;
    }

    public void setPages(int pages) {
        this.pages = pages;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getImageSrc() {
        return imageSrc;
    }

    public void setImageSrc(String imageSrc) {
        this.imageSrc = imageSrc;
    }
}
