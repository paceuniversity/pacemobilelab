package com.pacemobilelab.TutorsAtSeidenberg;

import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
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
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import com.estimote.sdk.SystemRequirementsChecker;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.FirebaseException;
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseRecyclerAdapter;

import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    SharedPreferences prefs = null;

    private Toolbar mToolbar;

    int hiddenSecret;

    RecyclerView recList;

    FirebaseRecyclerAdapter<Tutor, TutorViewHolder> adapter;

    Firebase mRef;
    TextView title;
    Button bFeedback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prefs = getSharedPreferences("com.pacemobilelab.TutorsAtSeidenberg", MODE_PRIVATE);
        mRef = new Firebase("https://tutorsatseidenberg.firebaseio.com/");

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

        instantiate();
    }

    private void instantiate() {

        title = (TextView) findViewById(R.id.tv_title);
        title.setText("Available right now");

        recList = (RecyclerView) findViewById(R.id.cardList_main);
        recList.setHasFixedSize(true);
        recList.setLayoutManager(new LinearLayoutManager(this));

        bFeedback = (Button) findViewById(R.id.bFeedback);
        bFeedback.setOnClickListener(this);

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
    }

    @Override
    protected void onStart() {
        super.onStart();

        final Firebase tut = mRef.child("tutors");

        Query queryRef = tut.orderByChild("atWork").equalTo(true);

        adapter = new FirebaseRecyclerAdapter<Tutor, TutorViewHolder>(
                Tutor.class,
                R.layout.card_layout_normal,
                TutorViewHolder.class,
                queryRef
        ) {
            @Override
            public void populateViewHolder(TutorViewHolder tutorViewHolder, final Tutor tutor, int position) {

                tutorViewHolder.vName.setText(tutor.name);
                tutorViewHolder.vEmail.setText(tutor.email);

                try {
                    tutorViewHolder.vImage.setBackgroundResource(tutor.imageResource);
                } catch (Resources.NotFoundException e) {
                    if (tutor.imageResource == 1234)
                        tutorViewHolder.vImage.setBackgroundResource(R.drawable.mickey);
                    else
                        tutorViewHolder.vImage.setBackgroundResource(R.drawable.noimage);
                }

                tutorViewHolder.bRate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openRatePage();
                    }
                });
                tutorViewHolder.bEmail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        sendEmail(tutor.email, "Tutor Question");
                    }
                });
            }
        };
        recList.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
                openRatePage();
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

    public void openRatePage() {
        Intent i = new Intent(this, RateTutorsActivity.class);
        startActivity(i);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //adapter.cleanup();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.bFeedback:
                sendEmail(getString(R.string.support_email), "Feedback on Tutor App");
                break;
        }
    }

    public static class TutorViewHolder extends RecyclerView.ViewHolder {

        protected TextView vName;
        protected TextView vEmail;
        protected ImageView vImage;
        protected Button bRate;
        protected Button bEmail;
        protected View v;

        public TutorViewHolder(View v) {
            super(v);
            this.v = v;
            vName = (TextView) v.findViewById(R.id.tv_tutor_name_general);
            vEmail = (TextView) v.findViewById(R.id.tv_tutor_email_general);
            vImage = (ImageView) v.findViewById(R.id.iv_tutor_general);
            bEmail = (Button) v.findViewById(R.id.b_email);
            bRate = (Button) v.findViewById(R.id.b_rate);
        }
    }
}
