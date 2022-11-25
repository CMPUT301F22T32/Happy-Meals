package com.example.happymeals.mealplan;


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

public class MealPlan extends DatabaseObject implements DatabaseListener {

    private Date startDate;
    private Date endDate;

    // I should make these constants since they're used in Recipes too
    private static final String TYPE = "type";
    private static final String MADE = "made";
    private static final String EXISTS = "exists";
    private static final String NAME = "displayName";

    public static final String REFERENCES = "reference";
    public static final String RECIPES = "recipe";
    public static final String COUNT = "count";

    private FireStoreManager fsm;

    private HashMap< String, HashMap < String, HashMap < String, Object > > > plans;
    private HashMap< String, HashMap< String, Object > > allIngredients;

    // These will be empty upon upload; they are merely used to provide the object ingredient or
    // recipes that the document references correspond to. Mainly used to facilitate setting up
    // views and fragments.
    private DatasetWatcher listeningActivity;
    private ArrayList<Ingredient> ingredients;
    private ArrayList<Recipe> recipes;

    /**
     * Empty constructor which is required by {@link FireStoreManager} to store
     * object in the database.
     * Creates empty HashMap fields for the database that can later be populated.
     */
    public MealPlan() {
        this.fsm = FireStoreManager.getInstance();
        initializeCollections();
    }

