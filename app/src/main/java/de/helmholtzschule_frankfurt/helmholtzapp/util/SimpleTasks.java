package de.helmholtzschule_frankfurt.helmholtzapp.util;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.support.annotation.RequiresApi;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import de.helmholtzschule_frankfurt.helmholtzapp.DataStorage;

public class SimpleTasks {

    private static DataStorage storage = DataStorage.getInstance();

    public static int getDayOfWeek(){
        GregorianCalendar calendar = new GregorianCalendar();
        switch (calendar.get(Calendar.DAY_OF_WEEK)){
            case Calendar.MONDAY: {
                return 0;
            }
            case Calendar.TUESDAY: {
                return 1;
            }
            case Calendar.WEDNESDAY: {
                return 2;
            }
            case Calendar.THURSDAY: {
                return 3;
            }
            case Calendar.FRIDAY: {
                return 4;
            }
            case Calendar.SATURDAY: {
                return 5;
            }
            case Calendar.SUNDAY: {
                return 6;
            }
        }
        return -1;
    }

    public static void setAlarm(Context context){
        AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        int today = getDayOfWeek();
        GregorianCalendar calendar = new GregorianCalendar();
        if(today >= 4){
            calendar.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        }
        else calendar.add(Calendar.DAY_OF_WEEK, 1);


        int[] startTime = today >= 4 ? storage.getSchoolStartTime(0) : storage.getSchoolStartTime(today + 1);
        if(startTime[0] == 30)intent.putExtra("NO_START", true);
        calendar.set(Calendar.HOUR_OF_DAY, startTime[0]);
        calendar.set(Calendar.MINUTE, startTime[1]);
        manager.set(AlarmManager.RTC, calendar.getTimeInMillis(), pendingIntent);

        System.out.println("I have been activated!");

        GregorianCalendar test = new GregorianCalendar();
        test.set(Calendar.HOUR_OF_DAY, 18);
        test.set(Calendar.MINUTE, 33);
       // manager.set(AlarmManager.RTC, test.getTimeInMillis(), pendingIntent);
    }

    public static void cancelAlarms(Context context){
        AlarmManager manager = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        manager.cancel(pendingIntent);
    }

    public static class AlarmReceiver extends BroadcastReceiver{

        @RequiresApi(api = Build.VERSION_CODES.M)
        @Override
        public void onReceive(Context context, Intent intent) {
            if(context.getSharedPreferences("MySPFILE", 0).getBoolean("MUTING_ACTIVE", true)){
                //setAlarm(context);
                AudioManager audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
                System.out.println("ALARM!!!");
                if(!intent.getBooleanExtra("NO_START", false)) {
                    audioManager.adjustStreamVolume(AudioManager.STREAM_RING, AudioManager.ADJUST_MUTE, 0);
                    audioManager.adjustStreamVolume(AudioManager.STREAM_NOTIFICATION, AudioManager.ADJUST_MUTE, 0);
                    audioManager.adjustStreamVolume(AudioManager.STREAM_MUSIC, AudioManager.ADJUST_MUTE, 0);
                    audioManager.adjustStreamVolume(AudioManager.STREAM_ALARM, AudioManager.ADJUST_MUTE, 0);
                    audioManager.adjustStreamVolume(AudioManager.STREAM_SYSTEM, AudioManager.ADJUST_MUTE, 0);
                    audioManager.adjustStreamVolume(AudioManager.STREAM_ACCESSIBILITY, AudioManager.ADJUST_MUTE, 0);
                    audioManager.adjustStreamVolume(AudioManager.STREAM_VOICE_CALL, AudioManager.ADJUST_MUTE, 0);
                }
            }
        }
    }
}