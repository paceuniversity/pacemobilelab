package com.example.jangerhard.tutor;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
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

import org.joda.time.DateTime;

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

    TextView title, tv_tutor1_name, tv_tutor1_email, tv_tutor2_name, tv_tutor2_email;
    CardView cTutor1, cTutor2;
    ImageView ivTutor1, ivTutor2;
    Button bTutor1, bTutor2, bRate1, bRate2, bFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences("com.mycompany.myAppName", MODE_PRIVATE);

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

        refresh();

        checkForBlueTooth();
        //startBeaconRanging();

    }

    private void instantiate() {

        title = (TextView) findViewById(R.id.tv_title);

        tv_tutor1_name = (TextView) findViewById(R.id.tv_tutor1_name);
        tv_tutor1_email = (TextView) findViewById(R.id.tv_tutor1_email);
        tv_tutor2_name = (TextView) findViewById(R.id.tv_tutor2_name);
        tv_tutor2_email = (TextView) findViewById(R.id.tv_tutor2_email);
        ivTutor1 = (ImageView) findViewById(R.id.iv_Tutor1);
        ivTutor2 = (ImageView) findViewById(R.id.iv_Tutor2);
        bTutor1 = (Button) findViewById(R.id.bTutor1);
        bTutor2 = (Button) findViewById(R.id.bTutor2);
        bRate1 = (Button) findViewById(R.id.bRate1);
        bRate1.setOnClickListener(this);
        bRate2 = (Button) findViewById(R.id.bRate2);
        bRate2.setOnClickListener(this);
        cTutor1 = (CardView) findViewById(R.id.card1);
        cTutor2 = (CardView) findViewById(R.id.card2);

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
                    refresh();
                } else {
                    title.setText("You're outside the reach of the beacons!");
                    removeCards();
                }
            }
        });
    }

    private void refresh(){

        removeCards();

        String[] tutors = timeTable.getTutors(DateTime.now());
        //String[] tutors = new String[]{"Bhushan Surayawanshi/bs38923n@pace.edu", "Ian Carvahlo/ic34882n@pace.edu"}; //For testing


        if (tutors == null) {
            title.setText(getString(R.string.title_not_available));

        } else {

            if (tutors.length > 1)
                title.setText(getString(R.string.title_available_plural));
            else
                title.setText(getString(R.string.title_available_single));

            displayCards(tutors);
        }

    }

    private void displayCards(String[] tutors){

        String[] tutor;

        if (tutors.length > 1) {
            tutor = tutors[1].split("/");

            tv_tutor2_name.setText(tutor[0]);
            tv_tutor2_email.setText(tutor[1]);
            setCardPhoto(ivTutor2, tutor[0].split(" ")[0]);
            bTutor2.setTag(tutor[1]);

            bTutor2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sendEmail(bTutor2.getTag().toString(), "Tutor Question");
                }
            });

            cTutor2.setVisibility(View.VISIBLE);
        }

        tutor = tutors[0].split("/");

        tv_tutor1_name.setText(tutor[0]);
        tv_tutor1_email.setText(tutor[1]);
        setCardPhoto(ivTutor1, tutor[0].split(" ")[0]);
        bTutor1.setTag(tutor[1]);

        bTutor1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendEmail(bTutor1.getTag().toString(), "Tutor Question");
            }
        });

        cTutor1.setVisibility(View.VISIBLE);


    }

    private void setCardPhoto(ImageView iv, String name){

        switch (name) {

            case "Dhruvil":
                iv.setImageResource(R.drawable.dhruvil);
                break;
            case "Bhushan":
                iv.setImageResource(R.drawable.bushan);
                break;
            case "Ian":
                iv.setImageResource(R.drawable.ian);
                break;
            case "Jigar":
                iv.setImageResource(R.drawable.jigar);
                break;
            case "Hardik":
                iv.setImageResource(R.drawable.hardik);
                break;
            default:
                iv.setImageResource(R.drawable.mickey);
                break;
        }
    }

    private void sendEmail(String emailAddress, String subject){
        Intent email = new Intent(Intent.ACTION_SEND);
        email.putExtra(Intent.EXTRA_EMAIL, new String[]{emailAddress});
        email.putExtra(Intent.EXTRA_SUBJECT, subject);
        email.setType("message/rfc822");
        startActivity(Intent.createChooser(email, "Choose an Email client :"));
    }

    private void removeCards(){
        cTutor1.setVisibility(View.GONE);
        cTutor2.setVisibility(View.GONE);
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

        refresh();

        //SystemRequirementsChecker.checkWithDefaultDialogs(this);

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
        }

        return super.onOptionsItemSelected(item);
    }

    private void showAlertDialog(String title, String message){
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
        switch (v.getId()){
            case R.id.bRate1:
                break;
            case R.id.bRate2:
                break;
            case R.id.bFeedback:
                sendEmail(getString(R.string.support_email), "Feedback on Tutor App");
                break;
        }
    }

    private void checkForBlueTooth() {

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        if (mBluetoothAdapter == null) {
            showAlertDialog("Bluetooth not supported!", "It appears your device does not support " +
                    "bluetooth, and you will therefore not get the full experience of this app..");
        } else {
            if (!mBluetoothAdapter.isEnabled()) {
                // Bluetooth is not enable :)
                showAlertDialog("Bluetooth not active!", "If you want to receive notifications, " +
                        "please activate your bluetooth!");
            }
        }
    }

}
