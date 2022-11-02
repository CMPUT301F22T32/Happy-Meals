package com.example.happymeals;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

public class IngredientStorageActivity extends AppCompatActivity {
    private ListView storageListView;
    private StorageArrayAdapter storageAdapter;
    private IngredientStorage storage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredient_storage);

//        storage = new IngredientStorage();
        // TODO: pull from database


        storageListView = findViewById(R.id.storage_list);
        storageAdapter = new StorageArrayAdapter(this, storage.getIngredients());
        storageListView.setAdapter(storageAdapter);





    }
}