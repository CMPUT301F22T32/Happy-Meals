package com.example.happymeals;

import java.util.ArrayList;
import java.util.HashMap;

public class Recipe extends DatabaseObject {

    private double cookTime;
    private String description;
    private ArrayList< HashMap< String, Object > > ingredients;
    private ArrayList< String > instructions;
    private double prepTime;
    private double servings;

    public Recipe(){}

    public Recipe( double cookTime, String description,
                   ArrayList< HashMap< String, Object > > ingredients,
                   ArrayList< String > instructions,
                   double prepTime, double servings ) {
        this.cookTime = cookTime;
        this.description = description;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.prepTime = prepTime;
        this.servings = servings;
    }

    public double getCookTime() {
        return cookTime;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<HashMap<String, Object>> getIngredients() {
        return ingredients;
    }

    public ArrayList<String> getInstructions() {
        return instructions;
    }

    public double getPrepTime() {
        return prepTime;
    }

    public double getServings() {
        return servings;
    }
}
