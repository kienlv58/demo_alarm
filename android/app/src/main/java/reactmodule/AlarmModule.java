package reactmodule;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;


import java.util.Calendar;

import alarm.AlarmReceiver;
import alarm.Const;
import untils.SharedPrefsUtils;

/**
 * Created by Admin on 10/12/2017.
 */

public class AlarmModule extends ReactContextBaseJavaModule {
    Context context;
    AlarmManager alarmManager;
    PendingIntent pending_intent;


    public AlarmModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.context = reactContext.getApplicationContext();
        alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);

    }

    @Override
    public String getName() {
        return "AlarmAndroid";
    }


    @ReactMethod
    public void createAlarm(int month,int day,String id_apoinment) {


        Calendar calendar = Calendar.getInstance();
        Intent my_intent = new Intent(context, AlarmReceiver.class);

        int hour_push = SharedPrefsUtils.getIntegerPreference(context,Const.HOUR_NOTI,20);
        int munite_push = SharedPrefsUtils.getIntegerPreference(context,Const.MUNITE_NOTI,00);

        //setting calendar instance with hour and minutes
//        calendar.set(Calendar.MONTH,month );
//        calendar.set(Calendar.DAY_OF_MONTH, day);
//        calendar.set(Calendar.HOUR_OF_DAY, hour_push);
//        calendar.set(Calendar.MINUTE, munite_push);
        calendar.set(Calendar.HOUR_OF_DAY, month);
        calendar.set(Calendar.MINUTE, day);
        calendar.set(Calendar.SECOND, 0);


        //put in extra string into my_intent
        my_intent.putExtra(Const.ALARM_NOTI, Const.ALARM_ON);
        my_intent.putExtra(Const.ALARM_NOTI_DATA, id_apoinment);

        //create pending intent
        String srtDay = day+""+month+""+calendar.get(Calendar.YEAR);
        final int id_unique = Integer.parseInt(srtDay);
        pending_intent = PendingIntent.getBroadcast(context, id_unique, my_intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        //set alarm manager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            alarmManager
                    .setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending_intent);
        } else {
            alarmManager
                    .set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending_intent);
        }
    }

    @ReactMethod
    private void cancelAlarm(int month,int day) {
        if (alarmManager!= null) {
            Calendar calendar = Calendar.getInstance();
            String srtDay = day+""+month+""+calendar.get(Calendar.YEAR);
            final int id_unique = Integer.parseInt(srtDay);
            Intent my_intent = new Intent(context, AlarmReceiver.class);
            PendingIntent  pendingIntent = PendingIntent.getBroadcast(context, id_unique, my_intent,
                    PendingIntent.FLAG_UPDATE_CURRENT);
            pendingIntent.cancel();
            alarmManager.cancel(pendingIntent);
        }
    }

}
