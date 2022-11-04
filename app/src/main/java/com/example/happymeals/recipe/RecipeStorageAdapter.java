package com.example.happymeals.recipe;

import android.content.Context;
import android.content.Intent;
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
    }

/*
    @NonNull
    @Override

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.recipe_content, parent, false);

        Recipe currentRecipe = recipeStorageList.get(position);

        TextView name = listItem.findViewById(R.id.recipe_list_name_field);
        TextView servings = listItem.findViewById( R.id.recipe_list_servings_field );
        TextView description = listItem.findViewById( R.id.recipe_list_description_field );
        name.setText( currentRecipe.getName() );
        servings.setText( String.valueOf( currentRecipe.getServings() ) );
        description.setText( currentRecipe.getDescription() );

        listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( context, RecipeDetailsActivity.class );
                intent.putExtra("recipe", currentRecipe );
                context.startActivity( intent );
            }
        });

        return listItem;
    }


 */
}