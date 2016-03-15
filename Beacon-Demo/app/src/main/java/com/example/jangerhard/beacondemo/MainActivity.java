package com.example.jangerhard.beacondemo;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.SystemRequirementsChecker;

import org.joda.time.DateTime;

import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private BeaconManager beaconManager;
    private Region region;

    TextView main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        main = (TextView) findViewById(R.id.tv_main);

        beaconManager = new BeaconManager(this);
        region = new Region("Common Area",
                UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), null, null);

        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                if (!list.isEmpty()) {

                    main.setText("Available tutor(s):\n\n" + getSchedule());

                } else {
                    main.setText("You're outside the common area!");
                }
            }
        });
    }

    private String getSchedule(){

        DateTime dt = new DateTime();
        int hourOfDay = dt.getHourOfDay();

        //hourOfDay = 13;

        switch (dt.getDayOfWeek()){
            case 1: //Monday
                if (hourOfDay >= 12 && hourOfDay <= 17 )
                    return "Bhushan Surayawanshi - bs38923n@pace.edu";
                break;
            case 2: //Tuesday
                if (hourOfDay >= 12 && hourOfDay <= 17 )
                    return "Bhushan Surayawanshi - bs38923n@pace.edu";
                break;
            case 3: //Wednesday
                if (hourOfDay >= 15 && hourOfDay <= 16 )
                    return "Dhruvil Gandhi - dg83196@pace.edu\n" +
                            "and\n" +
                            "Ian Carvahlo - ic34882n@pace.edu";
                else if (hourOfDay >= 16 && hourOfDay <= 18 )
                    return "Ian Carvahlo - ic34882n@pace.edu\n" +
                            "and\n" +
                            "Jigar Mehta - jm85438@pace.edu";
                else if (hourOfDay >= 12 && hourOfDay <= 16 )
                    return "Dhruvil Gandhi - dg83196@pace.edu";
                if (hourOfDay >= 16 && hourOfDay <= 19 )
                    return "Jigar Mehta - jm85438@pace.edu";
                break;
            case 4: //Thursday
                if (hourOfDay >= 10 && hourOfDay <= 12 )
                    return "Ian Carvahlo - ic34882n@pace.edu";
                else if (hourOfDay >= 12 && hourOfDay <= 15 )
                    return "Ian Carvahlo - ic34882n@pace.edu\n" +
                            "and\n" +
                            "Hardik Patel - hp68381@pace.edu";
                else if (hourOfDay >= 15 && hourOfDay <= 17 )
                    return "Hardik Patel - hp68381@pace.edu";
                break;
            case 5: //Friday
                if (hourOfDay >= 10 && hourOfDay <= 13 )
                    return "Ian Carvahlo - ic34882n@pace.edu";
                else if (hourOfDay >= 13 && hourOfDay <= 14 )
                    return "Ian Carvahlo - ic34882n@pace.edu\n" +
                            "and\n" +
                            "Hardik Patel - hp68381@pace.edu";
                else if (hourOfDay >= 14 && hourOfDay <= 17 )
                    return "Hardik Patel - hp68381@pace.edu";
                break;

        }

        return "Sorry, no tutors available right now..";
    }

    @Override
    protected void onResume() {
        super.onResume();

        SystemRequirementsChecker.checkWithDefaultDialogs(this);

        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(region);
            }
        });
    }

    @Override
    protected void onPause() {
        beaconManager.stopRanging(region);

        super.onPause();
    }
}
