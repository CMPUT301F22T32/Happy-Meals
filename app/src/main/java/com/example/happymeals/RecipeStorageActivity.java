package com.example.happymeals;

// recipe storage activity


import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

public class RecipeStorageActivity extends AppCompatActivity {
    private ListView recipeListView;
    private RecipeStorageAdapter recipeStorageAdapter;
    private RecipeStorage recipeStorage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_storage);

        recipeListView = (ListView) findViewById(R.id.recipe_list);
        recipeStorage = new RecipeStorage();
        recipeStorageAdapter = new RecipeStorageAdapter(this, recipeStorage);
        recipeListView.setAdapter(recipeStorageAdapter);
    }

}