package com.packagename.dbs.dao;

import com.packagename.dbs.dao.inputHandling.SafeInput;
import com.packagename.dbs.model.products.Book;
import com.packagename.dbs.model.Manufacturer;
import com.packagename.dbs.model.Store;

import java.sql.*;
import java.sql.Date;
import java.util.*;

@Deprecated
public class BookDao implements Dao{

    private String url = "jdbc:postgresql://localhost:5432/postgres";
    private String user = "postgres";
    private String password = "postgres";

    @Override
    public Optional get(int id) {
        return Optional.empty();
    }

    @Override
    public Collection getAll() {

        String ALL_SELECT_SQL = "SELECT books.id, book_name, genre, authors.name FROM books INNER JOIN authors ON books.author = authors.id";

        try (Connection con = (Connection) DriverManager.getConnection(url,  user, password);
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(ALL_SELECT_SQL)) {

            Collection<Book> books = new ArrayList<Book>();

            while(rs.next()) {
                Book temp = new Book(
                        rs.getInt("id"),
                        rs.getString("book_name"),
                        rs.getString("name"),
                        rs.getString("genre")
                );
                books.add(temp);
            }

            return books;

        } catch (SQLException ex) {

            System.out.println("SQL Connection ERROR");

        }
        return null;
    }

    public Collection getByName(String title) {

        String ALL_SELECT_SQL = "SELECT books.id, books.book_name, genre, authors.name FROM books JOIN authors ON books.author = authors.id WHERE lower(books.book_name) LIKE ?";


        try (Connection con = (Connection) DriverManager.getConnection(url,  user, password)){
            PreparedStatement st = con.prepareStatement(ALL_SELECT_SQL);
             st.setString(1, SafeInput.searchAll( title ));
            ResultSet rs = st.executeQuery();

            Collection<Book> books = new ArrayList<Book>();

            while(rs.next()) {
                Book temp = new Book(
                        rs.getInt("id"),
                        rs.getString("book_name"),
                        rs.getString("name"),
                        rs.getString("genre")
                );
                books.add(temp);
            }

            return books;

        } catch (SQLException ex) {

            System.out.println("SQL Connection ERROR");

        }
        return null;
    }


    public Collection getByAuthor(String name) {

        String ALL_SELECT_SQL = "SELECT books.id, books.book_name, genre, authors.name FROM books JOIN authors ON books.author = authors.id WHERE lower(authors.name) LIKE ?";


        try (Connection con = (Connection) DriverManager.getConnection(url,  user, password)){
            PreparedStatement st = con.prepareStatement(ALL_SELECT_SQL);
            st.setString(1, SafeInput.searchAll( name ));
            ResultSet rs = st.executeQuery();

            Collection<Book> books = new ArrayList<Book>();

            while(rs.next()) {
                Book temp = new Book(
                        rs.getInt("id"),
                        rs.getString("book_name"),
                        rs.getString("name"),
                        rs.getString("genre")
                );
                books.add(temp);
            }

            return books;

        } catch (SQLException ex) {

            System.out.println("SQL Connection ERROR");

        }
        return null;
    }

    public Collection getByPublisher(String name) {

        String ALL_SELECT_SQL = "SELECT books.id, books.book_name, authors.name, genre, manufacturers.name AS publisher FROM books\n" +
                "  INNER JOIN book_copies ON book_copies.book_id = books.id\n" +
                "  INNER JOIN manufacturers on book_copies.manufacturer_id = manufacturers.id\n" +
                "  INNER JOIN authors on books.author = authors.id WHERE lower(manufacturers.name) LIKE ?";


        try (Connection con = (Connection) DriverManager.getConnection(url,  user, password)){
            PreparedStatement st = con.prepareStatement(ALL_SELECT_SQL);
            st.setString(1, SafeInput.searchAll( name ));
            ResultSet rs = st.executeQuery();

            Collection<Book> books = new ArrayList<Book>();

            while(rs.next()) {
                Book temp = new Book(
                        rs.getInt("id"),
                        rs.getString("book_name"),
                        rs.getString("name"),
                        rs.getString("genre"),
                        rs.getString("publisher")
                );
                books.add(temp);
            }

            return books;

        } catch (SQLException ex) {

            System.out.println("SQL Connection ERROR");

        }
        return null;
    }


