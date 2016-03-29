package com.example.jangerhard.tutor;

import android.content.Context;

import org.joda.time.DateTime;

/**
 * Created by jangerhard on 18-Mar-16.
 */
public class TutorTimeTable {

    String tutor_Dhruvil, tutor_Ian, tutor_Bushan, tutor_Hardik, tutor_Jigar;

    public TutorTimeTable(Context context){

        tutor_Dhruvil = context.getString(R.string.tutor_Dhruvil);
        tutor_Ian = context.getString(R.string.tutor_Ian);
        tutor_Bushan = context.getString(R.string.tutor_Bhushan);
        tutor_Hardik = context.getString(R.string.tutor_Hardik);
        tutor_Jigar = context.getString(R.string.tutor_Jigar);

    }

    public String[] getTutors(DateTime rightNow) {

        int day = rightNow.getDayOfWeek();
        int hour = rightNow.getHourOfDay();
        //int minute = rightNow.getMinuteOfHour();

        switch (day){
            case 1: case 2: //Monday and Tuesday
                if (hour >= 12 && hour <= 17 )
                    return new String[] {tutor_Bushan};
                break;
            case 3: //Wednesday
                if (hour >= 15 && hour <= 16 )
                    return new String[] {tutor_Dhruvil, tutor_Ian};
                else if (hour >= 16 && hour <= 18 )
                    return new String[] {tutor_Ian, tutor_Jigar};
                else if (hour >= 12 && hour <= 16 )
                    return new String[] {tutor_Dhruvil};
                if (hour >= 16 && hour <= 19 )
                    return new String[] {tutor_Jigar};
                break;
            case 4: //Thursday
                if (hour >= 10 && hour <= 12 )
                    return new String[] {tutor_Ian};
                else if (hour >= 12 && hour <= 15 )
                    return new String[] {tutor_Hardik, tutor_Ian};
                else if (hour >= 15 && hour <= 17 )
                    return new String[] {tutor_Hardik};
                break;
            case 5: //Friday
                if (hour >= 10 && hour <= 13 )
                    return new String[] {tutor_Ian};
                else if (hour >= 13 && hour <= 14 )
                    return new String[] {tutor_Hardik, tutor_Ian};
                else if (hour >= 14 && hour <= 17 )
                    return new String[] {tutor_Hardik};
                break;

        }

        return null;
    }

}
