package com.example.happymeals.database;

/**
 * @author jeastgaa
 * @version 1.00.01
 * @see FireStoreManager
 * Interface used to subscribe a class to listen for the successfull database transactions.
 * This is different from {@link DatabaseListener} in that this interface is user so that
 * an array adapter can be updated.
 */
public interface DatasetWatcher {

    /**
     * Used so that the classes array adapter can be updated from change to list.
     */
    void signalChangeToAdapter();
}
