
package com.example.happymeals;

import static org.junit.jupiter.api.Assertions.assertEquals;

import android.net.Uri;

import com.example.happymeals.ingredient.Ingredient;
import com.example.happymeals.ingredient.IngredientStorage;
import com.example.happymeals.recipe.Recipe;
import com.example.happymeals.recipe.RecipeStorage;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;

public class RecipeTest {

    Recipe mockRecipe() {
        HashMap< String, HashMap< String, Object > > testMap = new HashMap<>();
        HashMap< String, Object > countMap = new HashMap<>();

        countMap.put("count", 3 );
        countMap.put("reference", "randomRecipe");
        testMap.put("randomRecipe", countMap );

        ArrayList< String > commentsToAdd = new ArrayList<>();
        commentsToAdd.add("CommentOne");
        commentsToAdd.add("Comment2");
        return new Recipe(
                "Test Recipe",
                "TestCreator",
                2.0,
                "This is a nice recipe",
                commentsToAdd,
                testMap,
                "Cook it good",
                25.0,
                12.0,
                "TestPathForImage"
        );
    }

    @Test
    void getNameTest() {
        Recipe recipe = mockRecipe();
        String name = recipe.getName();
        assertEquals("Test Recipe", name );
    }

    @Test
    void getDescriptionTest() {
        Recipe recipe = mockRecipe();
        String description = recipe.getDescription();
        assertEquals("This is a nice recipe", description );
    }

    @Test
    void getCommentsTest() {
        Recipe recipe = mockRecipe();
        ArrayList< String > comments = recipe.getComments();
        assertEquals( new ArrayList<>(Arrays.asList("CommentOne","Comment2")), comments);
    }

    @Test
    void getCreatorTest() {
        Recipe recipe = mockRecipe();
        String creator = recipe.getCreator();
        assertEquals( "TestCreator", creator);
    }

    @Test
    void getServingsTest() {
        Recipe recipe = mockRecipe();
        Double servings = mockRecipe().getServings();
        assertEquals((Double) 12.0, servings);
    }

    @Test
    void getCookTimeTest() {
        Recipe recipe = mockRecipe();
        Double cookTime = recipe.getCookTime();
        assertEquals((Double) 2.0, cookTime);
    }

    @Test
    void getPrepTimeTest() {
        Recipe recipe = mockRecipe();
        Double prepTime = recipe.getPrepTime();
        assertEquals((Double) 25.0, prepTime);
    }

    @Test
    void getIngerdientCountTest() {
        Recipe recipe = mockRecipe();
    }
//    @Test
//    void gettersTest() {
//        // making a recipe
//        IngredientStorage ingredientStorage = IngredientStorage.getInstance();
//        RecipeStorage recipeStorage = RecipeStorage.getInstance();
//
//        ArrayList<String> comments = new ArrayList<String>();
//        comments.add("Very Good");
//        comments.add("Try with ground turkey next time");
//        String strComments = "1: Very Good\n2: Try with ground turkey next time\n";
//        ArrayList<Ingredient> ingredients = new ArrayList<Ingredient>();
//        ingredients.add(new Ingredient("Lettuce", "Crisp romaine lettuce",
//                new Date(2022, 01, 11), Constants.Location.FRIDGE, 1,
//                Constants.AmountUnit.COUNT, Constants.IngredientCategory.FRUIT));
//        ingredients.add(new Ingredient("Ground beef", "Extra lean ground beef",
//                new Date(2023, 01, 01), Constants.Location.FREEZER, 500,
//                Constants.AmountUnit.MG, Constants.IngredientCategory.MEAT));
//        ArrayList instructions = new ArrayList<String>();
//        instructions.add("Cook beef in a hot pan");
//        instructions.add("Cut lettuce");
//        String strInstructions = "1: Cook beef in a hot pan\n2: Cut lettuce\n";
//        HashMap< String, HashMap< String, Object > > ingredientMap = new HashMap<>();
//        for( Ingredient i : ingredients ) {
//            ingredientMap.put(i.getName(), new HashMap<>() );
//        }
//        Recipe recipe = new Recipe("Tacos", 2, "The best tacos ever", "TestUser",
//                comments, recipeStorage.makeIngredientMapForRecipe(ingredientMap),
//                strInstructions, 15, 6, "images/HelloWorld");
//
//        // Test getters
//        assertEquals("Tacos", recipe.getName());
//        assertEquals(2, recipe.getCookTime());
//        assertEquals("The best tacos ever", recipe.getDescription());
//        assertEquals(comments, recipe.getComments());
//        assertEquals(strComments, recipe.getCommentsAsString());
//        assertEquals(ingredients, recipe.getIngredients());
//        assertEquals(instructions, recipe.getInstructions());
//        assertEquals(strInstructions, recipe.getInstructions());
//        assertEquals(15, recipe.getPrepTime());
//        assertEquals(6, recipe.getServings());
//
//    }



}
