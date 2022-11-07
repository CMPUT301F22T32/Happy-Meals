package com.example.happymeals.recipe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.example.happymeals.R;

import com.example.happymeals.database.DatabaseListener;
import com.example.happymeals.database.DatabaseObject;
import com.example.happymeals.ingredient.Ingredient;
import com.example.happymeals.ingredient.IngredientStorageArrayAdapter;
import com.example.happymeals.recipe.Recipe;
import com.example.happymeals.recipe.RecipeStorage;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Map;

public class RecipeDetailsActivity extends AppCompatActivity implements DatabaseListener {

    private Recipe recipe;

    private ArrayList<Ingredient> ingredients;
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

        RecipeStorage storage = RecipeStorage.getInstance();

        editButton = findViewById( R.id.edit_recipe_button );
        nameField = findViewById( R.id.recipe_name_field );
        descriptionField = findViewById( R.id.recipe_description_field );
        prepTimeField = findViewById( R.id.recipe_preptime_field );
        cookTimeField = findViewById( R.id.recipe_cooktime_field );
        ingredientsListField = findViewById( R.id.recipe_ingredients_listview );
        servingsField = findViewById( R.id.recipe_servings_field );
        instructionsField = findViewById( R.id.recipe_instructions_field );
        commendsField = findViewById( R.id.recipe_comment_field );

        // Get the recipe and ingredient list
        recipe = storage.getRecipe( (String) getIntent().getSerializableExtra("recipe") );
        // Get the array reference so that we can pass it into the adapter.
        ingredients = storage.getIngredientListReference();
        adapter = new IngredientStorageArrayAdapter( this, ingredients );
        // Pass the adapter into the array fetch to tell the storage to notify the adapter on data
        // change.
        if( recipe != null ) {
            ingredients = storage.getIngredientsAsList( recipe, adapter );
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
        editButton.setEnabled( false );

        descriptionField.setFocusable( true );
        prepTimeField.setFocusable( true );
        cookTimeField.setFocusable( true );
        servingsField.setFocusable( true );
    }

    @Override
    public void onDataFetchSuccess(DatabaseObject data) {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSpinnerFetchSuccess(Map<String, Object> data) {

    }
}
