package com.example.happymeals;

public class Constants {

    public static String SPINNER = "SPINNERS";

    public enum StoredSpinnerChoices {
        LOCATION,
        INGREDIENT_CATEGORY,
        AMOUNT_UNIT
    }

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
        MEAL_PLANS
    }

}
