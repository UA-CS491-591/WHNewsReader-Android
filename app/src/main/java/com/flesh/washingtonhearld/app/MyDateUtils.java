package com.flesh.washingtonhearld.app;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;

/**
 * Created by aaronfleshner on 5/18/14.
 */
public class MyDateUtils {

    public static String TimeFromTodayAccuracyToTheMinute(String date){
        String Time = "";
        DateTime StoryTime = new DateTime(date);
        DateTime NowTime = new DateTime();
        int minutes = Minutes.minutesBetween(StoryTime, NowTime).getMinutes();
        int hours = Hours.hoursBetween(StoryTime, NowTime).getHours();
        int days = Days.daysBetween(StoryTime, NowTime).getDays();
        if(days > 0){
            Time  = Time + " " + days + " d";
        }
        if(BelowADay(hours) > 0){
            int timeHours = BelowADay(hours);
            Time  = Time + " " + timeHours + " h";
        }
        if(BelowAHour(minutes) > 0){
            int timeMins = BelowAHour(minutes);
            Time  = Time + " " + timeMins + " m";
        }

        Time = Time + " ago.";
        if(Time.length()==5){
            Time = "less then a minute ago.";
        }
        return Time;
    }

    public static String TimeFromTodayAccuracyToTheHour(String date){
        String Time = "";
        DateTime StoryTime = new DateTime(date);
        DateTime NowTime = new DateTime();
        int hours = Hours.hoursBetween(StoryTime, NowTime).getHours();
        int days = Days.daysBetween(StoryTime, NowTime).getDays();
        if(days > 0){
            Time  = Time + " " + days + " d";
        }
        if(BelowADay(hours) > 0){
            int timeHours = BelowADay(hours);
            Time  = Time + " " + timeHours + " h";
        }
        Time = Time + " ago.";
        if(Time.length()==5){
            Time = "less then a hour ago.";
        }
        return Time;
    }

    public static String TimeFromTodayAccuracyToTheDay(String date){
        String Time = "";
        DateTime StoryTime = new DateTime(date);
        DateTime NowTime = new DateTime();
        int hours = Hours.hoursBetween(StoryTime, NowTime).getHours();
        int days = Days.daysBetween(StoryTime, NowTime).getDays();
        if(days > 0){
            Time  = Time + " " + days + " d";
        }
        Time = Time + " ago.";
        if(Time.length()==5){
            Time = "less then a day ago.";
        }
        return Time;
    }

    private static int BelowAHour(int minutes) {
        int temp = minutes;
        while(temp>=1440){// minus by the day
            temp = temp-1440;
        }
        while(temp>=60){//minus by the hour
            temp = temp-60;
        }
        return temp;
    }
    private static int BelowADay(int hours) {
        int temp = hours;
        while(temp>=24){//minus by the day
            temp = temp-24;
        }
        return temp;
    }
}
