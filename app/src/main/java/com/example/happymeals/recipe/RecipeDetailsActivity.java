package com.example.happymeals.recipe;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.happymeals.R;

import com.example.happymeals.database.DatabaseListener;
import com.example.happymeals.database.DatabaseObject;
import com.example.happymeals.fragments.InputErrorFragment;
import com.example.happymeals.ingredient.Ingredient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.example.happymeals.adapters.IngredientStorageArrayAdapter;
import com.google.android.gms.common.ErrorDialogFragment;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class RecipeDetailsActivity extends AppCompatActivity implements DatabaseListener {

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
    private ImageView imageView;


    private TextView editButton;

    private RecipeStorage storage;

    /**
     * This is the function called whenever the MainActivity is created -- in our
     * case, this is on the launch of the app or when navigating back to the home page.
     * It it responsible for sending the intents to access all the other main views.
     * @param savedInstanceState The instance state to restore the activity to (if applicable) {@link Bundle}
     */
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.recipe_details_activity );

        Intent intent = getIntent();
        if( !intent.hasExtra("Index") ) {
            // If no recipe has been passed we cannot display anything.
            // <todo> Some error checking here
            finish();
        }

        int recipeIndex = intent.getIntExtra("Index", 0 );

        storage = RecipeStorage.getInstance();

        editButton = findViewById( R.id.edit_recipe_button );
        nameField = findViewById( R.id.recipe_name_field );
        descriptionField = findViewById( R.id.recipe_description_field );
        prepTimeField = findViewById( R.id.recipe_preptime_field );
        cookTimeField = findViewById( R.id.recipe_cooktime_field );
        ingredientsListField = findViewById( R.id.recipe_ingredients_listview );
        servingsField = findViewById( R.id.recipe_servings_field );
        instructionsField = findViewById( R.id.recipe_instructions_field );
        commendsField = findViewById( R.id.recipe_comment_field );
        imageView = findViewById( R.id.recpie_details_image );

        // Get the recipe and ingredient list
        //imageFile = storage.getRecipeImage(recipe);
        recipe = storage.getRecipes().get( recipeIndex );
        // Get the array reference so that we can pass it into the adapter.
        ingredients = storage.getIngredientListReference();
        // Pass the adapter into the array fetch to tell the storage to notify the adapter on data
        // change.
        if( recipe != null ) {
            ingredientMap = storage.getRecipeIngredientMap( recipe );
            adapter = new IngredientStorageArrayAdapter( this, ingredients, ingredientMap);
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

        StorageReference storageReference = FirebaseStorage.getInstance().getReference().child("images/67.49.30.0/Test Recipe");
        try {
            final File localFile = File.createTempFile("Test Recipe", ".jpeg");
            storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                    Log.d( "Image Download", "Image has been downloaded." );
                    Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                    imageView.setImageBitmap(bitmap);
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.d( "Image Download", "Image was unable to be downloaded." );

                }

            });
        } catch (IOException e) {
            e.printStackTrace();
        }



        //imageView.setImageURI( recipe.getImage() );
        // System.out.println(recipe.getImage());

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

    public void onPublishClick( View view ) {
        if( !recipe.getCreator().equals( storage.getCurrentUser() ) ) {
            InputErrorFragment notifyFragment = new InputErrorFragment(
                    "Recipe Not Published",
                    "You cannot publish an already published recipe!",
                    this
            );
            notifyFragment.display();
            return;
        }
        Recipe newRecipe = recipe.clone();
        storage.publishRecipe( newRecipe );
        InputErrorFragment notifyFragment = new InputErrorFragment(
                "Recipe Published",
                "Your recipe has been sent off. Please confirm you see it published!",
                this
        );
        notifyFragment.display();
    }

    @Override
    public void onDataFetchSuccess( DatabaseObject data ) {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onSharedDataFetchSuccess(Recipe data) {

    }

    @Override
    public <T> void onSpinnerFetchSuccess( T map ) {

    }
}
