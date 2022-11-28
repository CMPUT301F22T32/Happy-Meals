package com.example.happymeals;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.happymeals.adapters.SpinnerViewAdapter;
import com.example.happymeals.fragments.InputStringFragment;
import com.example.happymeals.ingredient.IngredientStorage;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * @author jeastgaa
 * @version 1.00.01
 * Allows user to add or remove spinners used in the ingredients amount values.
 */
public class SpinnerSettingsActivity extends AppCompatActivity implements
        InputStringFragment.InputStringFragmentListener {

    private ArrayList<String> amountSpinnerArray;
    private ArrayList< String > locationSpinnerArray;
    private ArrayList< String > categorySpinnerArray;

    private SpinnerViewAdapter amountSpinnerAdapter;
    private SpinnerViewAdapter locationSpinnerAdapter;
    private SpinnerViewAdapter categorySpinnerAdapter;

    private IngredientStorage ingredientStorage;

    private FloatingActionButton addAmountButton;
    private FloatingActionButton addLocationButton;
    private FloatingActionButton addCategoryButton;

    private Constants.StoredSpinnerChoices expecting;

    /**
     * {@inheritDoc}
     */
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_edit_spinners );

        expecting = null;
        ingredientStorage = IngredientStorage.getInstance();

        amountSpinnerArray = ingredientStorage.getSpinners( 
                Constants.StoredSpinnerChoices.AMOUNT_UNIT
        );
        locationSpinnerArray = ingredientStorage.getSpinners( 
                Constants.StoredSpinnerChoices.LOCATION
        );
        categorySpinnerArray = ingredientStorage.getSpinners( 
                Constants.StoredSpinnerChoices.INGREDIENT_CATEGORY
        );

        addAmountButton = findViewById( R.id.spinner_add_amount_button );
        addLocationButton = findViewById( R.id.spinner_add_location_button );
        addCategoryButton = findViewById( R.id.spinner_add_category_button );

        ListView amountSpinnerList = findViewById( R.id.edit_spinner_amount_list );
        ListView locationSpinnerList = findViewById( R.id.edit_spinner_location_list );
        ListView categorySpinnerList = findViewById( R.id.edit_spinner_category_list );

        amountSpinnerAdapter = new SpinnerViewAdapter( this, amountSpinnerArray,
                Constants.StoredSpinnerChoices.AMOUNT_UNIT );
        locationSpinnerAdapter = new SpinnerViewAdapter( this, locationSpinnerArray,
                Constants.StoredSpinnerChoices.LOCATION );
        categorySpinnerAdapter = new SpinnerViewAdapter( this, categorySpinnerArray,
                Constants.StoredSpinnerChoices.INGREDIENT_CATEGORY );

        amountSpinnerList.setAdapter( amountSpinnerAdapter );
        locationSpinnerList.setAdapter( locationSpinnerAdapter );
        categorySpinnerList.setAdapter( categorySpinnerAdapter );

        addAmountButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                expecting = Constants.StoredSpinnerChoices.AMOUNT_UNIT;
                new InputStringFragment( "Input New Amount Spinner", 10 ).show( getSupportFragmentManager(), "L E S F" );
            }
        } );

        addCategoryButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                expecting = Constants.StoredSpinnerChoices.INGREDIENT_CATEGORY;
                new InputStringFragment( "Input New Category Spinner", 10 ).show( getSupportFragmentManager(), "L E S F" );
            }
        } );

        addLocationButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                expecting = Constants.StoredSpinnerChoices.LOCATION;
                new InputStringFragment( "Input New DefaultLocationSpinners Spinner", 10 ).show( getSupportFragmentManager(), "L E S F" );
            }
        } );
    }

    /**
     * This will remove the entry at input 'i' from the list of spinners.
     * This is done through the {@link IngredientStorage} and will update the database.
     * @param i The {@link Integer} that holds the index of the string being rmeoved.
     */
    public void removeSpinnerFromList( Constants.StoredSpinnerChoices choice, int i ) {
        IngredientStorage.getInstance().removeSpinner( choice, i );
        if( choice == Constants.StoredSpinnerChoices.AMOUNT_UNIT ) {
            amountSpinnerArray.remove( i );
            amountSpinnerAdapter.notifyDataSetChanged();
        } else if ( choice == Constants.StoredSpinnerChoices.LOCATION ) {
            locationSpinnerArray.remove( i );
            locationSpinnerAdapter.notifyDataSetChanged();
        } else if ( choice == Constants.StoredSpinnerChoices.INGREDIENT_CATEGORY ) {
            categorySpinnerArray.remove( i );
            categorySpinnerAdapter.notifyDataSetChanged();
        }
    }

    /**
     * This is part of the
     * {@link com.example.happymeals.fragments.InputStringFragment.InputStringFragmentListener}
     * and will be responsible for adding the given string to the database and the list of
     * available spinners through the {@link IngredientStorage}
     * @param str The passed {@link String}
     */
    @Override
    public void onConfirmClick( String str ) {
        IngredientStorage.getInstance().addSpinner( expecting, str );

        if( expecting == Constants.StoredSpinnerChoices.AMOUNT_UNIT ) {
            amountSpinnerArray.clear();
            amountSpinnerArray.addAll( 
                    ingredientStorage.getSpinners( 
                            Constants.StoredSpinnerChoices.AMOUNT_UNIT
                    )
            );
            amountSpinnerAdapter.notifyDataSetChanged();
        } else if ( expecting == Constants.StoredSpinnerChoices.LOCATION ) {
            locationSpinnerArray.clear();
            locationSpinnerArray.addAll( 
                    ingredientStorage.getSpinners( 
                            Constants.StoredSpinnerChoices.LOCATION
                    )
            );
            locationSpinnerAdapter.notifyDataSetChanged();
        } else if ( expecting == Constants.StoredSpinnerChoices.INGREDIENT_CATEGORY ) {
            categorySpinnerArray.clear();
            categorySpinnerArray.addAll( 
                    ingredientStorage.getSpinners( 
                            Constants.StoredSpinnerChoices.INGREDIENT_CATEGORY
                    )
            );
            categorySpinnerAdapter.notifyDataSetChanged();
        }
        expecting = null;
    }
}
