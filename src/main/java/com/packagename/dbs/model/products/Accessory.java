package com.packagename.dbs.model.products;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "accessories")
public class Accessory extends Product{
    @Column(name="name")
    private String name;
    @Column(name="type")
    private String type;

    public Accessory(){}

    public Accessory(String name, String type) {
        this.name = name;
        this.type = type;
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

    public String getManufacturerName(){
        return this.getManufacturer().getName();
    }

}
