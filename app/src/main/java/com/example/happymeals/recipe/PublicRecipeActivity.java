package com.example.happymeals.recipe;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happymeals.HappyMealBottomNavigation;
import com.example.happymeals.R;
import com.example.happymeals.adapters.GlobalRecipesAdapter;
import com.example.happymeals.database.DatasetWatcher;
import com.example.happymeals.ingredient.Ingredient;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;


/**
 * Activity that displays all the recipes published by other users.
 * Allows users to pull other recipes made by other users, or push
 * recipes of their own to the global pool. A unique feature that allows for
 * sharing between users.
 */
public class PublicRecipeActivity extends AppCompatActivity
        implements DatasetWatcher, SearchView.OnQueryTextListener {

    private static final Comparator<Recipe> ALPHABETICAL_COMPARATOR = new Comparator<Recipe>() {
        @Override
        public int compare( Recipe a, Recipe b ) {
            return a.getName().compareTo( b.getName() );
        }
    };
    /**
     * The {@link ArrayList} that stores all the {@link Recipe} objects.,
     */
    ArrayList< Recipe > allRecipes;
    GlobalRecipesAdapter adapter;
    Button toggleButton;

    private SearchView searchView;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_shared_recipes );

        RecipeStorage recipeStorage = RecipeStorage.getInstance();

        recipeStorage.updateSharedRecipes();
        recipeStorage.setSharedListeningActivity( this );

        RecyclerView listOfRecipesView = findViewById( R.id.public_recipes_results_list );
        toggleButton = findViewById( R.id.public_recipes_see_user_recipes_button );

        searchView = findViewById( R.id.search_recipes_search_field );
        searchView.setOnQueryTextListener( this );

        allRecipes = recipeStorage.getSharedRecipes();
        adapter = new GlobalRecipesAdapter( this );
        listOfRecipesView.setAdapter( adapter );

        adapter.add( allRecipes );
        listOfRecipesView.setLayoutManager( new LinearLayoutManager( this ) );

        HappyMealBottomNavigation bottomNavMenu =
                new HappyMealBottomNavigation( 
                        findViewById( R.id.bottomNavigationView ), this, R.id.recipe_menu );


        bottomNavMenu.setupBarListener();
    }

    public void toggleUserRecipes( View view ) {
        adapter.toggleUserRecipes();
        String currentMessage = toggleButton.getText().toString();
        if( currentMessage.equals( getString( R.string.see_personal_recipes ) ) ) {
            toggleButton.setText( R.string.see_all_recipes );
        } else {
            toggleButton.setText( R.string.see_personal_recipes );
        }

    }

    @Override
    public void signalChangeToAdapter() {
        adapter.replaceAll( allRecipes );
    }

    @Override
    public boolean onQueryTextSubmit( String s ) {
        return false;
    }

    @Override
    public boolean onQueryTextChange( String s ) {
        final ArrayList< Recipe > filteredRecipeList = filter( allRecipes, s );
        adapter.replaceAll( filteredRecipeList );
        return true;
    }

    public void updateAdapter() {
        adapter.notifyDataSetChanged();
    }
    private static ArrayList< Recipe > filter( ArrayList< Recipe > recipes, String query ) {
        final String[] strQList = query.toLowerCase().split( " " );
        final ArrayList< Recipe > filteredRecipes = new ArrayList<>();
        for( Recipe recipe : recipes ) {
            final String text = recipe.getName().toLowerCase() + recipe.getCreator().toLowerCase();
            if( Arrays.stream( strQList ).anyMatch( text::contains ) ) {
                filteredRecipes.add( recipe );
            }
        }
        return filteredRecipes;
    }
}
