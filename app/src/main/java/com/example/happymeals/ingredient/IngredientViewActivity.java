package com.example.happymeals.ingredient;

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

import com.example.happymeals.Constants;
import com.example.happymeals.database.FireStoreManager;
import com.example.happymeals.InputValidator;
import com.example.happymeals.R;
import com.example.happymeals.fragments.InputErrorFragment;
import com.example.happymeals.fragments.ModifyConfirmationFragment;

import java.time.Instant;
import java.util.Arrays;
import java.util.Date;

/**
 * This view allows the user to add, edit, and delete ingredients. It checks for valid user input
 * and upon success, uploads information to the database.
 * @author sruduke
 */
public class IngredientViewActivity extends AppCompatActivity {

    // User input components
    private EditText name;
    private EditText description;
    private EditText date;
    private EditText quantity;
    private Spinner unitSpinner;
    private Spinner locationSpinner;
    private Spinner categorySpinner;

    // Buttons
    private Button saveButton;
    private Button deleteButton;

    // Context used for fragments
    private Context context;

    // String keys for the Extras
    protected final static String ADD_INGREDIENT = "com.example.happymeals.addIngredientBooleanExtra";
    protected final static String INGREDIENT_EXTRA = "com.example.happymeals.ingredientExtra";
    // Extra values
    private Ingredient ingredient = null;
    private Boolean newIngredient;

    // Ingredient Storage
    private IngredientStorage ingredientStorage;

