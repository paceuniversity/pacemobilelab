package com.pacemobilelab.TutorsAtSeidenberg;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
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
import com.firebase.client.Query;
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseRecyclerAdapter;

public class RateTutorsActivity extends AppCompatActivity {

    SharedPreferences prefs = null;

    String ratingKey;

    FirebaseRecyclerAdapter<Tutor, TutorViewHolder> adapter;

    Toolbar mToolbar;
    RecyclerView recList;
    Firebase mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        prefs = getSharedPreferences("com.pacemobilelab.TutorsAtSeidenberg", MODE_PRIVATE);

        mRef = new Firebase("https://tutorsatseidenberg.firebaseio.com/");

        setupLayout();
    }

    private void setupLayout() {
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        recList = (RecyclerView) findViewById(R.id.cardList_rating);
        recList.setHasFixedSize(true);
        recList.addItemDecoration(new SimpleDividerItemDecoration(this));
        recList.setLayoutManager(new LinearLayoutManager(this));
    }

    private float getRating(String name) {

        String shortName = name.split(" ")[0];

        SharedPreferences sp = this.getPreferences(MODE_PRIVATE);
        return sp.getFloat("RATING_" + shortName, (float) 0.0);
    }

    private void saveStars(String name, float rating) {

        //Saving to local
        String shortName = name.split(" ")[0];
        SharedPreferences.Editor spEditor = this.getPreferences(Context.MODE_PRIVATE).edit();
        spEditor.putFloat("RATING_" + shortName, rating);
        spEditor.apply();

        //Saving to Firebase
        if (rating > 0){
            Firebase q = mRef.child("ratings/" + shortName + "/" + ratingKey);
            q.setValue(rating);
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        //adapter.cleanup();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (prefs.getBoolean("first", true)) {

            prefs.edit().putBoolean("first", false).commit();

            //Generate new rating Key
            Firebase q = mRef.child("ratings/");
            String newKey = q.push().getKey();
            Log.d("TEST", "New Key: " + newKey);
            prefs.edit().putString("ratingKey", newKey).commit();
        }
        ratingKey = prefs.getString("ratingKey", "test");

        final Firebase tut = mRef.child("tutors");

        adapter = new FirebaseRecyclerAdapter<Tutor, TutorViewHolder>(
                Tutor.class,
                R.layout.card_layout_rating,
                TutorViewHolder.class,
                tut
        ) {

            @Override
            public void populateViewHolder(TutorViewHolder tutorViewHolder, final Tutor tutor, int position) {

                tutorViewHolder.vName.setText(tutor.name);
                tutorViewHolder.vRating.setText("Average rating: " + tutor.rating_avg);
                tutorViewHolder.rb.setRating(getRating(tutor.name));
                try {
                    tutorViewHolder.vImage.setBackgroundResource(tutor.imageResource);
                } catch (Resources.NotFoundException e) {
                    if (tutor.imageResource == 1234)
                        tutorViewHolder.vImage.setBackgroundResource(R.drawable.mickey);
                    else
                        tutorViewHolder.vImage.setBackgroundResource(R.drawable.noimage);
                }

                tutorViewHolder.rb.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
                    @Override
                    public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                        saveStars(tutor.name, rating);
                    }
                });
            }
        };
        recList.setAdapter(adapter);

        Firebase ratings = mRef.child("ratings");

        ratings.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d("TEST", "There are " + dataSnapshot.getChildrenCount() + " tutors rated");
                for (DataSnapshot postSnapshot: dataSnapshot.getChildren()) {

                    float avg_rating = 0;
                    for (DataSnapshot rating: postSnapshot.getChildren()){
                        avg_rating += rating.getValue(float.class);
                    }
                    avg_rating = avg_rating/(float) postSnapshot.getChildrenCount();
                    Log.d("TEST", "Rating for " + postSnapshot.getKey() + " is " + avg_rating);

                    tut.child(postSnapshot.getKey()).child("rating_avg").setValue(avg_rating);
                }
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });
    }

    public static class TutorViewHolder extends RecyclerView.ViewHolder {

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
