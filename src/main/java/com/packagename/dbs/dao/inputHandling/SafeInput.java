package com.packagename.dbs.dao.inputHandling;

import java.text.SimpleDateFormat;
import java.util.Date;

public class SafeInput {

    public static int stringToInteger(String num) {
        try {
            int temp = Integer.valueOf(num);
            if (temp < 0){
                return 0;
            }
            return temp;
        } catch (Exception e){
            return 0;
        }
    }

    public static String searchAll(String s) {
         return "%" + s.toLowerCase() + "%";
    }

    public static String addNewYear(String s) {
        return s+"-01-01";
    }


    public static String addToYear(String year, String range){
        int y = SafeInput.stringToInteger(year);
        int r = SafeInput.stringToInteger(range);
        y = y+r;
        String n_year = String.valueOf(y);
        return n_year;
    }


    public static Date parseDate(String date) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(date);
        } catch (Exception e) {
            System.out.println("Parsing error");
        }
        return null;
    }


    public static String validYear(String year){
        return SafeInput.stringToInteger( year)+"";
    }
}
