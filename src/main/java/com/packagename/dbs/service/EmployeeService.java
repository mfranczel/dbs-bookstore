package com.packagename.dbs.service;

import com.packagename.dbs.dao.EmployeeDao;
import com.packagename.dbs.dao.paging.PageObject;
import com.packagename.dbs.model.Account;
import com.packagename.dbs.model.Employee;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.Collection;

@Service
public class EmployeeService implements UserDetailsService, service {

    EmployeeDao dao = new EmployeeDao();
    PageObject pagingEmployee = new PageObject();

    public EmployeeService(){
    }

    @Override
    public Collection<Employee> getAll() {
        return dao.getAll();
    }

    public Collection<Employee> getAllFromStore(String store){ return dao.getAllFromStore(store);}

    public Employee getEmployeeByAccount(Account a){
        return this.dao.getEmployeeById(a.getEmployee_id());
    }

    public Account getAccountByEmployee(Employee e){
        return this.dao.getAccountByMP(e.getId());
    }

    public Collection<String> getAllRoles(){return this.dao.getAllRoles();}

    public void createEmployeeWithAccount(String first_name, String last_name, String company_role, String street_name, String username,
                                          String password, String mail, String phone, LocalDate birth, String address){
        Date dateOfBirth = new Date(birth.getYear(), birth.getMonthValue(), birth.getDayOfMonth());
        Employee employee = new Employee(first_name, last_name, dateOfBirth, address, street_name, mail, phone);
        int empID = this.dao.save(employee);

        LocalTime midnight = LocalTime.MIDNIGHT;
        LocalDate today = LocalDate.now(ZoneId.of("Europe/Berlin"));
        LocalDateTime todayMidnight = LocalDateTime.of(today, midnight);
        LocalDateTime nextYear = todayMidnight.plusDays(365);

        Account account = new Account(username, password, company_role, empID, Timestamp.valueOf(nextYear));

        this.dao.save(account);
    }

    public void createEmployee(){}

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Account temp = this.dao.loadAccountByUsername(s);
        if(temp == null) {
            throw new UsernameNotFoundException("User " + s + " not found.");
        }
        return temp;
    }


    public boolean removeAccount(Account account){
        if (account == null) {
            return false;
        }
        return this.dao.delete(account);
    }

    public boolean disableAccount(Account account){
        if(account == null) {
            return false;
        }
        account.setExpiration_date(new Timestamp(System.currentTimeMillis()));
        this.update(account);
        return true;
    }

    public boolean removeEmployee(Employee employee, Account account){
        return this.dao.delete(employee);
    }

    public Collection<Employee> getPrev() {
        pagingEmployee.prev();
        return dao.getPage(pagingEmployee.getLimit(), pagingEmployee.getOffset());
    }

    public Collection<Employee> getNext() {
        pagingEmployee.next();
         return dao.getPage(pagingEmployee.getLimit(), pagingEmployee.getOffset());
    }

    public void update(Employee e) {
        this.dao.update(e);
    }

    public void update(Account a) {
        this.dao.update(a);
    }

    public Collection<Employee> filterByFirstName(String firstName){
        return dao.filterByFirstName(firstName);
    }

    public Collection<Employee> filterByLastName(String lastName){
        return dao.filterByLastName(lastName);
    }

    public void getCount() {
        pagingEmployee.setEnd(this.dao.getCount());
    }
}
