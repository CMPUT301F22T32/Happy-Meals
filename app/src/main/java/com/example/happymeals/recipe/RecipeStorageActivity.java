package com.example.happymeals.recipe;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

<<<<<<< HEAD:app/src/main/java/com/example/happymeals/recipe/RecipeStorageActivity.java
import com.example.happymeals.R;

=======
/**
 * Class that holds the {@link AppCompatActivity} which will run the activity for the user.
 * This activity will show all the recipes saved by the user and allow for the start
 * of new activities. It will display simple details of the recipe and allow the user to launch
 * activity to create a new recipe.
 */
public class RecipeStorageActivity extends AppCompatActivity implements DatasetWatcher {
>>>>>>> origin:app/src/main/java/com/example/happymeals/RecipeStorageActivity.java
public class RecipeStorageActivity extends AppCompatActivity {
    private ListView recipeListView;
    private RecipeStorage recipeStorage;
    private RecipeStorageAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_storage);
        recipeListView = findViewById(R.id.recipe_list);
        recipeStorage = RecipeStorage.getInstance();
        recipeStorage.setListeningActivity(this);
        adapter = new RecipeStorageAdapter(this, recipeStorage.getRecipes());
        recipeListView.setAdapter(adapter);
    }

<<<<<<< HEAD:app/src/main/java/com/example/happymeals/recipe/RecipeStorageActivity.java
        recipeListView = (ListView) findViewById(R.id.recipe_list);
        recipeStorage = new RecipeStorage();
        recipeStorageAdapter = new RecipeStorageAdapter(this, recipeStorage.getRecipes());
=======
        recipeStorage = new RecipeStorage( fsm ){
            @Override
            public void updateStorage() {
                recipeStorageAdapter.notifyDataSetChanged();
            }
        };

    public void signalChangeToAdapter() {
        adapter.notifyDataSetChanged();
        recipeStorageAdapter = new RecipeStorageAdapter(this, recipeStorage.getRecipes() );
>>>>>>> origin:app/src/main/java/com/example/happymeals/RecipeStorageActivity.java
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