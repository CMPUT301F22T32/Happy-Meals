package com.example.happymeals.recipe;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.happymeals.R;
import com.example.happymeals.database.FireStoreManager;

/**
 * Class that holds the {@link AppCompatActivity} which will run the activity for the user.
 * This activity will show all the recipes saved by the user and allow for the start
 * of new activities. It will display simple details of the recipe and allow the user to launch
 * activity to create a new recipe.
 * @author bfiogbe
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

        recipeListView = (ListView) findViewById(R.id.recipe_list);
        //recipeStorage = new RecipeStorage();
        recipeStorageAdapter = new RecipeStorageAdapter(this, recipeStorage.getRecipes());

        recipeStorage = new RecipeStorage( fsm ){
            @Override
            public void updateStorage() {
                recipeStorageAdapter.notifyDataSetChanged();
            }
        };

        recipeStorageAdapter = new RecipeStorageAdapter(this, recipeStorage.getRecipes() );
        recipeListView.setAdapter(recipeStorageAdapter);
    }

    @Override
    public void finish() {
        super.finish();
    }

    public void onGoBack( View view ) {
        finish();
    }
}