package com.example.happymeals.mealplan;



import com.example.happymeals.Constants;
import com.example.happymeals.database.DatabaseListener;
import com.example.happymeals.database.DatabaseObject;
import com.example.happymeals.database.FireStoreManager;
import com.example.happymeals.ingredient.Ingredient;
import com.example.happymeals.recipe.Recipe;
import com.example.happymeals.recipe.RecipeStorage;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MealPlan extends DatabaseObject implements DatabaseListener {

    private Date startDate;
    private Date endDate;
    private boolean selfMade;

    private static final String INGREDIENT = "ingredient";
    private static final String COUNT = "count";

    private FireStoreManager fsm;

    private HashMap< String, HashMap < String, Meal > > plans;

    private HashMap < String, HashMap < String, Object> > ingredients;

    /**
     * Empty constructor which is required by {@link FireStoreManager} to store
     * object in the database.
     * Creates empty HashMap fields for the database that can later be populated.
     */
    public MealPlan() {
        this.fsm = FireStoreManager.getInstance();
    }

    // constructor for autogeneration
    public MealPlan( Date startDate,
                    Date endDate,
                    ArrayList<DatabaseObject> breakfastItems,
                    ArrayList<DatabaseObject> lunchItems,
                    ArrayList<DatabaseObject> dinnerItems ) {

        this.fsm = FireStoreManager.getInstance();

        this.startDate = startDate;
        this.endDate = endDate;
        selfMade = false;

        plans = new HashMap<>();
        createMapForWeekday();
        generateMealPlan();
    }

    private void generateMealPlan() {
        //TODO assign ingredients/recipes to days
    }

    public boolean getSelfMade() {
        return selfMade;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getStartDateString() {
        return startDate.toString();
    }

    public String getEndDateString() {
        return endDate.toString();
    }
    /*

    private ArrayList<DatabaseObject> getBreakfastItems() {
        ArrayList<DatabaseObject> items = new ArrayList<>();
        for (Constants.DAY_OF_WEEK day : Constants.DAY_OF_WEEK.values()) {
            Constants.COLLECTION_NAME type = getMealType(day, Constants.MEAL_OF_DAY.BREAKFAST);
            if (type == Constants.COLLECTION_NAME.RECIPES)
                items.add(getMealRecipe(day, Constants.MEAL_OF_DAY.BREAKFAST));
            else if (type == Constants.COLLECTION_NAME.INGREDIENTS)
                items.add(getMealIngredients(day, Constants.MEAL_OF_DAY.BREAKFAST));
        }
    }

    private ArrayList<DatabaseObject> getLunchItems(ArrayList<DatabaseObject> items) {
        ArrayList<DatabaseObject> items = new ArrayList<>();
        for (Constants.DAY_OF_WEEK day : Constants.DAY_OF_WEEK.values()) {
            Constants.COLLECTION_NAME type = getMealType(day, Constants.MEAL_OF_DAY.DINNER);
            if (type == Constants.COLLECTION_NAME.RECIPES)
                items.add(getMealRecipe(day, Constants.MEAL_OF_DAY.DINNER));
            else if (type == Constants.COLLECTION_NAME.INGREDIENTS)
                items.add(getMealIngredients(day, Constants.MEAL_OF_DAY.DINNER));
        }
    }

    private ArrayList<DatabaseObject> getDinnerItems(ArrayList<DatabaseObject> items) {
        ArrayList<DatabaseObject> items = new ArrayList<>();
        for (Constants.DAY_OF_WEEK day : Constants.DAY_OF_WEEK.values()) {
            Constants.COLLECTION_NAME type = getMealType(day, Constants.MEAL_OF_DAY.DINNER);
            if (type == Constants.COLLECTION_NAME.RECIPES)
                items.add(getMealRecipe(day, Constants.MEAL_OF_DAY.DINNER));
            else if (type == Constants.COLLECTION_NAME.INGREDIENTS)
                items.add(getMealIngredients(day, Constants.MEAL_OF_DAY.DINNER));
        }
    }

     */

    public HashMap < String, HashMap< String, Object > > getAllIngredients() {
        ArrayList<Ingredient> allItems = new ArrayList<>();
        for (Constants.DAY_OF_WEEK day : Constants.DAY_OF_WEEK.values()) {
            for (Constants.MEAL_OF_DAY mealTime : Constants.MEAL_OF_DAY.values()) {

                Constants.COLLECTION_NAME type = getMealType(day, mealTime);
                ArrayList<DocumentReference> items = getMealItems(day, mealTime);

                if (type == Constants.COLLECTION_NAME.RECIPES) {
                    for (DocumentReference recipe : items)
                        fsm.getData( recipe, this, new Recipe() );
                }
                else if (type == Constants.COLLECTION_NAME.INGREDIENTS) {
                    for (DocumentReference ingredient : items)
                        fsm.getData( ingredient, this, new Ingredient());
                }
            }
        }
        return ingredients;
    }

    public boolean isWithinDate(Date date) {
        Date start = getStartDate();
        Date end = getEndDate();

        return !(date.before(start) || date.after(end));
    }

    public HashMap<String, Meal> getMealsForDay(Date date) {
        Date start = getStartDate();
        Date end = getEndDate();

        Constants.DAY_OF_WEEK day = null;

        if (date.after(start) && date.before(end)) {
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            day = Constants.DAY_OF_WEEK.valueOf(String.valueOf(cal.get(Calendar.DAY_OF_WEEK)));
        }
        else if (date.compareTo(start) == 0) {
            day = Constants.DAY_OF_WEEK.SUNDAY;
        }
        else if (date.compareTo(end) == 0) {
            day = Constants.DAY_OF_WEEK.SATURDAY;
        }

        if (day != null) {
            return plans.get(day.toString());
        }
        return null;
    }

    public String getName() {
        return getStartDateString();
    }

    /**
     * Used for the constructor to create a fully defined object to be stored in the database.
     * Meals can be added at a later time.
     * Meals are constructed by looping through two different enumerations and can be changed
     * accordingly.
     */
    private void createMapForWeekday() {
        for( Constants.DAY_OF_WEEK day : Constants.DAY_OF_WEEK.values() ) {
            for( Constants.MEAL_OF_DAY meal : Constants.MEAL_OF_DAY.values() ) {
                HashMap< String, Meal>  mealsOfDay = new HashMap<>();
                mealsOfDay.put( meal.toString(), null );
                plans.put( day.toString(), mealsOfDay );
            }
        }
    }
    
    private Meal getMeal( Constants.DAY_OF_WEEK dayOfWeek, Constants.MEAL_OF_DAY mealOfDay ) {
        // verbose null checking
        HashMap<String, Meal> days = plans.get(dayOfWeek.toString());
        if (days != null && days.get(mealOfDay.toString()) != null) {
            return (days.get(mealOfDay.toString()) == null) ? null : days.get(mealOfDay.toString());
        }
        return null;
    }

    public boolean setMeal( Constants.DAY_OF_WEEK dayOfWeek, Constants.MEAL_OF_DAY mealOfDay, Meal newMeal ) {
        if (plans.get(dayOfWeek.toString()) == null)
            return false;
        else {
            plans.get(dayOfWeek.toString()).put(mealOfDay.toString(), newMeal);
            return true;
        }
    }

    public Boolean getMealMade( Constants.DAY_OF_WEEK dayOfWeek, Constants.MEAL_OF_DAY mealOfDay ) {
        return (getMeal(dayOfWeek, mealOfDay) == null)? null : getMeal(dayOfWeek, mealOfDay).getMade();
    }

    public boolean setMealMade( Constants.DAY_OF_WEEK dayOfWeek, Constants.MEAL_OF_DAY mealOfDay, Boolean made) {
        if (getMeal(dayOfWeek, mealOfDay) == null)
            return false;
        getMeal(dayOfWeek, mealOfDay).setMade(made);
        return true;
    }

    public ArrayList<DocumentReference> getMealItems( Constants.DAY_OF_WEEK dayOfWeek, Constants.MEAL_OF_DAY mealOfDay ) {
        return (getMeal(dayOfWeek, mealOfDay) == null)? null : getMeal(dayOfWeek, mealOfDay).getItems();
    }

    public boolean setMealItemsRecipe( Constants.DAY_OF_WEEK dayOfWeek, Constants.MEAL_OF_DAY mealOfDay, ArrayList<DocumentReference> refs) {
        if (getMeal(dayOfWeek, mealOfDay) == null)
            return false;
        Meal meal = getMeal(dayOfWeek, mealOfDay);
        meal.setItems(refs);
        meal.setType(Constants.COLLECTION_NAME.RECIPES);
        return true;
    }

    public boolean setMealItemsIngredients( Constants.DAY_OF_WEEK dayOfWeek, Constants.MEAL_OF_DAY mealOfDay, ArrayList<DocumentReference> refs) {
        if (getMeal(dayOfWeek, mealOfDay) == null)
            return false;
        Meal meal = getMeal(dayOfWeek, mealOfDay);
        meal.setItems(refs);
        meal.setType(Constants.COLLECTION_NAME.INGREDIENTS);
        return true;
    }

    public Constants.COLLECTION_NAME getMealType( Constants.DAY_OF_WEEK dayOfWeek, Constants.MEAL_OF_DAY mealOfDay ) {
        return (getMeal(dayOfWeek, mealOfDay) == null)? null : getMeal(dayOfWeek, mealOfDay).getType();
    }

    public boolean setMealType(Constants.DAY_OF_WEEK dayOfWeek, Constants.MEAL_OF_DAY mealOfDay, Constants.COLLECTION_NAME type) {
        if (getMeal(dayOfWeek, mealOfDay) == null)
            return false;
        getMeal(dayOfWeek, mealOfDay).setType(type);
        return true;
    }

    /**
     * Removes a meal from the day of a week and sets the "made" value to false.
     * @param dayOfWeek The {@link Enum} of the week day the meal is being removed from.
     * @param mealOfDay The {@link Enum} of the meal of the day for which the meal is being
     *                  removed from.
     */
    public boolean removeMeal( Constants.DAY_OF_WEEK dayOfWeek, Constants.MEAL_OF_DAY mealOfDay ) {
        Meal meal = getMeal( dayOfWeek, mealOfDay );

        if (meal != null) {
            setMeal(dayOfWeek, mealOfDay, null);
            return true;
        }
        return false;
    }

    /**
     * Gets the whole week of plans held in the meal plan. This will include all days
     * Sunday-Saturday, with 3 meals defined a day.
     * @return
     */
    public HashMap<String, HashMap<String, Meal > > getPlans() {
        return plans;
    }

    @Override
    public void onDataFetchSuccess(DatabaseObject data) {
        if( data.getClass() == Recipe.class ) {
            Recipe recipe = (Recipe) data;
            for (Map.Entry<String, HashMap<String, Object>> ingredients : recipe.getIngredients().entrySet()) {
                HashMap<String, Object> entry = new HashMap<>();

                String ingredientName = ingredients.getKey();
                DocumentReference ref = (DocumentReference) ingredients.getValue().get(RecipeStorage.REFERENCE);
                Integer count = (Integer) ingredients.getValue().get(RecipeStorage.COUNT);

                if (this.ingredients.containsKey(ingredientName)) {
                    Integer currentCount = (Integer) this.ingredients.get(ingredientName).get(COUNT);
                    if (currentCount != null && count != null) {
                        entry.put(COUNT, currentCount + count);
                        this.ingredients.put(ingredientName, entry);
                    }
                }
                else {
                    entry.put(INGREDIENT, null);
                    entry.put(COUNT, count);
                    this.ingredients.put(ingredientName, entry);
                    fsm.getData( ref, this, new Ingredient() );
                }

            }
        } else if( data.getClass() == Ingredient.class ) {

            Ingredient ingredient = (Ingredient) data;
            String ingredientName = ingredient.getName();
            HashMap<String, Object> entry = new HashMap<>();

            if (this.ingredients.containsKey(ingredientName)) {
                HashMap<String, Object> map = this.ingredients.get(ingredientName);
                if (map.get(INGREDIENT) == null)
                    entry.put(INGREDIENT, ingredient);
                else {
                    Integer currentCount = (Integer) map.get(COUNT);
                    entry.put(COUNT, currentCount+ingredient.getAmount());
                }
                this.ingredients.put(ingredientName, entry);
            }
            else {
                entry.put(INGREDIENT, ingredient);
                entry.put(COUNT, ingredient.getAmount());
                this.ingredients.put(ingredientName, entry);
            }
        }
    }

    @Override
    public void onSpinnerFetchSuccess(Map<String, Object> data) {

    }
}
