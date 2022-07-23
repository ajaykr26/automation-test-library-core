package library.api;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class GetDateTime {

    private GetDateTime() {
    }

    public static String addDaysToDate_yyyymmdd(int addDays) {
        String DATE_FORMAT = "yyyyMMdd";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(DATE_FORMAT);
        Calendar dtCalender = Calendar.getInstance();
        dtCalender.add(Calendar.DATE, addDays);

        return simpleDateFormat.format(dtCalender.getTime());
    }
}
