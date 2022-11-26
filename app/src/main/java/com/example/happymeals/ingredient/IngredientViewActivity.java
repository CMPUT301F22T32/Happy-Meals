package com.example.happymeals.ingredient;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.happymeals.Constants;
import com.example.happymeals.InputValidator;
import com.example.happymeals.R;
import com.example.happymeals.fragments.InputErrorFragment;
import com.example.happymeals.fragments.ModifyConfirmationFragment;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class IngredientViewActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private EditText name;
    private EditText description;
    private EditText date;
    private Date dateArg;
    private EditText quantity;
    private Spinner unitSpinner;
    private Spinner locationSpinner;
    private Spinner categorySpinner;

    private Button saveButton;
    private Button deleteButton;
    private Button datePickerButton;

    private Context context;

    protected final static String ADD_INGREDIENT = "com.example.happymeals.addIngredientBooleanExtra";
    protected final static String INGREDIENT_EXTRA = "com.example.happymeals.ingredientExtra";

    private Ingredient ingredient = null;
    private Boolean newIngredient;

    private IngredientStorage ingredientStorage;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_ingredient_view);

        // won't need new instance after we have the singleton ingredient storage initialized in
        // main class
        ingredientStorage = IngredientStorage.getInstance();

        Intent intent = getIntent();
        context = this;

        name = findViewById( R.id.ing_content_name_input );
        description = findViewById( R.id.ing_content_desc_input);
        date = findViewById( R.id.ing_content_date_input );
        quantity = findViewById( R.id.ing_content_quantity_input );
        unitSpinner = findViewById( R.id.ing_content_unit_input );
        locationSpinner = findViewById( R.id.ing_content_location_input );
        categorySpinner = findViewById( R.id.ing_content_category_input );

        saveButton = findViewById(R.id.ing_view_save_button);
        deleteButton = findViewById(R.id.ing_view_delete_button);
        datePickerButton = findViewById(R.id.ingredient_date_button);

        // Populate the spinners
        populateSpinners();

        newIngredient = intent.getBooleanExtra( ADD_INGREDIENT, false );
        if ( !newIngredient )
            fillFields(intent);
        else
            deleteButton.setVisibility(View.GONE);

        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ModifyConfirmationFragment deleteFragment = new ModifyConfirmationFragment(
                        "Remove Ingredient",
                        String.format("Are you sure you want to remove %s?", ingredient.getDescription()),
                        context,
                        getDeleteListener());
                deleteFragment.display();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkInput();
            }
        });

        datePickerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                int year = c.get(Calendar.YEAR);
                int month = c.get(Calendar.MONTH);
                int day = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                dateArg = new Date(year, month, day);
                                date.setText(year + "-" + (monthOfYear + 1) + "-" + day);

                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

    }

    private void populateSpinners() {
        IngredientStorage ingredientStorage = IngredientStorage.getInstance();

        ArrayAdapter<String> unitAdapter = new ArrayAdapter<>( this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                ingredientStorage.getSpinners( Constants.StoredSpinnerChoices.AMOUNT_UNIT) );

        ArrayAdapter<String> locationAdapter = new ArrayAdapter<>( this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                ingredientStorage.getSpinners( Constants.StoredSpinnerChoices.LOCATION) );

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>( this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                ingredientStorage.getSpinners( Constants.StoredSpinnerChoices.INGREDIENT_CATEGORY ) );

        unitSpinner.setAdapter( unitAdapter );
        locationSpinner.setAdapter( locationAdapter );
        categorySpinner.setAdapter( categoryAdapter );
    }

    private void fillFields( Intent intent ) {
        //Integer ingredientIndex = intent.getIntExtra( INGREDIENT_EXTRA,  0);
        ingredient = (Ingredient) intent.getSerializableExtra(INGREDIENT_EXTRA);
        //ingredient = ingredientStorage.getIngredients().get( ingredientIndex );

        name.setText( ingredient.getName() );
        name.setEnabled( false );
        description.setText( ingredient.getDescription() );
        date.setText( ingredient.getBestBeforeDateAsString() );
        quantity.setText( Integer.toString(ingredient.getAmount()) );
        unitSpinner.setSelection(ingredientStorage.getSpinners( Constants.StoredSpinnerChoices.AMOUNT_UNIT)
                .indexOf( ingredient.getUnit()));
        locationSpinner.setSelection( ingredientStorage.getSpinners( Constants.StoredSpinnerChoices.LOCATION)
                .indexOf( ingredient.getLocation()));
        categorySpinner.setSelection( ingredientStorage.getSpinners( Constants.StoredSpinnerChoices.INGREDIENT_CATEGORY )
                .indexOf( ingredient.getCategory()) );
    }

    private void checkInput() {
        InputValidator validator = new InputValidator();
        String errorString = "The ingredient couldn't be saved for the following reasons:\n";

        validator.checkText( name, "Name");
        validator.checkDate( date );
        validator.checkNum( quantity, "Quantity" );
        validator.checkSpinner( unitSpinner, "Quantity Unit" );
        validator.checkSpinner( locationSpinner, "DefaultLocationSpinners" );
        validator.checkSpinner( categorySpinner, "Category" );

        String errors = validator.getErrors();
        if (errors != null) {
            errorString += errors;
            InputErrorFragment errorFragment = new InputErrorFragment( "Ingredient Save Error", errorString, this );
            errorFragment.display();
        }
        else {
            ModifyConfirmationFragment saveFragment = new ModifyConfirmationFragment(
                    "Save Ingredient",
                    "Do you wish to save your changes?",
                    this,
                    getSaveListener());
            saveFragment.display();
        }
    }

    private DialogInterface.OnClickListener getSaveListener() {

        return new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                String nameArg = name.getText().toString();
                String descriptionArg = description.getText().toString();
                // <todo> Change date to a fragment
                Date dateArg = new Date();
                String locationArg = (String) locationSpinner.getSelectedItem();
                int amountArg = Integer.parseInt(quantity.getText().toString());
                String amountUnitArg = (String) unitSpinner.getSelectedItem();
                String categoryArg = (String) categorySpinner.getSelectedItem();

                if (ingredient == null) {
                    ingredient = new Ingredient(nameArg, descriptionArg, dateArg , locationArg, amountArg, amountUnitArg, categoryArg);
                    ingredientStorage.addIngredient(ingredient);
                }

                else {
                    ingredient.setName( nameArg );
                    ingredient.setDescription( descriptionArg );
                    ingredient.setBestBeforeDate( dateArg );
                    ingredient.setLocation( locationArg );
                    ingredient.setAmount( amountArg );
                    ingredient.setUnit( amountUnitArg );
                    ingredient.setCategory( categoryArg );

                    ingredientStorage.updateIngredient(ingredient);
                }

                // go back to ingredient storage view after saving.
                finish();
            }
        };
    }

    private DialogInterface.OnClickListener getDeleteListener() {

        return new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ingredientStorage.removeIngredient(ingredient);
                finish();
            }
        };
    }


    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        dateArg = new Date(year, month, day);
        date.setText(String.format("%04d-%02d-%02d", year, month, day));
    }
}
