package com.example.happymeals.mealplan;


import android.util.Pair;

import com.example.happymeals.Constants;
import com.example.happymeals.database.DatabaseObject;
import com.example.happymeals.database.FireStoreManager;
import com.example.happymeals.ingredient.Ingredient;
import com.example.happymeals.ingredient.IngredientStorage;
import com.example.happymeals.recipe.Recipe;
import com.example.happymeals.recipe.RecipeStorage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

/**
 * A MealPlan class which represents the set of planned out meals for breakast, lunch, and
 * supper on a weekly Sunday - Saturday basis. A MealPlan manages meals ({@link Meal}) for the
 * week.
 * @author sruduke
 */
public class MealPlan extends DatabaseObject {

    // The keys of the HashMap used to gather all Ingredients in a MealPlan
    public static final String RECIPES = "recipes";
    public static final String COUNT = "count";

    private Date startDate;
    private Date endDate;

    private String creator;

    private final Constants.COLLECTION_NAME INGREDIENT_TYPE = Constants.COLLECTION_NAME.INGREDIENTS;
    private final Constants.COLLECTION_NAME RECIPE_TYPE = Constants.COLLECTION_NAME.RECIPES;

    private IngredientStorage ingredientStorage;
    private RecipeStorage recipeStorage;

    // This HashMap maps day of week (Sunday-Saturday) -> meal of day (Breakfast, lunch, supper) -> Meal
    private HashMap< String, HashMap < String, Meal > > plans;

    // This HashMap maps ingredients to a HashMap of key-value pairs. These keys are the RECIPES and
    //COUNT static strings. e.g ingredientName -> {"count":5, "recipes":["omelette"]}
    private HashMap< String, HashMap< String, Object > > allIngredients;

    /**
     * Empty constructor which is required by {@link FireStoreManager} to store
     * object in the database.
     */
    public MealPlan() {
        ingredientStorage = IngredientStorage.getInstance();
        recipeStorage = RecipeStorage.getInstance();
    }

    /**
     * MealPlan constructor which takes the name of the meal plan {@link Date} and the name
     * of the creator/user.
     * @param name is the {@link String} representation of the start day
     * @param creator is the {@link String} representation of the user
     */
    public MealPlan( String name, String creator ) {
        super( name, creator );
        this.creator=creator;
        ingredientStorage = IngredientStorage.getInstance();
        recipeStorage = RecipeStorage.getInstance();
        allIngredients = new HashMap<>();
        plans = new HashMap<>();
        createMapForWeekday();
    }

    /**
     * Gets the start day of the weekly meal plan.
     * @return {@link Date}
     */
    public Date getStartDate() {
        return startDate;
    }

    /**
     * Gets the end day of the weekly meal plan.
     * @return {@link Date}
     */
    public Date getEndDate() {
        return endDate;
    }

    /**
     * Sets the start day of the weekly meal plan.
     * @param {@link Date} the date to set it to.
     */
    public void setStartDate( Date date ) {
        startDate = date;
        this.id=String.format( "%s_%s", creator, getStartDateString() );
    }

    /**
     * Sets the end day of the weekly meal plan.
     * @param date {@link Date} the date to set it to.
     */
    public void setEndDate( Date date ) {
        endDate = date;
    }

    /**
     * Get the start day in a string format - "EEEE, MMM D".
     * @return {@link String} the string start day.
     */
    public String getStartDateString() {
        return new SimpleDateFormat( "EEEE, MMM d", Locale.CANADA ).format( startDate );
    }

    /**
     * Get the end day in a string format - "EEEE, MMM D".
     * @return {@link String} the string end day.
     */
    public String getEndDateString() {
        return new SimpleDateFormat( "EEEE, MMM d", Locale.CANADA ).format( endDate );
    }

