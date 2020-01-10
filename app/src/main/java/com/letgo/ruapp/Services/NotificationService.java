package com.letgo.ruapp.Services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.letgo.ruapp.Schedule.ScheduleObject;
import com.letgo.ruapp.Schedule.ScheduleObject.Status;
import com.letgo.ruapp.Handlers.ScheduleHandler;
import com.letgo.ruapp.LoginActivity;
import com.letgo.ruapp.R;

import java.util.Timer;
import java.util.TimerTask;

public class NotificationService extends Service {
    private String startTime = "";
    private String endTime = "";
    private int c;
    private boolean isInApp=true;
    private boolean wasConnected=true;
    private Timer timer;
    protected NotificationCompat.Builder builder;
    protected NotificationManagerCompat notificationManager;
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        builder = new NotificationCompat.Builder(this, "My Channel")
                .setSmallIcon(R.drawable.ic_launcher_foreground)
                .setContentTitle("Next Class Reminder")
                .setContentText("You have class in x minutes")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setOngoing(true);
        notificationManager = NotificationManagerCompat.from(this);
        if (timer!=null) {
            timer.cancel();
            wasConnected=true;
            isInApp=true;
        }
        timer=new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                ScheduleHandler handler = new ScheduleHandler();
                if (handler.getSchedule().isEmpty()) {
                    isInApp=false;
                    new ScheduleHandler().load(getApplicationContext());
                }
                if (isInApp){
                    ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
                    if (mWifi.isConnected() && !wasConnected) {
                        Toast.makeText(getApplicationContext(), "Wifi Connected, Reloading", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                        startActivity(intent);
                    }
                }
                Status status = handler.getStatus();
                ScheduleObject obj = handler.getNextObj();
                if (status == Status.WAITING) {
                    builder.setContentText("In: "+handler.getDifference() + " - Loc: " + obj.getVal("location"));
                    notificationManager.notify(1, builder.build());
                }
                else notificationManager.cancelAll();
                if (isInApp){
                    ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    wasConnected= connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).isConnected();
                }
            }
        }, 0, 5000);
        return START_STICKY;
    }
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
