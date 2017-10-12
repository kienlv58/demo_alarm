package alarm;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.IBinder;
import android.os.PowerManager;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.alarm_demo.MainActivity;
import com.alarm_demo.R;

/**
 * Created by LUONGCONGDU on 09/05/2017.
 */

public class RingtonePlayingService extends Service {
    MediaPlayer mediaPlayer;
    boolean isRunning;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("Local service", "Receiver start id " + startId + ": " + intent);

        //get the extra string values
        String state = intent.getExtras().getString("extra");

        Log.e("Ringtone state: ", state);

        assert state != null;
        switch (state) {
            case "alarm on":
                startId = 1;
                break;
            case "alarm off":
                startId = 0;
                break;
            default:
                startId = 0;
                break;
        }


        //if user want "alarm on"
        //music play
        if (!this.isRunning && startId == 1) {

            //create instance of the media player
            mediaPlayer = MediaPlayer.create(this, R.raw.alarm);
            mediaPlayer.setVolume(50, 50);
            mediaPlayer.start();

            this.isRunning = true;

            //create notification
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
            PendingIntent pendingMainIntent = PendingIntent.getActivity(this, 0, mainIntent, 0);

            Notification notification = new Notification.Builder(this)
                    .setContentTitle("Alarm is going off")
                    .setContentText("Tap here")
                    .setContentIntent(pendingMainIntent)
                    .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
                    .setAutoCancel(true)
                    .build();

            notificationManager.notify(0, notification);

//wake up screen
            PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
            PowerManager.WakeLock wakeLock =
                    pm.newWakeLock(PowerManager.FULL_WAKE_LOCK
                            | PowerManager.ACQUIRE_CAUSES_WAKEUP
                            | PowerManager.ON_AFTER_RELEASE, "WakeLockLauncher");
            wakeLock.acquire();

        }


        //if user want "alarm off"
        //music stop
        else if (this.isRunning && startId == 0) {

            //stop the ringtone
            mediaPlayer.stop();
            mediaPlayer.reset();

            this.isRunning = false;
        }

        //no music playing. user want "alarm off"
        //do nothing
        else if (!this.isRunning && startId == 0) {
            this.isRunning = false;
        }

        //music playing. user want "alarm on"
        //do nothing
        else if (this.isRunning && startId == 1) {
            this.isRunning = true;
        }

        return START_NOT_STICKY;
    }

    @Override
    public void onDestroy() {

        Toast.makeText(this, "on Destroy called", Toast.LENGTH_SHORT).show();
        super.onDestroy();
        this.isRunning = false;
    }

}
