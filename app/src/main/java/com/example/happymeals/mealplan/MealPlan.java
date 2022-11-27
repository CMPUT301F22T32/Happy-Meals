package com.example.happymeals.mealplan;


import static android.content.ContentValues.TAG;

import android.util.Log;
import android.util.Pair;

import com.example.happymeals.Constants;
import com.example.happymeals.database.DatabaseListener;
import com.example.happymeals.database.DatabaseObject;
import com.example.happymeals.database.DatasetWatcher;
import com.example.happymeals.database.FireStoreManager;
import com.example.happymeals.ingredient.Ingredient;
import com.example.happymeals.ingredient.IngredientStorage;
import com.example.happymeals.recipe.Recipe;
import com.example.happymeals.recipe.RecipeStorage;
import com.google.firebase.firestore.DocumentReference;

import java.sql.Array;
import java.sql.SQLOutput;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class MealPlan extends DatabaseObject {

    public static final String REFERENCE = "reference";
    public static final String RECIPES = "recipes";
    public static final String COUNT = "count";

    private Date startDate;
    private Date endDate;

    private String creator;

    private final Constants.COLLECTION_NAME INGREDIENT_TYPE = Constants.COLLECTION_NAME.INGREDIENTS;
    private final Constants.COLLECTION_NAME RECIPE_TYPE = Constants.COLLECTION_NAME.RECIPES;

    private FireStoreManager fsm;
    private IngredientStorage ingredientStorage;
    private RecipeStorage recipeStorage;

    private HashMap< String, HashMap < String, Meal > > plans;
    private HashMap< String, HashMap< String, Object > > allIngredients;

    private DatasetWatcher listeningActivity;

    /**
     * Empty constructor which is required by {@link FireStoreManager} to store
     * object in the database.
     * Creates empty HashMap fields for the database that can later be populated.
     */
    public MealPlan() {
        ingredientStorage = IngredientStorage.getInstance();
        recipeStorage = RecipeStorage.getInstance();
    }

    public MealPlan( String name, String creator ) {
        super( name, creator );
        this.creator=creator;
        ingredientStorage = IngredientStorage.getInstance();
        recipeStorage = RecipeStorage.getInstance();
        allIngredients = new HashMap<>();
        plans = new HashMap<>();
        createMapForWeekday();
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setStartDate(Date date) {
        startDate = date;
        this.id=String.format("%s_%s", creator, getStartDateString());
    }

    public void setEndDate(Date date) {
        endDate = date;
    }

    public String getStartDateString() {
        return new SimpleDateFormat("EEEE, MMM d", Locale.CANADA).format(startDate);
    }

    public String getEndDateString() {
        return new SimpleDateFormat("EEEE, MMM d", Locale.CANADA).format(endDate);
    }

    // Funcs needed for autogen

    public void generateMealPlan(HashMap<Constants.MEAL_OF_DAY, Pair<Constants.COLLECTION_NAME, ArrayList<?>>> itemsForAutoGen) {
        // this is gonna be super random -> the only efficiency I could possibly see to optimize on
        // would be least ingredients used (cheapest solution?) Might be a good idea.
        for (Map.Entry<Constants.MEAL_OF_DAY, Pair<Constants.COLLECTION_NAME, ArrayList<?>>> itemsForMeals : itemsForAutoGen.entrySet()) {
            if (itemsForMeals.getValue().first == Constants.COLLECTION_NAME.INGREDIENTS)
                distributeIngredients(itemsForMeals.getKey(), (ArrayList<Ingredient>) itemsForMeals.getValue().second);
            else if (itemsForMeals.getValue().first == Constants.COLLECTION_NAME.RECIPES)
                distributeRecipes(itemsForMeals.getKey(), (ArrayList<Recipe>) itemsForMeals.getValue().second);
        }
    }

    private void distributeIngredients( Constants.MEAL_OF_DAY mealOfDay, ArrayList<Ingredient> ingredients) {
        // Distribute ingredients on a one per day basis -- this way there aren't any weird collisions
        // (ingredients that shouldn't go together). Ensure that at least every ingredient is used
        // before assigning again (to increase variety).
        ArrayList<Ingredient> copy = new ArrayList<>(ingredients);
        Random random = new Random();

        for (Constants.DAY_OF_WEEK weekDay : Constants.DAY_OF_WEEK.values()) {
            int bound = copy.size() - 1;
            Ingredient selected;

            if (bound == 0) {
                selected = copy.remove(0);
                copy = new ArrayList<>(ingredients);
            }
            else
                selected = copy.remove(random.nextInt(bound));

            ArrayList<Ingredient> selectedList = new ArrayList<>();
            selectedList.add(selected);
            setMealItemsIngredients(weekDay, mealOfDay, selectedList);
        }
    }

    private void distributeRecipes( Constants.MEAL_OF_DAY mealOfDay, ArrayList<Recipe> recipes) {
        ArrayList<Recipe> copy = new ArrayList<>(recipes);
        Random random = new Random();

        for (Constants.DAY_OF_WEEK weekDay : Constants.DAY_OF_WEEK.values()) {
            int bound = copy.size() - 1;
            Recipe selected;

            if (bound == 0) {
                selected = copy.remove(0);
                copy = new ArrayList<>(recipes);
            }
            else
                selected = copy.remove(random.nextInt(bound));

            ArrayList<Recipe> selectedList = new ArrayList<>();
            selectedList.add(selected);
            setMealItemsRecipe(weekDay, mealOfDay, selectedList);
        }
    }

    private Constants.DAY_OF_WEEK convertDate( Date date ) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        int index = cal.get(Calendar.DAY_OF_WEEK);
        return Constants.DAY_OF_WEEK.values()[index-1];
    }

    public boolean isWithinDate(Date date) {
        return !(date.before(startDate) || date.after(endDate));
    }

    public HashMap<String, Meal> getMealsForDay(Constants.DAY_OF_WEEK day) {
        return plans.get(day.toString());
    }

    public HashMap<String, Meal> getMealsForDay(Date date) {
        return getMealsForDay(convertDate(date));
    }

    public int getNumberOfMealsForDay(Constants.DAY_OF_WEEK day) {
        int count = 0;
        for (Meal meal : getMealsForDay(day).values())
            count = (meal == null) ? count : count + 1;
        return count;
    }

    public int getNumberOfMealsForDay(Date date) {
        return getNumberOfMealsForDay(convertDate(date));
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
            HashMap< String, Meal > map = new HashMap<>();
            for( Constants.MEAL_OF_DAY meal : Constants.MEAL_OF_DAY.values() ) {
                map.put(meal.toString(), null);
            }
            plans.put( day.toString(), map );
        }
    }

    public Meal getMeal( Constants.DAY_OF_WEEK dayOfWeek, Constants.MEAL_OF_DAY mealOfDay ) {
        // verbose null checking
        try {
            return Objects.requireNonNull(Objects.requireNonNull(plans.get(dayOfWeek.toString())).get(mealOfDay.toString()));
        } catch (Exception ignore) { }

        return null;
    }

    public Meal getMeal( Date date, Constants.MEAL_OF_DAY mealOfDay ) {
        // verbose null checking
        return getMeal(convertDate(date), mealOfDay);
    }

    private boolean setMeal( Constants.DAY_OF_WEEK dayOfWeek, Constants.MEAL_OF_DAY mealOfDay, Meal meal) {
        try {
            HashMap< String, Meal > map = Objects.requireNonNull(plans.get(dayOfWeek.toString()));
            map.put(mealOfDay.toString(), meal);
            return true;
        } catch (Exception ignore) {
            return false;
        }
    }

    /**
     * Removes a meal from the day of a week and sets the "made" value to false.
     * @param dayOfWeek The {@link Enum} of the week day the meal is being removed from.
     * @param mealOfDay The {@link Enum} of the meal of the day for which the meal is being
     *                  removed from.
     */
    public boolean removeMeal( Constants.DAY_OF_WEEK dayOfWeek, Constants.MEAL_OF_DAY mealOfDay ) {
        if (getMeal( dayOfWeek, mealOfDay ) == null)
            return false;

        setMeal( dayOfWeek, mealOfDay, null );
        return true;
    }

    public void setMealItemsRecipe( Constants.DAY_OF_WEEK dayOfWeek, Constants.MEAL_OF_DAY mealOfDay, ArrayList<Recipe> recipes) {
        Meal meal = getMeal(dayOfWeek, mealOfDay);

        if (meal == null)
            meal = new Meal();

        HashMap < String, Double > recipeScales = new HashMap<>();
        for ( Recipe recipe : recipes )
            recipeScales.put(recipe.getName(), 1.0);

        meal.setItems(recipeScales);
        meal.setType(RECIPE_TYPE);

        setMeal(dayOfWeek, mealOfDay, meal);
    }

    public void setMealItemsIngredients( Constants.DAY_OF_WEEK dayOfWeek, Constants.MEAL_OF_DAY mealOfDay, ArrayList<Ingredient> ingredients) {
        Meal meal = getMeal(dayOfWeek, mealOfDay);

        if (meal == null)
            meal = new Meal();

        HashMap < String, Double > ingredientScales = new HashMap<>();
        for ( Ingredient ingredient : ingredients )
            ingredientScales.put(ingredient.getName(), 1.0);

        meal.setType(INGREDIENT_TYPE);
        meal.setItems(ingredientScales);

        setMeal(dayOfWeek, mealOfDay, meal);
    }

    public HashMap < String, HashMap< String, Object > > getAllIngredients() {
        return allIngredients;
    }

    public void generateAllIngredients() {
        allIngredients.clear();
        for (Constants.DAY_OF_WEEK weekDay : Constants.DAY_OF_WEEK.values()) {
            for (Constants.MEAL_OF_DAY mealTime : Constants.MEAL_OF_DAY.values()) {
                Meal meal = getMeal(weekDay, mealTime);

                if (meal == null || meal.isMade())
                    continue;

                HashMap<String, Double> items = meal.getItems();
                if (meal.getType() == INGREDIENT_TYPE) {

                    for (String ingredientName : items.keySet()) {
                        insertIngredientToAll(ingredientName, null,null, allIngredients);
                    }
                } else if (meal.getType() == RECIPE_TYPE) {
                    for (Map.Entry<String, Double> entry : items.entrySet()) {
                        String recipeName = entry.getKey();
                        Double scale = entry.getValue();

                        Recipe recipe = recipeStorage.getRecipe(recipeName);

                        for (Map.Entry<String, HashMap<String, Object>> ingredient : recipe.getIngredients().entrySet()) {
                            String ingredientName = ingredient.getKey();
                            HashMap<String, Object> ingredientDetails = new HashMap<>(ingredient.getValue());
                            Double ingredientAmount = (Double) ingredientDetails.get(COUNT);

                            Double scaledAmount = Math.ceil(scale * ingredientAmount);
                            insertIngredientToAll(ingredientName, recipeName, scaledAmount, allIngredients);
                        }
                    }
                }
            }
        }
    }

    public void insertIngredientToAll( String ingredientName, String recipeName, Double amount, HashMap<String, HashMap<String, Object>> allIngredients) {
        Ingredient ingredient = ingredientStorage.getIngredient(ingredientName);
        HashMap < String, Object > ingredientDetails;

        if (allIngredients.containsKey(ingredientName)) {
            ingredientDetails = allIngredients.get(ingredientName);
            Double previousCount = (Double) ingredientDetails.get(COUNT);

            if (amount == null)
                ingredientDetails.put(COUNT, previousCount + ingredient.getAmount());
            else
                ingredientDetails.put(COUNT, previousCount + amount);

            ArrayList<String> storedNames = (ArrayList<String>) allIngredients.get(ingredientName).get(MealPlan.RECIPES);
            if (recipeName != null && storedNames != null) {
                if (!storedNames.contains(recipeName)) {
                    storedNames.add(recipeName);
                    ingredientDetails.put(MealPlan.RECIPES, storedNames);
                }
            }

            allIngredients.put(ingredientName, ingredientDetails);
        }
        else {
            ingredientDetails = new HashMap<>();

            if (amount == null)
                ingredientDetails.put(COUNT, ingredient.getAmount());
            else
                ingredientDetails.put(COUNT, amount);

            if (recipeName != null) {
                ArrayList<String> recipeNames = new ArrayList<>();
                recipeNames.add(recipeName);
                ingredientDetails.put(RECIPES, recipeNames);
            }

            allIngredients.put(ingredientName, ingredientDetails);
        }
    }

    public boolean setMealMade( Constants.DAY_OF_WEEK dayOfWeek, Constants.MEAL_OF_DAY mealOfDay, Boolean made) {
        Meal meal = getMeal(dayOfWeek, mealOfDay);
        if (meal != null) {
            meal.setMade(made);
            return true;
        }
        return false;
    }

    public boolean setMealMade( Date date, Constants.MEAL_OF_DAY mealOfDay, Boolean made) {
        return setMealMade(convertDate(date), mealOfDay, made);
    }

    public Boolean getMealMade( Constants.DAY_OF_WEEK dayOfWeek, Constants.MEAL_OF_DAY mealOfDay ) {
        Meal meal = getMeal(dayOfWeek, mealOfDay);
        if (meal != null)
            return meal.isMade();

        return false;
    }

    public HashMap< String, Double > getMealItems( Constants.DAY_OF_WEEK dayOfWeek, Constants.MEAL_OF_DAY mealOfDay ) {
        Meal meal = getMeal(dayOfWeek, mealOfDay);
        if (meal != null)
            return meal.getItems();

        return null;
    }

    public ArrayList<Ingredient> getMealIngredients( Constants.DAY_OF_WEEK dayOfWeek, Constants.MEAL_OF_DAY mealOfDay ) {
        Meal meal = getMeal(dayOfWeek, mealOfDay);
        ArrayList<Ingredient> ingredients = new ArrayList<>();

        if (meal != null) {
            for (String ingredientName : meal.getItems().keySet()) {
                Ingredient ingredient = ingredientStorage.getIngredient(ingredientName);
                ingredients.add(ingredient);
            }
            return ingredients;
        }

        return null;
    }

    public ArrayList<Ingredient> getMealIngredients( Date date, Constants.MEAL_OF_DAY mealOfDay) {
        return getMealIngredients(convertDate(date), mealOfDay);
    }

    public ArrayList<Recipe> getMealRecipes( Constants.DAY_OF_WEEK dayOfWeek, Constants.MEAL_OF_DAY mealOfDay ) {
        Meal meal = getMeal(dayOfWeek, mealOfDay);
        ArrayList<Recipe> recipes = new ArrayList<>();

        if (meal != null && meal.getItems() != null) {
            for (String recipeName : meal.getItems().keySet()) {
                Recipe recipe = recipeStorage.getRecipe(recipeName);
                recipes.add(recipe);
            }
            return recipes;
        }

        return null;
    }

    public ArrayList<Recipe> getMealRecipes( Date date, Constants.MEAL_OF_DAY mealOfDay ) {
        return getMealRecipes(convertDate(date), mealOfDay);
    }

    public Constants.COLLECTION_NAME getMealType( Constants.DAY_OF_WEEK dayOfWeek, Constants.MEAL_OF_DAY mealOfDay ) {
        Meal meal = getMeal(dayOfWeek, mealOfDay);
        if (meal != null)
            return meal.getType();

        return null;
    }

    public Constants.COLLECTION_NAME getMealType( Date date, Constants.MEAL_OF_DAY mealOfDay ) {
        return getMealType(convertDate(date), mealOfDay);
    }

    public HashMap<String, HashMap<String, Object>> getMealPlanIngredients( Constants.DAY_OF_WEEK dayOfWeek, Constants.MEAL_OF_DAY mealOfDay) {
        return allIngredients;
    }

    public void setScaleOnItem( Constants.DAY_OF_WEEK dayOfWeek, Constants.MEAL_OF_DAY mealOfDay, String itemName, Double scaleFactor ) {
        Meal meal = getMeal(dayOfWeek, mealOfDay);
        if (meal != null) {
            meal.setScale(itemName, scaleFactor);
            generateAllIngredients();
        }
    }

    public void consumeMeal(Date date, Constants.MEAL_OF_DAY mealTime) {
        if (getMealType(date, mealTime) == INGREDIENT_TYPE) {
            for (Ingredient ingredient : getMealIngredients(date, mealTime))
                ingredientStorage.requestConsumptionOfIngredient(ingredient, ingredient.getAmount());

        }
        else if (getMealType(date, mealTime) == RECIPE_TYPE) {
            Meal meal = getMeal(convertDate(date), mealTime);
            for (Map.Entry<String, Double> entry : meal.getItems().entrySet()) {
                Double scaleFactor = entry.getValue();
                Recipe recipe = recipeStorage.getRecipe(entry.getKey());
                for (Map.Entry<String, HashMap<String, Object>> ingredientDetails : recipe.getIngredients().entrySet()) {
                    Ingredient ingredient = ingredientStorage.getIngredient(ingredientDetails.getKey());
                    recipeStorage.consumeIngredientInRecipe(ingredient, Math.ceil(ingredient.getAmount() * scaleFactor));
                }
            }
        }
        generateAllIngredients();
    }

    /**
     * Gets the whole week of plans held in the meal plan. This will include all days
     * Sunday-Saturday, with 3 meals defined a day.
     * @return
     */
    public HashMap<String, HashMap<String, Meal > > getPlans() {
        return plans;
    }

}
