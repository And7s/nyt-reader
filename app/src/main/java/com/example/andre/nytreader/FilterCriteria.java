package com.example.andre.nytreader;

import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * Created by Andre on 22/03/2017.
 */

public class FilterCriteria {
    public static int startDay, startMonth, startYear;
    public static String searchTerm;
    public static int sort, page;
    public static boolean includeArts, includeFashion, includeSports;
    public static boolean hasMorePages;

    public FilterCriteria() {
        Calendar c = new GregorianCalendar();
        startDay = c.get(Calendar.DAY_OF_MONTH);
        startMonth = c.get(Calendar.MONTH) + 1;
        startYear = c.get(Calendar.YEAR);
        searchTerm = "";
        sort = 0;
        includeArts = true;
        includeFashion = true;
        includeSports = true;
        page = 1;
        hasMorePages = false;

    }
}
