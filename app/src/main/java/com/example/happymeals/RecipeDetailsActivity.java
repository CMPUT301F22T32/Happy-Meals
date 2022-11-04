package com.example.happymeals;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RecipeDetailsActivity extends AppCompatActivity {

    private Recipe recipe;

    private ArrayList< Ingredient > ingredients;

    private TextView nameField;
    private TextView descriptionField;
    private TextView prepTimeField;
    private TextView cookTimeField;
    private TextView servingsField;
    private ListView ingredientsListField;
    private TextView instructionsField;
    private TextView commendsField;

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

        nameField = findViewById( R.id.recipe_name_field);
        descriptionField = findViewById( R.id.recipe_description_field);
        prepTimeField = findViewById( R.id.recipe_preptime_field );
        cookTimeField = findViewById( R.id.recipe_cooktime_field );
        ingredientsListField = findViewById( R.id.recipe_ingredients_listview );
        servingsField = findViewById( R.id.recipe_servings_field );
        instructionsField = findViewById( R.id.recipe_instructions_field );
        commendsField = findViewById( R.id.recipe_comment_field );

        // Get the recipe and ingredient list
        recipe = (Recipe) getIntent().getSerializableExtra("recipe");
        ingredients = recipe.getIngredients();
        if( recipe != null ) {
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
        servingsField.setText( String.valueOf( recipe.getCookTime() ) );
        instructionsField.setText( recipe.getInstructionsAsString() );
        commendsField.setText( recipe.getCommentsAsString() );

        IngredientArrayAdapter adapter = new IngredientArrayAdapter( this, ingredients );
        ingredientsListField.setAdapter( adapter );

    }

    /**
     * On the edit button click all fields become editable so that the user can change values.
     * @param view {@link View} of the view calling the functino.
     */
    public void onEditClick( View view ){
        finish();
    }
}