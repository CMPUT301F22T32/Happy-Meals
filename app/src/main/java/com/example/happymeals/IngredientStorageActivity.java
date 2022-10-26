package com.example.happymeals;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

public class IngredientStorageActivity extends AppCompatActivity {
    private ListView storageListView;
    private StorageArrayAdapter storageAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_storage);

        ArrayList<Ingredient> storage = new ArrayList<>();
        // TODO: pull from database
        // remove from here
        storage.add(new Ingredient("Apple", "01-11-2022", "Pantry", 2, "count", "Fruit"));
        // remove to here

        storageListView = findViewById(R.id.storage_list);
        storageAdapter = new StorageArrayAdapter(this, storage);
        storageListView.setAdapter(storageAdapter);





    }
}