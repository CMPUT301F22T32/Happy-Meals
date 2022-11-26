package com.example.happymeals.mealplan;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.happymeals.R;
import com.example.happymeals.recipe.Recipe;

import java.util.ArrayList;

// add button at end of gridview https://stackoverflow.com/questions/42991986/gridview-with-add-button

public class MealRecipeAdapter extends ArrayAdapter<Recipe> {
    private Context context;
    private ArrayList<Recipe> recipes;
    private Button addRecipesButton;

    public MealRecipeAdapter(@NonNull Context context, ArrayList<Recipe> recipes) {
        super(context, 0, recipes);
        this.context = context;
        this.recipes = recipes;
        this.addRecipesButton = (Button) addButton();

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

        ConstraintLayout constraintLayout = view.findViewById(R.id.constraintLayout);

        if (recipes.size() -1 == position) {
            constraintLayout.addView(addRecipesButton);
        }

        return view;
    }

    private View addButton() {
        ConstraintLayout.LayoutParams params = new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        Button addRecipeButton = new Button(context);
        addRecipeButton.setLayoutParams(params);
        addRecipeButton.setText("+");

        return addRecipeButton;
    }

    public View getAddButton() {
        return this.addRecipesButton;
    }
}
