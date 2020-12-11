package com.packagename.dbs.model.products;

import com.packagename.dbs.model.Manufacturer;
import com.packagename.dbs.model.Store;

import javax.persistence.*;

@Entity
@Table(name = "products")
@Inheritance(strategy = InheritanceType.JOINED)
public class Product {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Store.class)
    @JoinColumn(name = "store_id", referencedColumnName = "`id`")
    private Store store;

    @ManyToOne(fetch = FetchType.EAGER, targetEntity = Manufacturer.class)
    @JoinColumn(name = "manufacturer_id", referencedColumnName = "`id`")
    private Manufacturer manufacturer;

    @Column(name = "in_stock")
    private int in_stock;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Store getStore() {
        return store;
    }

    public void setStore(Store store) {
        this.store = store;
    }

    public Manufacturer getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(Manufacturer manufacturer) {
        this.manufacturer = manufacturer;
    }

    public int getIn_stock() {
        return in_stock;
    }

    public void setIn_stock(int in_stock) {
        this.in_stock = in_stock;
    }
}
