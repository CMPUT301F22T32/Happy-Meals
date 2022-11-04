package com.example.happymeals.recipe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.happymeals.R;

import java.util.ArrayList;

public class RecipeStorageAdapter extends ArrayAdapter<Recipe> {
    private ArrayList<Recipe> recipeStorageList;
    private Context context;

    public RecipeStorageAdapter(Context context, ArrayList<Recipe> recipeStorageList) {
        super(context, 0, recipeStorageList);
        this.context = context;
        this.recipeStorageList = recipeStorageList;
    }
/*
    @NonNull
    @Override

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.recipe_storage_content, parent, false);

        Recipe currentRecipe = recipeStorageList.get(position);

        TextView name = (TextView) listItem.findViewById(R.id.recipeName);
        name.setText(currentRecipe.getRecipeName());

        TextView ingredients = (TextView) listItem.findViewById(R.id.ingredients);
        ingredients.setText(currentRecipe.getIngredients());

        TextView instructions = (TextView) listItem.findViewById(R.id.instructions);
        instructions.setText(currentRecipe.getInstructions());

        return listItem;
    }


 */
}