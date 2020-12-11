package com.packagename.dbs.dao.repositories;

import com.packagename.dbs.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("select a from Order a where a.store.street=?1")
    List<Order> getAllFromStore(String store);

    @Query("select a from Order a where a.state=?1")
    List<Order> getAllByState(String state);

    @Query("select a from Order a where (a.state=?1 and a.store.street=?2)")
    List<Order> getAllByStateByStore(String state, String street);
}
