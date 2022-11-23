package com.example.happymeals.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happymeals.R;
import com.example.happymeals.recipe.Recipe;

import java.util.ArrayList;

public class GlobalRecipesAdapter extends
        RecyclerView.Adapter< GlobalRecipesAdapter.RecipeViewHolder> {

    public class RecipeViewHolder extends RecyclerView.ViewHolder {

        public TextView recipeNameLabel;
        public TextView recipeCreatorLabel;
        public TextView recipeServingsLabel;

        public RecipeViewHolder( View itemView ) {
            super( itemView );
            recipeNameLabel = itemView.findViewById( R.id.shared_recipes_content_name );
            recipeCreatorLabel = itemView.findViewById( R.id.shared_recipes_content_creator );
            recipeServingsLabel = itemView.findViewById( R.id.shared_recipe_content_servings );
        }
    }

    private ArrayList< Recipe > allRecipesList;

    public GlobalRecipesAdapter( ArrayList< Recipe > allRecipesList ) {
        this.allRecipesList = allRecipesList;
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

        Recipe recipeInItem = allRecipesList.get( position );
        nameView.setText( recipeInItem.getName() );
        creatorView.setText( recipeInItem.getCreator() );
        servingsView.setText( String.valueOf( recipeInItem.getServings() ) );


    }

    @Override
    public int getItemCount() {
        return allRecipesList.size();
    }
}
