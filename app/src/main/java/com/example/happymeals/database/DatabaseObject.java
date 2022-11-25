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
    protected String creator;
    protected String id;

    /**
     * Empty constructor required for Firestore construction.
     */
    public DatabaseObject() {}

    /**
     * Common constructor providing the minimal amount of detail for document storage in database.
     * @param name The {@link String} which will define the name of object document.
     */
    public DatabaseObject(String name, String creator ) {
        this.name = name;
        this.creator = creator;
        this.id = creator + "_" + name;
    }

    /**
     * Getter for the name attribute.
     * @return {@link String} of the name.
     */
    public String getName() {
        return this.name;
    }

    public String getCreator() {
        return creator;
    }

    public String getId() {
        return id;
    }
}
