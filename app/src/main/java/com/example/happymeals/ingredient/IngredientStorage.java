package com.example.happymeals.ingredient;

import android.util.Log;

import com.example.happymeals.Constants;
import com.example.happymeals.database.DatasetWatcher;
import com.example.happymeals.database.*;
import com.example.happymeals.recipe.Recipe;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * @author jeastgaa
 * @version 1.04.01
 * This class holds all the {@link Ingredient}s from the database. It is a singleton class which can only
 * be instantiated once. This should be done when the application is loaded up.
 * On instantiation the storage will be populated through a database call.
 * All database interactions for a {@link Ingredient} should be preformed through this class.
 */
public class IngredientStorage implements DatabaseListener {

    private static IngredientStorage instance;

    private DatasetWatcher listeningActivity;
    private CollectionReference ingredientCollection;

    private ArrayList<Ingredient> ingredients;
    private HashMap< String, ArrayList< String > > spinnerMap;
    private FireStoreManager fsm;

    /**
     * Default constructor which will get the {@link FireStoreManager}'s instance reference and
     * populate all the necessary variables/data to function.
     */
    private IngredientStorage() {
        this.ingredients = new ArrayList<>();
        this.spinnerMap = new HashMap<>();
        this.listeningActivity = null;
        this.fsm = FireStoreManager.getInstance();
        fsm.getAllSpinners( this );
        this.ingredientCollection = fsm.getCollectionReferenceTo( Constants.COLLECTION_NAME.INGREDIENTS );
        updateIngredientsFromDatabase();
    }

    public static void clearInstance() {
        instance = null;
    }

    /**
     * Allows outside classes to access this instantiated class. If this class has not been
     * instantiated yet then it will be done here.
     * @return The created instance of {@link IngredientStorage}
     */
    public static IngredientStorage getInstance() {
        if( instance == null ){
            instance = new IngredientStorage();
        }
        return instance;
    }

    /**
     * Adds a {@link String} as a spinner to the list of spinners as well as updates the
     * database with the addition.
     * @param choice {@link Constants.StoredSpinnerChoices} which will define the key to the
     *                                                     spinner map.
     * @param spinner {@link String} the string being added to the Spinner DB.
     */
    public void addSpinner( Constants.StoredSpinnerChoices choice, String spinner ) {
        if( spinnerMap.get( choice.toString() ) == null ) {
            spinnerMap.put( choice.toString(), new ArrayList<String >());
        }

        spinnerMap.get( choice.toString() ).add( spinner );
        fsm.storeSpinners( spinnerMap );
    }

    /**
     * Converts a {@link ArrayList} of {@link Ingredient} to a {@link HashMap} which will can
     * be stored in the database via {@link com.example.happymeals.recipe.Recipe}'s.
     * @param ingredientList The {@link ArrayList} which is being converted.
     * @return The database friendly {@link HashMap} which can be appended to a
     * {@link com.example.happymeals.recipe.Recipe} object.
     */
    public HashMap< String, DocumentReference > convertListToHashMap(ArrayList< Ingredient > ingredientList ) {
        HashMap< String, DocumentReference > mapToReturn = new HashMap<>();

        for( Ingredient ingredient : ingredientList ) {
            mapToReturn.put( ingredient.getName(), fsm.getDocReferenceTo(ingredientCollection, ingredient ) );
        }

        return mapToReturn;
    }

    /**
     * Removes a {@link String} from the {@link ArrayList} holding all the spinners as well as
     * updates the DB with the removal.
     * @param choice {@link Constants.StoredSpinnerChoices} that defines where the spinner
     *                                                     is being removed from.
     * @param i The {@link Integer} of the index where the requested {@link String} is to be
     *          removed from.
     */
    public void removeSpinner( Constants.StoredSpinnerChoices choice, int i ) {
        spinnerMap.get( choice.toString() ).remove( i );
        fsm.storeSpinners( spinnerMap );
    }

    public String getCurrentUser() {
        return FirebaseAuthenticationHandler.getFireAuth().authenticate.getCurrentUser().getDisplayName();
    }
    /**
     * Standard getter for spinners.
     * @return {@link Constants.StoredSpinnerChoices} which defines the key to fetch the strings.
     */
    public ArrayList<String> getSpinners( Constants.StoredSpinnerChoices choice ) {
        ArrayList< String > temp = new ArrayList<>();
        if( spinnerMap.get( choice.toString()) != null ) {
            temp.addAll( spinnerMap.get(choice.toString() ));
        }
        return temp;
    }

