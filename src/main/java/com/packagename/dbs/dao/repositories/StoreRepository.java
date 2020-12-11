package com.packagename.dbs.dao.repositories;

import com.packagename.dbs.model.Store;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StoreRepository  extends JpaRepository<Store, Long> {
}
