package com.example.happymeals;

import com.google.firebase.firestore.CollectionReference;

import java.util.ArrayList;

import io.grpc.ChannelLogger;

/**
 * This is a class for storing a list of ingredients
 */
public class IngredientStorage {
    private ArrayList<Ingredient> ingredients;
    private FireStoreManager fsm;
    private CollectionReference ingredientCollection;

    public IngredientStorage( FireStoreManager fsm ) {
        this.ingredients = new ArrayList<Ingredient>();
        this.fsm = fsm;
//        this.ingredientCollection = fsm.get
    }

    /**
     * This pulls the list of ingredients in the Firebase database and sets it to ingredients
     */
    public void storeIngredient( Ingredient ingredient ) {
//        fsm.addData();
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
