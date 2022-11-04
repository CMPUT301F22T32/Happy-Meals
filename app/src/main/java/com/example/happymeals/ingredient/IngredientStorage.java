package com.example.happymeals.ingredient;

import com.example.happymeals.Constants;
import com.example.happymeals.database.*;
import com.google.firebase.firestore.CollectionReference;

import java.util.ArrayList;
import java.util.Map;

/**
 * This is a class for storing a list of ingredients
 */
public class IngredientStorage implements DatabaseListener {
    private ArrayList<Ingredient> ingredients;
    private FireStoreManager fsm;
    private CollectionReference ingredientCollection;

    public IngredientStorage( FireStoreManager fsm ) {
        this.ingredients = new ArrayList< Ingredient >();
        this.fsm = fsm;
        this.ingredientCollection = fsm.getCollectionReferenceTo( Constants.COLLECTION_NAME.INGREDIENTS );
    }

    /**
     * This pulls the list of ingredients in the Firebase database and sets it to ingredients
     */
    public void storeIngredient( Ingredient ingredient ) {
        fsm.addData( ingredientCollection, ingredient );
    }

    public void updateIngredientsFromDatabase() {
        fsm.getAllFrom( ingredientCollection, this, new Ingredient() );
    }
    /**
     * This returns an ArrayList of the Ingredients
     * @return
     * Returns the ArrayList of Ingredients that the IngredientStorage has
     */
    public ArrayList< Ingredient > getIngredients() {
        return ingredients;
    }

    /**
     * This add and Ingredient to ingredients and adds the ingredient to the Firebase database
     * @param ingredient
     */
    public void addIngredient( Ingredient ingredient ) {
        ingredients.add( ingredient );
        fsm.addData(ingredientCollection, ingredient);
        // TODO implement database interaction
    }

    public void removeIngredient( Ingredient ingredient ) {
        fsm.deleteDocument(ingredientCollection, ingredient.getName());
    }

    public void updateIngredient( Ingredient ingredient ) {
        fsm.updateData(ingredientCollection, new DatabaseObject(ingredient.getName()));
    }

    @Override
    public void onDataFetchSuccess(DatabaseObject data) {

    }

    @Override
    public void onSpinnerFetchSuccess(Map<String, Object> data) {

    }
}
