package com.example.happymeals.ingredient;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.happymeals.R;

public class IngredientStorageActivity extends AppCompatActivity {
    private ListView storageListView;
    private IngredientStorageArrayAdapter storageAdapter;
    private IngredientStorage storage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_storage);

//        storage = new IngredientStorage();
        // TODO: pull from database


        storageListView = findViewById(R.id.storage_list);
        storageAdapter = new IngredientStorageArrayAdapter(this, storage.getIngredients());
        storageListView.setAdapter(storageAdapter);





    }
}