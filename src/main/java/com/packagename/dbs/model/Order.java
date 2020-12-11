package com.packagename.dbs.model;


import com.packagename.dbs.model.products.Product;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name="orders")
public class Order {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column(name = "state")
    private String state;

    @Column(name = "order_date")
    @Temporal(TemporalType.DATE)
    private Date orderDate;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Store.class)
    @JoinColumn(name = "store_id", referencedColumnName = "`id`")
    private Store store;

    @ManyToMany(fetch = FetchType.EAGER, targetEntity = Product.class)
    @JoinTable(name="order_items",
            joinColumns={@JoinColumn(name="product_id", referencedColumnName="id")},
            inverseJoinColumns={@JoinColumn(name="order_id", referencedColumnName="id")})
    private List<Product> products;

    public Order(){
        products = new ArrayList<>();
    }

    public Order(String state, Store store, List<Product> products){
        this.state = state;
        this.store = store;
        this.products = products;
    }

    public List<Product> getOrderItems() {
        return this.products;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public String getStoreAddress(){
        return this.store.getStreet();
    }

    public String getDate(){
        return this.orderDate.toString();
    }

    public int getNumberOfProducts(){
        int n = 0;
        for(Product p: this.products) {
            n++;
        }
        return n;
    }

    public String getManufacturer(){
        return this.products.get(0).getManufacturer().getName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
