package com.packagename.dbs.service;

import com.packagename.dbs.dao.repositories.AccessoryRepository;
import com.packagename.dbs.model.products.Accessory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class AccessoryService {

    @Autowired
    AccessoryRepository accessoryRepository;

    public ArrayList<Accessory> getAll(){
        return (ArrayList<Accessory>) this.accessoryRepository.findAll();
    }

    public Accessory getWholeAccInfo(Accessory accessory){
        return this.accessoryRepository.findById(accessory.getId()).get();
    }
}
