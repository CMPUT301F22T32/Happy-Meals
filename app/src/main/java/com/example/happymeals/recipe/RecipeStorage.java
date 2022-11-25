package com.example.happymeals.recipe;

import com.example.happymeals.Constants;
import com.example.happymeals.database.DatasetWatcher;
import com.example.happymeals.database.DatabaseListener;
import com.example.happymeals.database.DatabaseObject;
import com.example.happymeals.database.FireStoreManager;
import com.example.happymeals.ingredient.Ingredient;
import com.example.happymeals.ingredient.IngredientStorage;
import com.example.happymeals.adapters.IngredientStorageArrayAdapter;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author jeastgaa
 * @version 1.03.01
 * This class holds all the {@link Recipe}s from the database. It is a singleton class which can only
 * be instantiated once. This should be done when the application is loaded up.
 * On instantiation the storage will be populated through a database call.
 * All database interactions for a {@link Recipe} should be preformed through this class.
 */
public class RecipeStorage implements DatabaseListener {

    private static RecipeStorage instance;

    private DatasetWatcher listeningActivity;

    private ArrayList<Recipe> recipes;
    private IngredientStorage ingredientStorage;
    private FireStoreManager fsm;
    private CollectionReference collection;

    private ArrayList< Ingredient > ingredientHolderForReturn;
    private IngredientStorageArrayAdapter ingredientListener;

    /**
     * Base constructor that will create all instances and populate all required variables
     * needed to run the class. This will interact with asynchronous database calls so expect
     * a few seconds for everything to be properly populated.
     */
    private RecipeStorage() {
        this.recipes = new ArrayList<>();
        this.ingredientStorage = IngredientStorage.getInstance();
        this.ingredientHolderForReturn = new ArrayList<>();
        this.fsm = FireStoreManager.getInstance();
        this.collection = fsm.getCollectionReferenceTo( Constants.COLLECTION_NAME.RECIPES );
        this.fsm.getAllFrom( collection, this, new Recipe() );
        this.listeningActivity = null;
    }

    /**
     * Adds a recipe to the storage. This will update the storage and add this recipe to the
     * database.
     * @param recipe The {@link Recipe} which is being added.
     */
    public void addRecipe(Recipe recipe) {
        if(! recipes.contains( recipe ) ) {
            recipes.add(recipe);
        }
        fsm.addData( collection, recipe );
        updateStorage();
    }

    /**
     * This will send a request to the {@link IngredientStorage} singleton instantiation to
     * reduce the count of the given ingredient.
     * @param ingredient The {@link Ingredient} which is being altered.
     * @param amount The {@link Double } by which the ingredient amount is getting reduced by.
     */
    public void consumeIngredientInRecipe( Ingredient ingredient, Double amount ) {
        ingredientStorage.requestConsumptionOfIngredient(
                ingredient,
                Integer.parseInt( String.valueOf( amount ) ) );
    }

    /**
     * Given a {@link Recipe} and {@link IngredientStorageArrayAdapter} as a listener, this will
     * grab all the stored ingredients in the {@link Recipe} and add them to an arraylist.
     * When this is done we also require that the class ingredientListener is updated so that later
     * it can be notified of the dataset being changed.
     * @param recipe The {@link Recipe} which is being inspected for ingredients.
     * @param listener The {@link IngredientStorageArrayAdapter} which will be stored for later use.
     * @return The {@link ArrayList} of {@link Ingredient}s that has been grabbed from the database.
     * This is passed as a reference and will be updated as the database returns the requested
     * information.
     */
    public ArrayList< Ingredient > getIngredientsAsList( Recipe recipe, IngredientStorageArrayAdapter listener ) {
        if( this.ingredientHolderForReturn.size() != recipe.getIngredients().size() ){
            this.ingredientHolderForReturn.clear();
            this.ingredientListener = listener;
            for( HashMap< String, Object >  mapInstance: recipe.getIngredients().values() ) {
                if( mapInstance.get("reference") instanceof DocumentReference ) {
                    fsm.getData( (DocumentReference) mapInstance.get("reference"), this, new Ingredient() );
                }
            }
        }
        return this.ingredientHolderForReturn;
    }

    /**
     * This will get the {@link ArrayList} that the storage is holding all the current recipes
     * {@link Ingredient}s in. This {@link ArrayList} is populated through database calls from
     * this storage class which when returned will be added to this list. The list is cleared when
     * the getIngredientAsList method is called.
     * @return The {@link ArrayList} of {@link Ingredient}s which are being held in the current
     * parsed recipe.
     */
    public ArrayList< Ingredient > getIngredientListReference() {
        return this.ingredientHolderForReturn;
    }

    /**
     * Returns the only instantiation of this storage class. If one has not been made yet
     * it will be done here calling the default constructor.
     * @return {@link RecipeStorage} instance.
     */
    public static RecipeStorage getInstance() {
        if( instance == null ) {
            instance = new RecipeStorage();
        }
        return instance;
    }

