package com.packagename.dbs.dao;

import java.util.Collection;
import java.util.Optional;

public interface Dao<T, I> {
    Optional<T> get(int id);
    //Collection<T> getAll();
    Collection<T>getAll();
    void update(T t);
    boolean delete(T t);
}