    /**
     * This is a function to autogenerate a meal plan for the user. It uses a Hashmap containing all
     * items for breakfast, lunch, and dinner and distributes them across the meal plan accordingly.
     * @param itemsForAutoGen is a {@link HashMap} which maps the {@link Constants.MEAL_OF_DAY} to another {@link HashMap}.
     *                        The second hashmap maps the type of items ({@link Constants.COLLECTION_NAME})
     *                         to the {@link ArrayList} list of items.
     */
    public void generateMealPlan( HashMap<Constants.MEAL_OF_DAY, Pair<Constants.COLLECTION_NAME, ArrayList<?>>> itemsForAutoGen ) {
        // this is gonna be super random -> the only efficiency I could possibly see to optimize on
        // would be least ingredients used ( cheapest solution? ) Might be a good idea.
        for ( Map.Entry<Constants.MEAL_OF_DAY, Pair<Constants.COLLECTION_NAME, ArrayList<?>>> itemsForMeals : itemsForAutoGen.entrySet() ) {
            if ( itemsForMeals.getValue().first == Constants.COLLECTION_NAME.INGREDIENTS )
                distributeIngredients( itemsForMeals.getKey(), ( ArrayList<Ingredient> ) itemsForMeals.getValue().second );
            else if ( itemsForMeals.getValue().first == Constants.COLLECTION_NAME.RECIPES )
                distributeRecipes( itemsForMeals.getKey(), ( ArrayList<Recipe> ) itemsForMeals.getValue().second );
        }
    }

    /**
     * This function distributes a list of recipes to different days in the week of a weekly meal plan.
     * @param mealOfDay {@link Constants.MEAL_OF_DAY} is the meal of the day to assign a random ingredient.
     * @param ingredients {@link ArrayList} is the list of ingredients to distribute.
     */
    private void distributeIngredients( Constants.MEAL_OF_DAY mealOfDay, ArrayList<Ingredient> ingredients ) {
        // Distribute ingredients on a one per day basis -- this way there aren't any weird collisions
        // ( ingredients that shouldn't go together ). Ensure that at least every ingredient is used
        // before assigning again ( to increase variety ).
        ArrayList<Ingredient> copy = new ArrayList<>( ingredients );
        Random random = new Random();

        for ( Constants.DAY_OF_WEEK weekDay : Constants.DAY_OF_WEEK.values() ) {
            int bound = copy.size() - 1;
            Ingredient selected;

            if ( bound == 0 ) {
                selected = copy.remove( 0 );
                copy = new ArrayList<>( ingredients );
            }
            else
                selected = copy.remove( random.nextInt( bound ) );

            ArrayList<Ingredient> selectedList = new ArrayList<>();
            selectedList.add( selected );
            setMealItemsIngredients( weekDay, mealOfDay, selectedList );
        }
    }

    /**
     * This function distributes a list of recipes to different days in the week of a weekly meal plan.
     * @param mealOfDay {@link Constants.MEAL_OF_DAY} the meal of the day to assign a random ingredient.
     * @param recipes {@link ArrayList} is the list of ingredients to distribute.
     */
    private void distributeRecipes( Constants.MEAL_OF_DAY mealOfDay, ArrayList<Recipe> recipes ) {
        ArrayList<Recipe> copy = new ArrayList<>( recipes );
        Random random = new Random();

        for ( Constants.DAY_OF_WEEK weekDay : Constants.DAY_OF_WEEK.values() ) {
            int bound = copy.size() - 1;
            Recipe selected;

            if ( bound == 0 ) {
                selected = copy.remove( 0 );
                copy = new ArrayList<>( recipes );
            }
            else
                selected = copy.remove( random.nextInt( bound ) );

            ArrayList<Recipe> selectedList = new ArrayList<>();
            selectedList.add( selected );
            setMealItemsRecipe( weekDay, mealOfDay, selectedList );
        }
    }

    /**
     * This method converts a day to the day of the week for the current meal plan.
     * @param date
     * @return
     */
    private Constants.DAY_OF_WEEK convertDate( Date date ) {
        Calendar cal = Calendar.getInstance();
        cal.setTime( date );
        int index = cal.get( Calendar.DAY_OF_WEEK );
        return Constants.DAY_OF_WEEK.values()[index-1];
    }

    /**
     * Checks if the meal plan is within a given date.
     * @param date {@link Date} to check.
     * @return {@link boolean} whether the meal plan is within the provided date.
     */
    public boolean isWithinDate( Date date ) {
        return !( date.before( startDate ) || date.after( endDate ) );
    }

