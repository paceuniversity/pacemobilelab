package com.pacemobilelab.TutorsAtSeidenberg;

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

                List tutors = timeTable.getTutors(DateTime.now());
                //String[] tutors = new String[]{"Ian Carvahlo/ic34882n@pace.edu", "Bhushan Surayawanshi/bs38923n@pace.edu"}; //For testing


                switch (tutors.size()) {
                    case 0:
                        showNotification("Welcome to the common area!",
                                "No one available right now.");
                        break;
                    case 1:
                        showNotification("Welcome to the common area!",
                                "Available tutor right now: " +
                                        ((Tutor) tutors.get(0)).name.split(" ")[0]);
                        break;
                    default:
                        String txt = "Available tutors right now: ";

                        for (int i = 0; i < tutors.size(); i++) {
                            txt += ((Tutor) tutors.get(i)).name.split(" ")[i];

                            if (i==tutors.size()-1)
                                break;

                            txt += " and ";
                        }

                        showNotification("Welcome to the common area!", txt);

                }
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
                new Intent[]{notifyIntent}, PendingIntent.FLAG_UPDATE_CURRENT);
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