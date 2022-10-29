package com.example.happymeals;

import static com.example.happymeals.MealPlan.DAY_OF_WEEK.*;

import android.util.Pair;

import com.google.firebase.firestore.DocumentReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jeastgaa
 * @version 1.00.01
 * @see DatabaseObject The parent object for this class which allows it to be stored inside the
 * firebase databse using the {@link FireStoreManager}
 * This class represents Meal Plans that can be loaded inside the application and shown to the
 * end user. These meal plans will always be stored in the database as a week/week schema. This
 * means that the meal plan should be defined with the date as a Sunday value otherwise the
 * data will not turn out.
 */
public class MealPlan extends DatabaseObject {

    //<todo> Move enumeration over to a constants class.
    public enum DAY_OF_WEEK {
        SUNDAY,
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY,
        SATURDAY
    };

    public enum MEAL_OF_DAY {
        BREAKFAST,
        LUNCH,
        DINNER
    }

    public final static String IS_MADE_FIELD = "made";

    // Hashmap which can be mapped to the database
    private HashMap< String, HashMap< String, HashMap< String, Object > > > plans;

    /**
     * Empty constructor which is required by {@link FireStoreManager} to store
     * object in the database.
     * Creates empty HashMap fields for the database that can later be populated.
     */
    public MealPlan() {
        plans = new HashMap<>();
        createMapForWeekday();
    }

    // <todo> Add checking to ensure that the given date is a Monday value, this should
    // be handled by other classes but could also be here. </todo>

    /**
     * Constructor that will define the Meal Plan but a sunday date.
     * @param date The {@link String} of the date which will be represented as the name
     *             of the object allowing it to be located in the database and referenced.
     */
    public MealPlan( String date ) {
        this();
        this.name = date;
    }

    /**
     * Secondary Constructor that allows other classes to define a MealPlan by passing in a date
     * which will get translated to a String.
     * @param date Thew {@link Date} which will represent the start of the week which this meal
     *             plan is defining.
     */
    public MealPlan( Date date ) {
        this();
        this.name = new SimpleDateFormat("yyyy-MM-dd").format( date );
    }

    /**
     * Used for the constructor to create a fully defined object to be stored in the database.
     * Meals can be added at a later time.
     * Meals are constructed by looping through two different enumerations and can be changed
     * accordingly.
     */
    private void createMapForWeekday() {
        for( DAY_OF_WEEK week : DAY_OF_WEEK.values() ) {
            HashMap< String, HashMap< String, Object> > mealsOfDay = new HashMap<>();
            for( MEAL_OF_DAY meal : MEAL_OF_DAY.values() ) {
                HashMap< String, Object > details = new HashMap<>();
                details.put( IS_MADE_FIELD, false );
                mealsOfDay.put( meal.toString(), details);
            }
            plans.put( week.toString(), mealsOfDay );
        }
    }

    /**
     * Checks to see if the specified meal has been made. The meal is specified by the meal of the
     * day enumeration.
     * @param dayOfWeek The {@link Enum} DAY_OF_WEEK inside meal plan specifying which day of the
     *                  week we are looking for the meal in.
     * @param mealOfDay The {@link Enum} MEAL_OF_DAY we are looking for.
     * @return The {@link Boolean} value stored in the made field of requested meal.
     */
    public boolean isMealMade( DAY_OF_WEEK dayOfWeek, MEAL_OF_DAY mealOfDay ) {
        boolean isMade = ( Boolean ) plans.get( dayOfWeek.toString() ).get( mealOfDay.toString() ).get( IS_MADE_FIELD );
        return isMade;
    }

    public boolean isMealMade( DAY_OF_WEEK dayOfWeek, DocumentReference recipe ) {
        for( Map.Entry< String, HashMap< String, Object > > map : plans.get( dayOfWeek.toString() ).entrySet() ){
            for( Map.Entry< String, Object> detailsMap : map.getValue().entrySet() ) {
                if( detailsMap.getValue() == recipe ) {
                    return (Boolean ) map.getValue().get( IS_MADE_FIELD );
                }
            }
        }
        return false;
    }

    public void removeMeal( DAY_OF_WEEK dayOfWeek, MEAL_OF_DAY mealOfDay ) {
        plans.get( dayOfWeek.toString() ).get( mealOfDay.toString() ).remove( "meal" );
        setMealMade( dayOfWeek, mealOfDay, false );
    }

    /**
     * Allows other classes to add a meal from the {@link FireStoreManager}'s database into this
     * plan.
     * @param dayOfWeek The {@link Enum} which holds which day of the week this meal is being
     *                  planned for.
     * @param mealOfDay The {@link Enum} which holds the meal of the day this recipe is being planned
     *             for.
     * @param recipe The {@link DocumentReference} referring to the document in the FireStore
     *               Database holding the recipe that is being added.
     */
    public void setMealOfDay( DAY_OF_WEEK dayOfWeek, MEAL_OF_DAY mealOfDay, DocumentReference recipe ) {
        HashMap< String, HashMap< String, Object >> temp = plans.get( dayOfWeek.toString() );
        HashMap< String, Object > temp2 = temp.get( mealOfDay.toString() );
        temp2.put( "meal", recipe );
        plans.get( dayOfWeek.toString() ).get( mealOfDay.toString() ).put( "meal", recipe );
    }

    public void setMealMade( DAY_OF_WEEK dayOfWeek, MEAL_OF_DAY mealOfDay, boolean isMade ) {
        plans.get( dayOfWeek.toString() ).get( mealOfDay.toString() ).put( IS_MADE_FIELD, isMade);
    }

    public void setMealMade( DAY_OF_WEEK dayOfWeek, DocumentReference recipe, boolean isMade ) {
        for( Map.Entry< String, HashMap< String, Object > > map : plans.get( dayOfWeek.toString() ).entrySet() ){
            for( Map.Entry< String, Object> detailsMap : map.getValue().entrySet() ) {
                if( detailsMap.getValue() == recipe ) {
                    map.getValue().put( IS_MADE_FIELD, isMade );
                }
            }
        }
    }

    /**
     * Gets the whole week of plans held in the meal plan. This will include all days
     * Sunday-Saturday, with 3 meals defined a day.
     * @return
     */
    public HashMap<String, HashMap<String, HashMap< String, Object > > > getPlans() {
        return plans;
    }

    public DocumentReference getRecipeAt( DAY_OF_WEEK dayOfWeek, MEAL_OF_DAY mealOfDay ) {
        return ( DocumentReference ) plans.get( dayOfWeek.toString() ).get( mealOfDay.toString() ).get( "meal" );
    }
}
