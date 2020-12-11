package com.packagename.dbs.service;

import com.packagename.dbs.dao.StatDao;
import com.packagename.dbs.dao.inputHandling.SafeInput;
import com.packagename.dbs.model.CompanyStat;

import java.util.Collection;

public class StatsService implements service {

    StatDao dao;

    public StatsService(){
        this.dao = new StatDao();
    }

    @Override
    public Collection getAll(){
        return this.dao.getAll();
    }

    public Collection greaterThan(String num){
        return this.dao.greaterThan(num);
    }

    public Collection smallerThan(String num){
        return this.dao.smallerThen(num);
    }

    public String getAvg() {
        return this.dao.getAvg();
    }

    public String getMax() {
        return this.dao.getMax();
    }

    public String getMin() {
        return this.dao.getMin();
    }

    public String getTotal() {
        return this.dao.getTotal();
    }

    public Collection getEmployeesPerCity(String low, String upp) {
        if( SafeInput.stringToInteger(low )>= SafeInput.stringToInteger(upp)){
            return this.dao.getALLEmployeesPerCity();
        }else{
            return this.dao.getEmployeesPerCity(low, upp);
        }
    }

    public Collection getBookStatsPerStreet(){
        return dao.getBookStatsPerStreet();
    }

    public Collection getALLEmployeesPerCity() {
        return this.dao.getALLEmployeesPerCity();
    }

    public Collection<CompanyStat>  getAllInCity(String city) {
        return this.dao.getAllInCity(city);
    }

    public Collection<CompanyStat> getAvgBookStats() {
        return dao.getBookAvg();
    }
}
