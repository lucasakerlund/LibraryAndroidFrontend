package com.example.library.models;

public class Library {

    private int libraryId;
    private String name;
    private String address;
    private String county;

    public Library(int libraryId, String name, String address, String county) {
        this.libraryId = libraryId;
        this.name = name;
        this.address = address;
        this.county = county;
    }

    public int getLibraryId() {
        return libraryId;
    }

    public void setLibraryId(int libraryId) {
        this.libraryId = libraryId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }
}
