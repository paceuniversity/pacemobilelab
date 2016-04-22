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
import com.firebase.client.ValueEventListener;
import com.firebase.ui.FirebaseRecyclerAdapter;

public class RateTutorsActivity extends AppCompatActivity {

    SharedPreferences prefs = null;

    FirebaseRecyclerAdapter<Tutor, TutorViewHolder> adapter;

    Toolbar mToolbar;
    RecyclerView recList;
    Firebase mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        prefs = getSharedPreferences("com.pacemobilelab.TutorsAtSeidenberg", MODE_PRIVATE);

        mRef = new Firebase("https://tutorsatseidenberg.firebaseio.com/tutors/");

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

    /**
     * TODO: Fix rating system
     *
     * @param name Name of the tutor
     * @return The rating of the tutor
     */
    private float getRating(String name) {

        String shortName = name.split(" ")[0];

        SharedPreferences sp = this.getPreferences(MODE_PRIVATE);
        return sp.getFloat("RATING_" + shortName, (float) 0.0);
    }

    private void saveStars(String name, float rating) {

        String shortName = name.split(" ")[0];

        SharedPreferences.Editor spEditor = this.getPreferences(Context.MODE_PRIVATE).edit();

        spEditor.putFloat("RATING_" + shortName, rating);

        spEditor.commit();

    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.cleanup();
    }

    @Override
    protected void onResume() {
        super.onResume();


        adapter = new FirebaseRecyclerAdapter<Tutor, TutorViewHolder>(
                Tutor.class,
                R.layout.card_layout_rating,
                TutorViewHolder.class,
                mRef) {
            @Override
            public void populateViewHolder(TutorViewHolder tutorViewHolder, final Tutor tutor, int position) {
                tutorViewHolder.vName.setText(tutor.name);
                tutorViewHolder.vRating.setText("Average rating: " + tutor.rating_avg);
                tutorViewHolder.rb.setRating(getRating(tutor.name));
                try {
                    tutorViewHolder.vImage.setImageResource(tutor.imageResource);
                } catch (Resources.NotFoundException e){
                    tutorViewHolder.vImage.setImageResource(R.drawable.mickey);
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
