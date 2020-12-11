package com.packagename.dbs.model.products;


import com.packagename.dbs.model.Manufacturer;

import javax.persistence.*;
import java.util.Date;

@Entity
@Table(name = "book_copies")
public class BookCopy extends Product{

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = BookORM.class)
    @JoinColumn(name = "book_id", referencedColumnName = "`id`")
    private BookORM book;

    @Column(name="isbn")
    private String isbn;

    @Column(name = "day_of_publication")
    @Temporal(TemporalType.DATE)
    private Date dayOfPublication;

    public BookORM getBook() {
        return book;
    }

    public void setBook(BookORM book) {
        this.book = book;
    }

    public Date getDayOfPublication() {
        return dayOfPublication;
    }

    public void setDayOfPublication(Date dayOfPublication) {
        this.dayOfPublication = dayOfPublication;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }
}
