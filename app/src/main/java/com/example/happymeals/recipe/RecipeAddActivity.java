package com.example.happymeals.recipe;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.happymeals.R;
import com.example.happymeals.fragments.InputErrorFragment;
import com.example.happymeals.fragments.InputStringFragment;
import com.example.happymeals.fragments.SearchIngredientFragment;
import com.example.happymeals.ingredient.Ingredient;
import com.example.happymeals.adapters.IngredientStorageArrayAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

// sources for adding image:
// https://www.youtube.com/watch?v=YLUmfyGFjnU
// https://www.youtube.com/watch?v=qO3FFuBrT2E
// https://www.youtube.com/watch?v=XRdzAWIt8rw


/**
 * This is the activity that will display the required fields for adding a new recipe. The fields
 * will be initially blank and all the data entered will be stored in the database. Once filled
 * new recipes can be added by hitting the add button.
 
 */


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
    private RecipeStorage storage;

    private Button confirmButton;
    private Button cancelButton;
    private Button addCommentButton;
    private FloatingActionButton addIngredientsButton;

    private Context context;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    public static final int CAMERA_ACTION_CODE = 1;
    private Uri imagePath;
    private String imageFilePath;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recipe_add_view);

        context = this;
        imageFilePath = "";

        storage = RecipeStorage.getInstance();

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
                new SearchIngredientFragment( countMap ).show( getSupportFragmentManager(), "Edit Text");
            }
        });

        findViewById( R.id.recipe_add_add_ingredient_label )
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        new SearchIngredientFragment( countMap ).show( getSupportFragmentManager(), "Edit Text");
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

                if ( imagePath != null ) {
                    imageFilePath = storage.addImage(imagePath, newName);
                }
                String test = storage.getCurrentUser();
                storage.addRecipe( new Recipe(
                        newName,
                        storage.getCurrentUser(),
                        new Integer( newCookTime ),
                        newDescription,
                        comments,
                        RecipeStorage.getInstance().makeIngredientMapForRecipe( countMap ),
                        newInstructions,
                        new Integer( newPrepTime ),
                        new Double( newServings ), imageFilePath
                ));
                finish();
            }
        });

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bundle bundle = result.getData().getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    imagePath = saveImage(bitmap, RecipeAddActivity.this);
                    imageView.setImageURI(imagePath);

                }
            }
        });

        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                activityResultLauncher.launch(intent);
            }
        });

    }

    private Uri saveImage(Bitmap image, RecipeAddActivity recipeAddActivity) {
        File imagesFolder = new File(context.getCacheDir(), "images");
        Uri uri = null;

        try {
            imagesFolder.mkdir();
            File file = new File(imagesFolder, "captured_image.jpg");
            FileOutputStream stream = new FileOutputStream(file);
            image.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            stream.flush();
            stream.close();
            uri = FileProvider.getUriForFile(context.getApplicationContext(), "com.example.happymeals"+".provider", file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return uri;
    }

    @Override
    public void onConfirmClick( ArrayList<Ingredient> ingredientsToAdd,
                                HashMap< String, Double > countMap ) {
        this.ingredientsInRecipe.clear();
        this.countMap.clear();

        for( Ingredient i : ingredientsToAdd ) {
            ingredientsInRecipe.add( i );
            HashMap< String, Object > tempMap = new HashMap<>();
            tempMap.put("count", countMap.get( i.getId() ) );
            this.countMap.put( i.getId(), tempMap );
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