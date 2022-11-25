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

public class ShoppingList {
    private static ShoppingList instance = null;

    private FireStoreManager fsm;
    private IngredientStorage ingredientStorage;
    private MealPlanStorage mps;

    private ArrayList<ShoppingListItem> itemsToBuy;
    private ArrayList<Ingredient> ingredientsToBuy;


    private ShoppingList() {
        this.fsm = FireStoreManager.getInstance();
        mps = MealPlanStorage.getInstance();
        ingredientStorage = IngredientStorage.getInstance();

        ingredientsToBuy = new ArrayList<>();
        itemsToBuy = new ArrayList<>();
    }

    public static ShoppingList getInstance() {
        if( instance == null ) {
            instance = new ShoppingList();
        }
        return instance;
    }

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

    public ArrayList < ShoppingListItem > getShoppingList() {
        generateShoppingList();
        return itemsToBuy;
    }

    // needed for array adapter
    public ArrayList < Ingredient > getIngredientsToBuy() {
        if (ingredientsToBuy.size() == 0)
            generateShoppingList();
        return ingredientsToBuy;
    }

    public void pickUpItems(ArrayList<Ingredient> list) {
        for (Ingredient ingredient : list) {
            ingredient.setNeedsUpdate(true);
            ingredientStorage.updateIngredient(ingredient);
        }
    }

    public int getSize() {
        return itemsToBuy.size();
    }

}
