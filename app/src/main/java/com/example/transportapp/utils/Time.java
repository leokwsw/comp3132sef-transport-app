package com.example.transportapp.utils;

import android.os.Build;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Time {
    public static long getMinutesDifference(String timeStr) {
        Log.d("Time", String.format("timeStr : %s", timeStr));

        if(timeStr == null){
            return -1;
        }

        long minutesDifference = 0;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            ZonedDateTime targetTime = ZonedDateTime.parse(timeStr);
            ZonedDateTime currentTime = ZonedDateTime.now(ZoneId.of("Asia/Hong_Kong"));
            minutesDifference = ChronoUnit.MINUTES.between(currentTime, targetTime);
        } else {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX");
                sdf.setTimeZone(TimeZone.getTimeZone("Asia/Hong_Kong"));
                Date date = sdf.parse(timeStr);

                // Set target time
                Calendar targetTime = Calendar.getInstance(TimeZone.getTimeZone("Asia/Hong_Kong"));
                targetTime.setTime(date);

                // Get the current time
                Calendar currentTime = Calendar.getInstance(TimeZone.getTimeZone("Asia/Hong_Kong"));

                // Set the current minute to 24
                currentTime.set(Calendar.MINUTE, 24);
                currentTime.set(Calendar.SECOND, 0);
                currentTime.set(Calendar.MILLISECOND, 0);

                // Calculate difference in milliseconds and convert to minutes
                long diffMillis = targetTime.getTimeInMillis() - currentTime.getTimeInMillis();
                minutesDifference = diffMillis / (60 * 1000);
            } catch (Exception e) {
                // ignore
            }
        }
        if (minutesDifference < 0) {
            minutesDifference = 0;
        }
        return minutesDifference;
    }
}
