package com.example.happymeals;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
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
public class MealPlan extends DatabaseObject implements Serializable {

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
        for( Constants.DAY_OF_WEEK week : Constants.DAY_OF_WEEK.values() ) {
            HashMap< String, HashMap< String, Object> > mealsOfDay = new HashMap<>();
            for( Constants.MEAL_OF_DAY meal : Constants.MEAL_OF_DAY.values() ) {
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
    public boolean isMealMade( Constants.DAY_OF_WEEK dayOfWeek, Constants.MEAL_OF_DAY mealOfDay ) {
        boolean isMade = ( Boolean ) plans.get( dayOfWeek.toString() ).get( mealOfDay.toString() ).get( IS_MADE_FIELD );
        return isMade;
    }

    /**
     * Checks to see if the specified meal has eben made. The meal is specified by the day of the
     * week such as Tuesday, and the recipe. If there are multiple of the same recipe it will
     * return the first one found.
     * @param dayOfWeek The {@link Enum} DAY_OF_WEEK value which represents the day of the week
     *                  we are querying
     * @param recipe The {@link String} name of the recipe we are looking for.
     * @return A {@link Boolean} value of the "made" value attached to the requested recipe.
     */
    public boolean isMealMade( Constants.DAY_OF_WEEK dayOfWeek, String recipe ) {
        for( Map.Entry< String, HashMap< String, Object > > map : plans.get( dayOfWeek.toString() ).entrySet() ){
            for( Map.Entry< String, Object> detailsMap : map.getValue().entrySet() ) {
                if( detailsMap.getValue() == recipe ) {
                    return ( Boolean ) map.getValue().get( IS_MADE_FIELD );
                }
            }
        }
        return false;
    }

    /**
     * Removes a meal from the day of a week and sets the "made" value to false.
     * @param dayOfWeek The {@link Enum} of the week day the meal is being removed from.
     * @param mealOfDay The {@link Enum} of the meal of the day for which the meal is being
     *                  removed from.
     */
    public void removeMeal( Constants.DAY_OF_WEEK dayOfWeek, Constants.MEAL_OF_DAY mealOfDay ) {
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
     * @param recipe The {@link String} referring to the document in the FireStore
     *               Database holding the recipe that is being added.
     */
    public void setMealOfDay( Constants.DAY_OF_WEEK dayOfWeek, Constants.MEAL_OF_DAY mealOfDay, String recipe ) {
        HashMap< String, HashMap< String, Object >> temp = plans.get( dayOfWeek.toString() );
        HashMap< String, Object > temp2 = temp.get( mealOfDay.toString() );
        temp2.put( "meal", recipe );
        plans.get( dayOfWeek.toString() ).get( mealOfDay.toString() ).put( "meal", recipe );
    }

    /**
     * Set the value for a meal being made to a {@link Boolean} value of true or false. This can be
     * done for a day of the week, at a specific meal of the day such as lunch or dinner.
     * @param dayOfWeek The {@link Enum} representing the day of the week where the recipe is
     *                  stored.
     * @param mealOfDay The {@link Enum} representing the meal of the day such as breakfast for
     *                  which the "made" value is being set.
     * @param isMade The {@link Boolean} that the "made" value is being set to.
     */
    public void setMealMade( Constants.DAY_OF_WEEK dayOfWeek, Constants.MEAL_OF_DAY mealOfDay, boolean isMade ) {
        plans.get( dayOfWeek.toString() ).get( mealOfDay.toString() ).put( IS_MADE_FIELD, isMade);
    }

    /**
     * This will set the required meal's made value to be a {@link Boolean} value of true or false.
     * This value is false by default and can only be updated through this method.
     * @param dayOfWeek The {@link Enum} of the requested day which we are making a meal.
     * @param recipe The {@link String} of the recipe path that is being made.
     * @param isMade The {@link Boolean} value which will represent if the recipe has been made.
     */
    public void setMealMade( Constants.DAY_OF_WEEK dayOfWeek, String recipe, boolean isMade ) {
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

    /**
     * Navigates through the plans {@link HashMap} to the meal requeted. This meal will then be
     * returned as a {@link String} representing the path to the meal inside the database.
     * @param dayOfWeek The {@link Enum} which will define the day of the week we are requesting.
     * @param mealOfDay The {@link Enum} which will define the meal of the day we are requesting.
     * @return The {@link String} value of the path to the recipe requested.
     */
    public String getRecipePathAt( Constants.DAY_OF_WEEK dayOfWeek, Constants.MEAL_OF_DAY mealOfDay ) {
        return plans.get( dayOfWeek.toString() ).get( mealOfDay.toString() ).get( "meal" ).toString();
    }
}
