package com.example.happymeals;

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
    private ArrayList< HashMap< String, DocumentReference > > ingredients;
    private ArrayList< String > instructions;
    private double prepTime;
    private double servings;

    /**
     * Empty Constructor, this is required for {@link FireStoreManager}
     */
    public Recipe(){}

    /**
     * Full constructor to create a recipe will all the provided attributes.
     * @param cookTime The {@link Double} representing the time it takes to cook the meal in hrs.
     * @param description The {@link String} field which will hold the description of the recipe.
     * @param ingredients The {@link HashMap} which holds all the ingredient references in
     *                    the form of {@link DocumentReference}'s.
     * @param instructions {@link ArrayList} holding all the instructions in order to complete the
     *                                      recipe. These are all {@link String} values.
     * @param prepTime {@link Double} the time to prep the recipe measured in hrs.
     * @param servings {@link Double} The servings that the meal makes with the ingredients
     *                               described.
     */
    public Recipe( double cookTime, String description,
                   ArrayList< HashMap< String, DocumentReference > > ingredients,
                   ArrayList< String > instructions,
                   double prepTime, double servings ) {
        this.cookTime = cookTime;
        this.description = description;
        // <todo> Need to add functionality to use only partial amounts of each ingredient.
        this.ingredients = ingredients;
        this.instructions = instructions;
        this.prepTime = prepTime;
        this.servings = servings;
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
     * Gets the list of ingredients needed to make the meal with the described servings.
     * @return {@link ArrayList} holding all the ingredient references. These are held
     * by {@link DocumentReference}s.
     */
    public ArrayList<HashMap<String, DocumentReference>> getIngredients() {
        return ingredients;
    }

    /**
     * Gets the list of instructions which are stored in the order of which they need to be
     * completed.
     * @return The {@link ArrayList} holding all the {@link String} values representing the
     * instructions to make the meal.
     */
    public ArrayList<String> getInstructions() {
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
