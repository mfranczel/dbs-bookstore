package com.packagename.dbs.service;

import com.packagename.dbs.dao.StoreDao;
import com.packagename.dbs.model.Store;

import java.util.ArrayList;
import java.util.Collection;

public class StoreService implements service {

    StoreDao dao;

    public StoreService(){
        this.dao = new StoreDao();
    }

    @Override
    public Collection getAll(){
        return this.dao.getAll();
    }

    public Collection getAllStoreNames(){
        Collection<Store> stores = this.dao.getAll();
        Collection<String> store_names = new ArrayList<>();

        for(Store a: stores) {
            store_names.add(a.getCity().concat(", ").concat(a.getStreet()));
        }

        return store_names;
    }

    public Store getStoreByEmployeeId(int id) {
        return this.dao.getStoreByEmployeeID(id);
    }
}
