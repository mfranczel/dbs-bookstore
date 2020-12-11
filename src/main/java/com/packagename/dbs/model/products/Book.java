package com.packagename.dbs.model.products;

import com.packagename.dbs.model.Manufacturer;
import com.packagename.dbs.model.Store;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;

public class Book {
    private int id;
    private String name;
    private String author;
    private String store;
    private String genre;
    private String isbn;
    private String searchedResults;

    public String getSearchedResults() {
        return searchedResults;
    }

    public void setSearchedResults(String searchedResults) {
        this.searchedResults = searchedResults;
    }

    private int stock;

    public Collection getStores() {
        return stores;
    }

    public Collection getAvailability() {
        return availability;
    }

    public Collection getIsbns() {
        return isbns;
    }

    public Collection getPublicationDates() {
        return publicationDates;
    }

    public Collection getManufacturers() {
        return manufacturers;
    }

    Collection stores;
    Collection availability;
    Collection isbns;
    Collection publicationDates;
    Collection manufacturers;

    public Book(){
        this.stores = new ArrayList<Store>();
        this.availability = new ArrayList<Integer>();
        this.isbns = new ArrayList<String>();
        this.publicationDates = new ArrayList<Date>();
        this.manufacturers= new ArrayList<Manufacturer>();
        this.searchedResults = "";
    }

    public Book(int id, String name, String author, String genre) {
        this();
        this.name = name;
        this.author = author;
        this.genre = genre;
        this.id = id;
        this.searchedResults = "";
    }

    public Book(int id, String name, String author, String genre, String result) {
        this();
        this.name = name;
        this.author = author;
        this.genre = genre;
        this.id = id;
        this.searchedResults = result;
    }


    public Book(String name, String author, String store, int stock) {
        this();
        this.name = name;
        this.author = author;
        this.store = store;
        this.stock = stock;
        this.searchedResults = "";
    }

    public void setDetails(Collection stores, Collection availability, Collection isbns, Collection publicationDates, Collection manufacturers){
        this.stores = stores;
        this.availability = availability;
        this.publicationDates = publicationDates;
        this.isbns = isbns;
        this.manufacturers = manufacturers;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    private String publisher;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public int getId() {
        return id;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}
