package fristproject1.sample.com.fristproject1.time;

import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;

public class XTime {

    public static String TimeStampToDate(long timeStamp){
        Date date = new Date(timeStamp);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = simpleDateFormat.format(date);
        return dateStr;
    }

    public static int GetYearByTimeStamp(long timeStamp){
        String date = TimeStampToDate(timeStamp);
        String year = date.substring(0, 4);
        return Integer.parseInt(year);
    }

    public static int GetMonthByTimeStamp(long timeStamp){
        String date = TimeStampToDate(timeStamp);
        String month = date.substring(5, 7);
        return Integer.parseInt(month);
    }

    public static int GetDayByTimeStamp(long timeStamp){
        String date = TimeStampToDate(timeStamp);
        String day = date.substring(8, 10);
        return Integer.parseInt(day);
    }

    public static int GetHourByTimeStamp(long timeStamp){
        String date = TimeStampToDate(timeStamp);
        String hour = date.substring(11, 13);
        return Integer.parseInt(hour);
    }

    public static int GetMinuteByTimeStamp(long timeStamp){
        String date = TimeStampToDate(timeStamp);
        String minute = date.substring(14, 16);
        return Integer.parseInt(minute);
    }

    public static int GetSecondByTimeStamp(long timeStamp){
        String date = TimeStampToDate(timeStamp);
        String second = date.substring(17, 19);
        return Integer.parseInt(second);
    }

    //判断两个时间戳是否为同一天
    public static boolean IsTwoTimeStampDayEqual(long firstTimeStamp, long secondTimeStamp){
        if(GetYearByTimeStamp(firstTimeStamp) == GetYearByTimeStamp(secondTimeStamp) &&
                GetMonthByTimeStamp(firstTimeStamp) == GetMonthByTimeStamp(secondTimeStamp)
                && GetDayByTimeStamp(firstTimeStamp) == GetDayByTimeStamp(secondTimeStamp)){
            return true;
        }
        return false;
    }

    //将时间字符串转换为Date
    public static Date StringToDate(String dateString){
        ParsePosition position = new ParsePosition(0);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date dateValue = simpleDateFormat.parse(dateString, position);
        return dateValue;
    }
}
