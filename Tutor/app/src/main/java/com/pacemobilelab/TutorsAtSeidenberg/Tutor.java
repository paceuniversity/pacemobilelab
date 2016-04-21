package com.pacemobilelab.TutorsAtSeidenberg;

public class Tutor {

    String name;
    String email;
    int image_resource;
    float rating;
    float rating_avg;
    WorkSchedule sch;

    public String getName() {
        return name;
    }

    public int getImage_resource() {
        return image_resource;
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

    public Tutor(){
        image_resource = getImageResource(name.split(" ")[0]);
        rating = 0.5f;
        rating_avg = 1.5f;

        sch = new WorkSchedule();
    }

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
        return sch.isWorking(day,hour);
    }

    public WorkSchedule getSch(){
        return sch;
    }

    private int getImageResource(String name){
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
