package com.example.happymeals.shoppinglist;

import com.example.happymeals.ingredient.Ingredient;

import java.util.ArrayList;

/**
 * This is a class that defines a Shopping List Item.
 * @author sruduke
 */
public class ShoppingListItem {
    private Ingredient ingredient;
    private ArrayList<String> recipes;
    private Double amount;

    /**
     * This is a constructor for a ShoppingListItem
     * @param ingredient {@link Ingredient} the ingredient that is needed
     * @param recipes {@link ArrayList<String>} of recipe names this ingredient is needed for
     * @param amount {@link Double} of the ingredient that is needed
     */
    public ShoppingListItem(Ingredient ingredient, ArrayList<String> recipes, Double amount) {
        this.ingredient = ingredient;
        this.recipes = recipes;
        this.amount = amount;
    }

    /**
     * This returns the ingredient of the ShoppingListItem
     * @return {@link Ingredient}
     */
    public Ingredient getIngredient() {
        return ingredient;
    }

    /**
     * This sets the ingredient of the ShoppingListItem
     * @param ingredient {@link Ingredient}
     */
    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    /**
     * This returns the recipes that this ShoppingListItem is needed for
     * @return recipes {@link ArrayList<String>}
     */
    public ArrayList<String> getRecipes() {
        return recipes;
    }

    /**
     * This sets the recipes of the ShoppingListItem
     * @param recipes {@link ArrayList<String>}
     */
    public void setRecipes(ArrayList<String> recipes) {
        this.recipes = recipes;
    }

    /**
     * This returns the amount of the ShoppingListItem
     * @return amount {@link Double}
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * This sets the amount of the ShoppingListItem
     * @param amount {@link Double}
     */
    public void setAmount(Double amount) {
        this.amount = amount;
    }

}
