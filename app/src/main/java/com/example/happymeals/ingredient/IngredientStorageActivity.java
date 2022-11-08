package com.example.happymeals.ingredient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.happymeals.database.DatasetWatcher;
import com.example.happymeals.database.FireStoreManager;
import com.example.happymeals.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * This is the activity that will display a list of the ingredients in the storage. The list will
 * be persistent and display ingredient data that has been stored in the database. Ingredients can
 * be selected to view details, and new ingredients can be added by hitting the plus button.
 * @author kstark sruduke
 */
public class IngredientStorageActivity extends AppCompatActivity implements DatasetWatcher {

    /**
     * The {@link ListView} that displays the {@link Ingredient} objects currently stored.
     */
    private ListView storageListView;

    /**
     * The {@link IngredientStorageArrayAdapter} used to display the brief details of each
     * {@link Ingredient}.
     */
    private IngredientStorageArrayAdapter storageAdapter;

    /**
     * //TODO: fill in after singleton impl
     */
    private IngredientStorage ingredientStorage;

    /**
     * This function is called whenever the activity is spawned; it initializes the {@link #storageAdapter}
     * and {@link #storageListView} to properly display the polled data. Action listeners are also set
     * in this step; clicking on an {@link Ingredient} will open a view containing ingredient details
     * while clicking the plus button in the corner will open a view to add a new ingredient.
     * @param savedInstanceState The state to restore the view to (if applicable).
     */
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

    /**
     * This function facilitates the process of creating an intent for the edit/view activity for an
     * {@link Ingredient}. The parameters represent the extras to pass to the {@link IngredientViewActivity}
     * and the function starts the activity.
     * @param addingNewIngredient true when the user is adding a new {@link Ingredient}, false when the user
     *                            is trying to view an existing one. ({@link Boolean})
     * @param index optional parameter representing the index of the ingredient in the {@link IngredientStorage}
     *              list, which the {@link IngredientViewActivity} will use to get {@link Ingredient} data.
     */
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