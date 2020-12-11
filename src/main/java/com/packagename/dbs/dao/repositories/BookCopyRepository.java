package com.packagename.dbs.dao.repositories;

import com.packagename.dbs.model.products.BookCopy;
import com.vaadin.flow.component.crud.Crud;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookCopyRepository extends JpaRepository<BookCopy, Long> {

}