    public Collection<Book> getOnStock() {

        String ALL_SELECT_SQL = "SELECT books.id, books.book_name, authors.name, genre FROM books\n" +
                "  INNER JOIN book_copies ON book_copies.book_id = books.id\n" +
                "  INNER JOIN manufacturers on book_copies.manufacturer_id = manufacturers.id\n" +
                "  INNER JOIN authors on books.author = authors.id WHERE book_copies.in_stock > 0";


        try (Connection con = (Connection) DriverManager.getConnection(url,  user, password)){
            PreparedStatement st = con.prepareStatement(ALL_SELECT_SQL);
            ResultSet rs = st.executeQuery();

            Collection<Book> books = new ArrayList<Book>();

            while(rs.next()) {
                Book temp = new Book(
                        rs.getInt("id"),
                        rs.getString("book_name"),
                        rs.getString("name"),
                        rs.getString("genre")
                );
                books.add(temp);
            }

            return books;

        } catch (SQLException ex) {

            System.out.println("SQL Connection ERROR");

        }
        return null;

    }


    public Collection<Book> getNotStock() {

        String ALL_SELECT_SQL = "SELECT books.id, books.book_name, authors.name, genre FROM books\n" +
                "  INNER JOIN book_copies ON book_copies.book_id = books.id\n" +
                "  INNER JOIN manufacturers on book_copies.manufacturer_id = manufacturers.id\n" +
                "  INNER JOIN authors on books.author = authors.id WHERE book_copies.in_stock <= 0";


        try (Connection con = (Connection) DriverManager.getConnection(url,  user, password)){
            PreparedStatement st = con.prepareStatement(ALL_SELECT_SQL);
            ResultSet rs = st.executeQuery();

            Collection<Book> books = new ArrayList<Book>();

            while(rs.next()) {
                Book temp = new Book(
                        rs.getInt("id"),
                        rs.getString("book_name"),
                        rs.getString("name"),
                        rs.getString("genre")
                );
                books.add(temp);
            }

            return books;

        } catch (SQLException ex) {

            System.out.println("SQL Connection ERROR");

        }
        return null;

    }


    public Collection<Book> getByISBN(String publisher){

        String SELECT_SQL = "SELECT books.id, books.book_name, authors.name, genre, book_copies.isbn AS isbn FROM books" +
        " INNER JOIN book_copies ON book_copies.book_id = books.id" +
        " INNER JOIN authors ON books.author = authors.id WHERE book_copies.isbn LIKE ?";

        try (Connection con = (Connection) DriverManager.getConnection(url,  user, password)){
            PreparedStatement st = con.prepareStatement(SELECT_SQL);
            st.setString(1, publisher+'%');
            ResultSet rs = st.executeQuery();

            Collection<Book> books = new ArrayList<Book>();

            while(rs.next()) {
                Book temp = new Book(
                        rs.getInt("id"),
                        rs.getString("book_name"),
                        rs.getString("name"),
                        rs.getString("genre"),
                        rs.getString( "isbn" )
                );
                books.add(temp);
            }
            return books;

        } catch (SQLException ex) {
            System.out.println("SQL Connection ERROR");
        }
        return new ArrayList<Book>();
    }


    public Collection<Book> getByYear(String value, String range) {


        String SELECT_SQL = "SELECT books.id, books.book_name, authors.name, genre, to_char(day_of_publication, 'YYYY') AS year FROM books" +
                " INNER JOIN book_copies ON book_copies.book_id = books.id" +
                " INNER JOIN authors ON books.author = authors.id WHERE to_char(day_of_publication, 'YYYY') >= ? AND to_char(day_of_publication, 'YYYY') <= ?";

        try (Connection con = (Connection) DriverManager.getConnection(url,  user, password)){
            PreparedStatement st = con.prepareStatement(SELECT_SQL);
            st.setString(1, value);
            st.setString(2, SafeInput.addToYear(value, range));
            ResultSet rs = st.executeQuery();

            Collection<Book> books = new ArrayList<Book>();

            while(rs.next()) {
                Book temp = new Book(
                        rs.getInt("id"),
                        rs.getString("book_name"),
                        rs.getString("name"),
                        rs.getString("genre"),
                        rs.getString( "year" )
                );
                books.add(temp);
            }
            return books;

        } catch (SQLException ex) {
            System.out.println("SQL Connection ERROR");
        }

        return new ArrayList<Book>();


    }



    public Collection<Book> getAllFromStore(String address){

        String SELECT_SQL = "SELECT book_name, isbn, in_stock FROM book_copies " +
                "INNER JOIN books ON book_copies.book_id=books.id " +
                "WHERE book_copies.store_id=(SELECT id FROM stores WHERE street=?)";

        try (Connection con = (Connection) DriverManager.getConnection(url,  user, password)){
            PreparedStatement st = con.prepareStatement(SELECT_SQL);
            st.setString(1, address);
            ResultSet rs = st.executeQuery();

            ArrayList<Book> books = new ArrayList<>();

            while(rs.next()) {
                Book temp = new Book();
                temp.setName(rs.getString("book_name"));
                temp.setIsbn(rs.getString("isbn"));
                temp.setStock(rs.getInt("in_stock"));
                books.add(temp);
            }
            return books;


        } catch (SQLException e) {

            System.out.println("SQL Connection ERROR " + e.toString());

        }
        return null;
    }

