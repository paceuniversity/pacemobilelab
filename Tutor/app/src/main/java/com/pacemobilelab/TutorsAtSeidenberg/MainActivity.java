package com.pacemobilelab.TutorsAtSeidenberg;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import com.estimote.sdk.SystemRequirementsChecker;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences prefs = null;

    private Toolbar mToolbar;
    private Menu menu;

    private BeaconManager beaconManager;
    private Region region;
    private TutorTimeTable timeTable;

    int hiddenSecret;

    RecyclerView recList;
    GeneralCardAdapter ca;

    TextView title;
    Button bFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences("com.pacemobilelab.TutorsAtSeidenberg", MODE_PRIVATE);

        hiddenSecret = 0;
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hiddenSecret += 1;

                if (hiddenSecret >= 20) {
                    Toast.makeText(getApplicationContext(), "Stop pressing that..", Toast.LENGTH_SHORT).show();
                    hiddenSecret = 0;
                }
            }
        });

        timeTable = new TutorTimeTable(this);

        instantiate();

        //startBeaconRanging();

    }

    private void instantiate() {

        title = (TextView) findViewById(R.id.tv_title);

        recList = (RecyclerView) findViewById(R.id.cardList_main);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        List r = timeTable.getTutors(DateTime.now());
        //r = timeTable.getTestTutors();

        ca = new GeneralCardAdapter(r);

        recList.setAdapter(ca);

        setHeader(r.size());

        bFeedback = (Button) findViewById(R.id.bFeedback);
        bFeedback.setOnClickListener(this);
    }

    /**
     * Unused as ranging currently serves no purpose.
     * Could change in the future.
     */
    private void startBeaconRanging() {
        beaconManager = new BeaconManager(this);
        region = new Region("Common Area",
                UUID.fromString("B9407F30-F5F8-466E-AFF9-25556B57FE6D"), null, null);

        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                if (!list.isEmpty()) {
                    //refresh();
                } else {
                    title.setText("You're outside the reach of the beacons!");
                    //removeCards();
                }
            }
        });
    }

    public void setHeader(int size) {

        switch (size) {
            case 0:
                title.setText(getString(R.string.title_not_available));
                break;
            case 1:
                title.setText(getString(R.string.title_available_single));
                break;
            default:
                title.setText(getString(R.string.title_available_plural));

        }
    }

    public int getImageResource(String name) {
        switch (name) {

            case "Dhruvil":
                return R.drawable.dhruvil;
            case "Bhushan":
                return (R.drawable.bushan);
            case "Ian":
                return (R.drawable.ian);
            case "Jigar":
                return (R.drawable.jigar);
            case "Hardik":
                return (R.drawable.hardik);
            default:
                return (R.drawable.mickey);
        }
    }

    protected void sendEmail(String emailAddress, String subject) {
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{emailAddress});
        email.putExtra(Intent.EXTRA_SUBJECT, subject);
        email.setType("message/rfc822");
        startActivity(Intent.createChooser(email, "Choose an Email client :"));
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (prefs.getBoolean("firstrun", true)) {

            showAlertDialog("Welcome!", "Thank you for downloading this app! \n\n" +
                    "Check out the 'About' and 'How to use' in the top right corner, " +
                    "and enjoy!");

            prefs.edit().putBoolean("firstrun", false).commit();
        }

        SystemRequirementsChecker.checkWithDefaultDialogs(this);

//        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
//            @Override
//            public void onServiceReady() {
//                beaconManager.startRanging(region);
//            }
//        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        this.menu = menu;
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case R.id.settings:
                showAlertDialog(getString(R.string.title_about), getString(R.string.text_about));
                return true;
            case R.id.howto:
                showAlertDialog(getString(R.string.title_howto), getString(R.string.text_howto));
                return true;
            case R.id.rate:
                Intent i = new Intent(this, RateTutorsActivity.class);
                startActivity(i);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void showAlertDialog(String title, String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_new_launcher);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

    @Override
    protected void onPause() {
//        beaconManager.stopRanging(region);

        super.onPause();
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.bFeedback:
                sendEmail(getString(R.string.support_email), "Feedback on Tutor App");
                break;
        }
    }

}
