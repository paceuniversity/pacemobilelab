package com.pacemobilelab.TutorsAtSeidenberg;

import android.util.Log;

/**
 * Created by jangerhard on 20-Apr-16.
 */
public class WorkSchedule {

    /**
     * 7 days (Monday to sunday)
     * 11 hours (from 9:00 to 20:00)
     */

    final int START=9, END=20;

    boolean[][] calendar = new boolean[24][7];

    public WorkSchedule(){
        clear();
    }

    public void clear(){
        for (boolean[] hours: calendar)
            for (boolean hour: hours)
                hour = false;
    }

    public void addTime(int day, int startHour, int endHour) throws OutOfBoundsExeption {

        if (startHour < START || startHour > END) throw new OutOfBoundsExeption("Start hour " +
                "has to be between 0" + START + ":00 and " + END + ":00" );
        if (endHour < START || endHour > END) throw new OutOfBoundsExeption("End hour has " +
                "to be between 0" + START + ":00 and " + END + ":00" );

        for (int i=startHour; i<endHour; i++)
            calendar[i][day] = true;

    }

    public boolean isWorking(int day, int hour){

        return calendar[hour][day];
    }

    class OutOfBoundsExeption extends Exception
    {
        public OutOfBoundsExeption(String message)
        {
            super(message);
        }
    }

}
