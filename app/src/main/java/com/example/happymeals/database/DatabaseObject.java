package com.example.happymeals.database;

import java.io.Serializable;

/**
 * @author jeastgaa
 * @version 1.00.01
 * Class to be used as parent object for any objects being input into the Firestore Database
 * through {@link FireStoreManager}
 */
public class DatabaseObject implements Serializable {

    protected String name;

    /**
     * Empty constructor required for Firestore construction.
     */
    public DatabaseObject() {}

    /**
     * Common constructor providing the minimal amount of detail for document storage in database.
     * @param name The {@link String} which will define the name of object document.
     */
    public DatabaseObject(String name ) {
        this.name = name;
    }

    /**
     * Getter for the name attribute.
     * @return {@link String} of the name.
     */
    public String getName() {
        return this.name;
    }
}
