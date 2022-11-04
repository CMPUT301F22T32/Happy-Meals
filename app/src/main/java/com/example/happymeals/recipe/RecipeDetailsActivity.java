package com.example.happymeals.recipe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.happymeals.R;
import com.example.happymeals.ingredient.Ingredient;
import com.example.happymeals.ingredient.IngredientStorageArrayAdapter;
import com.example.happymeals.recipe.Recipe;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Date;

/**
 * @author bfiogbe
 */
public class RecipeDetailsActivity extends AppCompatActivity {

    private Recipe recipe;

    private ArrayList<Ingredient> ingredients;

    private TextView nameField;
    private TextView descriptionField;
    private TextView prepTimeField;
    private TextView cookTimeField;
    private TextView servingsField;
    private ListView ingredientsListField;
    private TextView instructionsField;
    private TextView commendsField;

    /**
     * This is the function called whenever the RecipeDetailsActivity is created -- in our
     * case, this is when the user selects a {@link Recipe} from the {@link RecipeStorageActivity}.
     * @param savedInstanceState The instance state to restore the activity to (if applicable) {@link Bundle}
     */
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_details_activity);

        Intent intent = getIntent();
        if( !intent.hasExtra("recipe") ) {
            // If no recipe has been passed we cannot display anything.
            // IT SHOULD DISPLAY EMPTY FEILDS!!!
            // <todo> Some error checking here
            //finish();
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
        //recipe = (Recipe) getIntent().getSerializableExtra("recipe");
        ArrayList<String> comments = new ArrayList<String>();
        comments.add("Very Good");
        comments.add("Try with ground turkey next time");
        String strComments = "1: Very Good\n2: Try with ground turkey next time\n";
        ingredients = new ArrayList<Ingredient>();
        ingredients.add(new Ingredient("Lettuce", "Crisp romaine lettuce",
                new Date(2022, 01, 11), Constants.Location.FRIDGE, 1,
                Constants.AmountUnit.COUNT, Constants.IngredientCategory.FRUIT));
        ingredients.add(new Ingredient("Ground beef", "Extra lean ground beef",
                new Date(2023, 01, 01), Constants.Location.FREEZER, 500,
                Constants.AmountUnit.MG, Constants.IngredientCategory.MEAT));
        ArrayList instructions = new ArrayList<String>();
        instructions.add("Cook beef in a hot pan");
        instructions.add("Cut lettuce");
        String strInstructions = "1: Cook beef in a hot pan\n2: Cut lettuce\n";

        recipe = new Recipe("Tacos", 2, "The best tacos ever",
                comments, ingredients, instructions, 15, 6);


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

        IngredientStorageArrayAdapter adapter = new IngredientStorageArrayAdapter( this, ingredients );
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