    /**
     * For a given {@link Recipe} and a {@link Ingredient} which can be found in the recipe, this
     * will return the amount of the said ingredient which is required for the {@link Recipe}.
     * @param recipe The {@link Recipe} where the {@link Ingredient} can be found.
     * @param ingredient The {@link Ingredient} which we are requesting the count for.
     * @return The {@link Double} of the count attatched to the {@link Ingredient}
     */
    public Double getCountForIngredientInRecipe( Recipe recipe, Ingredient ingredient ) {
        return (Double) getRecipeIngredientMap( recipe ).get( ingredient.getName() )
                .get("count");
    }

    public List< Recipe>  getRecipesByCookingTime(int cookTime) {
        this.ingredientHolderForReturn = new ArrayList<>();
        List<Recipe> result = new ArrayList<Recipe>();
        for (Recipe recipe : recipes) {
            if (recipe.getCookTime() == cookTime) {
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

    /**
     * Searches for the stored {@link Recipe} inside of this storage. If a object's name matches
     * the given string it will be returned.
     * @param recipeName The {@link String} representing the name being looked for.
     * @return The {@link Recipe} found in storage. Will return null if not found.
     */
    public Recipe getRecipe( String recipeName ) {
        for( Recipe recipe : this.recipes ) {
            if ( recipe.getName().equals( recipeName )) {
                return recipe;
            }
        }
        return null;
    }

    /**
     * This will find the map that stores both reference database paths and the amount of each
     * ingredient.
     * @param recipe {@link Recipe} for which we want the ingredients from.
     * @return The {@link HashMap} holding the requested reference/amount information.
     */
    public HashMap< String, HashMap< String, Object > > getRecipeIngredientMap ( Recipe recipe ) {
        return recipe.getIngredients();
    }

    /**
     * This will grab all the currently stored recipes.
     * @return The {@link ArrayList} holding all the recipes stored.
     */
    public ArrayList<Recipe> getRecipes() {
        return recipes;
    }

    /**
     * This will create a {@link HashMap} which follows the correct data structure
     * which can be stored in the database. It required a Map which holds only the amount for
     * each engredient, to which this method will populat each {@link HashMap} holding the amount
     * with the {@link DocumentReference} to be stored and referenced later if required.
     * @param givenMap The {@link HashMap} which needs to hold the name of each {@link Ingredient}
     *                 which requires a reference.
     * @return The {@link HashMap} which will now hold a key/value pair for each
     * {@link Ingredient}-name key.
     */
    public HashMap< String, HashMap< String, Object > > makeIngredientMapForRecipe(
            HashMap< String, HashMap< String, Object > > givenMap ) {

        for( Map.Entry<String, HashMap< String, Object > > entry : givenMap.entrySet() ) {
            entry.getValue().put(
                    "reference",
                    fsm.getDocReferenceTo(Constants.COLLECTION_NAME.INGREDIENTS,
                            entry.getKey() ) );
        }

        return givenMap;
    }

    /**
     * This will remove the provided recipe from both the storage and database. It then calls
     * the updateStorage() method in order to update the current listener.
     * @param recipe
     */
    public void removeRecipe(Recipe recipe) {
        recipes.remove(recipe);
        fsm.deleteDocument( collection, recipe );
        updateStorage();
    }

    /**
     * Re-assigns the current {@link DatasetWatcher} class to listen to storage changes.
     * @param context The {@link DatasetWatcher} to be set.
     */
    public void setListeningActivity( DatasetWatcher context ) {
        this.listeningActivity = context;
    }

    /**
     * This method sets all the stored recipes to the given {@link ArrayList}. This should ONLY
     * be used for DEBUG and TESTING. It will NOT interact with the database. The database should
     * only be interacted with from user defined methods.
     * @param recipes
     */
    public void setRecipes( ArrayList<Recipe> recipes ) {
        this.recipes = recipes;
        updateStorage();
    }
//    public List<Recipe> getRecipesByType(String type) {
//        List<Recipe> result = new ArrayList<Recipe>();
//        for (Recipe recipe : recipes) {
//            if (recipe.getType().equals(type)) {
//                result.add(recipe);
//            }
//        }
//        return result;


//    public List<Recipe> getRecipesByIngredient(Ingredient ingredient) {
//        List<Recipe> result = new ArrayList<Recipe>();
//        for (Recipe recipe : recipes) {
//            if (recipe.getIngredients().contains(ingredient)) {
//                result.add(recipe);
//            }
//        }
//        return result;
//    }

    /**
     * Notifies the current listening class of a dataset change.
     */
    public void updateStorage() {
        if( listeningActivity != null ) {
            listeningActivity.signalChangeToAdapter();
        }
    }

    //*******************************************
    // Overridden Functions for DatabaseListener
    //*******************************************

    /**
     * When the database fetches a new object we add to to our recipes or ingredients
     * {@link ArrayList}s depending on the datatype. The datatype is defined on request.
     * @param data {@link DatabaseObject} which holds the returned class.
     */
    @Override
    public void onDataFetchSuccess(DatabaseObject data) {
        if( data == null ) return;
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
    public <T> void onSpinnerFetchSuccess( T map ) {

    }
}