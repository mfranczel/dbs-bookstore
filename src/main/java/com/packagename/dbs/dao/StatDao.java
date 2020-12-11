package com.packagename.dbs.dao;

import com.packagename.dbs.dao.inputHandling.SafeInput;
import com.packagename.dbs.model.CompanyStat;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class StatDao implements Dao{

    String url = "jdbc:postgresql://localhost:5432/postgres";
    String user = "postgres";
    String password = "postgres";

    @Override
    public Optional get(int id) {
        return Optional.empty();
    }

    @Override
    public Collection<CompanyStat> getAll() {

        String GET_ALL_SQL = "SELECT foo.street, foo.city,  foo.num FROM ((SELECT employees.store_id, " +
                "COUNT(*) AS num FROM employees GROUP BY employees.store_id) " +
                "AS fii INNER JOIN stores ON stores.id=fii.store_id) AS foo ORDER BY foo.num DESC";

        try (Connection con = (Connection) DriverManager.getConnection(url,  user, password);
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(GET_ALL_SQL)) {

            Collection<CompanyStat> stats = new ArrayList<CompanyStat>();

            while(rs.next()) {
                CompanyStat temp = new CompanyStat();
                temp.setStreet(rs.getString("street"));
                temp.setCity(rs.getString("city"));
                temp.setNum_of_employees(rs.getLong("num"));
                stats.add(temp);
            }

            return stats;

        } catch (SQLException ex) {

            System.out.println("SQL Connection ERROR");

        }
        return new ArrayList<CompanyStat>();
    }


    public Collection getBookStatsPerStreet() {
        String GET_ALL_SQL = "SELECT DISTINCT store_id, street, city, COUNT(store_id) OVER (PARTITION BY store_id) AS book_titles, SUM(in_stock) OVER (PARTITION BY store_id) AS book_copies" +
                " FROM book_copies INNER JOIN products ON products.id=book_copies.id INNER JOIN stores ON stores.id=store_id";

        try (Connection con = (Connection) DriverManager.getConnection(url,  user, password);
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(GET_ALL_SQL)) {

            Collection<CompanyStat> stats = new ArrayList<CompanyStat>();

            while(rs.next()) {
                CompanyStat temp = new CompanyStat();
                temp.setStreet( rs.getString( "street" ) );
                temp.setCity(rs.getString("city"));
                temp.setBookTitles(rs.getLong( "book_titles" ));
                temp.setBookCopiesAvailable(rs.getLong( "book_copies" ));
                stats.add(temp);
            }

            return stats;

        } catch (SQLException ex) {

            System.out.println("SQL Connection ERROR");

        }
        return new ArrayList<CompanyStat>();
    }


    public Collection getBookAvg() {
        String GET_ALL_SQL = "SELECT CEIL(AVG(foo.book_titles)) AS avgTitles, CEIL(AVG(foo.book_copies)) AS avgCopies FROM (SELECT DISTINCT store_id, COUNT(store_id) OVER (PARTITION BY store_id) AS book_titles, SUM(in_stock) OVER (PARTITION BY store_id) AS book_copies FROM book_copies INNER JOIN products ON book_copies.id = products.id INNER JOIN stores ON stores.id=store_id) AS foo";

        try (Connection con = (Connection) DriverManager.getConnection(url,  user, password);
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(GET_ALL_SQL)) {

            Collection<CompanyStat> stats = new ArrayList<CompanyStat>();

            while(rs.next()) {
                CompanyStat temp = new CompanyStat();
                temp.setAvgTitles( rs.getLong( "avgTitles" ) );
                temp.setAvgCopies(rs.getLong("avgCopies"));
                stats.add(temp);
            }

            return stats;

        } catch (SQLException ex) {

            System.out.println("SQL Connection ERROR");

        }
        return new ArrayList<CompanyStat>();
    }

    public Collection getALLEmployeesPerCity() {

        String GET_ALL_SQL = "SELECT foo.city, COUNT(*) AS num_of_employees FROM (SELECT * FROM employees AS fii INNER JOIN stores ON stores.id=fii.store_id)" +
                " AS foo GROUP BY foo.city HAVING COUNT(*) > 0 ORDER BY COUNT(*) DESC";

        try (Connection con = (Connection) DriverManager.getConnection(url,  user, password);
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(GET_ALL_SQL)) {

            Collection<CompanyStat> stats = new ArrayList<CompanyStat>();

            while(rs.next()) {
                CompanyStat temp = new CompanyStat();
                temp.setCity(rs.getString("city"));
                temp.setNum_of_employees(rs.getLong("num_of_employees"));
                stats.add(temp);
            }

            return stats;

        } catch (SQLException ex) {

            System.out.println("SQL Connection ERROR");

        }
        return new ArrayList<CompanyStat>();
    }

    public Collection<CompanyStat> getEmployeesPerCity(String low, String upp) {

        String GET_ALL_SQL = "SELECT foo.city, COUNT(*) AS num_of_employees FROM (SELECT * FROM employees AS fii INNER JOIN stores ON stores.id=fii.store_id)" +
                " AS foo GROUP BY foo.city HAVING COUNT(*) > ? AND COUNT(*) <= ? ORDER BY COUNT(*) DESC";

        try (Connection con = (Connection) DriverManager.getConnection(url,  user, password)){
            PreparedStatement st = con.prepareStatement(GET_ALL_SQL);
            st.setLong(1, (long)SafeInput.stringToInteger(low));
            st.setLong(2, (long)SafeInput.stringToInteger(upp));
            ResultSet rs = st.executeQuery();

            Collection<CompanyStat> stats = new ArrayList<CompanyStat>();

            while(rs.next()) {
                CompanyStat temp = new CompanyStat();
                temp.setCity(rs.getString("city"));
                temp.setNum_of_employees(rs.getLong("num_of_employees"));
                stats.add(temp);
            }

            return stats;


        } catch (SQLException ex) {

            System.out.println("SQL Connection ERROR");

        }


        return new ArrayList<CompanyStat>();
    }


    public Collection greaterThan(String num) {

        String GET_ALL_SQL = "SELECT foo.street, foo.city, foo.num FROM ((SELECT employees.store_id, COUNT(*) AS num FROM employees"
                +" GROUP BY employees.store_id) AS fii INNER JOIN stores ON stores.id=fii.store_id) AS foo WHERE foo.num > ? ORDER BY foo.num DESC";


        try (Connection con = (Connection) DriverManager.getConnection(url,  user, password)){
            PreparedStatement st = con.prepareStatement(GET_ALL_SQL);
            st.setInt(1, SafeInput.stringToInteger(num));
            ResultSet rs = st.executeQuery();

            Collection<CompanyStat> stats = new ArrayList<CompanyStat>();

            while(rs.next()) {
                CompanyStat temp = new CompanyStat();
                temp.setNum_of_employees(rs.getLong("num"));
                temp.setCity(rs.getString("city"));
                temp.setStreet(rs.getString("street"));
                stats.add(temp);
            }

            return stats;


        } catch (SQLException ex) {

            System.out.println("SQL Connection ERROR");

        }

        return new ArrayList<CompanyStat>();
    }

    public Collection smallerThen(String num) {
        String GET_ALL_SQL = "SELECT foo.street,foo.city, foo.num FROM ((SELECT employees.store_id, COUNT(*) AS num FROM employees"
                +" GROUP BY employees.store_id) AS fii INNER JOIN stores ON stores.id=fii.store_id) AS foo WHERE foo.num < ? ORDER BY foo.num DESC";


        try (Connection con = (Connection) DriverManager.getConnection(url,  user, password)){
            PreparedStatement st = con.prepareStatement(GET_ALL_SQL);
            st.setInt(1, SafeInput.stringToInteger(num));
            ResultSet rs = st.executeQuery();

            Collection<CompanyStat> stats = new ArrayList<CompanyStat>();

            while(rs.next()) {
                CompanyStat temp = new CompanyStat();
                temp.setNum_of_employees(rs.getLong("num"));
                temp.setCity(rs.getString("city"));
                temp.setStreet(rs.getString("street"));
                stats.add(temp);
            }

            return stats;


        } catch (SQLException ex) {

            System.out.println("SQL Connection ERROR");

        }

        return new ArrayList<CompanyStat>();
    }

    public String getAvg() {

        String GET_ALL_SQL = "SELECT AVG(foo.num) AS avg FROM ((SELECT employees.store_id, COUNT(*) AS num FROM employees GROUP BY employees.store_id) AS fii INNER JOIN stores ON stores.id=fii.store_id) AS foo";

        try (Connection con = (Connection) DriverManager.getConnection(url,  user, password);
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(GET_ALL_SQL)) {

            String result = "";

            while(rs.next()) {
                double temp;
                temp = rs.getDouble("avg");
                result = "" + temp;
            }

            return result;

        } catch (SQLException ex) {

            System.out.println("SQL Connection ERROR");

        }
        return "ERROR - something unexpected happened";
    }

    public String getMax() {
        String GET_ALL_SQL = "SELECT MAX(foo.num) AS max FROM ((SELECT employees.store_id, COUNT(*) AS num FROM employees GROUP BY employees.store_id) AS fii INNER JOIN stores ON stores.id=fii.store_id) AS foo";

        try (Connection con = (Connection) DriverManager.getConnection(url,  user, password);
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(GET_ALL_SQL)) {

            String result = "";

            while(rs.next()) {
                double temp;
                temp = rs.getDouble("max");
                result = "" + temp;
            }

            return result;

        } catch (SQLException ex) {

            System.out.println("SQL Connection ERROR");

        }
        return "ERROR - something unexpected happened";
    }

    public String getMin() {
        String GET_ALL_SQL = "SELECT MIN(foo.num) AS min FROM ((SELECT employees.store_id, COUNT(*) AS num FROM employees GROUP BY employees.store_id) AS fii INNER JOIN stores ON stores.id=fii.store_id) AS foo";

        try (Connection con = (Connection) DriverManager.getConnection(url,  user, password);
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(GET_ALL_SQL)) {

            String result = "";

            while(rs.next()) {
                double temp;
                temp = rs.getDouble("min");
                result = "" + temp;
            }

            return result;

        } catch (SQLException ex) {

            System.out.println("SQL Connection ERROR");

        }
        return "ERROR - something unexpected happened";
    }

    public String getTotal() {
        String GET_ALL_SQL = "SELECT SUM(foo.num) AS sum FROM ((SELECT employees.store_id, COUNT(*) AS num FROM employees GROUP BY employees.store_id) AS fii INNER JOIN stores ON stores.id=fii.store_id) AS foo";

        try (Connection con = (Connection) DriverManager.getConnection(url,  user, password);
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(GET_ALL_SQL)) {

            String result = "";

            while(rs.next()) {
                double temp;
                temp = rs.getDouble("sum");
                result = "" + temp;
            }

            return result;

        } catch (SQLException ex) {

            System.out.println("SQL Connection ERROR");

        }
        return "ERROR - something unexpected happened";
    }


    public Optional save(Object o) {
        return null;
    }

    @Override
    public void update(Object o) {
    }

    @Override
    public boolean delete(Object o) {
        return true;
    }


    public Collection<CompanyStat>  getAllInCity(String city) {

        String GET_ALL_SQL = "SELECT foo.street, foo.city,  foo.num FROM ((SELECT employees.store_id, COUNT(*) AS num FROM employees GROUP BY employees.store_id) AS fii INNER JOIN stores ON stores.id=fii.store_id) AS foo WHERE foo.city=?";


        try (Connection con = (Connection) DriverManager.getConnection(url,  user, password)){
            PreparedStatement st = con.prepareStatement(GET_ALL_SQL);
            st.setString(1, city);
            ResultSet rs = st.executeQuery();

            Collection<CompanyStat> stats = new ArrayList<CompanyStat>();
            while(rs.next()) {
                CompanyStat temp = new CompanyStat();
                temp.setStreet(rs.getString("street"));
                temp.setCity(rs.getString("city"));
                temp.setNum_of_employees(rs.getLong("num"));
                stats.add(temp);
            }
            return stats;

        } catch (SQLException ex) {

            System.out.println("SQL Connection ERROR");

        }

//        return new ArrayList<CompanyStat>();
        return null;
    }

}
