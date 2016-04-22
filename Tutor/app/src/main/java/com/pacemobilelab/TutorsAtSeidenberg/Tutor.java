package com.pacemobilelab.TutorsAtSeidenberg;

public class Tutor {

    String name;
    String email;
    int imageResource;
    float rating;
    float rating_avg;
    //WorkSchedule sch;

    public Tutor(){}

    public String getName() {
        return name;
    }

    public int getImageResource() {
        return imageResource;
    }

    public String getEmail() {
        return email;
    }

    public float getRating_avg() {
        return rating_avg;
    }

    public float getRating() {
        return rating;
    }

    //public WorkSchedule getSch(){return sch;}

}
