package com.packagename.dbs.model;

public class CompanyStat {

    private String street;
    private String city;
    private long num_of_employees;
    private long shops_per_city;
    private long bookTitles;
    private long bookCopiesAvailable;
    private long avgTitles;
    private long avgCopies;


    public long getBookTitles() {
        return bookTitles;
    }

    public void setBookTitles(long bookTitles) {
        this.bookTitles = bookTitles;
    }

    public long getBookCopiesAvailable() {
        return bookCopiesAvailable;
    }

    public void setBookCopiesAvailable(long bookCopiesAvailable) {
        this.bookCopiesAvailable = bookCopiesAvailable;
    }



    public CompanyStat(){}
    public CompanyStat(String street, int num_of_employees){
        this.street = street;
        this.num_of_employees = num_of_employees;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public long getNum_of_employees() {
        return num_of_employees;
    }

    public void setNum_of_employees(long num_of_employees) {
        this.num_of_employees = num_of_employees;
    }

    public long getShops_per_city() {
        return shops_per_city;
    }

    public void setShops_per_city(long shops_per_city) {
        this.shops_per_city = shops_per_city;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public long getAvgTitles() {
        return avgTitles;
    }

    public void setAvgTitles(long avgTitles) {
        this.avgTitles = avgTitles;
    }

    public long getAvgCopies() {
        return avgCopies;
    }

    public void setAvgCopies(long avgCopies) {
        this.avgCopies = avgCopies;
    }
}