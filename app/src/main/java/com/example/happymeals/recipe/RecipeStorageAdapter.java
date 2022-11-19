package com.example.happymeals.recipe;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.happymeals.R;
import com.example.happymeals.fragments.ModifyConfirmationFragment;

import java.util.ArrayList;

public class RecipeStorageAdapter extends ArrayAdapter<Recipe> {
    private ArrayList<Recipe> recipeStorageList;
    private Context context;
    private Recipe currentRecipe;

    public RecipeStorageAdapter(@NonNull Context context, ArrayList<Recipe> recipeStorageList ) {
        super(context, 0 , recipeStorageList );
        this.context = context;
        this.recipeStorageList = recipeStorageList;
        this.currentRecipe = null;
    }

    @NonNull
    @Override

    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.recipe_content, parent, false);

        currentRecipe = recipeStorageList.get(position);

        TextView name = listItem.findViewById(R.id.ingredient_specific_list_name_field);
        TextView servings = listItem.findViewById( R.id.recipe_list_servings_field );
        TextView description = listItem.findViewById( R.id.recipe_list_description_field );
        name.setText( currentRecipe.getName() );
        servings.setText( String.valueOf( currentRecipe.getServings() ) );
        description.setText( currentRecipe.getDescription() );

        listItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( context, RecipeDetailsActivity.class );
                intent.putExtra("recipe", currentRecipe.getName() );
                context.startActivity( intent );
            }
        });

        listItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ModifyConfirmationFragment deleteFragment = new ModifyConfirmationFragment(
                        "Remove Recipe",
                        String.format("Are you sure you want to remove %s?", currentRecipe.getName() ),
                        context,
                        getDeleteListener() );
                deleteFragment.display();
                return true;

            }
        });

        return listItem;
    }

    private DialogInterface.OnClickListener getDeleteListener() {

        return new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                RecipeStorage.getInstance().removeRecipe( currentRecipe );
            }
        };
    }
}