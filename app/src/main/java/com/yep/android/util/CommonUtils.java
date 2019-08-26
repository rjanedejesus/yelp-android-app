package com.yep.android.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static timber.log.Timber.d;

public class CommonUtils {

    public static final int YEAR = 0;
    public static final int MONTH = 1;
    public static final int DAY = 2;
    public static final int HOUR = 3;
    public static final int MINUTE = 4;
    public static final int SECOND = 5;

    public static String getString(String value) {
        if(value == null) return "";
        else return value;
    }

    public static String getString(Object value) {
        if(value == null) return "";
        else return String.valueOf(value);
    }

    public static int getInt(String value) {
        if(value == null) return 0;
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static boolean isEmailValid(String email) {
        Pattern pattern;
        Matcher matcher;
        final String EMAIL_PATTERN =
                "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                        + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
        pattern = Pattern.compile(EMAIL_PATTERN);
        matcher = pattern.matcher(email);
        d("isEmailValid " + matcher.matches());
        return matcher.matches();
    }

    public static boolean isPasswordValid(String password) {
        return getString(password).length() >= 4;
    }

    public static String getTime() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("hh:mm aa");
        return df.format(c.getTime());
    }

    public static String formatDeviceDetailDate(String time) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";

        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat df = new SimpleDateFormat("dd MMM");
        SimpleDateFormat tf = new SimpleDateFormat("hh:mm aaa");

        Date date = null;
        String dateStr = "";
        String timeStr = "";

        try {
            date = inputFormat.parse(time);
            dateStr = df.format(date);
            timeStr = tf.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        d("formatDeviceDetailDate " + time + " " + dateStr + " - " + timeStr);
        return timeStr;
//        24 Nov - 8:30
    }

    public static String getDate(String time) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";

        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat df = new SimpleDateFormat("dd MMM");

        Date date = null;
        String dateStr = "";

        try {
            date = inputFormat.parse(time);
            dateStr = df.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        d("getDate " + time + " " + dateStr);
        return dateStr;
    }

    public static String formatDate(long millis) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(millis);
        return inputFormat.format(calendar.getTime());

    }

    public static long getTimeInMillis(String time) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        long timeInMillis = 0;
        Date date = null;

        try {
            date = inputFormat.parse(time);
            timeInMillis = date.getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        d("getTimeInMillis " + timeInMillis);
        return timeInMillis;
    }

    /**
     *
     * @param timeInMillis
     * @param getHrOrMin YEAR, MONTH, DAY, HOUR, MINUTE, SECOND;
     * @return
     */
    public static int getTime(long timeInMillis, int getHrOrMin) {

        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(timeInMillis);
        int mYear = c.get(Calendar.YEAR);
        int mMonth = c.get(Calendar.MONTH);
        int mDay = c.get(Calendar.DAY_OF_MONTH);
        int hr = c.get(Calendar.HOUR_OF_DAY);
        int min = c.get(Calendar.MINUTE);
        int sec = c.get(Calendar.SECOND);
        int result = 0;

        switch (getHrOrMin) {
            case YEAR: result = mYear; break;
            case MONTH: result = mMonth; break;
            case DAY: result = mDay; break;
            case HOUR: result = hr; break;
            case MINUTE: result = min; break;
            case SECOND: result = sec; break;
            default: result = 0; break;
        }

        d("getTime : " + result);
        return result;
    }

}