    private void initializeCollections() {
        plans = new HashMap<>();
        ingredients = new ArrayList<>();
        recipes = new ArrayList<>();
        allIngredients = new HashMap<>();
        createMapForWeekday();
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

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setStartDate(Date date) {
        startDate = date;
    }

    public void setEndDate(Date date) {
        endDate = date;
    }

    public String makeDisplayNameRecipe( ArrayList<Recipe> items) {
        StringBuilder sb = new StringBuilder();
        int size = items.size() - 1;

        for (int i = 0; i < size; i++) {
            sb.append(items.get(i).getName());
            sb.append(", ");
        }

        sb.append(items.get(size).getName());

        if (sb.length() > 25) {
            sb.setLength(25);
            sb.append(" ... ");
        }

        return sb.toString();
    }

    public String makeDisplayNameIngredient(ArrayList<Ingredient> items) {
        StringBuilder sb = new StringBuilder();
        int size = items.size() - 1;

        for (int i = 0; i < size; i++) {
            sb.append(items.get(i).getName());
            sb.append(", ");
        }

        if (size >= 0)
            sb.append(items.get(size).getName());

        if (sb.length() > 25) {
            sb.setLength(25);
            sb.append(" ... ");
        }

        return sb.toString();
    }

    public String getStartDateString() {
        return new SimpleDateFormat("EEEE, MMM d", Locale.CANADA).format(startDate);
    }

    public String getEndDateString() {
        return new SimpleDateFormat("EEEE, MMM d", Locale.CANADA).format(endDate);
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

    public ArrayList<String> getMealNamesForDay(Constants.DAY_OF_WEEK day) {
        ArrayList<String> names = new ArrayList<>();

        for (Constants.MEAL_OF_DAY mealTimes: Constants.MEAL_OF_DAY.values()) {
            names.add(getMealDisplayName(day, mealTimes));
        }

        return names;
    }

    public ArrayList<String> getMealNamesForDay(Date date) {
        return getMealNamesForDay(convertDate(date));
    }

    public ArrayList<Boolean> getMealMadeForDay(Constants.DAY_OF_WEEK day) {
        ArrayList<Boolean> bools = new ArrayList<>();

        for (Constants.MEAL_OF_DAY mealTimes: Constants.MEAL_OF_DAY.values()) {
            bools.add(getMealMade(day, mealTimes));
        }

        return bools;
    }

    public ArrayList<Boolean> getMealMadeForDay(Date date) {
        return getMealMadeForDay(convertDate(date));
    }

    public HashMap<String, HashMap<String, Object>> getMealsForDay(Date date) {
        return getMealsForDay(convertDate(date));
    }

    public HashMap<String, HashMap<String, Object>> getMealsForDay(Constants.DAY_OF_WEEK day) {
        return plans.get(day.toString());
    }

    public int getNumberOfMealsForDay(Constants.DAY_OF_WEEK day) {
        int i = 0;
        for (Constants.MEAL_OF_DAY mealTimes: Constants.MEAL_OF_DAY.values()) {
            i = (getMeal(day, mealTimes) != null) ? i + 1 : i;
        }
        return i;
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
            HashMap < String, HashMap < String, Object > > meals = new HashMap<>();
            for( Constants.MEAL_OF_DAY meal : Constants.MEAL_OF_DAY.values() ) {
                meals.put( meal.toString(), new HashMap<>(Map.of ( EXISTS, false ) ) );
            }
            plans.put( day.toString(), meals );
        }
    }

    private HashMap< String, Object > getMeal( Constants.DAY_OF_WEEK dayOfWeek, Constants.MEAL_OF_DAY mealOfDay ) {
        // verbose null checking
        try {
            HashMap<String, Object> meal = Objects.requireNonNull(Objects.requireNonNull(plans.get(dayOfWeek.toString())).get(mealOfDay.toString()));
            if ((Boolean) Objects.requireNonNull(meal.get(EXISTS)))
                return meal;
        } catch (Exception ignore) { }

        return null;
    }

    private boolean setMeal( Constants.DAY_OF_WEEK dayOfWeek, Constants.MEAL_OF_DAY mealOfDay, HashMap<String, Object> meal) {
        try {
            Objects.requireNonNull(plans.get(dayOfWeek.toString())).put(mealOfDay.toString(), meal);
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
        Map<String, Object> meal = getMeal( dayOfWeek, mealOfDay );
        if (meal == null)
            return false;

        setMeal(dayOfWeek, mealOfDay, new HashMap<>(Map.of(EXISTS, false)));
        return true;
    }

    public void setMealItemsRecipe( Constants.DAY_OF_WEEK dayOfWeek, Constants.MEAL_OF_DAY mealOfDay, ArrayList<Recipe> recipes) {
        HashMap<String, Object> meal = getMeal(dayOfWeek, mealOfDay);

        if (meal == null)
            meal = new HashMap<>();

        meal.put(NAME, makeDisplayNameRecipe(recipes));
        meal.put(TYPE, Constants.COLLECTION_NAME.RECIPES.toString());
        meal.put(EXISTS, true);
        meal.put(MADE, false);

        ArrayList<DocumentReference> refs = new ArrayList<>();

        for (Recipe recipe : recipes) {
            insertRecipeIngredientToAll(recipe);
            DocumentReference doc = fsm.getDocReferenceTo(Constants.COLLECTION_NAME.RECIPES, recipe);
            refs.add(doc);
        }
        meal.put(REFERENCES, refs);

        setMeal(dayOfWeek, mealOfDay, meal);
    }

    private void insertRecipeIngredientToAll(Recipe recipe) {
        HashMap< String, HashMap<String, Object> > ingredientInfo = recipe.getIngredients();

        for (Map.Entry<String, HashMap<String, Object>> entry : ingredientInfo.entrySet()) {
            String ingredientName = entry.getKey();
            HashMap<String, Object> details = entry.getValue();

            if (allIngredients.containsKey(ingredientName)) {
                ArrayList<String> recipeStrings;

                if (allIngredients.get(ingredientName).containsKey(RECIPES))
                    recipeStrings = (ArrayList<String>) allIngredients.get(ingredientName).get(RECIPES);
                else
                    recipeStrings = new ArrayList<>();

                if (!recipeStrings.contains(recipe.getName()))
                    recipeStrings.add(recipe.getName());

                details.put(RECIPES, recipeStrings);

                Double oldCount = (Double) allIngredients.get(ingredientName).get(COUNT);
                Double recipeCount = (Double) details.get(COUNT);
                details.put(COUNT, oldCount + recipeCount);

            } else {
                ArrayList<String> recipeStrings = new ArrayList<>();
                recipeStrings.add(recipe.getName());
                details.put(RECIPES, recipeStrings);
            }
            allIngredients.put(ingredientName, details);
        }
    }

    private void insertIngredientsToAll(Ingredient ingredient) {
        if (allIngredients.containsKey(ingredient.getName())) {
            Double oldCount = (Double) allIngredients.get(ingredient.getName()).get(COUNT);
            allIngredients.get(ingredient.getName()).put(COUNT, oldCount + ingredient.getAmount());
        } else {
            HashMap<String, Object> ingredientDetails = ne HashMap<>();
            ingredientDetails.put(REFERENCES, fsm.getDocReferenceTo(Constants.COLLECTION_NAME.INGREDIENTS, ingredient));
            ingredientDetails.put(COUNT, ingredient.getAmount());
            ingredientDetails.put(RECIPES, null);
            allIngredients.put(ingredient.getName(), ingredientDetails);
        }
    }

    public HashMap < String, HashMap< String, Object > > getAllIngredients() {
        return allIngredients;
    }

    public void setMealItemsIngredients( Constants.DAY_OF_WEEK dayOfWeek, Constants.MEAL_OF_DAY mealOfDay, ArrayList<Ingredient> ingredients) {
        HashMap<String, Object> meal = getMeal(dayOfWeek, mealOfDay);

        if (meal == null)
            meal = new HashMap<>();

        meal.put(NAME, makeDisplayNameIngredient(ingredients));
        meal.put(TYPE, Constants.COLLECTION_NAME.INGREDIENTS.toString());
        meal.put(EXISTS, true);
        meal.put(MADE, false);

        ArrayList<DocumentReference> refs = new ArrayList<>();

        for (Ingredient ingredient : ingredients) {
            insertIngredientsToAll(ingredient);
            DocumentReference doc = fsm.getDocReferenceTo(Constants.COLLECTION_NAME.INGREDIENTS, ingredient);
            refs.add(doc);
        }
        meal.put(REFERENCES, refs);

        setMeal(dayOfWeek, mealOfDay, meal);
    }

    public boolean setMealMade( Constants.DAY_OF_WEEK dayOfWeek, Constants.MEAL_OF_DAY mealOfDay, Boolean made) {
        HashMap<String, Object> meal = getMeal(dayOfWeek, mealOfDay);
        if (meal != null) {
            meal.put(MADE, made);
            setMeal(dayOfWeek, mealOfDay, meal);
            return true;
        }
        return false;
    }

    public boolean setMealMade( Date date, Constants.MEAL_OF_DAY mealOfDay, Boolean made) {
        return setMealMade(convertDate(date), mealOfDay, made);
    }

    public Boolean getMealMade( Constants.DAY_OF_WEEK dayOfWeek, Constants.MEAL_OF_DAY mealOfDay ) {
        try {
            return (Boolean) Objects.requireNonNull(getMeal(dayOfWeek, mealOfDay)).get(MADE);
        } catch (Exception ignore) {
            return false;
        }
    }

    public String getMealDisplayName( Constants.DAY_OF_WEEK dayOfWeek, Constants.MEAL_OF_DAY mealOfDay ) {
        try {
            return (String) Objects.requireNonNull(getMeal(dayOfWeek, mealOfDay)).get(NAME);
        } catch (Exception ignore) {
            return null;
        }
    }

    public Constants.COLLECTION_NAME getMealType( Constants.DAY_OF_WEEK dayOfWeek, Constants.MEAL_OF_DAY mealOfDay ) {
        try {
            String storedVal = (String) Objects.requireNonNull(getMeal(dayOfWeek, mealOfDay)).get(TYPE);
            return Constants.COLLECTION_NAME.valueOf(storedVal);
        } catch (Exception ignore) {
            return null;
        }
    }
    public Constants.COLLECTION_NAME getMealType( Date date, Constants.MEAL_OF_DAY mealOfDay ) {
        return getMealType(convertDate(date), mealOfDay);
    }

    public ArrayList<Ingredient> ingredientList() {
        return ingredients;
    }

    public ArrayList<Recipe> recipeList() {
        return recipes;
    }

    @SuppressWarnings("unchecked")
    public ArrayList<DocumentReference> getMealDocReferences( Constants.DAY_OF_WEEK dayOfWeek, Constants.MEAL_OF_DAY mealOfDay ) {
        try {
            return (ArrayList<DocumentReference>) Objects.requireNonNull(getMeal(dayOfWeek, mealOfDay)).get(REFERENCES);
        } catch (Exception ignore) {
            return null;
        }
    }

    public ArrayList<DocumentReference> getMealDocReferences( Date date, Constants.MEAL_OF_DAY mealOfDay ) {
        return getMealDocReferences(convertDate(date), mealOfDay);
    }

    public void getMealIngredients( Constants.DAY_OF_WEEK dayOfWeek, Constants.MEAL_OF_DAY mealOfDay, boolean clear ) {
        if (getMeal(dayOfWeek, mealOfDay) == null)
            return;

        if (clear)
            ingredients.clear();
        // Always fetch using doc ref in case details have changed
        for (DocumentReference ref : getMealDocReferences(dayOfWeek, mealOfDay)) {
            fsm.getData(ref, this, new Ingredient());
        }
    }

    public void setListeningActivity( DatasetWatcher context ) {
        this.listeningActivity = context;
    }

    public void updateStorage() {
        if( listeningActivity != null ) {
            listeningActivity.signalChangeToAdapter();
        }
    }

    public void getMealIngredients( Date date, Constants.MEAL_OF_DAY mealOfDay, boolean clear) {
        getMealIngredients(convertDate(date), mealOfDay, clear);
    }

    public void getMealRecipes( Constants.DAY_OF_WEEK dayOfWeek, Constants.MEAL_OF_DAY mealOfDay ) {
        if (getMeal(dayOfWeek, mealOfDay) == null)
            return;

        recipes.clear();
        // Always fetch using doc ref in case details have changed
        for (DocumentReference ref : getMealDocReferences(dayOfWeek, mealOfDay)) {
            fsm.getData(ref, this, new Recipe());
        }
    }

    public void getMealRecipes( Date date, Constants.MEAL_OF_DAY mealOfDay ) {
        getMealRecipes(convertDate(date), mealOfDay);
    }

    public void consumeMeal(Date date, Constants.MEAL_OF_DAY mealTime) {
        IngredientStorage ingredientStorage= IngredientStorage.getInstance();
        RecipeStorage recipeStorage = RecipeStorage.getInstance();

        if (getMealType(date, mealTime) == Constants.COLLECTION_NAME.INGREDIENTS) {
            ArrayList<DocumentReference> refs = getMealDocReferences(date, mealTime);
            for (DocumentReference ref : refs) {
                Ingredient ingredient = ingredientStorage.getIngredient(ref.getId());
                ingredientStorage.requestConsumptionOfIngredient(ingredient, 1.0);
            }
        }
        else if (getMealType(date, mealTime) == Constants.COLLECTION_NAME.RECIPES) {
            ArrayList<DocumentReference> refs = getMealDocReferences(date, mealTime);
            for (DocumentReference ref : refs) {
                Recipe recipe = recipeStorage.getRecipe(ref.getId());
                for (Map.Entry<String, HashMap<String, Object>> entry : recipe.getIngredients().entrySet()) {
                    String ingredientName = entry.getKey();
                    HashMap < String, Object > details = entry.getValue();
                    Ingredient ingredient = ingredientStorage.getIngredient(ingredientName);
                    recipeStorage.consumeIngredientInRecipe(ingredient, (Double) details.get(COUNT));
                }
            }
        }
    }

    /**
     * Gets the whole week of plans held in the meal plan. This will include all days
     * Sunday-Saturday, with 3 meals defined a day.
     * @return
     */
    public HashMap<String, HashMap<String, HashMap<String, Object > > > getPlans() {
        return plans;
    }

    @Override
    public void onDataFetchSuccess(DatabaseObject data) {
        if (data == null) {
            return;
        }

        if ( data.getClass() == Recipe.class ) {
            Recipe recipe = (Recipe) data;
            recipes.add(recipe);
            updateStorage();
        }

        else if ( data.getClass() == Ingredient.class ){
            Ingredient ingredient = (Ingredient) data;
            ingredients.add(ingredient);
            updateStorage();
        }
    }

    @Override
    public void onSpinnerFetchSuccess(Map<String, Object> data) {

    }
}
