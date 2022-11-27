package com.example.happymeals.mealplan;

import com.example.happymeals.Constants;
import com.example.happymeals.database.DatabaseObject;
import com.example.happymeals.database.FireStoreManager;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.HashMap;

public class Meal {
    private final Constants.COLLECTION_NAME ingredient = Constants.COLLECTION_NAME.INGREDIENTS;
    private final Constants.COLLECTION_NAME recipe = Constants.COLLECTION_NAME.RECIPES;

    private Constants.COLLECTION_NAME type;
    private boolean made;
    // Maps the (String) name of item -> (Double) scale
    private HashMap<String, Double> items;

    public Meal() {
        this.made = false;
    }

    public Meal(Constants.COLLECTION_NAME type, HashMap<String, Double> items) {

        this.type = type;
        this.made = false;
        this.items = items;

    }

    public Constants.COLLECTION_NAME getType() {
        return type;
    }

    public void setType(Constants.COLLECTION_NAME type) {
        this.type = type;
    }

    public boolean isMade() {
        return made;
    }

    public void setMade(boolean made) {
        this.made = made;
    }

    public HashMap<String, Double> getItems() {
        return items;
    }

    public void setItems(HashMap<String, Double> items) {
        this.items = items;
    }

    public void setScale( String itemName, Double value ) {
        items.put(itemName, value);
    }

}
