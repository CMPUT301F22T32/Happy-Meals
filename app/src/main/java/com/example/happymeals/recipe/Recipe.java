package com.example.happymeals.recipe;

import com.example.happymeals.database.*;
import com.google.firebase.firestore.DocumentReference;


import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author jeastgaa
 * @version 1.00.01
 * @see DatabaseObject The parent object for this class which allows it to be stored inside the
 * firebase databse using the {@link FireStoreManager}
 * This class represents recipes that can be loaded inside the application and shown to the
 * end user.
 */
public class Recipe extends DatabaseObject {

    private double cookTime;
    private String description;
    private ArrayList< String > comments;
    private HashMap< String, HashMap< String, Object > > ingredients;
    private String instructions;
    private double prepTime;
    private double servings;

    /**
     * Empty Constructor, this is required for {@link FireStoreManager}
     */
    public Recipe(){}

    /**
     * Full constructor to create a recipe will all the provided attributes.
     * @param name The {@link String} representing the name of the recipe
     * @param cookTime The {@link Double} representing the time it takes to cook the meal in hrs.
     * @param description The {@link String} field which will hold the description of the recipe.
     * @param comments The {@link ArrayList} which holds an array of {@link String}s which hold
     *                 comments the users might have added to the recipe.
     * @param ingredients The {@link HashMap} which holds all the ingredient used in the recipe.
     *                    Recipes keys are their names, and values are the references to the
     *                    documents they are stored in.
     * @param instructions {@link String} holding all the instructions in order to complete the
     *                                      recipe. These are all {@link String} values.
     * @param prepTime {@link Double} the time to prep the recipe measured in hrs.
     * @param servings {@link Double} The servings that the meal makes with the ingredients
     *                               described.
     */
    public Recipe( String name, double cookTime, String description, ArrayList< String > comments,
                   HashMap< String, HashMap< String, Object > > ingredients,
                   String instructions,
                   double prepTime, double servings ) {
        super(name);
        this.cookTime = cookTime;
        this.description = description;
        this.comments = comments;
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.prepTime = prepTime;
        this.servings = servings;
    }

    public Recipe(String description) {
        this.description = description;
    }

    /** Needed for RecipeStorageAdapter for MealPlan to properly function
     * @return
     */
    public String getName() {
        return name;
    }
    /**
     * Gets the cook time of the recipe.
     * @return {@link Double} cookTime measured in hrs.
     */
    public double getCookTime() {
        return cookTime;
    }

    /**
     * Gets the description of the recipe.
     * @return {@link String} object used to store the description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Gets the comments of the recipe.
     * @return {@link ArrayList} object used to store the comments.
     */
    public ArrayList< String > getComments() { return comments; }

    /**
     * Goes through all the comment strings and forms them into a single string.
     * This is intended to be used by activities such that the comments can be placed
     * as a string.
     * @return {@link String} of all the combined comments from comments {@link ArrayList}.
     */
    public String getCommentsAsString() {
        int count = 1;
        String instructionString = "";
        for( String str : comments){
            instructionString += count + ": " + str + "\n";
            count++;
        }
        return instructionString;
    }

    /**
     * Gets the list of ingredients needed to make the meal with the described servings.
     * @return {@link ArrayList} holding all the ingredients.
     */
    public HashMap<String, HashMap< String, Object > > getIngredients() {
        return ingredients;
    }

    /**
     * Gets the list of instructions which are stored in the order of which they need to be
     * completed.
     * @return The {@link String} the instructions of the meal.
     */
    public String getInstructions() {
        return instructions;
    }


    /**
     * Gets the stored prep time of the recipe. This is the total time - cook time.
     * @return THe {@link Double} representing the prepTime in hrs.
     */
    public double getPrepTime() {
        return prepTime;
    }

    /**
     * Gets the servings which the recipe will make with the given amount of each ingredient.
     * @return {@link Double} of the servings, can be fractional.
     */
    public double getServings() {
        return servings;
    }
}