    /**
     * Gets a {@link HashMap} that links the {@link String} representation of the
     * day of the week to its corresponding {@link Meal}.
     * @param day {@link Constants.DAY_OF_WEEK} is the day of the week
     * @return
     */
    public HashMap<String, Meal> getMealsForDay( Constants.DAY_OF_WEEK day ) {
        return plans.get( day.toString() );
    }

    /**
     * Gets a {@link HashMap} that links the {@link String} representation of the
     * day of the week to its corresponding {@link Meal}.
     * @param date {@link Date} is the date tp get the meals for
     * @return
     */
    public HashMap<String, Meal> getMealsForDay( Date date ) {
        return getMealsForDay( convertDate( date ) );
    }

    /**
     * Gets the number of meals for a certain day.
     * @param day {@link Constants.DAY_OF_WEEK} is the day of the week.
     * @return {@link int} the number of meals
     */
    public int getNumberOfMealsForDay( Constants.DAY_OF_WEEK day ) {
        int count = 0;
        for ( Meal meal : getMealsForDay( day ).values() )
            count = ( meal == null ) ? count : count + 1;
        return count;
    }

    /**
     * Gets the number of meals for a certain date.
     * @param date {@link Date}
     * @return {@link int} the number of meals
     */
    public int getNumberOfMealsForDay( Date date ) {
        return getNumberOfMealsForDay( convertDate( date ) );
    }

