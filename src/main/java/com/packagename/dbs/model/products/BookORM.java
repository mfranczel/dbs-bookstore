package com.packagename.dbs.model.products;

import com.packagename.dbs.model.Manufacturer;

import javax.persistence.*;

@Entity
@Table(name = "books")
public class BookORM {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name="book_name")
    private String name;

    @Column(name="genre")
    private String genre;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Author.class)
    @JoinColumn(name = "author", referencedColumnName = "`id`")
    private Author author;

    private String searchedResults;

    public BookORM() {
        this.searchedResults = "";
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAuthorName(){
        return this.author.getName();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public String getSearchedResults() {
        return searchedResults;
    }

    public void setSearchedResults(String searchedResults) {
        this.searchedResults = searchedResults;
    }
}


