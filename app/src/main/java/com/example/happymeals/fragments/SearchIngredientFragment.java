package com.example.happymeals.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.happymeals.R;
import com.example.happymeals.ingredient.Ingredient;
import com.example.happymeals.ingredient.IngredientSpecificArrayAdapter;
import com.example.happymeals.ingredient.IngredientStorage;
import com.example.happymeals.ingredient.IngredientStorageArrayAdapter;
import com.example.happymeals.recipe.RecipeAddActivity;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;

public class SearchIngredientFragment extends DialogFragment implements
        GetCountFragment.GetCountFragmentListener {

    @Override
    public void onConfirmClick(double count, Ingredient ingredient) {

        addedIngredientsList.add( ingredient );
        ingredientCountMap.put( ingredient.getName(), count );
        addedIngredientsAdapter.notifyDataSetChanged();
    }

    public interface SearchIngredientsFragmentListener{
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

    @Override
    public void onAttach( Context context ) {
        this.context = context;
        countListener = this;
        super.onAttach( context );
        if ( context instanceof  SearchIngredientsFragmentListener ) {
            listener = ( SearchIngredientsFragmentListener ) context;
        } else {
            throw new RuntimeException( context.toString() + " must implement listener.");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog( @Nullable Bundle savedInstance ) {
        View view = LayoutInflater.from( getActivity() ).inflate( R.layout.search_ingredients_fragment, null );

        storage = IngredientStorage.getInstance();
        addedIngredientsList = new ArrayList<>();
        ingredientCountMap = new HashMap<>();

        allIngredients = view.findViewById( R.id.recipe_add_all_ingredients_list );
        addedIngredients = view.findViewById( R.id.recipe_add_added_ingredients_list );

        allIngredientsAdapter = new IngredientStorageArrayAdapter( view.getContext(), storage.getIngredients() );
        addedIngredientsAdapter = new IngredientSpecificArrayAdapter( view.getContext(), addedIngredientsList );

        allIngredients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                GetCountFragment frag = new GetCountFragment(context, countListener,
                        (Ingredient) allIngredients.getItemAtPosition( i ) );

                frag.show(((RecipeAddActivity) context).getSupportFragmentManager(), "L E S F");
            }
        });
        allIngredients.setAdapter( allIngredientsAdapter );

        addedIngredients.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                addedIngredientsList.remove( addedIngredients.getItemAtPosition( i ) );
                addedIngredientsAdapter.notifyDataSetChanged();
            }
        });

        addedIngredients.setAdapter( addedIngredientsAdapter );

        AlertDialog.Builder builder = new AlertDialog.Builder( getActivity() );
        return builder
                .setView( view )
                .setTitle( "Ingredients to Add to Recipe" )
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick( DialogInterface dialogInterface, int i ) {
                        listener.onConfirmClick(
                                new ArrayList<>(addedIngredientsList),
                                ingredientCountMap );
                    }
                })
                .setNegativeButton( "Cancel", null )
                .create();
    }


}
