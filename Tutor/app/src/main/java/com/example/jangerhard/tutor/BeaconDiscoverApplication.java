package com.example.jangerhard.tutor;

import android.app.Application;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;

import org.joda.time.DateTime;

import java.util.List;
import java.util.UUID;

public class BeaconDiscoverApplication extends Application {

    private BeaconManager beaconManager;
    private TutorTimeTable timeTable;

    @Override
    public void onCreate() {
        super.onCreate();

        beaconManager = new BeaconManager(getApplicationContext());
        timeTable = new TutorTimeTable(getApplicationContext());

        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> list) {
                String[] tutors = timeTable.getTutors(DateTime.now());

                //String[] tutors = new String[]{"Ian Carvahlo/ic34882n@pace.edu", "Bhushan Surayawanshi/bs38923n@pace.edu"}; //For testing

                if (tutors == null)
                    showNotification("Welcome to the common area!","No one available right now.");

                else if (tutors.length > 1)
                    showNotification("Welcome to the common area!","Available tutors right now: "
                            + tutors[0].split("/")[0].split(" ")[0] //Returns first names only
                            + " and "
                            + tutors[1].split("/")[0].split(" ")[0]); //Returns first names only
                else
                    showNotification("Welcome to the common area!","Available tutor right now: "
                            + tutors[0].split("/")[0].split(" ")[0]); //Returns first names only
            }
            @Override
            public void onExitedRegion(Region region) {
                //showNotification("Exited!","You left the beacon region!");
            }
        });

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startMonitoring(new Region(
                        "Common Area",
                        UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"),
                        null, null));
            }
        });
    }

    public void showNotification(String title, String message) {
        Intent notifyIntent = new Intent(this, MainActivity.class);
        notifyIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivities(this, 0,
                new Intent[] { notifyIntent }, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new Notification.Builder(this)
                .setSmallIcon(R.drawable.ic_teacher_cutout)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notification.defaults |= Notification.DEFAULT_SOUND;
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1, notification);
    }
}