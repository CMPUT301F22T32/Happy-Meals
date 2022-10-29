package com.example.happymeals;

import static com.example.happymeals.MealPlan.DAY_OF_WEEK.*;

import android.util.Pair;

import com.google.firebase.firestore.DocumentReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;

public class MealPlan extends DatabaseObject {

    public enum DAY_OF_WEEK {
        SUNDAY,
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY,
        SATURDAY
    };

    private HashMap< DAY_OF_WEEK, HashMap< String, Object > > plans;

    public MealPlan() {
        plans.put( SUNDAY, createMapForWeekday( "Sunday" ) );
        plans.put( MONDAY, createMapForWeekday( "Monday" ) );
        plans.put( TUESDAY, createMapForWeekday( "Tuesday" ) );
        plans.put( WEDNESDAY, createMapForWeekday( "Wednesday" ) );
        plans.put( THURSDAY, createMapForWeekday( "Thursday" ) );
        plans.put( FRIDAY, createMapForWeekday( "Friday" ) );
        plans.put( SATURDAY, createMapForWeekday( "Saturday" ) );
    }

    public MealPlan( String date ) {
        this();
        this.name = date;
    }

    public MealPlan( Date date ) {
        this();
        this.name = new SimpleDateFormat("yyyy-MM-dd").format( date );
    }
    private HashMap< String, Object > createMapForWeekday( String weekday ) {
        HashMap< String, Object > temp = new HashMap<>();
        temp.put("Day of the Week", weekday );
        return temp;
    }

    public void setMealOfDay(DAY_OF_WEEK dayOfWeek, String meal, DocumentReference recipe ) {
        plans.get( dayOfWeek ).put( meal, recipe );
    }

    public HashMap<DAY_OF_WEEK, HashMap<String, Object>> getPlans() {
        return plans;
    }
}
