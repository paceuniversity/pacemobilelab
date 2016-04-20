package com.pacemobilelab.TutorsAtSeidenberg;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class RateTutorsActivity extends AppCompatActivity {

    SharedPreferences prefs = null;

    Toolbar mToolbar;
    RecyclerView recList;
    RateCardAdapter ca;
    SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        prefs = getSharedPreferences("com.pacemobilelab.TutorsAtSeidenberg", MODE_PRIVATE);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeColors(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshData();
            }
        });

        recList = (RecyclerView) findViewById(R.id.cardList_rating);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        refreshData();

    }

    private void refreshData(){
        ca = new RateCardAdapter(createList());
        recList.setAdapter(ca);
        recList.addItemDecoration(new SimpleDividerItemDecoration(this));
        mSwipeRefreshLayout.setRefreshing(false);
    }

    private List createList() {

        List result = new ArrayList();

        Resources res = getResources();
        String[] tutors = res.getStringArray(R.array.tutor_strings);

        for (String t: tutors){
            String[] info = t.split("/");

            TutorInfo ti = new TutorInfo();
            ti.name = info[0];
            ti.email = info[1];
            ti.rating = getRating(info[0]);
            ti.rating_avg = (float) 1.5;
            ti.image_resource = getImageResource(info[0]);

            result.add(ti);
        }

        return result;
    }

    /**
     * TODO: Fix rating system
     * @param name Name of the tutor
     * @return The rating of the tutor
     */
    private float getRating(String name){

        String shortName = name.split(" ")[0];

        SharedPreferences sp = this.getPreferences(MODE_PRIVATE);
        return sp.getFloat("RATING_" + shortName, (float)0.5);
    }

    public int getImageResource(String name){
        switch (name) {
            case "Dhruvil Gandhi":
                return R.drawable.dhruvil;
            case "Bhushan Surayawanshi":
                return(R.drawable.bushan);
            case "Ian Carvahlo":
                return(R.drawable.ian);
            case "Jigar Mehta":
                return(R.drawable.jigar);
            case "Hardik Patel":
                return(R.drawable.hardik);
            default:
                return(R.drawable.mickey);
        }
    }

    private void saveStars(){

        SharedPreferences.Editor spEditor = this.getPreferences(Context.MODE_PRIVATE).edit();

        for (TutorInfo ti: ca.getTutorInfo()){
            String shortName = ti.name.split(" ")[0];
            spEditor.putFloat("RATING_" + shortName, ti.rating);
        }

        spEditor.commit();

        Toast.makeText(this, "Saved ratings", Toast.LENGTH_SHORT).show();

    }

    /**
     * Checks if the user changed any ratings
     * @return true if the user has changed, and false if there was no change
     */
    private boolean hasChanged(){

        SharedPreferences spEditor = this.getPreferences(Context.MODE_PRIVATE);

        for (TutorInfo ti: ca.getTutorInfo()){
            String shortName = ti.name.split(" ")[0];
            float r = spEditor.getFloat("RATING_" + shortName, ti.rating);
            if (ti.rating != r)
                return true;
        }

        return false;
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (hasChanged()){
            Toast.makeText(this, "You changed your ratings!", Toast.LENGTH_SHORT).show();
            saveStars();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (prefs.getBoolean("firstrun", true)) {
            Toast.makeText(this, "First run", Toast.LENGTH_SHORT).show();
            saveStars();
            prefs.edit().putBoolean("firstrun", false).commit();
        }
    }




}
