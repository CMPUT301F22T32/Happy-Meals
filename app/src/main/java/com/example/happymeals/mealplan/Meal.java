package com.example.happymeals.mealplan;

import com.example.happymeals.Constants;

import java.util.HashMap;

/**
 * A Meal object is composed of items; a meal can either have any number of Recipes, or any number
 * of Ingredients, associated with it. A Meal is what the user actually plans out when they make their
 * weekly Meal Plan. A meal has a type -- it can rather be a collection of recipes or ingredients.
 * A meal plan can be made, or yet to be made. A meal plan also contains a collection of items, with
 * a corresponding scale factor.
 * @author sruduke
 */
public class Meal {

    private Constants.COLLECTION_NAME type;
    private boolean made;

    // Maps the ( String ) name of item -> ( Double ) scale
    private HashMap<String, Double> items;

    /**
     * Empty constructor for a new Meal object.
     */
    public Meal() {
        this.made = false;
    }

    /**
     * This constructor for a Meal takes in the type of meal being created as well as the items associated.
     * @param type {@link Constants.COLLECTION_NAME} of items this Meal contains {@see Recipe} or {@see Ingredient}
     * @param items {@link HashMap<String, Double> items} is a HashMap which maps the name of the items for the
     *                                                   current Meal to their scale factor {@link Double}.
     */
    public Meal( Constants.COLLECTION_NAME type, HashMap<String, Double> items ) {
        this.type = type;
        this.made = false;
        this.items = items;
    }

    public Constants.COLLECTION_NAME getType() {
        return type;
    }

    public void setType( Constants.COLLECTION_NAME type ) {
        this.type = type;
    }

    public boolean isMade() {
        return made;
    }

    public void setMade( boolean made ) {
        this.made = made;
    }

    public HashMap<String, Double> getItems() {
        return items;
    }

    public void setItems( HashMap<String, Double> items ) {
        this.items = items;
    }

    public void setScale( String itemName, Double value  ) {
        items.put( itemName, value );
    }

}
