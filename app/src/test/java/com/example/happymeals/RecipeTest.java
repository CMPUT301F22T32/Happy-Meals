
package com.example.happymeals;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.happymeals.ingredient.Ingredient;
import com.example.happymeals.recipe.Recipe;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Date;

public class RecipeTest {


    @Test
    void gettersTest() {
        // making a recipe
        ArrayList<String> comments = new ArrayList<String>();
        comments.add("Very Good");
        comments.add("Try with ground turkey next time");
        String strComments = "1: Very Good\n2: Try with ground turkey next time\n";
        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
        ingredients.add(new Ingredient("Lettuce", "Crisp romaine lettuce",
                new Date(2022, 01, 11), Constants.Location.FRIDGE, 1,
                Constants.AmountUnit.COUNT, Constants.IngredientCategory.FRUIT));
        ingredients.add(new Ingredient("Ground beef", "Extra lean ground beef",
                new Date(2023, 01, 01), Constants.Location.FREEZER, 500,
                Constants.AmountUnit.MG, Constants.IngredientCategory.MEAT));
        ArrayList instructions = new ArrayList<String>();
        instructions.add("Cook beef in a hot pan");
        instructions.add("Cut lettuce");
        String strInstructions = "1: Cook beef in a hot pan\n2: Cut lettuce\n";

        Recipe recipe = new Recipe("Tacos", 2, "The best tacos ever",
                comments, ingredients, instructions, 15, 6);

        // Test getters
        assertEquals("Tacos", recipe.getName());
        assertEquals(2, recipe.getCookTime());
        assertEquals("The best tacos ever", recipe.getDescription());
        assertEquals(comments, recipe.getComments());
        assertEquals(strComments, recipe.getCommentsAsString());
        assertEquals(ingredients, recipe.getIngredients());
        assertEquals(instructions, recipe.getInstructions());
        assertEquals(strInstructions, recipe.getInstructionsAsString());
        assertEquals(15, recipe.getPrepTime());
        assertEquals(6, recipe.getServings());

    }



}
