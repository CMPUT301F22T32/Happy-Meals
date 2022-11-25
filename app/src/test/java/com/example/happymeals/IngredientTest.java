
package com.example.happymeals;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.happymeals.ingredient.Ingredient;

import org.junit.jupiter.api.Test;

import java.util.Date;

public class IngredientTest {

    @Test
    void testSetters() {
        // this tests the ingredient setter methods using an empty ingredeint
        Ingredient emptyIngredient = new Ingredient();

        emptyIngredient.setDescription("Apple");
        assertEquals("Apple", emptyIngredient.getDescription());

        emptyIngredient.setBestBeforeDate(new Date(2022, 01, 01));
        assertEquals(new Date(2022, 01, 01), emptyIngredient.getBestBeforeDate());

        emptyIngredient.setLocation(Constants.Location.FREEZER);
        assertEquals(Constants.Location.FREEZER, emptyIngredient.getLocation());

        emptyIngredient.setAmount(5.0);
        assertEquals(new Integer(5), emptyIngredient.getAmount());

        emptyIngredient.setUnit(Constants.AmountUnit.COUNT);
        assertEquals(Constants.AmountUnit.COUNT, emptyIngredient.getUnit());

        emptyIngredient.setCategory(Constants.IngredientCategory.FRUIT);
        assertEquals(Constants.IngredientCategory.FRUIT, emptyIngredient.getCategory());
    }

    @Test
    void testGetters() {
        // this will test the ingredeint getter methods using an existing ingredeint
        Ingredient ingredient = new Ingredient("Ground Beef", "extra lean",
                new Date(2022, 01, 01), Constants.Location.FREEZER, 500,
                Constants.AmountUnit.MG, Constants.IngredientCategory.MEAT);

        assertEquals("Ground Beef", ingredient.getName());
        assertEquals("extra lean", ingredient.getDescription());
        assertEquals(new Date(2022, 01, 01), ingredient.getBestBeforeDate());
        assertEquals(Constants.Location.FREEZER, ingredient.getLocation());
        assertEquals(new Integer(500), ingredient.getAmount());
        assertEquals(Constants.AmountUnit.MG, ingredient.getUnit());
        assertEquals(Constants.IngredientCategory.MEAT, ingredient.getCategory());
    }



}