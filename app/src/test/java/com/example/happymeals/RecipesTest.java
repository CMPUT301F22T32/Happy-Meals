package com.example.happymeals;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class RecipesTest {

    @Test
    void testSetters() {
        Recipe recipe = new Recipe();

        recipe.setCookTime(2);
        assertEquals(2, recipe.getCookTime());

        recipe.setDescription("Chicken Alfredo");
        assertEquals("Chicken Alfredo", recipe.getDescription());

        ArrayList<String> comments = new ArrayList<String>();
        comments.add("Too much salt");
        comments.add("Delicious!");

        recipe.setComments(comments);
        assertEquals(comments, recipe.getComments());

        String comment = "Not enough garlic";
        recipe.addComments(comment);
        comments.add(comment);
        assertEquals(comments, recipe.getComments());

        ArrayList<>

        recipe.setIngredients();

    }

    @Test
    void testGetters() {
        Ingredient ingredient = new Ingredient("Ground Beef", "extra lean", "02-11-2022", Constants.Location.FREEZER, 500, Constants.AmountUnit.MG, Constants.IngredientCategory.MEAT);

        assertEquals("Ground Beef", ingredient.getName());
        assertEquals("extra lean", ingredient.getDescription());
        assertEquals("02-11-2022", ingredient.getBestBeforeDate());
        assertEquals(Constants.Location.FREEZER, ingredient.getLocation());
        assertEquals(new Integer(500), ingredient.getAmount());
        assertEquals(Constants.AmountUnit.MG, ingredient.getUnit());
        assertEquals(Constants.IngredientCategory.MEAT, ingredient.getCategory());
    }



}
