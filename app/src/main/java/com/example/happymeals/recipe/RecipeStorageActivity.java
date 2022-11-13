package com.example.happymeals.recipe;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.happymeals.DatasetWatcher;
import com.example.happymeals.R;

/**
 * Class that holds the {@link AppCompatActivity} which will run the activity for the user.
 * This activity will show all the recipes saved by the user and allow for the start
 * of new activities. It will display simple details of the recipe and allow the user to launch
 * activity to create a new recipe.
 */
public class RecipeStorageActivity extends AppCompatActivity implements DatasetWatcher {

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
        Spinner RecipeFilter = (Spinner) findViewById(R.id.recipe_filter);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(RecipeStorageActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.dropdown_options));
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        RecipeFilter.setAdapter(dataAdapter);
    }



    public void signalChangeToAdapter() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void finish() {
        super.finish();
    }

    public void onGoBack( View view ) {
        finish();
    }
}