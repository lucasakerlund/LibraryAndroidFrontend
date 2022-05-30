package com.example.library.models;

public class CopyItem {

    private int bookId;
    private boolean available;

    public CopyItem(int bookId, boolean available) {
        this.bookId = bookId;
        this.available = available;
    }

    public int getBookId() {
        return bookId;
    }

    public void setBookId(int bookId) {
        this.bookId = bookId;
    }

    public boolean isAvailable() {
        return available;
    }

    public void setAvailable(boolean available) {
        this.available = available;
    }
}
