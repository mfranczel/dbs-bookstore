package com.packagename.dbs.dao;

import com.packagename.dbs.model.Account;
import com.packagename.dbs.dao.inputHandling.SafeInput;
import com.packagename.dbs.model.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class EmployeeDao  implements Dao{

    String url = "jdbc:postgresql://localhost:5432/postgres";
    String user = "postgres";
    String password = "postgres";

    @Override
    public Optional get(int id) {
        return Optional.empty();
    }


    public Account loadAccountByUsername(String username){
        try (Connection con = (Connection) DriverManager.getConnection(url,  user, password)){
             PreparedStatement st = con.prepareStatement(
                     "SELECT accounts.id, accounts.username, accounts.password, role_name,accounts.employee_id, accounts.expiration_date " +
                     "FROM accounts INNER JOIN roles ON accounts.company_role = roles.id " +
                     "WHERE username=?");

             st.setString(1, username);
             ResultSet rs = st.executeQuery();
             String company_role;
             Account temp = new Account();

             if(rs.next()) {
                 temp = new Account(
                         rs.getInt("id"),
                         rs.getString("username"),
                         rs.getString("password"),
                         rs.getString("role_name"),
                         rs.getInt("employee_id"),
                         rs.getTimestamp("expiration_date")
                 );
             }

             return temp;

        } catch (SQLException ex) {

            System.out.println("SQL Connection ERROR");

        }
        return null;
    }



    @Override
    public Collection<Employee> getAll() {

                String GET_ALL_SQL = "SELECT * FROM employees INNER JOIN stores ON stores.id=employees.store_id";

        try (Connection con = (Connection) DriverManager.getConnection(url,  user, password)){
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(GET_ALL_SQL);
            Collection<Employee> employees = new ArrayList<Employee>();

            while(rs.next()) {
                Employee temp = new Employee();
                temp.setId(rs.getInt("id"));
                temp.setFirst(rs.getString("first_name"));
                temp.setLast(rs.getString("last_name"));
                temp.setStoreStreet(rs.getString("street"));
                employees.add(temp);
            }
            return employees;

        } catch (SQLException ex) {

            System.out.println("SQL Connection ERROR");

        }
        return null;
    }


    public Collection<String> getAllRoles(){
        String GET_ALL_SQL = "SELECT * FROM roles";

        try (Connection con = (Connection) DriverManager.getConnection(url,  user, password)){
            PreparedStatement st = con.prepareStatement(GET_ALL_SQL);
            ResultSet rs = st.executeQuery();

            Collection<String> roles = new ArrayList<String>();

            while(rs.next()) {
                roles.add(rs.getString("role_name"));
            }

            return roles;

        } catch (SQLException ex) {

            System.out.println("SQL Connection ERROR");

        }
        return null;
    }


    public Collection<Employee> getPage(long num_1, long num_2) {

        String GET_ALL_SQL = "SELECT * FROM employees INNER JOIN stores ON stores.id=employees.store_id LIMIT ? OFFSET ?";

        try (Connection con = (Connection) DriverManager.getConnection(url,  user, password)){
            PreparedStatement st = con.prepareStatement(GET_ALL_SQL);
            st.setLong(1, num_1);
            st.setLong(2, num_2);
            ResultSet rs = st.executeQuery();
            Collection<Employee> employees = new ArrayList<Employee>();

            while(rs.next()) {
                Employee temp = new Employee();
                temp.setId(rs.getInt("id"));
                temp.setFirst(rs.getString("first_name"));
                temp.setLast(rs.getString("last_name"));
                temp.setStoreStreet(rs.getString("street"));
                employees.add(temp);
            }
            return employees;
        } catch (SQLException ex) {
            System.out.println("SQL Connection ERROR");
        }
        return null;
    }


    public long getCount() {
        String GET_ALL_SQL = "SELECT COUNT(*) AS num FROM employees INNER JOIN stores ON stores.id=employees.store_id";

        try (Connection con = (Connection) DriverManager.getConnection(url,  user, password)){
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery(GET_ALL_SQL);

            long result = 0;

            while(rs.next()) {
                long temp;
                temp = (long) rs.getDouble("num");
                result = temp;
            }
            return result;

        } catch (SQLException ex) {
            System.out.println("SQL Connection ERROR here");
        }
        return 0;
    }




    public Collection<Employee> getAllFromStore(String store) {

        String GET_ALL_SQL = "SELECT * FROM employees INNER JOIN stores ON stores.id=employees.store_id  WHERE store_id = (SELECT id FROM stores WHERE street=? LIMIT 1);";

        try (Connection con = (Connection) DriverManager.getConnection(url,  user, password)){
             PreparedStatement st = con.prepareStatement(GET_ALL_SQL);
             st.setString(1, store);
             ResultSet rs = st.executeQuery();

            Collection<Employee> employees = new ArrayList<Employee>();

            while(rs.next()) {
                Employee temp = new Employee();
                temp.setId(rs.getInt("id"));
                temp.setFirst(rs.getString("first_name"));
                temp.setLast(rs.getString("last_name"));
                temp.setStoreStreet(rs.getString("street"));
                employees.add(temp);
            }

            return employees;

        } catch (SQLException ex) {

            System.out.println("SQL Connection ERROR");

        }
        return null;
    }

    public Collection<Employee> filterByFirstName(String firstName){

        String GET_ALL_SQL = "SELECT * FROM  (SELECT * FROM employees WHERE lower(employees.first_name) LIKE ?) AS search INNER JOIN stores ON stores.id=search.store_id ";

        try (Connection con = (Connection) DriverManager.getConnection(url,  user, password)){
            PreparedStatement st = con.prepareStatement(GET_ALL_SQL);
            st.setString(1, SafeInput.searchAll(firstName));
            ResultSet rs = st.executeQuery();

            Collection<Employee> employees = new ArrayList<Employee>();

            while(rs.next()) {
                Employee temp = new Employee();
                temp.setId(rs.getInt("id"));
                temp.setFirst(rs.getString("first_name"));
                temp.setLast(rs.getString("last_name"));
                temp.setStoreStreet(rs.getString("street"));
                employees.add(temp);
            }

            return employees;


        } catch (SQLException ex) {

            System.out.println("SQL Connection ERROR");

        }
        return null;
    }


    public Collection<Employee> filterByLastName(String lastName){

        String GET_ALL_SQL = "SELECT * FROM  (SELECT * FROM employees WHERE lower(employees.last_name) LIKE ?) AS search INNER JOIN stores ON stores.id=search.store_id ";

        try (Connection con = (Connection) DriverManager.getConnection(url,  user, password)){
            PreparedStatement st = con.prepareStatement(GET_ALL_SQL);
            st.setString(1, SafeInput.searchAll(lastName));
            ResultSet rs = st.executeQuery();

            Collection<Employee> employees = new ArrayList<Employee>();

            while(rs.next()) {
                Employee temp = new Employee();
                temp.setId(rs.getInt("id"));
                temp.setFirst(rs.getString("first_name"));
                temp.setLast(rs.getString("last_name"));
                temp.setStoreStreet(rs.getString("street"));
                employees.add(temp);
            }

            return employees;


        } catch (SQLException ex) {

            System.out.println("SQL Connection ERROR");

        }
        return null;
    }

    public Optional save(Account a) {
        String INSERT_SQL = "INSERT INTO accounts(username, password, creation_date, expiration_date, company_role, employee_id) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = (Connection) DriverManager.getConnection(url,  user, password);){
            Timestamp now = new Timestamp(System.currentTimeMillis());
            ResultSet rs;
            PreparedStatement st = con.prepareStatement("SELECT id FROM roles WHERE role_name=?");
            st.setString(1, a.getCompany_role());
            rs = st.executeQuery();
            int company_role = 0;
            if (rs.next()) {
                int role = rs.getInt("id");
                company_role = role;
            }

            st = con.prepareStatement(INSERT_SQL);
            st.setString(1, a.getUsername());
            st.setString(2, a.getPassword());
            st.setTimestamp(3, now);
            st.setTimestamp(4, a.getExpiration_date());
            st.setInt(5, company_role);
            st.setInt(6, a.getEmployee_id());

            st.executeUpdate();
        } catch (SQLException ex) {
            System.out.println("SQL Connection ERROR");
        }
        return null;
    }

    public int save(Employee o) {
        int empID = -1;
        Employee employee = (Employee) o;
        String INSERT_SQL = "INSERT INTO employees(first_name, last_name, birthday, address, mail, telephone_number, store_id) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id;";


        try (Connection con = (Connection) DriverManager.getConnection(url,  user, password);){
            PreparedStatement st2 = con.prepareStatement("SELECT id FROM stores WHERE street=?;");

            st2.setString(1, employee.getStoreStreet());
            ResultSet rs = st2.executeQuery();
            int storeID = -1;
            if(rs.next()) {
                storeID = rs.getInt("id");
            }
            st2.close();


            PreparedStatement st = con.prepareStatement(INSERT_SQL);
            st.setString(1, employee.getFirst());
            st.setString(2, employee.getLast());
            st.setDate(3, employee.getBirthday());
            st.setString(4, employee.getAddress());
            st.setString(5, employee.getMail());
            st.setString(6, employee.getPhone());
            st.setInt(7, storeID);

            rs = st.executeQuery();
            if(rs.next()) {
                empID = rs.getInt("id");
            }
            st.close();

        } catch (SQLException ex) {

            System.out.println("SQL Connection ERROR");

        }
        return empID;
    }

    public Employee getEmployeeById(int id){
        String SQL_SELECT = "SELECT * FROM employees INNER JOIN stores ON store_id = stores.id WHERE employees.id=?";
        Employee e = new Employee();

        try (Connection con = (Connection) DriverManager.getConnection(url,  user, password);){

            PreparedStatement st = con.prepareStatement(SQL_SELECT);
            st.setInt(1, id);

            ResultSet rs = st.executeQuery();
            if(rs.next()) {
                e = new Employee(rs.getString("first_name"),
                        rs.getString("last_name"),
                        rs.getDate("birthday"),
                        rs.getString("address"),
                        rs.getString("street"),
                        rs.getString("mail"),
                        rs.getString("telephone_number")
                        );
            }
            st.close();
            return e;


        } catch (SQLException ex) {
            System.out.println(ex.toString());
        }

        return null;
    }


    @Override
    public void update(Object o) {
        Account e = (Account) o;

        String SQL_UPDATE = "UPDATE accounts SET username=?, password=?, expiration_date=? WHERE id=?";

        try (Connection con = (Connection) DriverManager.getConnection(url,  user, password);){

            PreparedStatement st = con.prepareStatement(SQL_UPDATE);

            st.setString(1, e.getUsername());
            st.setString(2, e.getPassword());
            st.setTimestamp(3, e.getExpiration_date());
            st.setInt(4, e.getID());

            st.executeUpdate();

        } catch (SQLException ex) {

            System.out.println(ex.toString());

        }

    }

    public Account getAccountByMP(int id) {
        String SELECT_SQL = "SELECT * FROM accounts INNER JOIN roles ON accounts.company_role = roles.id WHERE employee_id=?";
        try (Connection con = (Connection) DriverManager.getConnection(url,  user, password);){

            PreparedStatement st = con.prepareStatement(SELECT_SQL);
            st.setInt(1, id);

            ResultSet rs = st.executeQuery();
            if(rs.next()) {
                Account a = new Account(rs.getInt("id"), rs.getString("username"),
                        rs.getString("password"), rs.getString("role_name"),
                        rs.getInt("employee_id"), rs.getTimestamp("expiration_date"));
                return a;

            }
            st.close();

        } catch (SQLException ex) {

            System.out.println(ex.toString());

        }
        return null;
    }

    public EmployeeDao(){
        System.err.close();
    }

    @Override
    public boolean delete(Object o) {
        String DELETE_SQL = "";
        int id = 0;
        if (o instanceof Account) {
            DELETE_SQL = "DELETE FROM ONLY accounts WHERE id=? RETURNING 1";
            id = ((Account) o).getID();
        } else {
            DELETE_SQL = "DELETE FROM ONLY employees WHERE id=? RETURNING 1";
            id = ((Employee) o).getId();
        }

        try (Connection con = (Connection) DriverManager.getConnection(url,  user, password);){

            PreparedStatement st = con.prepareStatement(DELETE_SQL);
            st.setInt(1, id);
            ResultSet rs = st.executeQuery();

            if (rs.next()) {
                st.close();
                return true;
            } else {
                st.close();
                return false;
            }


        } catch (SQLException ex) {

            System.out.println(ex.toString());

        }
        return false;
    }

}
