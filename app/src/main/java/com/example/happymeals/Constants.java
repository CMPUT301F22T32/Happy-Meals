package com.example.happymeals;

import java.util.ArrayList;
import java.util.Arrays;

public class Constants {

    public static String SPINNER = "SPINNERS";
    public static String SPINNER_ING_DOC = "IngredientSpinners";
    public static String AMOUNT_SPINNER = "AmountSpinner";
    public static String LOCATION_SPINNER = "LocationSpinner";
    public static String CATEGORY_SPINNER = "CategorySpinner";
    public static String LOCAL_USERS = "localUsers";

    public enum StoredSpinnerChoices {
        LOCATION,
        INGREDIENT_CATEGORY,
        AMOUNT_UNIT
    }

    public static ArrayList< String > DefaultLocationSpinners = new ArrayList<>(
            Arrays.asList("Pantry", "Fridge", "Freezer")
    );

    public static ArrayList< String > DefaultIngredientCategorySpiners = new ArrayList<>(
            Arrays.asList("Dairy", "Meat", "Fruit", "Vegetable", "Snack", "Bread")
    );

    public static ArrayList< String > DefaultAmountUnitSpinners = new ArrayList<>(
            Arrays.asList("Count", "mg", "mL", "kg", "g", "oz")
    );

    public enum Location {
        PANTRY, FRIDGE, FREEZER
    }

    public enum IngredientCategory {
        DAIRY, MEAT, FRUIT, VEGETABLE, SNACK
    }

    public enum AmountUnit {
        COUNT, MG, mL
    }

    public enum COLLECTION_NAME {
        INGREDIENTS,
        RECIPES,
        MEAL_PLANS,
        GLOBAL_USERS
    }

    public enum DAY_OF_WEEK {
        SUNDAY,
        MONDAY,
        TUESDAY,
        WEDNESDAY,
        THURSDAY,
        FRIDAY,
        SATURDAY
    }

    public enum MEAL_OF_DAY {
        BREAKFAST,
        LUNCH,
        DINNER
    }

}
