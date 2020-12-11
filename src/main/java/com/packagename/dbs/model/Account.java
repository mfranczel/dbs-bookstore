package com.packagename.dbs.model;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.sql.Timestamp;
import java.util.Arrays;
import java.util.Collection;

@Entity
@Table(name = "accounts")
public class Account implements UserDetails {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private int ID;
    private String username;
    private String password;
    private String company_role;
    private int employee_id;
    private Timestamp expiration_date;

    public Account(){}

    public Account(int id, String username, String password, String company_role, int employee_id, Timestamp expiration_date) {
        this.ID = id;
        this.username = username;
        this.password = password;
        this.company_role = company_role;
        this.employee_id = employee_id;
        this.expiration_date = expiration_date;
    }

    public Account(String username, String password, String company_role, int employee_id, Timestamp expiration_date) {
        this.username = username;
        this.password = password;
        this.company_role = company_role;
        this.employee_id = employee_id;
        this.expiration_date = expiration_date;
    }

    public Account(String username, String password, int employee_id, Timestamp expiration_date) {
        this.username = username;
        this.password = password;
        this.company_role = company_role;
        this.employee_id = employee_id;
        this.expiration_date = expiration_date;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        if(this.company_role.equals("Employee")) {
            return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
        } else {
            return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
        }

    }

    public Timestamp getExpiration_date() {
        return expiration_date;
    }

    public void setExpiration_date(Timestamp expiration_date) {
        this.expiration_date = expiration_date;
    }

    public int getEmployee_id() {
        return employee_id;
    }

    public void setEmployee_id(int employee_id) {
        this.employee_id = employee_id;
    }

    public String getCompany_role() {
        return company_role;
    }

    public void setCompany_role(String company_role) {
        this.company_role = company_role;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        if(this.expiration_date == null || this.expiration_date.before(new Timestamp(System.currentTimeMillis()))){
            return false;
        }
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }
}
