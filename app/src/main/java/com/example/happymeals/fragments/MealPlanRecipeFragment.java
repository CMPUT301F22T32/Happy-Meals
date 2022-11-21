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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.happymeals.R;
import com.example.happymeals.recipe.RecipeStorage;
import com.example.happymeals.recipe.RecipeStorageAdapter;

public class MealPlanRecipeFragment extends DialogFragment {
    private ListView recipeListView;
    private RecipeStorageAdapter recipeAdapter;
    private RecipeStorage recipeStorage;
    private OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener {
        void onOkPressed();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof  OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        }
        else {
            throw new RuntimeException(context.toString() + "This is not the correct fragment");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View view = LayoutInflater
                .from(getActivity())
                .inflate(R.layout.meal_plan_recipe_fragment, null);

        recipeListView = view.findViewById(R.id.meal_plan_recipe_list_view);
        recipeStorage = RecipeStorage.getInstance();
        recipeAdapter = new RecipeStorageAdapter(view.getContext(), recipeStorage.getRecipes() );
        recipeListView.setAdapter(recipeAdapter);

        recipeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // add this to the meal plan array
                recipeStorage.getRecipeByIndex(i);
            }
        });


        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());


        return builder
                .setView(view)
                .setTitle("Add Recipes")
                .setNegativeButton("Cancel", null)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.onOkPressed();
                    }
                }).create();



    }


}