    /**
     * This will ask the database to update the ingredient provided with a count of:
     * current - provided count.
     * @param ingredient The {@link Ingredient} which is losing count.
     * @param count The {@link Double} representing the count to take away from the
     *              provided {@link Ingredient}.
     */
    public void requestConsumptionOfIngredient( Ingredient ingredient, Double count ) {
        ingredient.setAmount( ingredient.getAmount() - count );
        updateIngredient( ingredient );
    }

    /**
     * Links a {@link DatasetWatcher} instance to this class so data fetching can return results.
     * This should be set/called any time a new class is watching the storage contents and need
     * to be upddated on a change.
     * @param listener The {@link DatasetWatcher} implementation responsible for dealing with
     *                 a change in the dataset.
     */
    public void setListeningActivity( DatasetWatcher listener ) {
        this.listeningActivity = listener;
    }

    /**
     * This will send a signal to the class which is currently linked to the storage
     * that the dataset has changed.
     */
    public void updateStorage() {
        if (listeningActivity != null) {
            listeningActivity.signalChangeToAdapter();
        } else {
            Log.d( "Ingredient Storage Error: ", "No listening activity was defined"
            + "Cannot update adapter.", null );
        }
    }

    /**
     * This will grab all the ingredient values stored in the database.
     * This classes OnDataFetchSuccess method will handle the response on a successful data fetch.
     */
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
    }

    /**
     * This will return a specific {@link Ingredient} given the name.
     * @param ingredientName The {@link String} which holds the name of the requested
     * {@link Ingredient}.
     * @return The {@link Ingredient} object.
     */
    public Ingredient getIngredient( String ingredientName ) {
        for( Ingredient ingredient : ingredients ) {
            if ( ingredient.getName().equals( ingredientName ) ) {
                return ingredient;
            }
        }
        return null;
    }

    /**
     * This will remove the provided {@link Ingredient} from the database and this classes
     * storage {@link ArrayList}.
     * @param ingredient The {@link Ingredient} that is being removed form storage.
     */
    public void removeIngredient( Ingredient ingredient ) {
        ingredients.remove( ingredient );
        updateStorage();
        fsm.deleteDocument( ingredientCollection, ingredient );
    }

    /**
     * Given an {@link Ingredient} this will go through all the stored ingredients and upon
     * matching the name of the passed {@link Ingredient} it will replace that object with
     * the given one. If it cannot be found then a Log message will be produced.
     * @param ingredient The {@link Ingredient} which is being updated in the storage.
     */
    public void updateIngredient( Ingredient ingredient ) {
        for( Ingredient i : ingredients ) {
            if( i.getName().equals(ingredient.getName() ) ){
                i = ingredient;
                updateStorage();
                fsm.updateData( ingredientCollection, ingredient );
                return;
            }
        }
        Log.d("Ingredient Storage:", "An ingredient update was requested on:\n"
        + ingredient.getName() + ", but no stored ingredient could be found", null );

    }

    /**
     * Once the database has returned a successfull data retreival this method will be called.
     * On the call this will look for the {@link Ingredient} which is already stored to updated.
     * If no such {@link Ingredient} can be found then the  passed in object will be added to the
     * {@link ArrayList} of stored {@link Ingredient}'s.
     * @param data {@link DatabaseObject} which holds the returned class.
     */
    @Override
    public void onDataFetchSuccess(DatabaseObject data) {
        Ingredient ingredient = ( Ingredient ) data;
        Boolean replace = Boolean.FALSE;
        // Loop through the list of ingredients and see if we are adding a new one
        // or updating a pre-existing one.
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
    public void onSharedDataFetchSuccess(Recipe data) {

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> void onSpinnerFetchSuccess( T mapOfSpinners ) {
        if( mapOfSpinners != null ) {
            if( mapOfSpinners.getClass() == HashMap.class ) {
                System.out.println( mapOfSpinners);
                spinnerMap = (HashMap<String, ArrayList<String>>) mapOfSpinners;
            }
        }
    }

    public Ingredient getIngredientByIndex(int i){
        return ingredients.get(i);
    }
}
