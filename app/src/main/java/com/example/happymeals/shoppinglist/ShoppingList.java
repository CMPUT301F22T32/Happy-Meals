package com.example.happymeals.shoppinglist;

import android.util.Pair;

import com.example.happymeals.database.DatabaseListener;
import com.example.happymeals.database.DatabaseObject;
import com.example.happymeals.database.FireStoreManager;
import com.example.happymeals.ingredient.Ingredient;
import com.example.happymeals.ingredient.IngredientStorage;
import com.example.happymeals.mealplan.MealPlan;
import com.example.happymeals.recipe.Recipe;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ShoppingList implements DatabaseListener {
    private static ShoppingList instance = null;

    private FireStoreManager fsm;
    private IngredientStorage ingredientStorage;
    private MealPlan mealPlan;

    // Each ingredient will have a corresponding list of tuples; each tuple represents a recipe and
    // the amount of the ingredient in the specific recipe (must be an ArrayList so duplicates are
    // allowed)
    // e.g. {Egg : [("Omelette", 2), ("Cake", 4)]}
    private HashMap<Ingredient, ArrayList<Pair<String, Integer>>> allIngredients;
    private ArrayList<Ingredient> allRecipeIngredients;

    private ShoppingList() {
        this.fsm = FireStoreManager.getInstance();
        ingredientStorage = IngredientStorage.getInstance();
        mealPlan = null;
        allRecipeIngredients = new ArrayList<>();
        generateShoppingList();
    }

    public static ShoppingList getInstance() {
        if( instance == null ) {
            instance = new ShoppingList();
        }
        return instance;
    }

    private void generateTotalIngredients() {
        // I noticed it was a HashMap in the actual class, will need to read the docs further
        // to see how to just get a list of all the recipes. This is a temp variable until then.
        ArrayList<Recipe> recipesFromMealPlan = null;

        for (Recipe recipe : recipesFromMealPlan) {
            for (Map.Entry<String, DocumentReference> entry : recipe.getIngredients().entrySet()) {
                String recipeName = entry.getKey();
                DocumentReference reference = entry.getValue();

                fsm.getData(reference, this, new Ingredient());
                int lastIndex = allRecipeIngredients.size() - 1;

                Ingredient ingredient = allRecipeIngredients.get(lastIndex);
                Pair<String, Integer> recipeTuple = new Pair<>(recipeName, ingredient.getAmount());

                if (allIngredients.get(ingredient) == null) {
                    ArrayList<Pair<String, Integer>> recipes = new ArrayList<>();
                    allIngredients.put(ingredient, recipes);
                }
                allIngredients.get(ingredient).add(recipeTuple);
            }
        }
    }

    private void generateShoppingList() {
        generateTotalIngredients();

        for (Map.Entry<Ingredient, ArrayList<Pair<String, Integer>>> entry : allIngredients.entrySet()) {
            // Ingredients in Meal Planner
            Ingredient recipeIngredient = entry.getKey();
            ArrayList<Pair<String, Integer>> recipes = entry.getValue();

            // Ingredients in Storage
            Ingredient storageIngredient = ingredientStorage.getIngredient(recipeIngredient.getName());

            Integer total = 0;
            for (Pair<String, Integer> recipeTuple : recipes) {
                total += recipeTuple.second;
            }

            if (total < storageIngredient.getAmount())
                allIngredients.remove(recipeIngredient);

            else {
                // should work in place but not certain
                recipeIngredient.setAmount(total - storageIngredient.getAmount());
            }
        }
    }

    public ArrayList<Ingredient> getIngredientsToBuy() {
        return new ArrayList<Ingredient>(allIngredients.keySet());
    }

    public HashMap<Ingredient, ArrayList<Pair<String, Integer>>> getRecipesforIngredient() {
        return allIngredients;
    }

    public ArrayList<Ingredient> getStoredIngredients() {
        return ingredientStorage.getIngredients();
    }

    @Override
    public void onDataFetchSuccess(DatabaseObject data) {
        allRecipeIngredients.add((Ingredient) data);
    }

    @Override
    public void onSpinnerFetchSuccess(Map<String, Object> data) {

    }
}
