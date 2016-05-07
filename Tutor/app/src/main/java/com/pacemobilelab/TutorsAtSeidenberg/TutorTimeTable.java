package com.pacemobilelab.TutorsAtSeidenberg;

import android.content.Context;
import android.util.Log;

import org.joda.time.DateTime;

import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Create a better way of instantiating tutors and updating the schedule.
 */
public class TutorTimeTable {
    public final int MONDAY=0, TUESDAY=1, WEDNESDAY=2,
            THURSDAY=3, FRIDAY=4, SATURDAY=5, SUNDAY =6;

    List allTutors;

    public TutorTimeTable(Context context) {

        allTutors = new ArrayList();

        String[] tutorStrings = context.getResources().getStringArray(R.array.tutor_strings);

        //Bhushan
        Tutor t = new Tutor(tutorStrings[0].split("/")[0], tutorStrings[0].split("/")[1]);
        t.addWork(MONDAY, 12, 17);
        t.addWork(TUESDAY, 12, 17);
        allTutors.add(t);

        //Ian
        t = new Tutor(tutorStrings[1].split("/")[0], tutorStrings[1].split("/")[1]);
        t.addWork(WEDNESDAY, 15, 18);
        t.addWork(THURSDAY, 10, 15);
        t.addWork(FRIDAY, 10, 12);
        allTutors.add(t);

        //Jigar
        t = new Tutor(tutorStrings[2].split("/")[0], tutorStrings[2].split("/")[1]);
        t.addWork(WEDNESDAY, 16, 19);
        allTutors.add(t);

        //Dhruvil
        t = new Tutor(tutorStrings[3].split("/")[0], tutorStrings[3].split("/")[1]);
        t.addWork(WEDNESDAY, 12, 16);
        allTutors.add(t);

        //Hardik
        t = new Tutor(tutorStrings[4].split("/")[0], tutorStrings[4].split("/")[1]);
        t.addWork(THURSDAY, 12, 17);
        t.addWork(FRIDAY, 12, 17);
        allTutors.add(t);

    }

    /**
     * TODO: MUST BE IMPROVED!!
     *
     * @param rightNow
     * @return
     */
    public List getTutors(DateTime rightNow) {

        List result = new ArrayList();

        int day = rightNow.getDayOfWeek();
        int hour = rightNow.getHourOfDay();

        Log.d("TEST", "day: " + day);

        for (int i = 0; i < allTutors.size(); i++) {
            if (isWorking(day, allTutors.get(i), hour))
                result.add(allTutors.get(i));
        }

        return result;
    }

    public List getTestTutors(){
        List l = new ArrayList();

        l.add(allTutors.get(1));
        l.add(allTutors.get(2));

        return l;
    }

    private boolean isWorking(int day, Object o, int hour) {

        Tutor t = (Tutor) o;

        return t.isWorking(day-1, hour);
    }

    public List getAllTutors() {
        return allTutors;
    }

}
