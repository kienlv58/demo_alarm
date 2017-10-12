package reactmodule;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;


import java.util.Calendar;

import alarm.AlarmReceiver;

/**
 * Created by Admin on 10/12/2017.
 */

public class AlarmModule extends ReactContextBaseJavaModule {
    Context context;
    AlarmManager alarmManager;
    PendingIntent pending_intent;
    public AlarmModule(ReactApplicationContext reactContext) {
        super(reactContext);
        this.context = reactContext;
        alarmManager = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
    }

    @Override
    public String getName() {
        return "AlarmAndroid";
    }


    @ReactMethod
    public void createAlarm(int hour, int minute) {


        Calendar calendar = Calendar.getInstance();
        Intent my_intent = new Intent(context, AlarmReceiver.class);

        //setting calendar instance with hour and minutes
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);


        //put in extra string into my_intent
        my_intent.putExtra("extra", "alarm on");

        //create pending intent
        pending_intent = PendingIntent.getBroadcast(context, 0, my_intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        //set alarm manager
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pending_intent);
    }

    @ReactMethod
    private void cancelAlarm() {
        if (alarmManager!= null) {
            alarmManager.cancel(pending_intent);
        }
    }

}
