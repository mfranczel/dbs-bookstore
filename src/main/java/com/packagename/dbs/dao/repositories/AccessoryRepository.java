package com.packagename.dbs.dao.repositories;

import com.packagename.dbs.model.products.Accessory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AccessoryRepository extends JpaRepository<Accessory, Long> {

    @Query("select a from Accessory a where a.manufacturer.id=?1")
    Accessory findByManufacturer(Long manID);
}