    /**
     * This function is called whenever the activity is spawned. If viewing an existing {@link Ingredient},
     * it will fill the input fields with their stored values and allow the user to modify or delete
     * it. If adding a new {@link Ingredient}, the fields will be empty/at their default values and the user
     * can add data accordingly. This function will format the activity based on the extras passed
     * through the {@link Intent}. It also sets the action listeners for buttons on the view.
     * @param savedInstanceState The state to restore the view to (if applicable).
     */
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingredient_content_view);

        // won't need new instance after we have the singleton ingredient storage initialized in
        // main class
        ingredientStorage = new IngredientStorage(new FireStoreManager());

        // Find IDs for user input components
        name = findViewById( R.id.ing_content_name_input );
        description = findViewById( R.id.ing_content_desc_input);
        date = findViewById( R.id.ing_content_date_input );
        quantity = findViewById( R.id.ing_content_quantity_input );
        unitSpinner = findViewById( R.id.ing_content_unit_input );
        locationSpinner = findViewById( R.id.ing_content_location_input );
        categorySpinner = findViewById( R.id.ing_content_category_input );

        // Find IDs for buttons
        saveButton = findViewById(R.id.ing_view_save_button);
        deleteButton = findViewById(R.id.ing_view_delete_button);

        // Populate the spinners with their possible values
        populateSpinners();

        // Examine the extras that were passed and determine if we are adding a new ingredient
        // or viewing an existing one.
        Intent intent = getIntent();
        context = this;

        newIngredient = intent.getBooleanExtra( ADD_INGREDIENT, false );
        // If viewing an existing ingredient, populate the input fields with their stored values
        if ( !newIngredient )
            fillFields(intent);
        // If adding a new ingredient, leave the input fields empty and hide the delete button.
        else
            deleteButton.setVisibility(View.GONE);

        // Set the delete button to display a confirmation fragment and delete the ingredient
        // upon user approval.
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

        // Set the save button the display a confirmation fragment and save the ingredient if all
        // inputs are valid.
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkInput();
            }
        });

    }

    /**
     * This function creates an {@link ArrayAdapter} for each spinner on the view (anything with
     * an enumerated value in {@link Constants}. It then sets the adapter for the corresponding
     * component to populate the spinners with their default values.
     */
    private void populateSpinners() {

        ArrayAdapter<Constants.AmountUnit> unitAdapter = new ArrayAdapter<>( this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                Constants.AmountUnit.values() );

        ArrayAdapter<Constants.Location> locationAdapter = new ArrayAdapter<>( this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                Constants.Location.values() );

        ArrayAdapter<Constants.IngredientCategory> categoryAdapter = new ArrayAdapter<>( this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                Constants.IngredientCategory.values() );

        unitSpinner.setAdapter( unitAdapter );
        locationSpinner.setAdapter( locationAdapter );
        categorySpinner.setAdapter( categoryAdapter );
    }

    /**
     * This function is called only if {@link #newIngredient} is true from the {@link Intent} passed
     * from the {@link IngredientStorageActivity}. In other words, this is executed when viewing
     * an ingredient that is already in the storage. It fetches the ingredient from te storage
     * and populates all the input fields with their corresponding value as saved in the DB.
     * @param intent The {@link Intent} from which this activity was spawned.
     */
    private void fillFields( Intent intent ) {
        // Get the Ingredient data
        Integer ingredientIndex = intent.getIntExtra( INGREDIENT_EXTRA,  0);
        ingredient = ingredientStorage.getIngredients().get(ingredientIndex);

        // Set the fields to their corresponding values
        name.setText( ingredient.getName() );
        description.setText( ingredient.getDescription() );
        date.setText( ingredient.getBestBeforeDate().toString() );
        quantity.setText( Integer.toString(ingredient.getAmount()) );
        unitSpinner.setSelection(Arrays.asList( Constants.AmountUnit.values() ).indexOf( ingredient.getUnit() ) );
        locationSpinner.setSelection( Arrays.asList( Constants.Location.values() ).indexOf( ingredient.getLocation() ) );
        categorySpinner.setSelection(Arrays.asList( Constants.IngredientCategory.values() ).indexOf( ingredient.getCategory() ) );
    }

    /**
     * This function is called whenever a user wishes to save. It uses the {@link InputValidator} class
     * to check every component in the view for proper input. If all fields are valid, a fragment will
     * appear to confirm the save; else, a fragment signifying which fields need to be fixed will appear.
     */
    private void checkInput() {
        // Instantiate a new instance of the validator
        InputValidator validator = new InputValidator();
        String errorString = "The ingredient couldn't be saved for the following reasons:\n";

        // Check every single component
        validator.checkText( name, "Name");
        //validator.checkText( description, "Description" );
        validator.checkDate( date );
        validator.checkNum( quantity, "Quantity" );
        validator.checkSpinner( unitSpinner, "Quantity Unit" );
        validator.checkSpinner( locationSpinner, "Location" );
        validator.checkSpinner( categorySpinner, "Category" );

        String errors = validator.getErrors();
        // Display the error fragment if encountered
        if (errors != null) {
            errorString += errors;
            InputErrorFragment errorFragment = new InputErrorFragment( "Ingredient Save Error", errorString, this );
            errorFragment.display();
        }
        // If all inputs were valid, display the save fragment
        else {
            ModifyConfirmationFragment saveFragment = new ModifyConfirmationFragment(
                    "Save Ingredient",
                    "Do you wish to save your changes?",
                    this,
                    getSaveListener());
            saveFragment.display();
        }
    }

    /**
     * This function consists of the actions to carry out upon the user successfully passing the
     * entry validation and confirming their save. These actions are in the form of a
     * {@link DialogInterface.OnClickListener} since it will be passed to the fragment for saving.
     * If saving a new ingredient, it will create a new {@link Ingredient} object and add it to the
     * DB. If modifying an existing ingredient, it will set the fields to the new values and update
     * the ingredient in the DB. Upon success, it will return to the parent activity {@link IngredientStorageActivity}.
     * @return {@link DialogInterface.OnClickListener} for the actions to take when saving an {@link Ingredient}
     */
    private DialogInterface.OnClickListener getSaveListener() {

        return new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

                // Getting all the values from the components into their proper forms
                String nameArg = description.getText().toString();
                String descriptionArg = description.getText().toString();
                Date dateArg = Date.from(Instant.parse(date.getText().toString()));
                Constants.Location locationArg = (Constants.Location) locationSpinner.getSelectedItem();
                int amountArg = Integer.parseInt(quantity.getText().toString());
                Constants.AmountUnit amountUnitArg = (Constants.AmountUnit) unitSpinner.getSelectedItem();
                Constants.IngredientCategory categoryArg = (Constants.IngredientCategory) categorySpinner.getSelectedItem();

                // If saving a brand new ingredient, make a new instance and add it to DB
                if (ingredient == null) {
                    ingredient = new Ingredient(nameArg, descriptionArg, dateArg, locationArg, amountArg, amountUnitArg, categoryArg);
                    ingredientStorage.addIngredient(ingredient);
                }

                // If modifying an existing ingredient, set its values and update it in DB
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

                // Return to ingredient storage view after saving.
                finish();
            }
        };
    }

    /**
     * This function consists of the actions to carry out upon the user wishing to delete the
     * current {@link Ingredient}. These actions are in the form of a {@link DialogInterface.OnClickListener}
     * since it will be passed to the fragment for deleting. In this case, the ingredient is
     * removed from storage and the DB, and the activity returns to the parent {@link IngredientStorageActivity}.
     * @return {@link DialogInterface.OnClickListener} for the actions to take when deleting an {@link Ingredient}
     */
    private DialogInterface.OnClickListener getDeleteListener() {

        return new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                ingredientStorage.removeIngredient(ingredient);
                finish();
            }
        };
    }

}
