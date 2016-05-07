package com.pacemobilelab.TutorsAtSeidenberg;

import android.util.Log;

public class Tutor {

    protected String name;
    protected String email;
    protected int image_resource;
    protected float rating;
    protected float rating_avg;
    protected WorkSchedule sch;
    protected static final String NAME_PREFIX = "Name_";
    protected static final String EMAIL_PREFIX = "email_";
    protected static final String IMAGE_PREFIX = "image_";
    protected static final String RATING_PREFIX = "rating_";
    protected static final String RATING_AVG_PREFIX = "rating_avg_";

    public Tutor(){}

    public Tutor(String name, String email){

        this.name = name;
        this.email = email;
        image_resource = getImageResource(name.split(" ")[0]);
        rating = 0.5f;
        rating_avg = 1.5f;

        sch = new WorkSchedule();
    }

    public void addWork(int day, int startHour, int finishHour){
        try {
            sch.addTime(day, startHour,finishHour);
        } catch (WorkSchedule.OutOfBoundsExeption outOfBoundsExeption) {
            outOfBoundsExeption.printStackTrace();
        }
    }

    public boolean isWorking(int day, int hour){
        boolean truth = sch.isWorking(day,hour);

        Log.d("TEST", name + " is working on " + day + "? " + truth);

        return truth;
    }

    public WorkSchedule getSch(){
        return sch;
    }

    public int getImageResource(String name){
        switch (name) {

            case "Dhruvil":
                return R.drawable.dhruvil;
            case "Bhushan":
                return(R.drawable.bushan);
            case "Ian":
                return(R.drawable.ian);
            case "Jigar":
                return(R.drawable.jigar);
            case "Hardik":
                return(R.drawable.hardik);
            default:
                return(R.drawable.mickey);
        }
    }

}
