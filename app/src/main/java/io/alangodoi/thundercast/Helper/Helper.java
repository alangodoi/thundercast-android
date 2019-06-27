package io.alangodoi.thundercast.Helper;

import java.util.ArrayList;
import java.util.Calendar;

public class Helper {

    public String releaseDate (String date) {

        String[] dateParts = date.split("-");
        int year = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]);
        int day = Integer.parseInt(dateParts[2]);

//        Calendar cal = Calendar.getInstance();
//        cal.set(Calendar.YEAR, year);
//        cal.set(Calendar.MONTH, month - 1);
//        cal.set(Calendar.DAY_OF_MONTH, day);

//        return cal.getTime();
        return month(month) + " " + day;
    }

    private String month(int month){

        ArrayList<String> months = new ArrayList<>();
        months.add("JANUARY");
        months.add("FEBRUARY");
        months.add("MARCH");
        months.add("APRIL");
        months.add("MAY");
        months.add("JUNE");
        months.add("JULY");
        months.add("AUGUST");
        months.add("SEPTEMBER");
        months.add("OCTOBER");
        months.add("NOVEMBER");
        months.add("DECEMBER");

        return months.get(month-1);
    }

}
