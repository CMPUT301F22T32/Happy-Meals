package com.example.happymeals.recipe;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happymeals.R;
import com.example.happymeals.adapters.GlobalRecipesAdapter;
import com.example.happymeals.database.DatasetWatcher;

import java.util.ArrayList;

public class PublicRecipeActivity extends AppCompatActivity implements DatasetWatcher {

    ArrayList< Recipe > allRecipes;
    GlobalRecipesAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_recipes);

        RecipeStorage recipeStorage = RecipeStorage.getInstance();

        recipeStorage.updateSharedRecipes();
        recipeStorage.setSharedListeningActivity( this );

        RecyclerView listOfRecipesView = findViewById( R.id.public_recipes_results_list );

        allRecipes = recipeStorage.getSharedRecipes();
        adapter = new GlobalRecipesAdapter( allRecipes, this );
        listOfRecipesView.setAdapter( adapter );
        listOfRecipesView.setLayoutManager( new LinearLayoutManager(this));

    }

    @Override
    public void signalChangeToAdapter() {
        adapter.notifyDataSetChanged();
    }
}
