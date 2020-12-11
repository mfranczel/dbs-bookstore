package com.packagename.dbs.dao;

import com.packagename.dbs.model.Employee;
import com.packagename.dbs.model.Store;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class StoreDao implements Dao{
    String url = "jdbc:postgresql://localhost:5432/postgres";
    String user = "postgres";
    String password = "postgres";


    @Override
    public Optional get(int id) {
        return Optional.empty();
    }

    @Override
    public Collection getAll() {
        try (Connection con = (Connection) DriverManager.getConnection(url,  user, password);
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery("SELECT * FROM stores")) {

            Collection<Store> stores = new ArrayList<Store>();

            while(rs.next()) {
                Store store = new Store();

                store.setStreet(rs.getString("street"));
                store.setCity(rs.getString("city"));
                stores.add(store);

            }

            return stores;

        } catch (SQLException ex) {

            System.out.println("SQL Connection ERROR");

        }
        return null;
    }

    public Optional save(Object o) {
        return Optional.empty();
    }

    public Store getStoreByEmployeeID(int id){
        try (Connection con = (Connection) DriverManager.getConnection(url,  user, password)){
            PreparedStatement st = con.prepareStatement("SELECT * FROM stores " +
                            "WHERE stores.id=(SELECT store_id FROM employees WHERE employees.id=?)");

            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            Store store = new Store();

            if(rs.next()) {
                store.setStreet(rs.getString("street"));
                store.setCity(rs.getString("city"));

            }

            return store;

        } catch (SQLException ex) {

            System.out.println("SQL Connection ERROR");

        }
        return null;
    }

    @Override
    public void update(Object o) {

    }

    @Override
    public boolean delete(Object o) {
        return true;
    }
}
