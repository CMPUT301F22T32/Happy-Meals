package com.example.happymeals;

import java.util.ArrayList;

/**
 * This is a class for storing a list of ingredients
 */
public class IngredientStorage {
    private ArrayList<Ingredient> ingredients;

    public IngredientStorage() {
        ingredients = new ArrayList<Ingredient>();
    }

    /**
     * This pulls the list of ingredients in the Firebase database and sets it to ingredients
     */
    public void setIngredients() {
        // TODO pull information from database
        ingredients.add(new Ingredient("Milk", "DD-MM-YYYY", Location.Pantry, 2, AmountUnit.count, IngredientCategory.Fruit));
    }

    /**
     * This returns an ArrayList of the Ingredients
     * @return
     * Returns the ArrayList of Ingredients that the IngredientStorage has
     */
    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    /**
     * This add and Ingredient to ingredients and adds the ingredient to the Firebase database
     * @param ingredient
     */
    public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
        // TODO implement database interaction
    }
}
