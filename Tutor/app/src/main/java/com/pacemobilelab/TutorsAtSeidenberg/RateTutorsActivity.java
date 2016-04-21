package com.pacemobilelab.TutorsAtSeidenberg;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RateTutorsActivity extends AppCompatActivity {

    SharedPreferences prefs = null;

    FirebaseRecyclerAdapter<Tutor, TutorViewHolder> adapter;

    Toolbar mToolbar;
    RecyclerView recList;
    TutorTimeTable timetable;
    Firebase mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        prefs = getSharedPreferences("com.pacemobilelab.TutorsAtSeidenberg", MODE_PRIVATE);
        timetable = new TutorTimeTable(this);

        mRef = new Firebase("https://tutorsatseidenberg.firebaseio.com/tutors/");

        //setupTutors();

        setupLayout();
    }

    private void setupTutors() {

        Tutor t = (Tutor) timetable.getAllTutors().get(0);
        mRef.child(t.name.split(" ")[0]).setValue(t);
        t = (Tutor) timetable.getAllTutors().get(1);
        mRef.child(t.name.split(" ")[0]).setValue(t);
        t = (Tutor) timetable.getAllTutors().get(2);
        mRef.child(t.name.split(" ")[0]).setValue(t);
        t = (Tutor) timetable.getAllTutors().get(3);
        mRef.child(t.name.split(" ")[0]).setValue(t);
        t = (Tutor) timetable.getAllTutors().get(4);
        mRef.child(t.name.split(" ")[0]).setValue(t);
    }

    private void setupLayout() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recList = (RecyclerView) findViewById(R.id.cardList_rating);
        recList.setHasFixedSize(true);
        recList.setLayoutManager(new LinearLayoutManager(this));
    }

    /**
     * TODO: Fix rating system
     *
     * @param name Name of the tutor
     * @return The rating of the tutor
     */
    private float getRating(String name) {

        String shortName = name.split(" ")[0];

        SharedPreferences sp = this.getPreferences(MODE_PRIVATE);
        return sp.getFloat("RATING_" + shortName, (float) 0.5);
    }

    private void saveStars() {

        SharedPreferences.Editor spEditor = this.getPreferences(Context.MODE_PRIVATE).edit();

//        for (Tutor ti : ca.getTutorInfo()) {
//            String shortName = ti.name.split(" ")[0];
//            spEditor.putFloat("RATING_" + shortName, ti.rating);
//        }

        spEditor.commit();

        Toast.makeText(this, "Saved ratings", Toast.LENGTH_SHORT).show();

    }

    @Override
    protected void onStart() {
        super.onStart();

        Log.d("TEST", "Setting up adapter on ref: " + mRef.getRef());
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("TEST", "DataChange: " + dataSnapshot.getValue().toString());
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

        adapter = new FirebaseRecyclerAdapter<Tutor, TutorViewHolder>(
                Tutor.class,
                R.layout.card_layout_rating,
                TutorViewHolder.class,
                mRef) {
            @Override
            public void populateViewHolder(TutorViewHolder tutorViewHolder, Tutor tutor, int position) {
                Log.d("TEST", "Got tutor: " + tutor.name);
                tutorViewHolder.vName.setText(tutor.name);
                tutorViewHolder.vRating.setText("Average rating: " + tutor.rating_avg);
                tutorViewHolder.rb.setRating(getRating(tutor.name));
                tutorViewHolder.vImage.setImageResource(tutor.image_resource);
            }
        };
        recList.setAdapter(adapter);
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.cleanup();
    }

    public class TutorViewHolder extends RecyclerView.ViewHolder {

        protected TextView vName;
        protected TextView vRating;
        protected ImageView vImage;
        protected RatingBar rb;

        public TutorViewHolder(View v) {
            super(v);
            vName = (TextView) v.findViewById(R.id.tv_tutor_name_rating);
            vRating = (TextView) v.findViewById(R.id.tv_tutor_avg_rating_rating);
            vImage = (ImageView) v.findViewById(R.id.iv_tutor_rating);
            rb = (RatingBar) v.findViewById(R.id.ratingbar);
        }
    }
}
