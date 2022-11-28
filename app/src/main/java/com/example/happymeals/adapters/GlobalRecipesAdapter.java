package com.example.happymeals.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;

import com.example.happymeals.R;
import com.example.happymeals.fragments.InputErrorFragment;
import com.example.happymeals.recipe.PublicRecipeActivity;
import com.example.happymeals.recipe.Recipe;
import com.example.happymeals.recipe.RecipeStorage;
import com.example.happymeals.recipe.SharedRecipeDetailsActivity;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Comparator;

/**
 * @author jeastgaa
 * @version 1.00.01
 * Adapter for the {@link RecyclerView} used to view all the global recipes. This will allow
 * updates to the view live time with searches as well as remove recipes from the global space.
 * @see com.example.happymeals.recipe.PublicRecipeActivity
 */
public class GlobalRecipesAdapter extends
        RecyclerView.Adapter< GlobalRecipesAdapter.RecipeViewHolder> {

    /**
     * @author jeastgaa
     * @version 1.00.01
     * {@link androidx.recyclerview.widget.RecyclerView.ViewHolder} that allows the recycler to
     * hold views which data can then be sent to and displayed.
     */
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

            // Add button will act as a delete button for users who own recipe.
            detailButton = itemView.findViewById( R.id.shared_recipes_content_details_button );
            addButton = itemView.findViewById( R.id.shared_recipes_content_add_recipe_button );
        }
    }

    private ArrayList< Recipe > allRecipeList;
    private Context context;
    private boolean isUsers;

    /** Sorted list which will be used to display recipes on parent activity.
     * This list will deal with sorting/adding/removing/updating the recipes being displayed.
     */
    final SortedList< Recipe > sortedRecipeList = new SortedList<>(
            Recipe.class, new SortedList.Callback< Recipe >() {

        @Override
        public void onInserted(int position, int count) {
            notifyItemRangeInserted( position, count );
        }

        @Override
        public void onRemoved(int position, int count) {
            notifyItemRangeRemoved( position, count );
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
            notifyItemMoved( fromPosition, toPosition );
        }

        @Override
        public int compare(Recipe o1, Recipe o2) {
            return 0;
        }

        @Override
        public void onChanged(int position, int count) {
            notifyItemRangeChanged( position, count );
        }

        @Override
        public boolean areContentsTheSame(Recipe oldItem, Recipe newItem) {
            return oldItem.equals( newItem );
        }

        @Override
        public boolean areItemsTheSame(Recipe item1, Recipe item2) {
            return item1.getId() == item2.getId();
        }
    } );


    private final LayoutInflater inflater;
    public GlobalRecipesAdapter( Context context ) {
        this.inflater = LayoutInflater.from( context );
        this.allRecipeList = RecipeStorage.getInstance().getSharedRecipes();
        this.context = context;
        this.isUsers = false;
    }

    @NonNull
    @Override
    public GlobalRecipesAdapter.RecipeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
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

//        Recipe recipeInItem =
        final Recipe recipeInItem = sortedRecipeList.get( position );
        int indexOfItem = allRecipeList.indexOf( recipeInItem );

        nameView.setText( recipeInItem.getName() );
        creatorView.setText( recipeInItem.getCreator() );
        servingsView.setText( String.valueOf( recipeInItem.getServings() ) );

        Button addButton = holder.addButton;
        Button detailsButton = holder.detailButton;

        if( isUsers ) {
            addButton.setText("Delete");
        } else {
            addButton.setText("Add");
        }

        detailsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(  context, SharedRecipeDetailsActivity.class ) ;
                intent.putExtra( "Index", indexOfItem ) ;
                context.startActivity( intent ); ;
            }
        });

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                RecipeStorage storage = RecipeStorage.getInstance();
                if( isUsers ) {
                    RecipeStorage.getInstance().removeSharedRecipe( recipeInItem );
                    InputErrorFragment inputErrorFragment =
                            new InputErrorFragment(
                                    "Removed Shared Recipe",
                                    "You have removed " + recipeInItem.getName() +
                                            " from shared recipes",
                                    context
                            );
                    notifyArrayUpdate();
                    inputErrorFragment.display();
                } else {
                    if( !storage.alreadyHave( recipeInItem ) ) {
                        storage.addRecipe( recipeInItem );
                        InputErrorFragment inputErrorFragment =
                                new InputErrorFragment(
                                        "Added Shared Recipe",
                                        "You have added " + recipeInItem.getName() + " into your inventory",
                                        context
                                );
                        inputErrorFragment.display();
                    } else {
                        InputErrorFragment inputErrorFragment =
                                new InputErrorFragment(
                                        "Cannot Add Shared Recipe",
                                        "You have already added " + recipeInItem.getName() + " into your inventory",
                                        context
                                );
                        inputErrorFragment.display();
                    }
                }
                notifyDataSetChanged();
            }
        });
    }

    public void toggleUserRecipes() {
        isUsers = !isUsers;
        notifyArrayUpdate();
    }

    @Override
    public int getItemCount() {
        return sortedRecipeList.size();
    }

    public void notifyArrayUpdate() {
        ArrayList< Recipe > userRecipeList = new ArrayList<>();
        for( Recipe recipe : allRecipeList ) {
            if( recipe.getCreator().equals( RecipeStorage.getInstance().getCurrentUser() ) ) {
                userRecipeList.add( recipe );
            }
        }
        if( isUsers ) {
            replaceAll( userRecipeList );
        } else {
           replaceAll( allRecipeList );
        }
        notifyDataSetChanged();
    }

    public void add( Recipe recipe ) {
        sortedRecipeList.add( recipe );
    }

    public void remove( Recipe recipe ) {
        sortedRecipeList.remove( recipe );
    }

    public void add( ArrayList< Recipe > recipesArray ) {
        sortedRecipeList.addAll( recipesArray );
    }

    public void replaceAll( ArrayList< Recipe > recipeList ) {
        sortedRecipeList.beginBatchedUpdates();
        sortedRecipeList.clear();
        if( isUsers ) {
            for( Recipe recipe : recipeList ) {
                if( recipe.getCreator().equals( RecipeStorage.getInstance().getCurrentUser() ) ) {
                    sortedRecipeList.add( recipe );
                }
            }
        } else {
            sortedRecipeList.addAll( recipeList );
        }
        sortedRecipeList.endBatchedUpdates();
    }
}
