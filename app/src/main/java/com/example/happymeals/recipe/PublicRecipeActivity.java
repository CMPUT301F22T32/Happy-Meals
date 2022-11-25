package com.example.happymeals.recipe;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;

import com.example.happymeals.R;
import com.example.happymeals.adapters.GlobalRecipesAdapter;
import com.example.happymeals.database.DatasetWatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class PublicRecipeActivity extends AppCompatActivity
        implements DatasetWatcher, SearchView.OnQueryTextListener {

    private static final Comparator<Recipe> ALPHABETICAL_COMPARATOR = new Comparator<Recipe>() {
        @Override
        public int compare(Recipe a, Recipe b) {
            return a.getName().compareTo(b.getName());
        }
    };

    ArrayList< Recipe > allRecipes;
    GlobalRecipesAdapter adapter;
    Button toggleButton;

    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_recipes);

        RecipeStorage recipeStorage = RecipeStorage.getInstance();

        recipeStorage.updateSharedRecipes();
        recipeStorage.setSharedListeningActivity( this );

        RecyclerView listOfRecipesView = findViewById( R.id.public_recipes_results_list );
        toggleButton = findViewById( R.id.public_recipes_see_user_recipes_button );

        searchView = findViewById( R.id.search_recipes_search_field );
        searchView.setOnQueryTextListener( this );

        allRecipes = recipeStorage.getSharedRecipes();
        adapter = new GlobalRecipesAdapter( this);
        listOfRecipesView.setAdapter( adapter );

        adapter.add( allRecipes );
        listOfRecipesView.setLayoutManager( new LinearLayoutManager(this));

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
    public boolean onQueryTextSubmit(String s) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String s) {
        final ArrayList< Recipe > filteredRecipeList = filter( allRecipes, s );
        adapter.replaceAll( filteredRecipeList );
        return true;
    }

    private static ArrayList< Recipe > filter( ArrayList< Recipe > recipes, String query ) {
        final String[] strQList = query.toLowerCase().split(" ");
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
