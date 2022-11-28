package com.example.happymeals.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.happymeals.R;
import com.example.happymeals.ingredient.Ingredient;
import com.example.happymeals.adapters.IngredientSpecificArrayAdapter;
import com.example.happymeals.ingredient.IngredientStorage;
import com.example.happymeals.adapters.IngredientStorageArrayAdapter;
import com.example.happymeals.recipe.RecipeAddActivity;
import com.example.happymeals.recipe.RecipeDetailsActivity;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author jeastgaa
 * @version 1.00.01
 * This fragment is responsible for allowing the user to select ingredients and the required count
 * of such ingredients through the {@link GetCountFragment} class.
 */
public class SearchIngredientFragment extends DialogFragment implements
        GetCountFragment.GetCountFragmentListener {

    /**
     * When the confirmation button is clicked on this fragment this method will be called.
     * This method is responsible for adding the count for the passed ingredient as well as the
     * {@link HashMap} responsible for keeping track of how much of the ingredient is required.
     * This information is passed into provided references which are hosted by the calling class.
     * @param count {@link Double} which holds the count of the ingredient inputted.
     * @param ingredient {@link Ingredient} object which is being added to the ingredient
     * @link ArrayList}
     */
    @Override
    public void onConfirmClick( double count, Ingredient ingredient ) {
        addedIngredientsList.add( ingredient );
        ingredientCountMap.put( ingredient.getId(), count );
        addedIngredientsAdapter.notifyDataSetChanged();
    }

    /**
     * @author jeastgaa
     * @version 1.00.01
     * This interface is used by the fragment calling class so that all information collected
     * within this fragment can be returned.
     */
    public interface SearchIngredientsFragmentListener{
        /**
         * When the confirm button is pressed on the fragment this method should be called.
         * It will pass the information collected to the implimented listener.
         * @param ingredientsToAdd The {@link ArrayList} which holds all the
         *                         added {@link Ingredient}s.
         * @param countsToAdd The {@link HashMap} which maps to each ingredient added so that
         *                    the count for each ingredient can be kept track of.
         */
        void onConfirmClick( ArrayList< Ingredient > ingredientsToAdd, HashMap< String, Double > countsToAdd );
    }

    private Context context;
    private ListView allIngredients;
    private ListView addedIngredients;
    private GetCountFragment.GetCountFragmentListener countListener;
    private SearchIngredientsFragmentListener listener;

    private ArrayList<Ingredient> addedIngredientsList;
    private HashMap< String, Double > ingredientCountMap;

    private IngredientStorageArrayAdapter allIngredientsAdapter;
    private IngredientSpecificArrayAdapter addedIngredientsAdapter;

    private IngredientStorage storage;

    public SearchIngredientFragment( HashMap< String, HashMap< String, Object > > ingredientCountMap ) {
        this.ingredientCountMap = new HashMap<>();
        addedIngredientsList = new ArrayList<>();

        for ( HashMap.Entry<String, HashMap<String, Object>> mapElement : ingredientCountMap.entrySet() ) {
            this.ingredientCountMap.put( 
                    mapElement.getKey(),
                    Double.parseDouble( ( mapElement.getValue().get( "count" ) ).toString() ) );
            addedIngredientsList.add( IngredientStorage.getInstance().getIngredient( 
                    mapElement.getKey()
            ) );
        }

    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void onAttach( Context context ) {
        this.context = context;
        countListener = this;
        super.onAttach( context );
        if ( context instanceof  SearchIngredientsFragmentListener ) {
            listener = ( SearchIngredientsFragmentListener ) context;
        } else {
            throw new RuntimeException( context.toString() + " must implement listener." );
        }
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public Dialog onCreateDialog( @Nullable Bundle savedInstance ) {
        View view = LayoutInflater.from( getActivity() ).inflate( R.layout.search_ingredients_fragment, null );

        storage = IngredientStorage.getInstance();

        allIngredients = view.findViewById( R.id.recipe_add_all_ingredients_list );
        addedIngredients = view.findViewById( R.id.recipe_add_added_ingredients_list );

        allIngredientsAdapter = new IngredientStorageArrayAdapter( view.getContext(),
                storage.getIngredients() );
        addedIngredientsAdapter = new IngredientSpecificArrayAdapter( view.getContext(),
                addedIngredientsList,
                ingredientCountMap );


        allIngredients.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick( AdapterView<?> adapterView, View view, int i, long l ) {
                GetCountFragment frag = new GetCountFragment( countListener,
                        ( Ingredient ) allIngredients.getItemAtPosition( i ) );
                if( context.getClass() == RecipeAddActivity.class ) {
                    frag.show( ( ( RecipeAddActivity )context ).getSupportFragmentManager(), "L E S F" );
                } else if( context.getClass() == RecipeDetailsActivity.class ){
                    frag.show( ( ( RecipeDetailsActivity )context ).getSupportFragmentManager(), "L E S F" );
                } else {
                    Log.e( "Search Ingredient Frag:", "Incorrect Context Passed To Fragment." );
                }

            }
        } );

        allIngredients.setAdapter( allIngredientsAdapter );

        addedIngredients.setOnItemClickListener( new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick( AdapterView<?> adapterView, View view, int i, long l ) {
                addedIngredientsList.remove( addedIngredients.getItemAtPosition( i ) );
                addedIngredientsAdapter.notifyDataSetChanged();
            }
        } );

        addedIngredients.setAdapter( addedIngredientsAdapter );

        AlertDialog.Builder builder = new AlertDialog.Builder( getActivity() );
        return builder
                .setView( view )
                .setTitle( "Ingredients to Add to Recipe" )
                .setPositiveButton( "Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick( DialogInterface dialogInterface, int i ) {
                        listener.onConfirmClick( 
                                new ArrayList<>( addedIngredientsList ),
                                ingredientCountMap );
                    }
                } )
                .setNegativeButton( "Cancel", null )
                .create();
    }


}