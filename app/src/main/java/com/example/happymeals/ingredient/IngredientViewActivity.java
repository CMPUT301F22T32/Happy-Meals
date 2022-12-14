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
import com.example.happymeals.HappyMealBottomNavigation;
import com.example.happymeals.InputValidator;
import com.example.happymeals.R;
import com.example.happymeals.fragments.InputErrorFragment;
import com.example.happymeals.fragments.ModifyConfirmationFragment;

import java.util.Calendar;
import java.util.Date;



/**
 * This is the activity that will display the details of a specific ingredient. The view will
 * be persistent and display ingredient data that has been stored in the database. Ingredients can
 * be edited and updated.
 * @author kstark sruduke
 */
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
    protected final static String INGREDIENT_INDEX = "INDEX";

    private Ingredient ingredient = null;
    private Boolean newIngredient;

    private IngredientStorage ingredientStorage;


    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_ingredient_details );

        // won't need new instance after we have the singleton ingredient storage initialized in
        // main class
        ingredientStorage = IngredientStorage.getInstance();

        Intent intent = getIntent();
        context = this;

        name = findViewById( R.id.ing_content_name_input );
        description = findViewById( R.id.ing_content_desc_input );
        date = findViewById( R.id.ing_content_date_input );
        quantity = findViewById( R.id.ing_content_quantity_input );
        unitSpinner = findViewById( R.id.ing_content_unit_input );
        locationSpinner = findViewById( R.id.ing_content_location_input );
        categorySpinner = findViewById( R.id.ing_content_category_input );

        saveButton = findViewById( R.id.ing_view_save_button );
        datePickerButton = findViewById( R.id.ingredient_date_button );

        // Populate the spinners
        populateSpinners();

        HappyMealBottomNavigation bottomNavMenu =
                new HappyMealBottomNavigation( 
                        findViewById( R.id.bottomNavigationView ), this, R.id.ingredient_menu );

        bottomNavMenu.setupBarListener();

        findViewById( R.id.ingredient_details_back_button ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        newIngredient = intent.getBooleanExtra( ADD_INGREDIENT, false );
        if ( !newIngredient )
            fillFields( intent );

        saveButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                checkInput();
            }
        } );

        datePickerButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                // Get Current Date
                final Calendar c = Calendar.getInstance();
                int year = c.get( Calendar.YEAR );
                int month = c.get( Calendar.MONTH );
                int day = c.get( Calendar.DAY_OF_MONTH );


                DatePickerDialog datePickerDialog = new DatePickerDialog( context,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet( DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth ) {
                                dateArg = new Date( year, month, day );
                                String strMonth = String.valueOf( monthOfYear + 1 );
                                strMonth = strMonth.length() < 2 ? "0"+strMonth : strMonth;
                                String strDay = String.valueOf( dayOfMonth );
                                strDay = strDay.length() < 2 ? "0"+strDay : strDay;
                                date.setText( year + "-" + strMonth + "-" + strDay );

                            }
                        }, year, month, day );
                datePickerDialog.show();
            }
        } );

    }

    /**
     *  Function to populate the selection spinners with custom user data from {@link com.example.happymeals.SpinnerSettingsActivity }
     *  and allows users to select spinners to help define ingredients.
     */
    private void populateSpinners() {
        IngredientStorage ingredientStorage = IngredientStorage.getInstance();

        ArrayAdapter<String> unitAdapter = new ArrayAdapter<>( this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                ingredientStorage.getSpinners( Constants.StoredSpinnerChoices.AMOUNT_UNIT ) );

        ArrayAdapter<String> locationAdapter = new ArrayAdapter<>( this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                ingredientStorage.getSpinners( Constants.StoredSpinnerChoices.LOCATION ) );

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>( this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
                ingredientStorage.getSpinners( Constants.StoredSpinnerChoices.INGREDIENT_CATEGORY ) );

        unitSpinner.setAdapter( unitAdapter );
        locationSpinner.setAdapter( locationAdapter );
        categorySpinner.setAdapter( categoryAdapter );
    }

    private void fillFields( Intent intent ) {

        ingredient = ingredientStorage.getIngredientByIndex( 
                getIntent().getIntExtra( INGREDIENT_INDEX, 0 ) );

        name.setText( ingredient.getName() );
        name.setEnabled( false );
        description.setText( ingredient.getDescription() );
        date.setText( ingredient.getBestBeforeDateAsString() );
        quantity.setText( Double.toString( ingredient.getAmount() ) );
        unitSpinner.setSelection( ingredientStorage.getSpinners( Constants.StoredSpinnerChoices.AMOUNT_UNIT )
                .indexOf( ingredient.getUnit() ) );
        locationSpinner.setSelection( ingredientStorage.getSpinners( Constants.StoredSpinnerChoices.LOCATION )
                .indexOf( ingredient.getLocation() ) );
        categorySpinner.setSelection( ingredientStorage.getSpinners( Constants.StoredSpinnerChoices.INGREDIENT_CATEGORY )
                .indexOf( ingredient.getCategory() ) );
    }

    private void checkInput() {
        InputValidator validator = new InputValidator();
        String errorString = "The ingredient couldn't be saved for the following reasons:\n";

        validator.checkText( name, "Name" );
        validator.checkDate( date );
        validator.checkNum( quantity, "Quantity" );
        validator.checkSpinner( unitSpinner, "Quantity Unit" );
        validator.checkSpinner( locationSpinner, "DefaultLocationSpinners" );
        validator.checkSpinner( categorySpinner, "Category" );

        String errors = validator.getErrors();
        if ( errors != null ) {
            errorString += errors;
            InputErrorFragment errorFragment = new InputErrorFragment( "Ingredient Save Error", errorString, this );
            errorFragment.display();
        }
        else {
            ModifyConfirmationFragment saveFragment = new ModifyConfirmationFragment( 
                    "Save Ingredient",
                    "Do you wish to save your changes?",
                    this,
                    getSaveListener() );
            saveFragment.display();
        }
    }

    private DialogInterface.OnClickListener getSaveListener() {

        return new DialogInterface.OnClickListener() {

            @Override
            public void onClick( DialogInterface dialogInterface, int i ) {

                String nameArg = name.getText().toString();
                String descriptionArg = description.getText().toString();
                String locationArg = ( String ) locationSpinner.getSelectedItem();
                Double amountArg = Double.parseDouble( quantity.getText().toString() );
                String amountUnitArg = ( String ) unitSpinner.getSelectedItem();
                String categoryArg = ( String ) categorySpinner.getSelectedItem();

                if ( ingredient == null ) {
                    ingredient = new Ingredient( nameArg,
                            ingredientStorage.getCurrentUser(),
                            descriptionArg,
                            dateArg ,
                            locationArg,
                            amountArg,
                            amountUnitArg,
                            categoryArg );
                    ingredientStorage.addIngredient( ingredient );
                }

                else {
                    if( dateArg == null ) {
                        dateArg = ingredient.getBestBeforeDate();
                    }
                    ingredient.setName( nameArg );
                    ingredient.setDescription( descriptionArg );
                    ingredient.setBestBeforeDate( dateArg );
                    ingredient.setLocation( locationArg );
                    ingredient.setAmount( amountArg );
                    ingredient.setUnit( amountUnitArg );
                    ingredient.setCategory( categoryArg );

                    ingredientStorage.updateIngredient( ingredient );
                }

                // go back to ingredient storage view after saving.
                finish();
            }
        };
    }

    @Override
    public void onDateSet( DatePicker datePicker, int year, int month, int day ) {
        date.setText( String.format( "%04d-%02d-%02d", year, month, day ) );
    }
}