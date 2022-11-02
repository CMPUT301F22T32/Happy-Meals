package com.example.happymeals;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class IngredientTest {

    @Test
    void testSetters() {
        Ingredient emptyIngredient = new Ingredient();

        emptyIngredient.setDescription("Apple");
        assertEquals("Apple", emptyIngredient.getDescription());

        emptyIngredient.setBestBeforeDate("01-11-2022");
        assertEquals("01-11-2022", emptyIngredient.getBestBeforeDate());

        emptyIngredient.setLocation(Constants.Location.FREEZER);
        assertEquals(Constants.Location.FREEZER, emptyIngredient.getLocation());

        emptyIngredient.setAmount(5);
        assertEquals(new Integer(5), emptyIngredient.getAmount());

        emptyIngredient.setUnit(Constants.AmountUnit.COUNT);
        assertEquals(Constants.AmountUnit.COUNT, emptyIngredient.getUnit());

        emptyIngredient.setCategory(Constants.IngredientCategory.FRUIT);
        assertEquals(Constants.IngredientCategory.FRUIT, emptyIngredient.getCategory());
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
