package com.packagename.dbs.dao.repositories;

import com.packagename.dbs.model.products.Book;
import com.packagename.dbs.model.products.BookCopy;
import com.packagename.dbs.model.products.BookORM;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Date;
import java.util.List;

@Repository("BookRepository")
public interface BookRepository extends JpaRepository<BookORM, Long> {

    @Query("select a from BookORM a where lower(a.name) like ?1")
    List<BookORM> getByNameALL(String name);

    @Query("select a from BookORM a where lower(a.author.name) like ?1")
    List<BookORM> getByAuthorALL(String name);

    @Query("select a from BookCopy a where lower(a.manufacturer.name) like ?1")
    List<BookCopy> getByPublisherALL(String name);

    @Query("select a.book from BookCopy a where a.in_stock > 0")
    List<BookORM> getOnStockALL();

    @Query("select a.book from BookCopy a where a.in_stock < 1")
    List<BookORM> getNotStockALL();

    @Query("select a from BookCopy a where a.isbn like ?1")
    List<BookCopy> getByISBNALL(String isbn);

    @Query("select a from BookCopy a where a.dayOfPublication >= ?1 and a.dayOfPublication <= ?2")
    List<BookCopy> getByYearALL(Date year, Date rangeYear);

    ////

    @Query("select a.book from BookCopy a where a.store.street = ?1")
    List<BookORM> getByStore(String storeStreet);

    @Query("select a from BookCopy a where a.book.id = ?1")
    List<BookCopy> getAllCopies(long book_id);

    @Query("from BookCopy a where a.book.id = ?1")
    List<BookCopy> getAllCopiesS(long book_id);

    //PAGEABLE below

    @Query("select a from BookORM a where lower(a.name) like ?1")
    List<BookORM> getByName(String name, Pageable page);

    @Query("select a from BookORM a where lower(a.author.name) like ?1")
    List<BookORM> getByAuthor(String name,  Pageable page);

    @Query("select a from BookCopy a where lower(a.manufacturer.name) like ?1")
    List<BookCopy> getByPublisher(String name,  Pageable page);

    @Query("select a.book from BookCopy a where a.in_stock > 0")
    List<BookORM> getOnStock( Pageable page);

    @Query("select a.book from BookCopy a where a.in_stock < 1")
    List<BookORM> getNotStock(Pageable page);

    @Query("select a from BookCopy a where a.isbn like ?1")
    List<BookCopy> getByISBN(String isbn, Pageable page);

    @Query("select a from BookCopy a where a.dayOfPublication >= ?1 and a.dayOfPublication <= ?2")
    List<BookCopy> getByYear(Date year, Date rangeYear, Pageable page);

}
