package com.example.happymeals.mealplan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.happymeals.R;
import com.example.happymeals.recipe.Recipe;

import java.util.ArrayList;

public class MealRecipeAdapter extends ArrayAdapter<Recipe> {
    private Context context;
    private ArrayList<Recipe> recipes;

    public MealRecipeAdapter(@NonNull Context context, ArrayList<Recipe> recipes) {
        super(context, 0, recipes);
        this.context = context;
        this.recipes = recipes;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.meal_recipe_content, parent, false);
        }

        Recipe recipe = recipes.get(position);

        TextView name = view.findViewById(R.id.meal_recipe_name);
        name.setText(recipe.getDescription());

        return view;
    }
}
