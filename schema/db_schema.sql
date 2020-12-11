DROP TABLE IF EXISTS stores, employees, accounts, roles, book_copies ,accessories, products, books, authors, manufacturers;

CREATE TABLE stores (
    id serial PRIMARY KEY,
    street VARCHAR(100),
    city VARCHAR(100)
);

CREATE TABLE employees (
                           id serial PRIMARY KEY NOT NULL,
                           first_name VARCHAR(50) NOT NULL,
                           last_name VARCHAR(50) NOT NULL,
                           birthday DATE,
                           address VARCHAR(150),
                           mail VARCHAR(50),
                           telephone_number VARCHAR(50),
                           store_id INTEGER REFERENCES stores(id)
);

CREATE TABLE roles (
                       id serial PRIMARY KEY,
                       role_name VARCHAR(50)
);

CREATE TABLE accounts (
    id serial PRIMARY KEY NOT NULL,
    username VARCHAR(50),
    password VARCHAR(50),
    creation_date TIMESTAMP ,
    expiration_date TIMESTAMP,
    company_role INTEGER references roles(id),
    employee_id INTEGER references employees(id) ON DELETE CASCADE
);

CREATE TABLE manufacturers(
    id serial PRIMARY KEY,
    name VARCHAR(50),
    type VARCHAR(50),
    contact_mail VARCHAR(50),
    contact_phone VARCHAR(50)
);

CREATE TABLE authors(
    id SERIAL PRIMARY KEY,
    name VARCHAR(150),
    birthday DATE
);

CREATE TABLE books(
    id SERIAL PRIMARY KEY,
    book_name VARCHAR(50),
    genre VARCHAR(50),
    author INTEGER references authors(id)
);






