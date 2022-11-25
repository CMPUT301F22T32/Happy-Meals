package com.example.happymeals.shoppinglist;

import com.example.happymeals.ingredient.Ingredient;

import java.util.ArrayList;

public class ShoppingListItem {
    private Ingredient ingredient;
    private ArrayList<String> recipes;
    private Double amount;

    public ShoppingListItem(Ingredient ingredient, ArrayList<String> recipes, Double amount) {
        this.ingredient = ingredient;
        this.recipes = recipes;
        this.amount = amount;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public void setIngredient(Ingredient ingredient) {
        this.ingredient = ingredient;
    }

    public ArrayList<String> getRecipes() {
        return recipes;
    }

    public void setRecipes(ArrayList<String> recipes) {
        this.recipes = recipes;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

}