    /**
     * Gets the name of the meal plan (the start date as a string representation).
     * @return {@link String}
     */
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
                map.put( meal.toString(), null );
            }
            plans.put( day.toString(), map );
        }
    }

    /**
     * This returns the meal for a certain day of the week and meal of the day (breakfast, lunch, dinner).
     * @param dayOfWeek {@link Constants.DAY_OF_WEEK dayOfWeek} is the day of the week (Sunday, Monday, ...)
     * @param mealOfDay {@link Constants.MEAL_OF_DAY mealOfDay} is the meal of the day (Breakfast, Lunch, Dinner)
     * @return
     */
    public Meal getMeal( Constants.DAY_OF_WEEK dayOfWeek, Constants.MEAL_OF_DAY mealOfDay ) {
        // verbose null checking
        try {
            return Objects.requireNonNull( Objects.requireNonNull( plans.get( dayOfWeek.toString() ) ).get( mealOfDay.toString() ) );
        } catch ( Exception ignore ) { }

        return null;
    }

    /**
     * This returns the meal for a certain date and meal of day.
     * @param date {@link Date} is the date to find the meal for
     * @param mealOfDay {@link Constants.MEAL_OF_DAY mealOfDay} is the meal of the day (Breakfast, Lunch, Dinner)
     * @return
     */
    public Meal getMeal( Date date, Constants.MEAL_OF_DAY mealOfDay ) {
        // verbose null checking
        return getMeal( convertDate( date ), mealOfDay );
    }

    /**
     * This sets the meal for a certain day of the week and meal of the day (breakfast, lunch, dinner).
     * @param dayOfWeek {@link Constants.DAY_OF_WEEK dayOfWeek} is the day of the week (Sunday, Monday, ...)
     * @param mealOfDay {@link Constants.MEAL_OF_DAY mealOfDay} is the meal of the day (Breakfast, Lunch, Dinner)
     * @return
     */
    private boolean setMeal( Constants.DAY_OF_WEEK dayOfWeek, Constants.MEAL_OF_DAY mealOfDay, Meal meal ) {
        try {
            HashMap< String, Meal > map = Objects.requireNonNull( plans.get( dayOfWeek.toString() ) );
            map.put( mealOfDay.toString(), meal );
            return true;
        } catch ( Exception ignore ) {
            return false;
        }
    }

    /**
     * Removes a meal from the day of a week and sets the "made" value to false.
     * @param dayOfWeek The {@link Constants.DAY_OF_WEEK} of the week day the meal is being removed from.
     * @param mealOfDay The {@link Constants.MEAL_OF_DAY} of the meal of the day for which the meal is being
     *                  removed from.
     */
    public boolean removeMeal( Constants.DAY_OF_WEEK dayOfWeek, Constants.MEAL_OF_DAY mealOfDay ) {
        if ( getMeal( dayOfWeek, mealOfDay ) == null )
            return false;

        setMeal( dayOfWeek, mealOfDay, null );
        return true;
    }

    /**
     * Sets or creates a meal which will contain recipes ({@link Recipe}) for the specific day and meal of day.
     * @param dayOfWeek The {@link Constants.DAY_OF_WEEK} is the day of the week (Sunday, Monday, ...) to set
     * @param mealOfDay The {@link Constants.MEAL_OF_DAY} is the meal of the day (Breakfast, Lunch, Dinner) to set
     */
    public void setMealItemsRecipe( Constants.DAY_OF_WEEK dayOfWeek, Constants.MEAL_OF_DAY mealOfDay, ArrayList<Recipe> recipes ) {
        Meal meal = getMeal( dayOfWeek, mealOfDay );

        if ( meal == null )
            meal = new Meal();

        HashMap < String, Double > recipeScales = new HashMap<>();
        for ( Recipe recipe : recipes )
            recipeScales.put( recipe.getName(), 1.0 );

        meal.setItems( recipeScales );
        meal.setType( RECIPE_TYPE );

        setMeal( dayOfWeek, mealOfDay, meal );
    }

    /**
     * Sets or creates a meal which will contain ingredients ({@link Ingredient}) for the specific day and meal of day.
     * @param dayOfWeek The {@link Constants.DAY_OF_WEEK} is the day of the week (Sunday, Monday, ...) to set
     * @param mealOfDay The {@link Constants.MEAL_OF_DAY} is the meal of the day (Breakfast, Lunch, Dinner) to set
     */
    public void setMealItemsIngredients( Constants.DAY_OF_WEEK dayOfWeek, Constants.MEAL_OF_DAY mealOfDay, ArrayList<Ingredient> ingredients ) {
        Meal meal = getMeal( dayOfWeek, mealOfDay );

        if ( meal == null )
            meal = new Meal();

        HashMap < String, Double > ingredientScales = new HashMap<>();
        for ( Ingredient ingredient : ingredients )
            ingredientScales.put( ingredient.getName(), 1.0 );

        meal.setType( INGREDIENT_TYPE );
        meal.setItems( ingredientScales );

        setMeal( dayOfWeek, mealOfDay, meal );
    }

    /**
     * This returns a {@link HashMap} which maps the name of an {@link Ingredient} to another {@link HashMap}.
     * The second HashMap maps the properties of an ingredient to the value. Such properties are the count and
     * recipes the ingredient has.
     * @return {@link HashMap} {Ingredient Name : {"count" : 0, "recipes": []}
     */
    public HashMap < String, HashMap< String, Object > > getAllIngredients() {
        return allIngredients;
    }

    /**
     * This traverses through all the defined meals in the meal plan and extracts the ingredients. It will
     * keep track of the ingredients and add them to a {@link HashMap} which contains the relevant Ingredient
     * information which will be needed by the shopping list module. The hashmap has the form:
     * {Ingredient Name : {"count" : 0, "recipes": []}.
     */
    public void generateAllIngredients() {
        allIngredients.clear();
        for ( Constants.DAY_OF_WEEK weekDay : Constants.DAY_OF_WEEK.values() ) {
            for ( Constants.MEAL_OF_DAY mealTime : Constants.MEAL_OF_DAY.values() ) {
                Meal meal = getMeal( weekDay, mealTime );

                if ( meal == null || meal.isMade() )
                    continue;

                HashMap<String, Double> items = meal.getItems();
                if ( meal.getType() == INGREDIENT_TYPE ) {

                    for ( String ingredientName : items.keySet() ) {
                        insertIngredientToAll( ingredientName, null,null, allIngredients );
                    }
                } else if ( meal.getType() == RECIPE_TYPE ) {
                    for ( Map.Entry<String, Double> entry : items.entrySet() ) {
                        String recipeName = entry.getKey();
                        Double scale = entry.getValue();

                        Recipe recipe = recipeStorage.getRecipe( recipeName );

                        for ( Map.Entry<String, HashMap<String, Object>> ingredient : recipe.getIngredients().entrySet() ) {
                            String ingredientName = ingredient.getKey();
                            HashMap<String, Object> ingredientDetails = new HashMap<>( ingredient.getValue() );
                            Double ingredientAmount = ( Double ) ingredientDetails.get( COUNT );

                            Double scaledAmount = Math.ceil( scale * ingredientAmount );
                            insertIngredientToAll( ingredientName, recipeName, scaledAmount, allIngredients );
                        }
                    }
                }
            }
        }
    }

    /**
     * This is used when creating meals for the meal plan to keep track of all the ingredients in the weekly meal plan. It inserts
     * ingredient information into a {@link HashMap} that will contain another {@link HashMap} of details for each ingredient.
     * This populates the HashMap with the necessary info that will later be used by the shopping list module.
     * The hashmap has the form: {Ingredient Name : {"count" : 0, "recipes": []}.
     * @param ingredientName {@link String} name of the ingredient
     * @param recipeName {@link String} name of recipe the ingredient is a part of
     * @param amount {@link Double} amount of the ingredient
     * @param allIngredients the {@link HashMap} to populate the information to
     */
    public void insertIngredientToAll( String ingredientName, String recipeName, Double amount, HashMap<String, HashMap<String, Object>> allIngredients ) {
        Ingredient ingredient = ingredientStorage.getIngredient( ingredientName );
        HashMap < String, Object > ingredientDetails;

        if ( allIngredients.containsKey( ingredientName ) ) {
            ingredientDetails = allIngredients.get( ingredientName );
            Double previousCount = ( Double ) ingredientDetails.get( COUNT );

            if ( amount == null )
                ingredientDetails.put( COUNT, previousCount + ingredient.getAmount() );
            else
                ingredientDetails.put( COUNT, previousCount + amount );

            ArrayList<String> storedNames = ( ArrayList<String> ) allIngredients.get( ingredientName ).get( MealPlan.RECIPES );
            if ( recipeName != null && storedNames != null ) {
                if ( !storedNames.contains( recipeName ) ) {
                    storedNames.add( recipeName );
                    ingredientDetails.put( MealPlan.RECIPES, storedNames );
                }
            }

            allIngredients.put( ingredientName, ingredientDetails );
        }
        else {
            ingredientDetails = new HashMap<>();

            if ( amount == null )
                ingredientDetails.put( COUNT, ingredient.getAmount() );
            else
                ingredientDetails.put( COUNT, amount );

            if ( recipeName != null ) {
                ArrayList<String> recipeNames = new ArrayList<>();
                recipeNames.add( recipeName );
                ingredientDetails.put( RECIPES, recipeNames );
            }

            allIngredients.put( ingredientName, ingredientDetails );
        }
    }

    /**
     * This sets the meal for a iven a day of the week and meal time to whether it was made.
     * @param dayOfWeek The {@link Constants.DAY_OF_WEEK} is the day of the week (Sunday, Monday, ...) to set
     * @param mealOfDay The {@link Constants.MEAL_OF_DAY} is the meal of the day (Breakfast, Lunch, Dinner) to set
     * @param made {@link boolean} whether the meal is made or not
     */
    public boolean setMealMade( Constants.DAY_OF_WEEK dayOfWeek, Constants.MEAL_OF_DAY mealOfDay, Boolean made ) {
        Meal meal = getMeal( dayOfWeek, mealOfDay );
        if ( meal != null ) {
            meal.setMade( made );
            return true;
        }
        return false;
    }

    /**
     * This sets the meal for a iven a day of the week and meal time to whether it was made.
     * @param date The {@link Date} to set meal made for
     * @param mealOfDay The {@link Constants.MEAL_OF_DAY} is the meal of the day (Breakfast, Lunch, Dinner) to set meal made for
     * @param made {@link boolean} whether the meal is made or not
     */
    public boolean setMealMade( Date date, Constants.MEAL_OF_DAY mealOfDay, Boolean made ) {
        return setMealMade( convertDate( date ), mealOfDay, made );
    }

    /**
     * Given a day of the week and meal time, this gets whether the meal was made.
     * @param dayOfWeek The {@link Constants.DAY_OF_WEEK} is the day of the week (Sunday, Monday, ...) to get
     * @param mealOfDay The {@link Constants.MEAL_OF_DAY} is the meal of the day (Breakfast, Lunch, Dinner) to get
     * @return {@link Boolean} whether the meal at the certain time is made.
     */
    public Boolean getMealMade( Constants.DAY_OF_WEEK dayOfWeek, Constants.MEAL_OF_DAY mealOfDay ) {
        Meal meal = getMeal( dayOfWeek, mealOfDay );
        if ( meal != null )
            return meal.isMade();

        return false;
    }

    /**
     * Given a day of the week and meal time, this gets the ingredients for the meal.
     * @param dayOfWeek The {@link Constants.DAY_OF_WEEK} is the day of the week (Sunday, Monday, ...) to get the ingredients for
     * @param mealOfDay The {@link Constants.MEAL_OF_DAY} is the meal of the day (Breakfast, Lunch, Dinner) to get the ingredients for
     * @return {@link ArrayList} of {@link Ingredient} in the meal
     */
    public ArrayList<Ingredient> getMealIngredients( Constants.DAY_OF_WEEK dayOfWeek, Constants.MEAL_OF_DAY mealOfDay ) {
        Meal meal = getMeal( dayOfWeek, mealOfDay );
        ArrayList<Ingredient> ingredients = new ArrayList<>();

        if ( meal != null ) {
            for ( String ingredientName : meal.getItems().keySet() ) {
                Ingredient ingredient = ingredientStorage.getIngredient( ingredientName );
                ingredients.add( ingredient );
            }
            return ingredients;
        }

        return null;
    }

    /**
     * Given a date and meal time, this gets the ingredients for the meal.
     * @param date The {@link Date} to get the ingredients for
     * @param mealOfDay The {@link Constants.MEAL_OF_DAY} is the meal of the day (Breakfast, Lunch, Dinner) to get the ingredients for
     * @return {@link ArrayList} of {@link Ingredient} in the meal
     */
    public ArrayList<Ingredient> getMealIngredients( Date date, Constants.MEAL_OF_DAY mealOfDay ) {
        return getMealIngredients( convertDate( date ), mealOfDay );
    }

    /**
     * Given a day of the week and meal time, this gets the recipes for the meal.
     * @param dayOfWeek The {@link Constants.DAY_OF_WEEK} is the day of the week (Sunday, Monday, ...) to get the recipes for
     * @param mealOfDay The {@link Constants.MEAL_OF_DAY} is the meal of the day (Breakfast, Lunch, Dinner) to get the recipes for
     * @return {@link ArrayList} of {@link Recipe} in the meal
     */
    public ArrayList<Recipe> getMealRecipes( Constants.DAY_OF_WEEK dayOfWeek, Constants.MEAL_OF_DAY mealOfDay ) {
        Meal meal = getMeal( dayOfWeek, mealOfDay );
        ArrayList<Recipe> recipes = new ArrayList<>();

        if ( meal != null && meal.getItems() != null ) {
            for ( String recipeName : meal.getItems().keySet() ) {
                Recipe recipe = recipeStorage.getRecipe( recipeName );
                recipes.add( recipe );
            }
            return recipes;
        }

        return null;
    }

    /**
     * Given a date, this gets the recipes for the meal.
     * @param date The {@link Date} to get the recipes for
     * @param mealOfDay The {@link Constants.MEAL_OF_DAY} is the meal of the day (Breakfast, Lunch, Dinner) to get the recipes for
     * @return {@link ArrayList} of {@link Recipe} in the meal
     */
    public ArrayList<Recipe> getMealRecipes( Date date, Constants.MEAL_OF_DAY mealOfDay ) {
        return getMealRecipes( convertDate( date ), mealOfDay );
    }

    /**
     * Given a day of the week and meal time, this gets the meal type for a meal in the weekly meal plan.
     * @param dayOfWeek The {@link Constants.DAY_OF_WEEK} is the day of the week (Sunday, Monday, ...) to get
     * @param mealOfDay The {@link Constants.MEAL_OF_DAY} is the meal of the day (Breakfast, Lunch, Dinner) to get
     * @return {@link Constants.COLLECTION_NAME} the {@link Enum} as to whether the meal is for Recipes or Ingredients.
     */
    public Constants.COLLECTION_NAME getMealType( Constants.DAY_OF_WEEK dayOfWeek, Constants.MEAL_OF_DAY mealOfDay ) {
        Meal meal = getMeal( dayOfWeek, mealOfDay );
        if ( meal != null )
            return meal.getType();

        return null;
    }

    /**
     * Given a date, this gets the meal type for a meal in the weekly meal plan.
     * @param date The {@link Date} to get the type of
     * @param mealOfDay The {@link Constants.MEAL_OF_DAY} is the meal of the day (Breakfast, Lunch, Dinner) to get
     * @return {@link Constants.COLLECTION_NAME} the {@link Enum} as to whether the meal is for Recipes or Ingredients.
     */
    public Constants.COLLECTION_NAME getMealType( Date date, Constants.MEAL_OF_DAY mealOfDay ) {
        return getMealType( convertDate( date ), mealOfDay );
    }

    /**
     * Sets the scale on a {@link Recipe} in the meal plan.
     * @param dayOfWeek {@link Constants.DAY_OF_WEEK} is the day of the week (Sunday, Monday, ...) to set the scale for.
     * @param mealOfDay {@link Constants.MEAL_OF_DAY} is the meal of the day (Breakfast, Lunch, Dinner) to set the scale for.
     * @param itemName {@link String} the name of the recipe to scale.
     * @param scaleFactor {@link Double} the amount to scale the recipe by.
     */
    public void setScaleOnItem( Constants.DAY_OF_WEEK dayOfWeek, Constants.MEAL_OF_DAY mealOfDay, String itemName, Double scaleFactor ) {
        Meal meal = getMeal( dayOfWeek, mealOfDay );
        if ( meal != null ) {
            meal.setScale( itemName, scaleFactor );
            generateAllIngredients();
        }
    }

    /**
     * Given a date and meal time, this will decrement the resources needed for a certain meal (recipes or ingredients)
     * from the user's storage.
     * @param date {@link Date} is the date which this meal was defined,
     * @param mealTime {@link Constants.MEAL_OF_DAY mealOfDay} is the meal of the day (Breakfast, Lunch, Dinner) to consume,
     */
    public void consumeMeal( Date date, Constants.MEAL_OF_DAY mealTime ) {
        if ( getMealType( date, mealTime ) == INGREDIENT_TYPE ) {
            for ( Ingredient ingredient : getMealIngredients( date, mealTime ) )
                ingredientStorage.requestConsumptionOfIngredient( ingredient, ingredient.getAmount() );

        }
        else if ( getMealType( date, mealTime ) == RECIPE_TYPE ) {
            Meal meal = getMeal( convertDate( date ), mealTime );
            for ( Map.Entry<String, Double> entry : meal.getItems().entrySet() ) {
                Double scaleFactor = entry.getValue();
                Recipe recipe = recipeStorage.getRecipe( entry.getKey() );
                for ( Map.Entry<String, HashMap<String, Object>> ingredientDetails : recipe.getIngredients().entrySet() ) {
                    Ingredient ingredient = ingredientStorage.getIngredient( ingredientDetails.getKey() );
                    recipeStorage.consumeIngredientInRecipe( ingredient, Math.ceil( ingredient.getAmount() * scaleFactor ) );
                }
            }
        }
        generateAllIngredients();
    }

    /**
     * Gets the whole week of plans held in the meal plan. This will include all days
     * Sunday-Saturday, with 3 meals defined a day.
     * @return {@link HashMap}
     */
    public HashMap<String, HashMap<String, Meal > > getPlans() {
        return plans;
    }

}
