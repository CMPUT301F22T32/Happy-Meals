package com.example.happymeals;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.happymeals.fragments.FragmentAction;
import com.example.happymeals.fragments.InputErrorFragment;
import com.example.happymeals.fragments.ModifyConfirmationFragment;

import java.util.Arrays;

public class IngredientViewActivity extends AppCompatActivity implements ModifyConfirmationFragment.ModifyFragmentInteractionListener {

    private EditText description;
    private EditText date;
    private EditText quantity;
    private Spinner unitSpinner;
    private Spinner locationSpinner;
    private Spinner categorySpinner;

    private Button saveButton;
    private Button deleteButton;

    private Context context;

    protected final static String ADD_INGREDIENT = "com.example.happymeals.addIngredientBooleanExtra";
    protected final static String INGREDIENT_EXTRA = "com.example.happymeals.ingredientExtra";

    private Ingredient ingredient = null;
    private Boolean newIngredient;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingredient_content_view);

        Intent intent = getIntent();
        context = this;

        description = findViewById( R.id.ing_content_desc_input );
        date = findViewById( R.id.ing_content_date_input );
        quantity = findViewById( R.id.ing_content_quantity_input );
        unitSpinner = findViewById( R.id.ing_content_unit_input );
        locationSpinner = findViewById( R.id.ing_content_location_input );
        categorySpinner = findViewById( R.id.ing_content_category_input );

        saveButton = findViewById(R.id.ing_view_save_button);
        deleteButton = findViewById(R.id.ing_view_delete_button);

        // Populate the spinners
        ArrayAdapter<AmountUnit> unitAdapter = new ArrayAdapter<>( this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, AmountUnit.values() );
        ArrayAdapter<Location> locationAdapter = new ArrayAdapter<>( this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, Location.values() );
        ArrayAdapter<IngredientCategory> categoryAdapter = new ArrayAdapter<>( this, androidx.appcompat.R.layout.support_simple_spinner_dropdown_item, IngredientCategory.values() );

        System.out.println("\nSHOULD BE RIGHT AFTER THIS\n");
        for (AmountUnit u : AmountUnit.values()) {
            System.out.println(u.name());
        }

        unitSpinner.setAdapter( unitAdapter );
        locationSpinner.setAdapter( locationAdapter );
        categorySpinner.setAdapter( categoryAdapter );

        newIngredient = intent.getBooleanExtra( ADD_INGREDIENT, false );
        if ( newIngredient )
            fillFields(intent);
        else
            deleteButton.setVisibility(View.GONE);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ModifyConfirmationFragment.newInstance(
                                "Remove Ingredient",
                                String.format("Are you sure you want to remove %s?", ingredient.getDescription()),
                                FragmentAction.Delete)
                                .show(getSupportFragmentManager(), "REMOVE_INGREDIENT");
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkInput();
            }
        });

    }

    private void fillFields( Intent intent ) {
        ingredient = ( Ingredient ) intent.getSerializableExtra( INGREDIENT_EXTRA );
        description.setText( ingredient.getDescription() );
        date.setText( ingredient.getBestBeforeDate() );
        quantity.setText( ingredient.getAmount() );
        unitSpinner.setSelection(Arrays.asList( AmountUnit.values() ).indexOf( ingredient.getUnit() ) );
        locationSpinner.setSelection( Arrays.asList( Location.values() ).indexOf( ingredient.getLocation() ) );
        categorySpinner.setSelection(Arrays.asList( IngredientCategory.values() ).indexOf( ingredient.getCategory() ) );
    }

    private void checkInput() {
        InputValidator validator = new InputValidator();
        String errorString = "The ingredient couldn't be saved for the following reasons:\n";

        validator.checkText( description, "Description" );
        validator.checkDate( date );
        validator.checkNum( quantity, "Quantity" );
        validator.checkSpinner( unitSpinner, "Quantity Unit" );
        validator.checkSpinner( locationSpinner, "Location" );
        validator.checkSpinner( categorySpinner, "Category" );

        String errors = validator.getErrors();
        if (errors != null) {
            errorString += errors;
            InputErrorFragment errorFragment = new InputErrorFragment( "Ingredient Save Error", errorString, this );
            errorFragment.display();
        }
        else {
            ModifyConfirmationFragment.newInstance(
                    "Save Ingredient",
                    "Do you wish to save your changes?",
                    FragmentAction.Save)
                    .show(getSupportFragmentManager(), "SAVE_INGREDIENT");
        }
    }

    private void updateIngredient() {
        String descriptionArg = description.getText().toString();
        String dateArg = date.getText().toString();
        Location locationArg = (Location) locationSpinner.getSelectedItem();
        int amountArg = Integer.parseInt(quantity.getText().toString());
        AmountUnit amountUnitArg = (AmountUnit) unitSpinner.getSelectedItem();
        IngredientCategory categoryArg = (IngredientCategory) categorySpinner.getSelectedItem();

        if (ingredient == null)
            ingredient = new Ingredient( descriptionArg, dateArg, locationArg, amountArg, amountUnitArg, categoryArg );
        else {
            ingredient.setDescription( descriptionArg );
            ingredient.setBestBeforeDate( dateArg );
            ingredient.setLocation( locationArg );
            ingredient.setAmount( amountArg );
            ingredient.setUnit( amountUnitArg );
            ingredient.setCategory( categoryArg );
        }
    }

    @Override
    public void onConfirmPressed(FragmentAction action) {
        if (FragmentAction.Save == action) {
            updateIngredient();
            // TODO: update/add ingredient to DB
        }

        else if (FragmentAction.Delete == action) {
            // TODO: remove ingredient from DB
        }
    }
}
