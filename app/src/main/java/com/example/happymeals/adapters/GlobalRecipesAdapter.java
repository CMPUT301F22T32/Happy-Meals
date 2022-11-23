package com.example.happymeals.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happymeals.R;
import com.example.happymeals.fragments.InputErrorFragment;
import com.example.happymeals.recipe.Recipe;
import com.example.happymeals.recipe.RecipeDetailsActivity;
import com.example.happymeals.recipe.RecipeStorage;
import com.example.happymeals.recipe.SharedRecipeDetailsActivity;

import java.util.ArrayList;

public class GlobalRecipesAdapter extends
        RecyclerView.Adapter< GlobalRecipesAdapter.RecipeViewHolder> {

    public class RecipeViewHolder extends RecyclerView.ViewHolder {

        public TextView recipeNameLabel;
        public TextView recipeCreatorLabel;
        public TextView recipeServingsLabel;

        public Button detailButton;
        public Button addButton;

        public RecipeViewHolder( View itemView ) {
            super( itemView );
            recipeNameLabel = itemView.findViewById( R.id.shared_recipes_content_name );
            recipeCreatorLabel = itemView.findViewById( R.id.shared_recipes_content_creator );
            recipeServingsLabel = itemView.findViewById( R.id.shared_recipe_content_servings );

            detailButton = itemView.findViewById( R.id.shared_recipes_content_details_button );
            addButton = itemView.findViewById( R.id.shared_recipes_content_add_recipe_button );
        }
    }

    private ArrayList< Recipe > allRecipesList;

    private Context context;

    public GlobalRecipesAdapter( ArrayList< Recipe > allRecipesList, Context context ) {
        this.allRecipesList = allRecipesList;
        this.context = context;
    }

    @NonNull
    @Override
    public GlobalRecipesAdapter.RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from( context );

        // Inflate Custom Layout
        View recipeView = inflater.inflate( R.layout.content_shared_recipe, parent, false );

        // Return a new holder instance
        RecipeViewHolder viewHolder = new RecipeViewHolder( recipeView );
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull GlobalRecipesAdapter.RecipeViewHolder holder, int position) {
        TextView nameView = holder.recipeNameLabel;
        TextView creatorView = holder.recipeCreatorLabel;
        TextView servingsView = holder.recipeServingsLabel;

        int indexOfItem = position;

        Recipe recipeInItem = allRecipesList.get( indexOfItem );
        nameView.setText( recipeInItem.getName() );
        creatorView.setText( recipeInItem.getCreator() );
        servingsView.setText( String.valueOf( recipeInItem.getServings() ) );

        Button addButton = holder.addButton;
        Button detailsButton = holder.detailButton;

        detailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // <todo> Change this to a shared recipe view activity which will need a new class.
                Intent intent = new Intent(  context, SharedRecipeDetailsActivity.class ) ;
                intent.putExtra( "Index", indexOfItem ) ;
                context.startActivity( intent ); ;
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecipeStorage.getInstance().addRecipe( recipeInItem );
                InputErrorFragment inputErrorFragment =
                        new InputErrorFragment(
                                "Added Shared Recipe",
                                "You have added " + recipeInItem.getName() + " into your inventory",
                                context
                        );
                inputErrorFragment.display();
            }
        });
    }

    @Override
    public int getItemCount() {
        return allRecipesList.size();
    }
}
