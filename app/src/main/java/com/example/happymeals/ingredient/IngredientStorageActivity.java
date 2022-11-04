package com.example.happymeals.ingredient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.happymeals.database.FireStoreManager;
import com.example.happymeals.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class IngredientStorageActivity extends AppCompatActivity {
    private ListView storageListView;
    private IngredientStorageArrayAdapter storageAdapter;
    private IngredientStorage ingredientStorage;

    @Override
    protected void onCreate( Bundle savedInstanceState)  {
        super.onCreate( savedInstanceState) ;
        setContentView( R.layout.activity_ingredient_storage ) ;

        ingredientStorage = new IngredientStorage( new FireStoreManager() ) ;

        storageListView = findViewById( R.id.storage_list) ;
        storageAdapter = new IngredientStorageArrayAdapter( this, ingredientStorage.getIngredients() ) ;
        storageListView.setAdapter( storageAdapter ) ;

        FloatingActionButton add_button = findViewById( R.id.add_new_ingredient_button) ;

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
}