package com.example.happymeals;


import java.io.Serializable;
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
public class Recipe extends DatabaseObject implements Serializable {

    private double cookTime;
    private String description;
    private ArrayList< String > comments;
    private ArrayList< HashMap< String, String > > ingredients;
    private ArrayList< String > instructions;
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
     * @param ingredients The {@link HashMap} which holds all the ingredient references in
     *                    the form of {@link String}'s.
     * @param instructions {@link ArrayList} holding all the instructions in order to complete the
     *                                      recipe. These are all {@link String} values.
     * @param prepTime {@link Double} the time to prep the recipe measured in hrs.
     * @param servings {@link Double} The servings that the meal makes with the ingredients
     *                               described.
     */
    public Recipe( String name, double cookTime, String description, ArrayList< String > comments,
                   ArrayList< HashMap< String, String > > ingredients,
                   ArrayList< String > instructions,
                   double prepTime, double servings ) {
        super(name);
        this.cookTime = cookTime;
        this.description = description;
        this.comments = comments;
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
     * @return {@link ArrayList} holding all the ingredient references. These are held
     * by {@link String}s.
     */
    public ArrayList<HashMap<String, String>> getIngredients() {
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
     * Goes through all the instruction strings and forms them into a single string.
     * This is intended to be used by activities such that the instructions can be placed
     * as a string.
     * @return {@link String} of all the combined comments from instructions {@link ArrayList}.
     */
    public String getInstructionsAsString() {
        int count = 1;
        String instructionString = "";
        for( String str : instructions){
            instructionString += count + ": " + str + "\n";
            count++;
        }
        return instructionString;
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
