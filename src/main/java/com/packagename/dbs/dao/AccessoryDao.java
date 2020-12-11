package com.packagename.dbs.dao;

import com.packagename.dbs.model.products.Accessory;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Deprecated
public class AccessoryDao implements Dao{

    private String url = "jdbc:postgresql://localhost:5432/postgres";
    private String user = "postgres";
    private String password = "postgres";

    @Override
    public Optional get(int id) {
        return Optional.empty();
    }

    @Override
    public Collection getAll() {
        String ALL_SELECT_SQL = "SELECT accessories.id, accessories.name AS accname, accessories.type, manufacturers.name AS manname FROM accessories " +
                "INNER JOIN manufacturers ON manufacturer_id = manufacturers.id";

        try (Connection con = (Connection) DriverManager.getConnection(url,  user, password);){
             PreparedStatement st = con.prepareStatement(ALL_SELECT_SQL);
             ResultSet rs = st.executeQuery();

            Collection<Accessory> accessories = new ArrayList<Accessory>();

            while(rs.next()) {
                Accessory temp = new Accessory(
                        rs.getString("accname"),
                        rs.getString("type")
                );

                accessories.add(temp);
            }

            return accessories;

        } catch (SQLException ex) {

            System.out.println("SQL Connection ERROR");

        }
        return null;
    }

    public Accessory getWholeAccInfo(Accessory accessory){
        String ALL_SELECT_SQL = "" +
                "SELECT in_stock, street, city FROM accessories " +
                "INNER JOIN stores ON stores.id=store_id " +
                "WHERE accessories.id=?";

        try (Connection con = (Connection) DriverManager.getConnection(url,  user, password);){
            PreparedStatement st = con.prepareStatement(ALL_SELECT_SQL);
            ResultSet rs = st.executeQuery();

            if(rs.next()){
                //accessory.setAvailability(rs.getInt("in_stock"));
                return accessory;
            }


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
        return false;
    }
}
