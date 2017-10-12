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
        Log.e("We are in the receiver", "Danny");

        //get string from intent
        String get_your_string = intent.getExtras().getString("extra");
        Log.e("What is the key", get_your_string);



        //create intent to the ringtone service
        Intent serviceIntent = new Intent(context, RingtonePlayingService.class);
        //pass the extra string from Main to Ringtone
        serviceIntent.putExtra("extra", get_your_string);

        context.startService(serviceIntent);
    }
}
