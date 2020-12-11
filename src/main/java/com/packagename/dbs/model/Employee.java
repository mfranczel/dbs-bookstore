package com.packagename.dbs.model;

import javax.persistence.*;
import java.sql.Date;

@Entity
@Table(name = "employees")
public class Employee {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int id;

    private String first;
    private String last;
    private String mail;
    private String phone;
    private String storeStreet;
    private Date birthday;
    private String address;

    public Employee(String firstName , String lastName, Date birthday, String address, String storeStreet, String mail, String phone) {
        this.birthday = birthday;
        this.address = address;
        this.first = firstName;
        this.last = lastName;
        this.storeStreet = storeStreet;
        this.id = id;
        this.phone = phone;
        this.mail = mail;
    }

    public Employee(String firstName , String lastName, String storeStreet, String mail, String phone) {
        this.first = firstName;
        this.last = lastName;
        this.storeStreet = storeStreet;
        this.id = id;
        this.phone = phone;
        this.mail = mail;
    }

    public Employee(String firstName , String lastName, String storeStreet, int id) {
        this.first = firstName;
        this.last = lastName;
        this.storeStreet = storeStreet;
        this.id = id;
    }

    public Employee(String firstName , String lastName, String storeStreet) {
        this.first = firstName;
        this.last = lastName;
        this.storeStreet = storeStreet;

    }

    public Employee() {}

    public String getFirst() {
        return first;
    }

    public void setFirst(String firstName) {
        this.first = firstName;
    }

    public String getLast() {
        return last;
    }

    public void setLast(String lastName) {
        this.last = lastName;
    }

    public String getStoreStreet() {
        return storeStreet;
    }

    public void setStoreStreet(String storeStreet) {
        this.storeStreet = storeStreet;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

}
