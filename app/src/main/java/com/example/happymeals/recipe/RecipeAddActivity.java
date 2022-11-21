package com.example.happymeals.recipe;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.happymeals.R;
import com.example.happymeals.fragments.InputStringFragment;
import com.example.happymeals.fragments.SearchIngredientFragment;
import com.example.happymeals.ingredient.Ingredient;
import com.example.happymeals.ingredient.IngredientStorage;
import com.example.happymeals.ingredient.IngredientStorageArrayAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

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
    private ImageView imageView;
    private FloatingActionButton addImageButton;

    private ArrayList< Ingredient > ingredientsInRecipe;
    private ArrayList< String > comments;
    private HashMap< String, HashMap< String, Object > > countMap;
    private IngredientStorageArrayAdapter adapter;

    private Button confirmButton;
    private Button cancelButton;
    private Button addCommentButton;
    private FloatingActionButton addIngredientsButton;

    private Context context;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    public static final int CAMERA_ACTION_CODE = 1;

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
        imageView = findViewById( R.id.recpie_add_image );
        addImageButton = findViewById( R.id.recipe_add_image_button);

        ingredientsView.setAdapter( adapter );

        addIngredientsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SearchIngredientFragment().show( getSupportFragmentManager(), "Edit Text");
            }
        });
        addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new InputStringFragment().show( getSupportFragmentManager(), "L E S F");
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
                // <todo> Validate all the fields before converting into a recipe object.
                String newName = nameField.getText().toString();
                String newDescription = descriptionField.getText().toString();
                double newPrepTime = new Double( prepTimeField.getText().toString() );
                double newCookTime = new Double( cookTimeField.getText().toString() );
                String newInstructions = instructionsField.getText().toString();
                double newServings = new Double( servingsField.getText().toString() );
                storage.addRecipe( new Recipe( newName, newCookTime, newDescription, comments,
                        RecipeStorage.getInstance().makeIngredientMapForRecipe(
                                countMap
                        ),
                        newInstructions, newPrepTime, newServings
                        ));
                finish();
            }
        });

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bundle bundle = result.getData().getExtras();
                    Bitmap finalImage = (Bitmap) bundle.get("data");
                    imageView.setImageBitmap(finalImage);
                }
            }
        });

        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                activityResultLauncher.launch(intent);
                /*
                if (intent.resolveActivity(getPackageManager()) != null) {
                    activityResultLauncher.launch(intent);
                }
                else {
                    Toast.makeText(RecipeAddActivity.this, "There is no app that supports this action", Toast.LENGTH_LONG).show();
                }*/
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
