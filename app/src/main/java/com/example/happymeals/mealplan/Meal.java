package com.example.happymeals.mealplan;

import com.example.happymeals.Constants;
import com.example.happymeals.database.DatabaseListener;
import com.example.happymeals.database.DatabaseObject;
import com.example.happymeals.database.FireStoreManager;
import com.example.happymeals.ingredient.Ingredient;
import com.example.happymeals.recipe.Recipe;
import com.google.firebase.firestore.DocumentReference;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Map;

public class Meal extends DatabaseObject implements DatabaseListener {

    private String name;
    private Constants.COLLECTION_NAME type;
    private Boolean made;

    private FireStoreManager fsm;

    private ArrayList<Ingredient> ingredients = null;
    private ArrayList<Recipe> recipes = null;

    private ArrayList<DocumentReference> refs;

    public Meal() {
        fsm = FireStoreManager.getInstance();
        this.made = false;
        refs = new ArrayList<>();
    }

    public Constants.COLLECTION_NAME getType() {
        return type;
    }

    public Boolean getMade() {
        return made;
    }

    public void setMade(Boolean made) {
        this.made = made;
    }

    public ArrayList<DocumentReference> getReferences() {
        return refs;
    }

    public void setIngredients(ArrayList<Ingredient> ingredients) {
        refs.clear();
        this.recipes = null;
        this.ingredients = new ArrayList<>();

        this.type = Constants.COLLECTION_NAME.INGREDIENTS;
        for (Ingredient ingredient : ingredients) {
            DocumentReference doc = fsm.getDocReferenceTo(Constants.COLLECTION_NAME.INGREDIENTS, ingredient);
            refs.add(doc);
        }
    }

    public void setRecipes(ArrayList<Recipe> recipes) {
        refs.clear();
        this.ingredients = null;
        this.recipes = new ArrayList<>();

        this.type = Constants.COLLECTION_NAME.RECIPES;
        for (Recipe recipe : recipes) {
            DocumentReference doc = fsm.getDocReferenceTo(Constants.COLLECTION_NAME.RECIPES, recipe);
            refs.add(doc);
        }
    }

    public ArrayList<Ingredient> getIngredients() {
        if (refs.size() == 0)
            return null;

        ingredients = new ArrayList<>();
        // Always fetch using doc ref in case details have changed
        for (DocumentReference ref : refs) {
            fsm.getData(ref, this, new Ingredient());
        }
        return ingredients;
    }

    public ArrayList<Recipe> getRecipes() {
        if (refs.size() == 0)
            return null;

        recipes = new ArrayList<>();
        // Always fetch using doc ref in case details have changed
        for (DocumentReference ref : refs) {
            fsm.getData(ref, this, new Recipe());
        }
        return recipes;
    }

    public String getDisplayName() {
        StringBuilder displayString = new StringBuilder();
        displayString.setLength(32);
        for (DocumentReference doc : refs) {
            String[] tokens = doc.toString().split("/");
            int length = tokens.length;
            String name = tokens[length - 1];
            displayString.append(name);
        }
        return displayString.toString() + " ...";
    }

    @Override
    public void onDataFetchSuccess(DatabaseObject data) {
        if ( data.getClass() == Recipe.class ) {
            Recipe recipe = (Recipe) data;
            recipes.add(recipe);
        }
        else if (data.getClass() == Ingredient.class ){
            Ingredient ingredient = (Ingredient) data;
            ingredients.add(ingredient);
        }
    }

    @Override
    public void onSpinnerFetchSuccess(Map<String, Object> data) {

    }
}
