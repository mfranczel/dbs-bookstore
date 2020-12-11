package com.packagename.dbs.model;

import com.packagename.dbs.model.products.Author;
import com.packagename.dbs.model.products.Product;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "manufacturers")
public class Manufacturer {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "type")
    private String type;
    @Column(name = "contact_mail")
    private String contact_mail;
    @Column(name = "contact_phone")
    private String contact_phone;

//    @OneToMany(mappedBy = "Product")
//    private List<Product> products;

    public Manufacturer(){}

    public Manufacturer(String name, String type, String contact_mail, String contact_phone){
        this.name = name;
        this.type = type;
        this.contact_mail = contact_mail;
        this.contact_phone = contact_phone;
    }

    public String getContact_phone() {
        return contact_phone;
    }

    public void setContact_phone(String contact_phone) {
        this.contact_phone = contact_phone;
    }

    public String getContact_mail() {
        return contact_mail;
    }

    public void setContact_mail(String contact_mail) {
        this.contact_mail = contact_mail;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
