package com.example.happymeals.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.happymeals.R;
import com.example.happymeals.database.DatabaseObject;
import com.example.happymeals.database.DatasetWatcher;
import com.example.happymeals.ingredient.IngredientStorage;
import com.example.happymeals.ingredient.IngredientStorageArrayAdapter;
import com.example.happymeals.recipe.RecipeStorage;
import com.example.happymeals.recipe.RecipeStorageAdapter;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MealPlanItemsFragment} factory method to
 * create an instance of this fragment.
 */
public class MealPlanItemsFragment extends DialogFragment implements DatasetWatcher {

    private boolean showIngredients;

    private IngredientStorage ingredientStorage;
    private RecipeStorage recipeStorage;

    private Context context;

    private ListView list;
    private HashSet<Integer> selected;

    public MealPlanItemsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = LayoutInflater
                .from(getActivity())
                .inflate(R.layout.activity_select_meals, null);

        selected = new HashSet<>();

        ingredientStorage = IngredientStorage.getInstance();
        recipeStorage = RecipeStorage.getInstance();

        RadioGroup radioGroup = view.findViewById(R.id.mp_type_of_item);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                showIngredients = !showIngredients;
                switchList();
            }
        });

        int checked = radioGroup.getCheckedRadioButtonId();
        RadioButton ingredients = view.findViewById(R.id.ingredient_radio);
        int ingredientID = ingredients.getId();

        showIngredients = (ingredientID == checked);

        list = view.findViewById(R.id.meal_plan_item_list);

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                for (Integer index : selected) {
                    if (index == i) {
                        view.setBackgroundColor(Color.WHITE);
                        selected.remove(i);
                        return;
                    }
                }
                view.setBackgroundColor(Color.LTGRAY);
                selected.add(i);
            }
        });

        switchList();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        return builder.setView(view).create();
    }

    private void switchList() {
        if (showIngredients) {
            //ingredientStorage.setListeningActivity(this);
            IngredientStorageArrayAdapter adapter = new IngredientStorageArrayAdapter( context, ingredientStorage.getIngredients() ) ;
            list.setAdapter( adapter ) ;
        }
        else {
            RecipeStorageAdapter adapter = new RecipeStorageAdapter( context, recipeStorage.getRecipes() );
            list.setAdapter( adapter );
        }
    }

    @Override
    public void signalChangeToAdapter() {

    }
}