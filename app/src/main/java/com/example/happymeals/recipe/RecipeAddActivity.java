package com.example.happymeals.recipe;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.happymeals.R;
import com.example.happymeals.fragments.InputErrorFragment;
import com.example.happymeals.fragments.InputStringFragment;
import com.example.happymeals.fragments.SearchIngredientFragment;
import com.example.happymeals.ingredient.Ingredient;
import com.example.happymeals.adapters.IngredientStorageArrayAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;

public class RecipeAddActivity extends AppCompatActivity  implements SearchIngredientFragment.SearchIngredientsFragmentListener,
        InputStringFragment.InputStringFragmentListener {

    private EditText nameField;
    private EditText descriptionField;
    private EditText prepTimeField;
    private EditText cookTimeField;
    private EditText servingsField;
    private ListView ingredientsView;
    private EditText instructionsField;
    private TextView commentsField;

    private ArrayList< Ingredient > ingredientsInRecipe;
    private ArrayList< String > comments;
    private HashMap< String, HashMap< String, Object > > countMap;
    private IngredientStorageArrayAdapter adapter;

    private Button confirmButton;
    private Button cancelButton;
    private Button addCommentButton;
    private FloatingActionButton addIngredientsButton;

    private Context context;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_add_view);

        context = this;

        RecipeStorage storage = RecipeStorage.getInstance();

        ingredientsInRecipe = new ArrayList<>();
        countMap = new HashMap<>();
        comments = new ArrayList<>();
        adapter = new IngredientStorageArrayAdapter( this, ingredientsInRecipe, countMap );

        confirmButton = findViewById( R.id.recipe_add_save_button );
        cancelButton = findViewById( R.id.recipe_add_cancel_button );
        addIngredientsButton = findViewById( R.id.recipe_add_ingredient_add_button );
        addCommentButton = findViewById( R.id.recipe_add_add_comment_button );
        ingredientsView = findViewById( R.id.recipe_ingredients_listview );
        nameField = findViewById( R.id.recipe_add_name_field );
        descriptionField = findViewById( R.id.recipe_add_description_field );
        prepTimeField = findViewById( R.id.recipe_add_prep_time_field );
        cookTimeField = findViewById( R.id.recipe_add_cook_time_field );
        servingsField = findViewById( R.id.recipe_add_servings_field );
        instructionsField = findViewById( R.id.recipe_add_instructions_field );
        commentsField = findViewById( R.id.recipe_add_comments_field );

        ingredientsView.setAdapter( adapter );

        addIngredientsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SearchIngredientFragment().show( getSupportFragmentManager(), "Edit Text");
            }
        });

        findViewById( R.id.recipe_add_add_ingredient_label )
                .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SearchIngredientFragment().show( getSupportFragmentManager(), "Edit Text");
            }
        });

        addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new InputStringFragment("Comment To Add To Recipe", 120).show( getSupportFragmentManager(), "L E S F");
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        confirmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create the new recipe object and add it to the storage.
                String newName = nameField.getText().toString();
                String newDescription = descriptionField.getText().toString();
                String newPrepTime = prepTimeField.getText().toString();
                String newCookTime = cookTimeField.getText().toString();
                String newInstructions = instructionsField.getText().toString();
                String newServings = servingsField.getText().toString();

                // Check and make sure that required fields have been filled out.
                if( newName.length() < 1 || newPrepTime.length() < 1 || newCookTime.length() < 1 ) {
                    if( newName.length() < 1 ){
                        nameField.setError("Missing Field");
                    }
                    if( newPrepTime.length() < 1 ) {
                        prepTimeField.setError("Missing Field");
                    }
                    if( newCookTime.length() < 1 ) {
                        cookTimeField.setError("Missing Field");
                    }
                    InputErrorFragment inputErrorFragment = new InputErrorFragment(
                            "Missing Information",
                            "Please ensure you have all required information filled out.",
                            context
                    );
                    inputErrorFragment.display();
                    return;
                }

                storage.addRecipe( new Recipe(
                        newName,
                        new Double( newCookTime ),
                        newDescription,
                        comments,
                        RecipeStorage.getInstance().makeIngredientMapForRecipe( countMap ),
                        newInstructions,
                        new Double( newPrepTime ),
                        new Double( newServings )
                        ));
                finish();
            }
        });

    }

    @Override
    public void onConfirmClick( ArrayList<Ingredient> ingredientsToAdd,
                                HashMap< String, Double > countMap ) {
        for( Ingredient i : ingredientsToAdd ) {
            ingredientsInRecipe.add( i );
            HashMap< String, Object > tempMap = new HashMap<>();
            tempMap.put("count", countMap.get( i.getName() ) );
            this.countMap.put( i.getName(), tempMap );
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onConfirmClick(String str) {
        comments.add( str );
        String commentsSoFar = commentsField.getText().toString();
        if( comments.size() == 1 ) {
            commentsSoFar = "1. " + str;
        } else {
            commentsSoFar += "\n" + String.valueOf( comments.size() ) + ". " + str;
        }
        commentsField.setText( commentsSoFar );

    }
}
