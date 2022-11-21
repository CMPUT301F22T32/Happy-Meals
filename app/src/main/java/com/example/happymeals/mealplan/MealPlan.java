package com.example.happymeals.mealplan;

import android.provider.ContactsContract;

import com.example.happymeals.database.DatabaseObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class MealPlan extends DatabaseObject {
    private static String BREAKFAST = "breakfast";
    private static String LUNCH = "lunch";
    private static String DINNER = "dinner";

    private Date startDate;
    private Date endDate;

    private HashMap<String, ArrayList<DatabaseObject>> selectedItems;

    public MealPlan( Date startDate,
                    Date endDate,
                    ArrayList<DatabaseObject> breakfastItems,
                    ArrayList<DatabaseObject> lunchItems,
                    ArrayList<DatabaseObject> dinnerItems ) {

        this.startDate = startDate;
        this.endDate = endDate;

        selectedItems = new HashMap<>();
        this.setBreakfastItems(breakfastItems);
        this.setLunchItems(lunchItems);
        this.setDinnerItems(dinnerItems);
    }

    private void generateMealPlan() {
        //TODO assign ingredients/recipes to days
    }

    public ArrayList<DatabaseObject> getBreakfastItems(ArrayList<DatabaseObject> items) {
        return selectedItems.get(BREAKFAST);
    }

    public ArrayList<DatabaseObject> getLunchItems(ArrayList<DatabaseObject> items) {
        return selectedItems.get(LUNCH);
    }

    public ArrayList<DatabaseObject> getDinnerItems(ArrayList<DatabaseObject> items) {
        return selectedItems.get(DINNER);
    }

    public void setBreakfastItems(ArrayList<DatabaseObject> items) {
        selectedItems.put(BREAKFAST, items);
    }

    public void setLunchItems(ArrayList<DatabaseObject> items) {
        selectedItems.put(LUNCH, items);
    }

    public void setDinnerItems(ArrayList<DatabaseObject> items) {
        selectedItems.put(DINNER, items);
    }
}
