package com.example.happymeals.database;

import com.example.happymeals.recipe.Recipe;

import java.util.ArrayList;

/**
 * @author jeastgaa
 * @version 1.00.01
 * @see FireStoreManager
 * Interface used to subscribe a class to listen for the successfull database transaction.
 */
public interface DatabaseListener {

    /**
     * To be used when data has been successfully grabbed from the database.
     * @param data {@link DatabaseObject} which holds the returned class.
     */
    void onDataFetchSuccess( DatabaseObject data );

    /**
     * To be used to fetch a collection of Recipe objects that have been shared globally
     * with all users.
     * @param data The {@link DatabaseObject} that holds the recipe. This is a {@link DatabaseObject}
     *             in order to allow for future additions.
     */
    void onSharedDataFetchSuccess( Recipe data );

    /**
     * To be used when the Spinner values have been fetched from the database on request.
     * @param listOfSpinners The {@link ArrayList} which holds all the spinner from the DB.
     */
    <T> void onSpinnerFetchSuccess( T listOfSpinners );
}
