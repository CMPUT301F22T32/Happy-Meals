package com.example.happymeals.recipe;



// recipe storage class

import com.example.happymeals.ingredient.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class RecipeStorage {

    private ArrayList<Recipe> recipes;

    public RecipeStorage() {
        recipes = new ArrayList<Recipe>();
    }

    public void addRecipe(Recipe recipe) {
        recipes.add(recipe);
    }

    public ArrayList<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(ArrayList<Recipe> recipes) {
        this.recipes = recipes;
    }

    public void removeRecipe(Recipe recipe) {
        recipes.remove(recipe);
    }

    public List<Recipe> getRecipesByType(String type) {
        List<Recipe> result = new ArrayList<Recipe>();
        for (Recipe recipe : recipes) {
            if (recipe.getType().equals(type)) {
                result.add(recipe);
            }
        }
        return result;
    }

    public List<Recipe> getRecipesByIngredient(Ingredient ingredient) {
        List<Recipe> result = new ArrayList<Recipe>();
        for (Recipe recipe : recipes) {
            if (recipe.getIngredients().contains(ingredient)) {
                result.add(recipe);
            }
        }
        return result;
    }

    public List<Recipe> getRecipesByDescription(String description) {
        List<Recipe> result = new ArrayList<Recipe>();
        for (Recipe recipe : recipes) {
            if (recipe.getDescription().equals(description)) {
                result.add(recipe);
            }
        }
        return result;
    }

    public List<Recipe> getRecipesByCookingTime(int cookTime) {
        List<Recipe> result = new ArrayList<Recipe>();
        for (Recipe recipe : recipes) {
            if (recipe.getCookTime() == cookTime) {
                result.add(recipe);
            }
        }
        return result;
    }

}