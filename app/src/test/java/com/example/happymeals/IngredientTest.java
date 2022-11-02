package com.example.happymeals;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class IngredientTest {

    @Test
    void testSetters() {
        Ingredient emptyIngredient = new Ingredient();

        emptyIngredient.setDescription("Apple");
        assertEquals(emptyIngredient.getDescription(), "Apple");

        emptyIngredient.setBestBeforeDate("01-11-2022");
        assertEquals(emptyIngredient.getBestBeforeDate(), "01-11-2022");

        emptyIngredient.setLocation(Constants.Location.FREEZER);
        assertEquals(emptyIngredient.getLocation(), Constants.Location.FREEZER);

        emptyIngredient.setAmount(5);
        assertEquals(emptyIngredient.getAmount(), new Integer(5));

        emptyIngredient.setUnit(Constants.AmountUnit.COUNT);
        assertEquals(emptyIngredient.getUnit(), Constants.AmountUnit.COUNT);

        emptyIngredient.setCategory(Constants.IngredientCategory.FRUIT);
        assertEquals(emptyIngredient.getCategory(), Constants.IngredientCategory.FRUIT);
    }

    @Test
    void testGetters() {
        Ingredient ingredient = new Ingredient("Ground Beef", "extra lean", "02-11-2022", Constants.Location.FREEZER, 500, Constants.AmountUnit.MG, Constants.IngredientCategory.MEAT);

        assertEquals(ingredient.getName(), "Ground Beef");
        assertEquals(ingredient.getDescription(), "extra lean");
        assertEquals(ingredient.getBestBeforeDate(), "02-11-2022");
        assertEquals(ingredient.getLocation(), Constants.Location.FREEZER);
        assertEquals(ingredient.getAmount(), new Integer(500));
        assertEquals(ingredient.getUnit(), Constants.AmountUnit.MG);
        assertEquals(ingredient.getCategory(), Constants.IngredientCategory.MEAT);
    }



}
