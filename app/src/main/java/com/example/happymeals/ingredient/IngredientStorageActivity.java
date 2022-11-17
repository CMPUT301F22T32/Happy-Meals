package com.example.happymeals.ingredient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.happymeals.DatasetWatcher;
import com.example.happymeals.database.FireStoreManager;
import com.example.happymeals.R;
import com.example.happymeals.recipe.RecipeStorageActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class IngredientStorageActivity extends AppCompatActivity implements DatasetWatcher {
    private ListView storageListView;
    private IngredientStorageArrayAdapter storageAdapter;
    private IngredientStorage ingredientStorage;

    @Override
    protected void onCreate( Bundle savedInstanceState)  {
        super.onCreate( savedInstanceState) ;
        setContentView( R.layout.activity_ingredient_storage ) ;

        ingredientStorage = IngredientStorage.getInstance();

        storageListView = findViewById( R.id.storage_list) ;
        ingredientStorage.setListeningActivity(this);
        storageAdapter = new IngredientStorageArrayAdapter( this, ingredientStorage.getIngredients() ) ;
        storageListView.setAdapter( storageAdapter ) ;

        FloatingActionButton add_button = findViewById( R.id.add_new_ingredient_button) ;

        Spinner IngredientSort = findViewById(R.id.ingredient_filter);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(IngredientStorageActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.ingredient_options));
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        IngredientSort.setAdapter(dataAdapter);


        storageListView.setOnItemClickListener( new AdapterView.OnItemClickListener( )  {
            @Override
            public void onItemClick( AdapterView<?> adapterView, View view, int i, long l )  {
                startIngredientActivity( false, i) ;
            }
        }) ;
        
        add_button.setOnClickListener( new View.OnClickListener( )  {
            @Override
            public void onClick( View view)  {
                startIngredientActivity( true ) ;
            }
        }) ;
    }
    
    private void startIngredientActivity( boolean addingNewIngredient, int... index )  {
        Intent ingredientIntent = new Intent(  this, IngredientViewActivity.class ) ;
        ingredientIntent.putExtra( IngredientViewActivity.ADD_INGREDIENT, addingNewIngredient ) ;
        if ( index.length > 0 )
            ingredientIntent.putExtra( IngredientViewActivity.INGREDIENT_EXTRA, index[0] ) ;
        startActivity( ingredientIntent ) ;
    }

    @Override
    public void signalChangeToAdapter() {
        storageAdapter.notifyDataSetChanged();
    }
}