    public Book getWholeBookInfo(Book book) {

        Collection stores = new ArrayList<Store>();
        Collection availability = new ArrayList<Integer>();
        Collection isbns = new ArrayList<String>();
        Collection publicationDates = new ArrayList<Date>();
        Collection manufacturers= new ArrayList<Manufacturer>();

        String BOOK_SELECT_SQL = "SELECT * FROM books " +
                "INNER JOIN book_copies ON book_copies.book_id = books.id " +
                "INNER JOIN stores ON book_copies.store_id = stores.id " +
                "INNER JOIN manufacturers on book_copies.manufacturer_id = manufacturers.id " +
                "INNER JOIN authors on books.author = authors.id " +
                "WHERE books.id = ? ";

        try (Connection con = (Connection) DriverManager.getConnection(url,  user, password)){
            PreparedStatement st = con.prepareStatement(BOOK_SELECT_SQL);
            st.setInt(1, book.getId());
            ResultSet rs = st.executeQuery();

            while(rs.next()) {
                stores.add(new Store(rs.getString("street"), rs.getString("city")));
                availability.add(rs.getInt("in_stock"));
                isbns.add(rs.getString("isbn"));
                publicationDates.add(rs.getDate("day_of_publication"));
                manufacturers.add(new Manufacturer(rs.getString("name"),
                        rs.getString("type"),
                        rs.getString("contact_mail"),
                        rs.getString("contact_phone")));
            }

            book.setDetails(stores, availability, isbns, publicationDates, manufacturers);
            return book;


        } catch (SQLException e) {

            System.out.println("SQL Connection ERROR " + e.toString());

        }
        return book;
    }

    public boolean transferBooks(String from, String to, int howMuch, String isbn){

        String UPDATE_SRC_SQL = "UPDATE products SET in_stock=in_stock-? " +
                "WHERE store_id=(SELECT id from stores WHERE street = ?)" +
                "AND (SELECT isbn from book_copies WHERE book_copies.id=products.id)=?;";
        String CHECK_SQL="select exists(select 1 FROM products " +
                "WHERE store_id=(SELECT id from stores WHERE street = ?) " +
                "AND (SELECT isbn from book_copies WHERE book_copies.id=products.id)=?)";
        String UPDATE_DEST_SQL = "UPDATE products SET in_stock=in_stock+? " +
                "WHERE store_id=(SELECT id from stores WHERE street = ?) " +
                "AND (SELECT isbn from book_copies WHERE book_copies.id=products.id)=?;";
        String INSERT_DEST_SQL2 = "INSERT INTO products(store_id, manufacturer_id, in_stock) VALUES(" +
                "(SELECT id FROM stores WHERE street=?), " +
                "(SELECT manufacturer_id FROM products WHERE id=(SELECT id from book_copies WHERE isbn=?)), " +
                "?) RETURNING id;";
        String INSERT_DEST_SQL = "INSERT INTO book_copies(id, book_id, isbn, day_of_publication) VALUES (" +
                "?, " +
                "(SELECT book_id FROM book_copies WHERE isbn=?), " +
                "?, " +
                "(SELECT day_of_publication FROM book_copies WHERE isbn=?));";

        try (Connection con = (Connection) DriverManager.getConnection(url,  user, password)){
            con.setAutoCommit(false);

            PreparedStatement st = con.prepareStatement(UPDATE_SRC_SQL);
            st.setInt(1, howMuch);
            st.setString(2, from);
            st.setString(3, isbn);
            st.executeUpdate();

            st = con.prepareStatement(CHECK_SQL);
            st.setString(1, to);
            st.setString(2, isbn);
            ResultSet rs = st.executeQuery();

            if (rs.next() && rs.getBoolean(1)) {
                st = con.prepareStatement(UPDATE_DEST_SQL);
                st.setInt(1, howMuch);
                st.setString(2, to);
                st.setString(3, isbn);
                st.executeUpdate();
            } else {

                st = con.prepareStatement(INSERT_DEST_SQL2);
                st.setString(1, to);
                st.setString(2, isbn);
                st.setInt(3, howMuch);
                rs = st.executeQuery();
                rs.next();

                int newId = rs.getInt("id");

                st = con.prepareStatement(INSERT_DEST_SQL);
                st.setInt(1, newId);
                st.setString(2, isbn);
                st.setString(3, isbn);
                st.setString(4, isbn);
                st.executeUpdate();
            }
            con.commit();
            return true;

        } catch (SQLException e) {

            System.out.println("SQL Connection ERROR " + e.toString());

        }
        return false;
    };

    @Override
    public void update(Object o) {

    }

    @Override
    public boolean delete(Object o) {
        return false;
    }
}
