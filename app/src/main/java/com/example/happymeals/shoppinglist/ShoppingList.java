package com.example.happymeals.shoppinglist;

import com.example.happymeals.ingredient.Ingredient;
import com.example.happymeals.ingredient.IngredientStorage;
import com.example.happymeals.mealplan.MealPlan;
import com.example.happymeals.mealplan.MealPlanStorage;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This is a class that defines a Shopping List. The Shopping List is used to indicate to the users
 * what ingredients they need to buy according to their meal plan and ingredient storage
 * @author sruduke
 */
public class ShoppingList {

    private static ShoppingList instance = null;

    private IngredientStorage ingredientStorage;
    private MealPlanStorage mps;

    // itemsToBuy is the ArrayList of actual ShoppingListItems -> it will contain the amounts necessary
    // for each item.
    private ArrayList<ShoppingListItem> itemsToBuy;

    // ingredientsToBuy is the ArrayList of just the ingredient objects that must be bought; this
    // allows us to display the ingredients using the Ingredient adapter instead of having to
    // create a new one.
    private ArrayList<Ingredient> ingredientsToBuy;


    /**
     * This is an empty constructor for the shopping list. It is mainly used as a point to get instances
     * of the singleton Ingredient and MealPlan storage.
     */
    private ShoppingList() {
        mps = MealPlanStorage.getInstance();
        ingredientStorage = IngredientStorage.getInstance();

        ingredientsToBuy = new ArrayList<>();
        itemsToBuy = new ArrayList<>();
    }

    /**
     * Allows outside classes to access this instantiated class. If this class has not been
     * instantiated yet then it will be done here.
     * @return The created instance of {@link ShoppingList}
     */
    public static ShoppingList getInstance() {
        if( instance == null  ) {
            instance = new ShoppingList();
        }
        return instance;
    }

    /**
     * Generating the shopping list is a solely deterministic function. It compares the current
     * ingredients in a user's meal plans and compares it to the ingredients currently in their
     * storage. Any differences are noted and stored accordingly. This method only generates the
     * shopping list, and has separate getters based on what context the ShoppingList is needed for.
     *
     * It pulls the recipes from the meal plan and determines if there are enough ingredients in the
     * ingredient storage to make the recipe. If there is not enough, it will add the amount still
     * needed to the itemsToBuy ArrayList.
     */
    private void generateShoppingList() {
        itemsToBuy.clear();
        ingredientsToBuy.clear();

        for ( Map.Entry<String, HashMap<String, Object>> entry : mps.getAllIngredients().entrySet() ) {

            String ingredientName = entry.getKey();
            HashMap<String, Object> details = entry.getValue();

            ArrayList<String> recipeNames;
            try {
                recipeNames = (ArrayList<String>) details.get(MealPlan.RECIPES);
            } catch (Exception e) {
                recipeNames = null;
            }

            Double count = ( Double ) details.get( MealPlan.COUNT );

            Ingredient storageIngredient = ingredientStorage.getIngredient( ingredientName );

            if ( storageIngredient != null && count > storageIngredient.getAmount() ) {
                Double amountNeeded = Math.abs( storageIngredient.getAmount() - count );
                ShoppingListItem item = new ShoppingListItem( storageIngredient, recipeNames, amountNeeded );
                itemsToBuy.add( item );
                ingredientsToBuy.add( storageIngredient );
            } else if ( storageIngredient == null ) {
                // case if ingredient not in storage ... not sure best way to handle
                ShoppingListItem item = new ShoppingListItem( storageIngredient, recipeNames, count );
                itemsToBuy.add( item );
                ingredientsToBuy.add( storageIngredient );
            }
        }
    }

    /**
     * This will return an ArrayList of ShoppingListItems which an array adapter can use
     * to populate its amount fields.
     * @return The {@link ArrayList<ShoppingListItem>} of ShoppingListItems that the user needs to buy
     */
    public ArrayList < ShoppingListItem > getShoppingList() {
        generateShoppingList();
        return itemsToBuy;
    }

    /**
     * This will return a ArrayList of Ingredients for an array adapter to display
     * @return The {@link ArrayList<Ingredient>} of Ingredients that will be displayed.
     */
    public ArrayList < Ingredient > getIngredientsToBuy() {
        if ( ingredientsToBuy.size() == 0 )
            generateShoppingList();
        return ingredientsToBuy;
    }

    /**
     * This is used to indicate when an ingredient item has been picked up
     * @param list {@link ArrayList<Ingredient>}
     */
    public void pickUpItems( ArrayList<Ingredient> list ) {
        for ( Ingredient ingredient : list ) {
            ingredient.setNeedsUpdate( true );
        }
    }

    /**
     * This returns to number of ingredients that the user needs to buy
     * @return The {@link int} size of the itemsToBuy ArrayList
     */
    public int getSize() {
        return itemsToBuy.size();
    }

}
