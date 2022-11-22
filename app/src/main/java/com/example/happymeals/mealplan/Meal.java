package com.example.happymeals.mealplan;

import com.example.happymeals.Constants;
import com.example.happymeals.database.DatabaseObject;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;

public class Meal extends DatabaseObject {

    private String name;
    private ArrayList<DocumentReference> items;
    private Constants.COLLECTION_NAME type;
    private Boolean made;

    public Meal() {
    }

    public Meal(Boolean made, Constants.COLLECTION_NAME type, ArrayList<DocumentReference> items) {
        this.items = items;
        this.type = type;
        this.made = made;
    }

    public void setMade(Boolean made) {
        this.made = made;
    }

    public Boolean getMade() {
        return made;
    }

    public void setItems(ArrayList<DocumentReference> items) {
        this.items = items;
    }

    public ArrayList<DocumentReference> getItems() {
        return items;
    }

    public void setType(Constants.COLLECTION_NAME type) {
        this.type = type;
    }

    public Constants.COLLECTION_NAME getType() {
        return type;
    }

    public String getDisplayName() {
        StringBuilder displayString = new StringBuilder();
        displayString.setLength(32);
        for (DocumentReference doc : items) {
            String[] tokens = doc.toString().split("/");
            int length = tokens.length;
            String name = tokens[length - 1];
            displayString.append(name);
        }
        return displayString.toString() + " ...";
    }

}
