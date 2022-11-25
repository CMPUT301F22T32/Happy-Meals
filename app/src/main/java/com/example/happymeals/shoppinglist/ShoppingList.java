package com.example.happymeals.shoppinglist;

import android.util.Pair;
import android.widget.ArrayAdapter;

import com.example.happymeals.Constants;
import com.example.happymeals.database.DatabaseListener;
import com.example.happymeals.database.DatabaseObject;
import com.example.happymeals.database.DatasetWatcher;
import com.example.happymeals.database.FireStoreManager;
import com.example.happymeals.ingredient.Ingredient;
import com.example.happymeals.ingredient.IngredientStorage;
import com.example.happymeals.mealplan.MealPlan;
import com.example.happymeals.mealplan.MealPlanStorage;
import com.example.happymeals.recipe.Recipe;
import com.google.firebase.firestore.DocumentReference;

import org.w3c.dom.Document;

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

    private FireStoreManager fsm;
    private IngredientStorage ingredientStorage;
    private MealPlanStorage mps;

    private ArrayList<ShoppingListItem> itemsToBuy;
    private ArrayList<Ingredient> ingredientsToBuy;


    /**
     * This is a constructor for the shopping list
     */
    private ShoppingList() {
        this.fsm = FireStoreManager.getInstance();
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
        if( instance == null ) {
            instance = new ShoppingList();
        }
        return instance;
    }

    /**
     * This generates the shopping list. It pulls the recipes from the meal plan and determines if
     * there are enough ingredients in the ingredient storage to make the recipe. If there is not
     * enough, it will add the amount still needed to the itemsToBuy ArrayList.
     */
    private void generateShoppingList() {
        itemsToBuy.clear();
        ingredientsToBuy.clear();

        for (Map.Entry<String, HashMap<String, Object>> entry : mps.getAllIngredients().entrySet()) {

            String ingredientName = entry.getKey();
            HashMap<String, Object> details = entry.getValue();

            ArrayList<String> recipeNames = (ArrayList<String>) details.get(MealPlan.RECIPES);
            Double count = (Double) details.get(MealPlan.COUNT);

            Ingredient storageIngredient = ingredientStorage.getIngredient(ingredientName);

            if (storageIngredient != null && count > storageIngredient.getAmount()) {
                Double amountNeeded = Math.abs(storageIngredient.getAmount() - count);
                ShoppingListItem item = new ShoppingListItem(storageIngredient, recipeNames, amountNeeded);
                itemsToBuy.add(item);
                ingredientsToBuy.add(storageIngredient);
            } else if (storageIngredient == null) {
                // case if ingredient not in storage ... not sure best way to handle
                ShoppingListItem item = new ShoppingListItem(storageIngredient, recipeNames, count);
                itemsToBuy.add(item);
                ingredientsToBuy.add(storageIngredient);
            }
        }
    }

    /**
     *
     * @return The {@link ArrayList<Ingredient>} of Ingredient items that the user needs to buy
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
        if (ingredientsToBuy.size() == 0)
            generateShoppingList();
        return ingredientsToBuy;
    }

    /**
     * This is used to indicate when an ingredient item has been picked up
     * @param list {@link ArrayList<Ingredient>}
     */
    public void pickUpItems(ArrayList<Ingredient> list) {
        for (Ingredient ingredient : list) {
            ingredient.setNeedsUpdate(true);
            ingredientStorage.updateIngredient(ingredient);
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
