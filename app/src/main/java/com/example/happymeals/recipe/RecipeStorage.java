package com.example.happymeals.recipe;



// recipe storage class

import android.content.Context;

<<<<<<< HEAD:app/src/main/java/com/example/happymeals/recipe/RecipeStorage.java
import com.example.happymeals.ingredient.Ingredient;

=======
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
>>>>>>> origin:app/src/main/java/com/example/happymeals/RecipeStorage.java
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecipeStorage implements DatabaseListener{

    private static RecipeStorage instance;

    private DatasetWatcher listeningActivity;

    private ArrayList<Recipe> recipes;
<<<<<<< HEAD:app/src/main/java/com/example/happymeals/recipe/RecipeStorage.java
=======
    private FireStoreManager fsm;
    private CollectionReference collection;

    private ArrayList< Ingredient > ingredientHolderForReturn;
    private IngredientArrayAdapter ingredientListener;

    private RecipeStorage() {
        this.recipes = new ArrayList<Recipe>();
        this.ingredientHolderForReturn = new ArrayList<>();
        this.fsm = FireStoreManager.getInstance();
        this.collection = fsm.getCollectionReferenceTo( Constants.COLLECTION_NAME.RECIPES );
        this.fsm.getAllFrom( collection, this, new Recipe() );
        this.listeningActivity = null;

    }

    public static RecipeStorage getInstance() {
        if( instance == null ) {
            instance = new RecipeStorage();
        }
        return instance;
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

    public Recipe getRecipe( String recipeName ) {
        for( Recipe recipe : this.recipes ) {
            if ( recipe.getName().equals( recipeName )) {
                return recipe;
            }
        }
        return null;
    }

    public ArrayList< Ingredient > getIngredientListReference() {
        return this.ingredientHolderForReturn;
    }
    public ArrayList< Ingredient > getIngredientsAsList( Recipe recipe, IngredientArrayAdapter listener ) {
        if( this.ingredientHolderForReturn.size() != recipe.getIngredients().size() ){
            this.ingredientHolderForReturn.clear();
            this.ingredientListener = listener;
            for( DocumentReference doc : recipe.getIngredients().values() ) {
                fsm.getData( doc, this, new Ingredient() );
            }
        }
        return this.ingredientHolderForReturn;
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

//    public List<Recipe> getRecipesByIngredient(Ingredient ingredient) {
//        List<Recipe> result = new ArrayList<Recipe>();
//        for (Recipe recipe : recipes) {
//            if (recipe.getIngredients().contains(ingredient)) {
//                result.add(recipe);
//            }
//        }
//        return result;
//    }

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
        this.ingredientHolderForReturn = new ArrayList<>();
        List<Recipe> result = new ArrayList<Recipe>();
        for (Recipe recipe : recipes) {
            if (recipe.getCookTime() == cookTime) {
                result.add(recipe);
            }
        }
        return result;
    }

    public void setListeningActivity( DatasetWatcher context ) {
        this.listeningActivity = context;
    }
    public void updateStorage() {
        if( listeningActivity != null ) {
            listeningActivity.signalChangeToAdapter();
        }
    }

    @Override
    public void onDataFetchSuccess(DatabaseObject data) {
        if( data.getClass() == Recipe.class ) {
            recipes.add( (Recipe) data );
            updateStorage();
        } else if( data.getClass() == Ingredient.class ) {
            this.ingredientHolderForReturn.add( (Ingredient) data );
            if( ingredientListener != null ) {
                ingredientListener.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onSpinnerFetchSuccess(Map<String, Object> data) {

    }
}