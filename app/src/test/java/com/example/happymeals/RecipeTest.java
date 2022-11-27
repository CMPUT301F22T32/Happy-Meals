
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
    void getSetDescriptionTest() {
        Recipe recipe = mockRecipe();
        assertEquals("This is a nice recipe", recipe.getDescription() );
        recipe.setDescription("New");
        assertEquals("New", recipe.getDescription() );
    }

    @Test
    void getSetCommentsTest() {
        Recipe recipe = mockRecipe();
        ArrayList< String > comments = recipe.getComments();
        assertEquals( new ArrayList<>(Arrays.asList("CommentOne","Comment2")), comments);
        ArrayList<String> tempList = new ArrayList<>(Arrays.asList("HelloThere"));
        recipe.setComments( tempList );
        assertEquals(tempList, recipe.getComments());
    }

    @Test
    void getCreatorTest() {
        Recipe recipe = mockRecipe();
        String creator = recipe.getCreator();
        assertEquals( "TestCreator", creator);
    }

    @Test
    void getSetServingsTest() {
        Recipe recipe = mockRecipe();
        Double servings = mockRecipe().getServings();
        assertEquals((Double) 12.0, servings);
        recipe.setServings(33);
        assertEquals(33, recipe.getServings());
    }

    @Test
    void getSetCookTimeTest() {
        Recipe recipe = mockRecipe();
        Double cookTime = recipe.getCookTime();
        assertEquals((Double) 2.0, cookTime);
        recipe.setCookTime( 25 );
        assertEquals(25, recipe.getCookTime());
    }

    @Test
    void getSetPrepTimeTest() {
        Recipe recipe = mockRecipe();
        Double prepTime = recipe.getPrepTime();
        assertEquals((Double) 25.0, prepTime);
        recipe.setPrepTime( 22 );
        assertEquals( 22, recipe.getPrepTime() );
    }

    @Test
    void getSetIngerdientCountTest() {
        Recipe recipe = mockRecipe();
        assertEquals(3, recipe.getIngredients()
                .get("randomRecipe").get("count"));
        HashMap< String, HashMap< String, Object > > tempMap = new HashMap<>();
        HashMap< String, Object > countMap = new HashMap<>();
        countMap.put("count", 23);
        tempMap.put("alfredo", countMap );
        recipe.setIngredients( tempMap );
        assertEquals(23, recipe.getIngredients().get("alfredo").get("count"));
    }

    @Test
    void getSetImagePathTest() {
        Recipe recipe = mockRecipe();
        assertEquals("TestPathForImage", recipe.getImageFilePath());
        recipe.setImageFilePath("TestPath");
        assertEquals("TestPath", recipe.getImageFilePath());
    }


}
