package uk.tudorsirbu.track.util;


import java.text.SimpleDateFormat;
import java.util.Calendar;


/**
 * Created by tudorsirbu on 27/06/2017.
 */

public class TimeHelper {

    public static String millisToString(long millis){
        if(millis <= 0)
            return null;

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);

        SimpleDateFormat format = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
        return format.format(calendar.getTime());
    }
}
