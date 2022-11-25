package com.example.happymeals.recipe;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.happymeals.R;

import com.example.happymeals.database.DatabaseListener;
import com.example.happymeals.database.DatabaseObject;
import com.example.happymeals.fragments.InputErrorFragment;
import com.example.happymeals.fragments.InputStringFragment;
import com.example.happymeals.fragments.SearchIngredientFragment;
import com.example.happymeals.ingredient.Ingredient;
import com.example.happymeals.adapters.IngredientStorageArrayAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class RecipeDetailsActivity extends AppCompatActivity implements DatabaseListener,
        SearchIngredientFragment.SearchIngredientsFragmentListener,
        InputStringFragment.InputStringFragmentListener{

    private Recipe recipe;

    private ArrayList<Ingredient> ingredients;
    private HashMap< String, HashMap< String, Object > > ingredientMap;
    private IngredientStorageArrayAdapter adapter;

    private TextView nameField;
    private EditText descriptionField;
    private EditText prepTimeField;
    private EditText cookTimeField;
    private EditText servingsField;
    private ListView ingredientsListField;
    private TextView instructionsField;
    private TextView commendsField;

    private TextView editButton;
    private Button cancelButton;
    private Button saveButton;
    private FloatingActionButton addIngredientButton;

    private RecipeStorage storage;

    /**
     * This is the function called whenever the MainActivity is created -- in our
     * case, this is on the launch of the app or when navigating back to the home page.
     * It it responsible for sending the intents to access all the other main views.
     * @param savedInstanceState The instance state to restore the activity to (if applicable) {@link Bundle}
     */
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_details_activity);

        Intent intent = getIntent();
        if( !intent.hasExtra("recipe") ) {
            // If no recipe has been passed we cannot display anything.
            // <todo> Some error checking here
            finish();
        }

        storage = RecipeStorage.getInstance();

        editButton = findViewById( R.id.edit_recipe_button );
        cancelButton = findViewById( R.id.recipe_details_cancel_button );
        saveButton = findViewById( R.id.recipe_details_button_save_button );
        addIngredientButton = findViewById( R.id.recipe_details_add_ingredients_button );

        addIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SearchIngredientFragment(ingredientMap).show( getSupportFragmentManager(), "Edit Text");
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                disabledEditOnViews();
                setAllValues();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveChanges();
            }
        });
        nameField = findViewById( R.id.recipe_name_field );
        descriptionField = findViewById( R.id.recipe_description_field );
        prepTimeField = findViewById( R.id.recipe_preptime_field );
        cookTimeField = findViewById( R.id.recipe_cooktime_field );
        ingredientsListField = findViewById( R.id.recipe_ingredients_listview );
        servingsField = findViewById( R.id.recipe_servings_field );
        instructionsField = findViewById( R.id.recipe_instructions_field );
        commendsField = findViewById( R.id.recipe_comment_field );

        disabledEditOnViews();

        // Get the recipe and ingredient list
        recipe = storage.getRecipe( (String) getIntent().getSerializableExtra("recipe") );
        // Get the array reference so that we can pass it into the adapter.
        ingredients = storage.getIngredientListReference();
        // Pass the adapter into the array fetch to tell the storage to notify the adapter on data
        // change.
        if( recipe != null ) {
            ingredientMap = storage.getRecipeIngredientMap( recipe );
            adapter = new IngredientStorageArrayAdapter( this, ingredients, ingredientMap);
            storage.getIngredientsAsList( recipe, adapter );

            setAllValues();
        }
    }

    /**
     * This will set all the values ot display inside the .xml file.
     */
    private void setAllValues() {
        nameField.setText( recipe.getName() );
        descriptionField.setText( recipe.getDescription() );
        prepTimeField.setText( String.valueOf( recipe.getPrepTime() ) );
        cookTimeField.setText( String.valueOf( recipe.getCookTime() ) );
        servingsField.setText( String.valueOf( recipe.getServings() ) );
        instructionsField.setText( recipe.getInstructions() );
        commendsField.setText( recipe.getCommentsAsString() );

        ingredientsListField.setAdapter( adapter );

    }

    /**
     * On the edit button click all fields become editable so that the user can change values.
     * @param view {@link View} of the view calling the functino.
     */
    public void onEditClick( View view ){
        editButton.setVisibility( View.GONE );
        saveButton.setVisibility( View.VISIBLE );
        cancelButton.setVisibility( View.VISIBLE );
        addIngredientButton.setVisibility( View.VISIBLE );

        descriptionField.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        prepTimeField.setInputType( InputType.TYPE_CLASS_NUMBER );
        cookTimeField.setInputType( InputType.TYPE_CLASS_NUMBER );
        servingsField.setInputType( InputType.TYPE_CLASS_NUMBER );
        instructionsField.setInputType( InputType.TYPE_TEXT_FLAG_MULTI_LINE );

    }

    public void disabledEditOnViews() {
        editButton.setVisibility( View.VISIBLE );
        saveButton.setVisibility( View.GONE );
        cancelButton.setVisibility( View.GONE );
        addIngredientButton.setVisibility( View.GONE );

        descriptionField.setInputType( 0 );
        prepTimeField.setInputType( 0 );
        cookTimeField.setInputType( 0 );
        servingsField.setInputType( 0 );
        instructionsField.setInputType( 0 );
    }

    public void saveChanges() {
        String newDescription = descriptionField.getText().toString();
        String newPrepTime = prepTimeField.getText().toString();
        String newCookTime = cookTimeField.getText().toString();
        String newInstructions = instructionsField.getText().toString();
        String newServings = servingsField.getText().toString();

        // Check and make sure that required fields have been filled out.
        if( newPrepTime.length() < 1 || newCookTime.length() < 1 ) {
            if( newPrepTime.length() < 1 ) {
                prepTimeField.setError("Missing Field");
            }
            if( newCookTime.length() < 1 ) {
                cookTimeField.setError("Missing Field");
            }
            InputErrorFragment inputErrorFragment = new InputErrorFragment(
                    "Missing Information",
                    "Please ensure you have all required information filled out.",
                    this
            );
            inputErrorFragment.display();
            return;
        }

        recipe.setDescription( newDescription );
        recipe.setPrepTime( new Double( newPrepTime ));
        recipe.setCookTime( new Double( newCookTime ));
        recipe.setServings( new Double( newServings ));
        recipe.setComments( new ArrayList<>() );
        recipe.setInstructions( newInstructions );
        recipe.setIngredients(storage.makeIngredientMapForRecipe( ingredientMap ));

        storage.addRecipe( recipe );
        disabledEditOnViews();
    }

    @Override
    public void onDataFetchSuccess(DatabaseObject data) {
        adapter.notifyDataSetChanged();
    }

    @Override
    public <T> void onSpinnerFetchSuccess( T map ) {

    }

    @Override
    public void onConfirmClick(String str) {

    }

    @Override
    public void onConfirmClick(ArrayList<Ingredient> ingredientsToAdd, HashMap<String, Double> countsToAdd) {
        ingredients.clear();
        this.ingredientMap.clear();
        for( Ingredient i : ingredientsToAdd ) {
            ingredients.add( i );
            HashMap< String, Object > tempMap = new HashMap<>();
            tempMap.put("count", ingredientMap.get( i.getName() ) );
            this.ingredientMap.put( i.getName(), tempMap );
        }
        adapter.notifyDataSetChanged();
    }
}
