package com.example.happymeals;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import java.util.Date;

public class IngredientTest {

    @Test
    void testSetters() {
        Ingredient emptyIngredient = new Ingredient();

        emptyIngredient.setDescription("Apple");
        assertEquals("Apple", emptyIngredient.getDescription());

        Date date = new Date(2022, 03, 05);
        emptyIngredient.setBestBeforeDate(date);
        assertEquals(date, emptyIngredient.getBestBeforeDate());

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
        Date d = new Date(2022, 11, 01);
        Ingredient ingredient = new Ingredient("Ground Beef", "extra lean", d, Constants.Location.FREEZER, 500, Constants.AmountUnit.MG, Constants.IngredientCategory.MEAT);

        assertEquals("Ground Beef", ingredient.getName());
        assertEquals("extra lean", ingredient.getDescription());
        assertEquals(d, ingredient.getBestBeforeDate());
        assertEquals(Constants.Location.FREEZER, ingredient.getLocation());
        assertEquals(new Integer(500), ingredient.getAmount());
        assertEquals(Constants.AmountUnit.MG, ingredient.getUnit());
        assertEquals(Constants.IngredientCategory.MEAT, ingredient.getCategory());
    }



}
