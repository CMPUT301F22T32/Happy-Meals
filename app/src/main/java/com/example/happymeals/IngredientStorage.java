package com.example.happymeals;

import java.util.ArrayList;

public class IngredientStorage {
    private ArrayList<Ingredient> ingredients;

    public IngredientStorage() {
        ingredients = new ArrayList<Ingredient>();
    }

    public void setIngredients() {
        // TODO pull information from database
        ingredients.add(new Ingredient("Milk", "DD-MM-YYYY", Location.Pantry, 2, "mg", IngredientCategory.Fruit));
    }

    public ArrayList<Ingredient> getIngredients() {
        return ingredients;
    }

    public void addIngredient(Ingredient ingredient) {
        ingredients.add(ingredient);
        // TODO implement database interaction
    }
}
