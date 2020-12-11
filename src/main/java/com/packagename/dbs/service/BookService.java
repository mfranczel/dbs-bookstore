package com.packagename.dbs.service;

import com.packagename.dbs.dao.BookDao;
import com.packagename.dbs.dao.repositories.BookCopyRepository;
import com.packagename.dbs.dao.inputHandling.SafeInput;
import com.packagename.dbs.dao.repositories.BookRepository;
import com.packagename.dbs.model.products.Book;
import com.packagename.dbs.model.products.BookCopy;
import com.packagename.dbs.model.products.BookORM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.web.SortDefault;
import org.springframework.data.web.config.EnableSpringDataWebSupport;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Service
public class BookService {
    ArrayList<Book>list = new ArrayList<>();
    BookDao dao = new BookDao();

    protected int start;
    protected int limit;

    @Autowired
    BookRepository bookRepository;
    Pageable pg;
    int prevCase;
    String saved_range;
    String saved_value;

    @Autowired
    BookCopyRepository bookCopyRepository;

    public BookService() {

        this.start = 0;
        this.limit = 100;

        this.prevCase = -1;

        this.saved_range = "";
        this.saved_value = "";

        this.pg = PageRequest.of(start, limit, Sort.unsorted());
    }


    public Collection getAllWithOffset(Object offset, Object limit) {
        Pageable page = PageRequest.of((int)offset, (int)limit);

        List<BookORM> allTenDollarProducts =
                this.bookRepository.findAll(page).toList();

        return allTenDollarProducts;
    }

  
    public Collection getAllBooks(){
        return bookRepository.findAll();
    }

    public List<BookORM> getByName(String title) {
        return bookRepository.getByName(SafeInput.searchAll(title), pg);
    }

    public List<BookCopy> getAllCopies(long bookId) {
        return this.bookRepository.getAllCopies(bookId);
    }

    public Collection<BookORM> getAllFromStore(String store){
        store = store.split(", ")[1];
        return this.bookRepository.getByStore(store);
    }

    public boolean transferBooks(String from, String to, double howMuch, String isbn){
        int howMuch1 = (int)howMuch;
        return dao.transferBooks(from, to, howMuch1, isbn);
    }

    public long count(){
        return this.bookRepository.count();
    }

    public Collection<BookORM> getByAuthor(String name) {
        return this.bookRepository.getByAuthor(SafeInput.searchAll(name), pg);
    }

    public Collection<BookORM> getByISBN(String isbn) {
        List<BookCopy> temp = this.bookRepository.getByISBN(isbn+"%", pg);
        Collection<BookORM> r = new ArrayList<BookORM>();
        for (BookCopy c : temp) {
            c.getBook().setSearchedResults( c.getIsbn());
            r.add( c.getBook() );
        }
        return r;
    }

    public Collection<BookORM> getByPublisher(String name) {

        List<BookCopy> temp = this.bookRepository.getByPublisher(SafeInput.searchAll(name), pg);
        Collection<BookORM> r = new ArrayList<BookORM>();
        for (BookCopy c : temp) {
            c.getBook().setSearchedResults( c.getManufacturer().getName());
            r.add( c.getBook() );
        }
        return r;
    }

    public Collection<BookORM> getByYear(String value, String range) {
        List<BookCopy> temp = this.bookRepository.getByYear( SafeInput.parseDate( SafeInput.addNewYear( value ) ), SafeInput.parseDate( SafeInput.addNewYear( SafeInput.addToYear( value, range ) ) ), pg);
        Collection<BookORM> r = new ArrayList<BookORM>();
        for (BookCopy c : temp) {
            c.getBook().setSearchedResults( c.getDayOfPublication().toString() );
            r.add( c.getBook() );
        }
        return r;
    }

    public Collection<BookORM> getOnStock() {
        return this.bookRepository.getOnStock(pg);
    }

    private Collection<BookORM> getNotStock() {
        return this.bookRepository.getNotStock(pg);
    }


    public Collection<BookORM> new_choice(int indexOf, String range, String val) {
        this.prevCase = indexOf;
        this.start = 0;
        this.limit = 100;
        this.pg = PageRequest.of(start, limit, Sort.unsorted());

        this.prevCase = indexOf;
        this.saved_range = "";
        this.saved_value = "";

        return this.choice( indexOf, range, val);
    }


    public Collection<BookORM> choice(int indexOf, String range, String val) {

        switch (indexOf) {
            case 0:
                this.saved_value = val;
                return this.getByName(val);
            case 1:
                this.saved_value = val;
                return this.getByAuthor(val);
            case 2:
                this.saved_value = val;
                this.saved_range = range;
                return this.getByYear(val, range);
            case 3:
                this.saved_value = val;
                return this.getByISBN(val);
            case 4:
                this.saved_value = val;
                return this.getByPublisher(val);
            case 5:
                return this.getOnStock();
            case 6:
                return this.getNotStock();
        }
        return this.getAllBooks();
    }

    public Collection<BookORM> getPrev() {
        this.pg = this.pg.previousOrFirst();
        return this.choice(this.prevCase, this.saved_range, this.saved_value);
    }

    public Collection<BookORM> getNext() {
        this.pg = this.pg.next();
        return this.choice(this.prevCase, this.saved_range, this.saved_value);
    }

    public Collection<BookORM> getALL() {
        this.pg = Pageable.unpaged();
        return this.choice(this.prevCase, this.saved_range, this.saved_value);
    }

}
