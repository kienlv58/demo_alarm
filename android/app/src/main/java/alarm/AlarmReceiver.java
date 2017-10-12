package alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by LUONGCONGDU on 08/05/2017.
 */

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {


        String n = intent.getExtras().getString(Const.ALARM_NOTI);
        //get string from intent
        if (n.equals(Const.ALARM_ON)) {Intent serviceIntent = new Intent(context, RingtonePlayingService.class);
            serviceIntent.putExtra(Const.ALARM_NOTI, Const.ALARM_ON);
            serviceIntent.putExtra(Const.ALARM_NOTI_DATA, intent.getExtras().getString(Const.ALARM_NOTI_DATA));
            context.startService(serviceIntent);
        }

    }
}
