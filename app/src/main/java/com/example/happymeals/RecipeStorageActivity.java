package com.example.happymeals;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * Class that holds the {@link AppCompatActivity} which will run the activity for the user.
 * This activity will show all the recipes saved by the user and allow for the start
 * of new activities. It will display simple details of the recipe and allow the user to launch
 * activity to create a new recipe.
 */
public class RecipeStorageActivity extends AppCompatActivity {
    private ListView recipeListView;
    private RecipeStorageAdapter recipeStorageAdapter;
    private RecipeStorage recipeStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_storage);
        FireStoreManager fsm = new FireStoreManager();
        recipeListView = findViewById(R.id.recipe_list);

        recipeStorage = new RecipeStorage( fsm ){
            @Override
            public void updateStorage() {
                recipeStorageAdapter.notifyDataSetChanged();
            }
        };

        recipeStorageAdapter = new RecipeStorageAdapter(this, recipeStorage.getRecipes() );
        recipeListView.setAdapter(recipeStorageAdapter);

        FloatingActionButton addButton = findViewById(R.id.AddRecipeButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                //TODO: Send intent for Recipe View Activity
                Intent intent = new Intent( RecipeStorageActivity.this, RecipeDetailsActivity.class );
                startActivity( intent );
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
    }

    public void onGoBack( View view ) {
        finish();
    }
}