package com.example.happymeals;

import java.util.HashMap;

public class MealPlan extends DatabaseObject {

    private HashMap< String, Object > plans;

    public MealPlan() {
    }

    public HashMap<String, Object> getPlans() {
        return plans;
    }
}
