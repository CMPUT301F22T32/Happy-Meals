package com.example.happymeals.recipe;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.happymeals.HappyMealBottomNavigation;
import com.example.happymeals.R;
import com.example.happymeals.adapters.IngredientStorageArrayAdapter;
import com.example.happymeals.fragments.InputErrorFragment;
import com.example.happymeals.fragments.InputStringFragment;
import com.example.happymeals.ingredient.Ingredient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;

public class SharedRecipeDetailsActivity extends AppCompatActivity implements InputStringFragment.InputStringFragmentListener {

    private Recipe recipe;
    private Context context;

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
    private ImageView imageView;

    private ArrayList< String > comments;

    private Button addButton;

    private RecipeStorage storage;

    /**
     * This is the function called whenever the MainActivity is created -- in our
     * case, this is on the launch of the app or when navigating back to the home page.
     * It it responsible for sending the intents to access all the other main views.
     * @param savedInstanceState The instance state to restore the activity to ( if applicable ) {@link Bundle}
     */
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_shared_recipe_details );

        context = this;

        Intent intent = getIntent();
        if( !intent.hasExtra( "Index" ) ) {
            // If no recipe has been passed we cannot display anything.
            // <todo> Some error checking here
            finish();
        }

        int recipeIndex = intent.getIntExtra( "Index", 0 );

        storage = RecipeStorage.getInstance();

        addButton = findViewById( R.id.shared_recipe_details_add_button );

        nameField = findViewById( R.id.recipe_name_field );
        descriptionField = findViewById( R.id.recipe_description_field );
        prepTimeField = findViewById( R.id.recipe_preptime_field );
        cookTimeField = findViewById( R.id.recipe_cooktime_field );
        ingredientsListField = findViewById( R.id.recipe_ingredients_listview );
        servingsField = findViewById( R.id.recipe_servings_field );
        instructionsField = findViewById( R.id.recipe_instructions_field );
        commendsField = findViewById( R.id.recipe_comment_field );

        imageView = findViewById( R.id.shared_recipe_image );

        // Get the recipe and ingredient list
        recipe = storage.getSharedRecipes().get( recipeIndex );
        // Get the array reference so that we can pass it into the adapter.
        ingredients = storage.getIngredientListReference();
        // Pass the adapter into the array fetch to tell the storage to notify the adapter on data
        // change.
        if( recipe != null ) {
            ingredientMap = storage.getRecipeIngredientMap( recipe );
            adapter = new IngredientStorageArrayAdapter( this, ingredients, ingredientMap );
            ingredients = storage.getIngredientsAsList( recipe, adapter );
            setAllValues();
        }

        HappyMealBottomNavigation bottomNavMenu =
                new HappyMealBottomNavigation( 
                        findViewById( R.id.bottomNavigationView ), this, R.id.recipe_menu );


        bottomNavMenu.setupBarListener();

        addButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                if( !storage.alreadyHave( recipe ) ) {
                    storage.addRecipe( recipe );
                    InputErrorFragment inputErrorFragment =
                            new InputErrorFragment( 
                                    "Added Shared Recipe",
                                    "You have added " + recipe.getName() + " into your inventory",
                                    context
                            );
                    inputErrorFragment.display();
                } else {
                    InputErrorFragment inputErrorFragment =
                            new InputErrorFragment( 
                                    "Cannot Add Shared Recipe",
                                    "You have already added " + recipe.getName() + " into your inventory",
                                    context
                            );
                    inputErrorFragment.display();
                }
            }
        } );
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

        comments = recipe.getComments();

        if (comments.size() > 0) {
            commendsField.setVisibility(View.VISIBLE);
            commendsField.setText(recipe.getCommentsAsString());
        }

        ingredientsListField.setAdapter( adapter );

        if ( recipe.getImageFilePath() != "" && recipe.getImageFilePath() != null ) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child( recipe.getImageFilePath() );
            try {
                final File localFile = File.createTempFile( "Test Recipe", ".jpeg" );
                storageReference.getFile( localFile ).addOnSuccessListener( new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess( FileDownloadTask.TaskSnapshot taskSnapshot ) {
                        Log.d( "Image Download", "Image has been downloaded." );
                        Bitmap bitmap = BitmapFactory.decodeFile( localFile.getAbsolutePath() );
                        imageView.setImageBitmap( bitmap );
                    }
                } ).addOnFailureListener( new OnFailureListener() {
                    @Override
                    public void onFailure( @NonNull Exception e ) {
                        Log.d( "Image Download", "Image was unable to be downloaded." );
                    }

                } );
            } catch ( IOException e ) {
                e.printStackTrace();
            }
        }

        commendsField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new InputStringFragment( "Comment To Add To Recipe", 120 ).show( getSupportFragmentManager(), "L E S F" );
            }
        });
    }

    @Override
    public void onConfirmClick(String str) {
        comments.add( str );
        String commentsSoFar = commendsField.getText().toString();
        commendsField.setVisibility(View.VISIBLE);
        if( comments.size() == 1 ) {
            commentsSoFar = "1. " + str;
        } else {
            commentsSoFar += "\n" + String.valueOf( comments.size() ) + ". " + str;
        }
        commendsField.setText( commentsSoFar );
    }
}
