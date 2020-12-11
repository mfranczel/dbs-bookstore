package com.packagename.dbs;

import com.github.javafaker.Faker;
import com.packagename.dbs.dao.repositories.*;
import com.packagename.dbs.model.Order;
import com.packagename.dbs.model.products.Accessory;
import com.packagename.dbs.model.products.Book;
import com.packagename.dbs.model.products.BookCopy;
import com.packagename.dbs.model.products.BookORM;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Random;
import java.util.concurrent.TimeUnit;


@Component
public class DataGenerator implements ApplicationRunner {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private BookCopyRepository bookCopyRepository;
    @Autowired
    private AccessoryRepository accessoryRepository;
    @Autowired
    private AuthorRepository authorRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private ManufacturerRepository manufacturerRepository;
    @Autowired
    private OrderRepository orderRepository;

    public void test() throws SQLException {
        Faker faker = new Faker(new Locale("sk"));
        String[] roles = {"Employee", "Manager"};


        // server config
        String url = "jdbc:postgresql://localhost:5432/postgres";
        String user = "postgres";
        String password = "postgres";


        // insert queries
        String EMP_SQL = "INSERT INTO employees(first_name,last_name, birthday, address, mail, telephone_number, store_id) "
                + "VALUES(?,?,?,?,?,?,?)";
        String STORE_SQL = "INSERT INTO stores(street,city) "
                + "VALUES(?,?)";
        String ACC_SQL = "INSERT INTO accounts(username, password, creation_date, expiration_date, company_role, employee_id) "
                + "VALUES(?, ?, ?, ?, ?, ?)";

        String ROLE_SQL = "INSERT INTO roles(role_name) VALUES(?)";

        String MANU_SQL = "INSERT INTO manufacturers(name, type, contact_mail, contact_phone) " +
                "VALUES(?,?,?,?)";

        String BOOK_SQL="INSERT INTO books(book_name, genre, author) VALUES (?,?,?)";
        String BOOK_CPY_SQL= "INSERT INTO book_copies(store_id, manufacturer_id, in_stock, book_id, isbn, day_of_publication) " +
                "VALUES (?,?,?,?,?,?)";
        String ACCESS_SQL="INSERT INTO accessories(store_id, manufacturer_id, in_stock, name, type) VALUES " +
                "(?,?,?,?,?)";
        String AUTHOR_SQL="INSERT INTO authors(name, birthday) VALUES (?,?)";

        //test

        int nOfOrders = 100000;

        int nOfStores = 100000;
        int nOfEmployees = 500000;

        int nOfBooks = 500000;
        int nOfBookCopies = 5000000;
        int nOfAuthors = 200000;
        int nOfAccessories = 150000;

        int nOfCompanies = 100000;
        int nOfPoblishers = nOfCompanies/2;
        int nOfOtherCompanies = nOfCompanies-nOfPoblishers;


        try {Connection conn = (Connection) DriverManager.getConnection(url,  user, password);


            // insert all roles into database
            PreparedStatement st = conn.prepareStatement(ROLE_SQL);

            for(int i = 0; i < roles.length; i++) {
                st.setString(1, roles[i]);
                st.executeUpdate();
            }

            //insert all stores into database
            st = conn.prepareStatement(STORE_SQL);

            for(int i = 0; i < nOfStores; i++) {
                String street = faker.address().streetAddress();
                String city = faker.address().city();
                st.setString(1, street);
                st.setString(2, city);
                st.executeUpdate();

            }

            // insert all employees into database, including admin and basic user for testing purposes
            st = conn.prepareStatement(EMP_SQL);

            st.setString(1, "admin");
            st.setString(2, "admin");
            st.setDate(3, new Date(1999, 6, 5));
            st.setString(4, "Tomankova, Bratislava");
            st.setString(5, "admin.admin@admin.ad");
            st.setString(6, "9385738557");
            st.setInt(7,1);

            st.executeUpdate();

            st.setString(1, "user");
            st.setString(2, "user");
            st.setDate(3, new Date(1999, 6, 5));
            st.setString(4, "Tomankova, Bratislava");
            st.setString(5, "user.user@user.us");
            st.setString(6, "9385738557");
            st.setInt(7,1);

            st.executeUpdate();

            for(int i = 0; i < nOfEmployees; i++) {
                String f_name = faker.name().firstName();
                String l_name = faker.name().lastName();
                java.util.Date birthday_1 = faker.date().birthday();
                Date birthday = new Date(birthday_1.getYear(), birthday_1.getMonth(), birthday_1.getDay());
                String address = faker.address().fullAddress();
                String mail = faker.internet().emailAddress();
                String phone = faker.phoneNumber().cellPhone();
                int store = new Random().nextInt(nOfStores);

                st.setString(1, f_name);
                st.setString(2, l_name);
                st.setDate(3, birthday);
                st.setString(4, address);
                st.setString(5, mail);
                st.setString(6, phone);
                st.setInt(7, store+1);
                st.executeUpdate();
            }

            st.close();

            // insert all accounts into database
            st = conn.prepareStatement(ACC_SQL);
            Timestamp prev = new Timestamp(2019, 4, 20, 5, 5, 5, 5);
            Timestamp next = new Timestamp(2029, 4, 20, 5, 5, 5, 5);

            st.setString(1, "admin");
            st.setString(2, "admin");
            st.setTimestamp(3, prev);
            st.setTimestamp(4, next);
            st.setInt(5, 2);
            st.setInt(6, 1);

            st.executeUpdate();

            st.setString(1, "user");
            st.setString(2, "user");
            st.setTimestamp(3, prev);
            st.setTimestamp(4, next);
            st.setInt(5, 1);
            st.setInt(6, 2);

            st.executeUpdate();

            for(int i = 3; i < nOfEmployees+3; i++) {
                String username = faker.name().username();
                String pass = faker.internet().password();
                st.setString(1, username);
                st.setString(2, pass);
                st.setTimestamp(3, prev);
                st.setTimestamp(4, next);
                st.setInt(5, faker.random().nextInt(2)+1);
                st.setInt(6, i);
                st.executeUpdate();
            }
            st.close();

            // generate manufacturers

            st = conn.prepareStatement(MANU_SQL);

            for(int i = 0; i < nOfPoblishers; i++) {
                String name = faker.book().publisher();
                String type = "Publisher";
                String contact_mail = faker.internet().emailAddress();
                String phone = faker.phoneNumber().phoneNumber();

                st.setString(1, name);
                st.setString(2, type);
                st.setString(3, contact_mail);
                st.setString(4, phone);

                st.executeUpdate();
            }

            for(int i = 0; i < nOfOtherCompanies; i++) {
                String name = faker.company().name();
                String type = faker.company().industry();
                String contact_mail = faker.internet().emailAddress();
                String phone = faker.phoneNumber().phoneNumber();

                st.setString(1, name);
                st.setString(2, type);
                st.setString(3, contact_mail);
                st.setString(4, phone);

                st.executeUpdate();
            }
            st.close();


            //generate authors
            st = conn.prepareStatement(AUTHOR_SQL);
            for(int i = 0; i < nOfAuthors; i++){
                String name = faker.book().author();
                Date birthday = new Date(faker.date().birthday(16,100).getTime());

                st.setString(1, name);
                st.setDate(2, birthday);

                st.executeUpdate();
            }
            st.close();

            //generate books

            for(int i = 0; i < nOfBooks; i++) {
                BookORM newBook = new BookORM();
                String title = faker.book().title();
                String genre = faker.book().genre();
                int author = faker.random().nextInt(nOfAuthors-1)+1;

                newBook.setName(title);
                newBook.setGenre(genre);
                newBook.setAuthor(this.authorRepository.getOne((long)author));

                this.bookRepository.saveAndFlush(newBook);
            }


            //gernerate book copies
            for(int i = 0; i < nOfBookCopies; i++) {

                BookCopy bookCopy = new BookCopy();

                int store = new Random().nextInt(nOfStores-1)+1;
                int manufacturer = faker.random().nextInt(nOfPoblishers)+1;
                int inStock = faker.random().nextInt(50);
                int book = faker.random().nextInt(nOfBooks-1)+1;
                String ISBN = faker.code().isbn13();
                Date publication = new Date(faker.date().past(10000, TimeUnit.DAYS).getTime());

                bookCopy.setStore(this.storeRepository.getOne((long)store));
                bookCopy.setManufacturer(this.manufacturerRepository.getOne((long)manufacturer));
                bookCopy.setIn_stock(inStock);
                bookCopy.setBook(this.bookRepository.getOne((long)book));
                bookCopy.setIsbn(ISBN);
                bookCopy.setDayOfPublication(publication);

                this.bookCopyRepository.saveAndFlush(bookCopy);
            }

            //generate accessories
            for(int i = 0; i < nOfAccessories; i++){
                Accessory acc = new Accessory();
                int store = new Random().nextInt(nOfStores-1)+1;
                int manufacturer = faker.random().nextInt(nOfPoblishers+1, nOfPoblishers+nOfOtherCompanies-1)+1;
                int inStock = faker.random().nextInt(50);
                String name = faker.beer().name();
                String type = faker.beer().style();

                acc.setStore(this.storeRepository.getOne((long)store));
                acc.setManufacturer(this.manufacturerRepository.getOne((long)manufacturer));
                acc.setIn_stock(inStock);
                acc.setName(name);
                acc.setType(type);

                this.accessoryRepository.saveAndFlush(acc);
            }

            ArrayList<String> states = new ArrayList<>();
            states.add("Sent");
            states.add("Accepted");

            //generate orders
            for(int i = 0; i < nOfOrders; i++) {
                Order order = new Order();
                order.setState(states.get(faker.random().nextInt(states.size())));
                order.setOrderDate(new java.util.Date());
                order.setStore(this.storeRepository.getOne(faker.random().nextLong(nOfStores)));

                for(int j = 0; j < faker.random().nextInt(5)+1; j++) {
                    BookCopy bc = this.bookCopyRepository.findById(faker.random().nextLong(nOfBookCopies)).get();
                    Accessory accessory = this.accessoryRepository.findByManufacturer(bc.getManufacturer().getId());

                    order.getOrderItems().add(accessory);
                    order.getOrderItems().add(bc);
                }


                this.orderRepository.saveAndFlush(order);
            }

        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }

    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        if(this.bookRepository.findAll().isEmpty()) {
            System.out.println("Tables are being populated with test data.");
            this.test();
            System.out.println("Tables are populated with test data.");
        }
    }
}
