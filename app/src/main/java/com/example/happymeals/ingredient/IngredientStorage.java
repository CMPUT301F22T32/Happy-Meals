package com.example.happymeals.ingredient;

import com.example.happymeals.Constants;
import com.example.happymeals.DatasetWatcher;
import com.example.happymeals.database.*;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * This is a class for storing a list of ingredients
 */
public class IngredientStorage implements DatabaseListener {

    private static IngredientStorage instance;

    private DatasetWatcher listeningActivity;

    private ArrayList<Ingredient> ingredients;
    private FireStoreManager fsm;
    private CollectionReference ingredientCollection;

    private IngredientStorage() {
        this.ingredients = new ArrayList< Ingredient >();
        this.fsm = FireStoreManager.getInstance();
        this.ingredientCollection = fsm.getCollectionReferenceTo( Constants.COLLECTION_NAME.INGREDIENTS );
        fsm.getAllFrom( ingredientCollection, this, new Ingredient() );
        this.listeningActivity = null;
    }

    public static IngredientStorage getInstance() {
        if( instance == null ){
            instance = new IngredientStorage();
        }
        return instance;
    }

    public HashMap< String, DocumentReference > convertListToHashMap(ArrayList< Ingredient > iList ) {
        HashMap< String, DocumentReference > mapToReturn = new HashMap<>();
        for( Ingredient i : iList ) {
            mapToReturn.put( i.getName(), fsm.getDocReferenceTo(ingredientCollection, i ) );
        }
        return mapToReturn;
    }

    public void setListeningActivity( DatasetWatcher context ) {
        this.listeningActivity = context;
    }

    public void updateStorage() {
        if (listeningActivity != null) {
            listeningActivity.signalChangeToAdapter();
        }
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
        updateStorage();
        fsm.addData(ingredientCollection, ingredient);
        // TODO implement database interaction
    }

    public Ingredient getIngredient( String ingredientName ) {
        for( Ingredient ingredient : ingredients ) {
            if (ingredient.getName().equals(ingredientName)) {
                return ingredient;
            }
        }
        return null;
    }

    public void removeIngredient( Ingredient ingredient ) {
        ingredients.remove( ingredient );
        updateStorage();
        fsm.deleteDocument(ingredientCollection, ingredient);
    }

    public void updateIngredient( Ingredient ingredient ) {
        for( Ingredient i : ingredients ) {
            if( i.getName().equals(ingredient.getName() ) ){
                i = ingredient;
            }
        }
        updateStorage();
        fsm.updateData( ingredientCollection, ingredient );
    }

    @Override
    public void onDataFetchSuccess(DatabaseObject data) {
        Ingredient ingredient = (Ingredient) data;
        Boolean replace = Boolean.FALSE;
        for( Ingredient ing : this.ingredients ) {
            if( ing.getName() == ingredient.getName() ){
                ing = ingredient;
                replace = Boolean.TRUE;
                break;
            }
        }
        if( !replace ){
            ingredients.add( ingredient );
        }
        updateStorage();
    }

    @Override
    public void onSpinnerFetchSuccess(Map<String, Object> data) {

    }
}
