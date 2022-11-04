package com.example.happymeals.recipe;

// recipe storage class

import com.example.happymeals.Constants;
import com.example.happymeals.database.DatabaseListener;
import com.example.happymeals.database.DatabaseObject;
import com.example.happymeals.database.FireStoreManager;
import com.example.happymeals.ingredient.Ingredient;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author bfiogbe
 */
public class RecipeStorage implements DatabaseListener {

    private ArrayList<Recipe> recipes;
    private FireStoreManager fsm;
    private CollectionReference collection;
    public RecipeStorage( FireStoreManager fsm ) {

        this.recipes = new ArrayList<Recipe>();
        this.fsm = fsm;
        this.collection = fsm.getCollectionReferenceTo( Constants.COLLECTION_NAME.RECIPES );
        this.fsm.getAllFrom( collection, this, new Recipe() );

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

    public ArrayList< Ingredient > getIngredientsAsList( Recipe recipe ) {
        return ( recipe.getIngredients() );
    }
//    public List<Recipe> getRecipesByType(String type) {
//        List<Recipe> result = new ArrayList<Recipe>();
//        for (Recipe recipe : recipes) {
//            if (recipe.getType().equals(type)) {
//                result.add(recipe);
//            }
//        }
//        return result;
//    }

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

    public void updateStorage() {}

    @Override
    public void onDataFetchSuccess(DatabaseObject data) {
        if( data.getClass() == Recipe.class ) {
            recipes.add( (Recipe) data );
            updateStorage();
        }
    }

    @Override
    public void onSpinnerFetchSuccess(Map<String, Object> data) {

    }
}