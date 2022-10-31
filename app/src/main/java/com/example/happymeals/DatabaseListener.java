package com.example.happymeals;

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
}
