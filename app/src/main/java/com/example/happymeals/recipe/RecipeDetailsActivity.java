package com.example.happymeals.recipe;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Log;
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
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.happymeals.HappyMealBottomNavigation;
import com.example.happymeals.R;

import com.example.happymeals.database.DatabaseListener;
import com.example.happymeals.database.DatabaseObject;
import com.example.happymeals.fragments.InputErrorFragment;
import com.example.happymeals.fragments.InputStringFragment;
import com.example.happymeals.fragments.SearchIngredientFragment;
import com.example.happymeals.ingredient.Ingredient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.example.happymeals.adapters.IngredientStorageArrayAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class RecipeDetailsActivity extends AppCompatActivity implements DatabaseListener,
        SearchIngredientFragment.SearchIngredientsFragmentListener,
        InputStringFragment.InputStringFragmentListener{

    private Recipe recipe;

    private ArrayList<Ingredient> ingredients;
    private HashMap< String, HashMap< String, Object > > ingredientMap;
    private IngredientStorageArrayAdapter adapter;
    private ArrayList< String > comments;

    private TextView nameField;
    private EditText descriptionField;
    private EditText prepTimeField;
    private EditText cookTimeField;
    private EditText servingsField;
    private ListView ingredientsListField;
    private TextView instructionsField;
    private TextView commentsField;
    private ImageView imageView;

    private TextView publishTextView;
    private TextView editButton;
    private Button cancelButton;
    private Button saveButton;
    private FloatingActionButton addIngredientButton;
    private Button addCommentsButton;

    private Context context;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    public static final int CAMERA_ACTION_CODE = 1;
    private Uri imagePath;
    private String imageFilePath;
    private FloatingActionButton addImageButton;

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
        setContentView( R.layout.activity_recipe_details);

        Intent intent = getIntent();
        if( !intent.hasExtra("Index") ) {
            // If no recipe has been passed we cannot display anything.
            // <todo> Some error checking here
            finish();
        }

        storage = RecipeStorage.getInstance();

        context = this;

        editButton = findViewById( R.id.edit_recipe_button );
        publishTextView = findViewById( R.id.recipe_details_publish_label );
        cancelButton = findViewById( R.id.recipe_details_cancel_button );
        saveButton = findViewById( R.id.recipe_details_button_save_button );
        addIngredientButton = findViewById( R.id.recipe_details_add_ingredients_button );
        addCommentsButton = findViewById( R.id.recipe_details_add_comment_button );

        HappyMealBottomNavigation bottomNavMenu =
                new HappyMealBottomNavigation(
                        findViewById(R.id.bottomNavigationView), this, R.id.recipe_menu );


        bottomNavMenu.setupBarListener();

        addIngredientButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SearchIngredientFragment(ingredientMap).show( getSupportFragmentManager(), "Edit Text");
            }
        });

        addCommentsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new InputStringFragment("Comment To Add To Recipe", 120).show( getSupportFragmentManager(), "L E S F");
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
        commentsField = findViewById( R.id.recipe_comment_field );
        addImageButton = findViewById( R.id.recipe_edit_image_button );
        addImageButton.setVisibility(View.GONE);

        disabledEditOnViews();

        imageView = findViewById( R.id.recpie_details_image );

        // Get the recipe and ingredient list
        recipe = storage.getRecipeByIndex( getIntent().getIntExtra("Index", 0 ) );
        //imageFile = storage.getRecipeImage(recipe);
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

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if(result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Bundle bundle = result.getData().getExtras();
                    Bitmap bitmap = (Bitmap) bundle.get("data");
                    imagePath = saveImage(bitmap);
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
        commentsField.setText( recipe.getCommentsAsString() );

        comments = recipe.getComments();

        if ( recipe.getImageFilePath() != "" && recipe.getImageFilePath() != null ) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference().child(recipe.getImageFilePath());
            try {
                final File localFile = File.createTempFile("Test Recipe", ".jpeg");
                storageReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                        Log.d("Image Download", "Image has been downloaded.");
                        Bitmap bitmap = BitmapFactory.decodeFile(localFile.getAbsolutePath());
                        imageView.setImageBitmap(bitmap);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("Image Download", "Image was unable to be downloaded.");

                    }

                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        ingredientsListField.setAdapter( adapter );

    }

    /**
     * On the edit button click all fields become editable so that the user can change values.
     * @param view {@link View} of the view calling the functino.
     */
    public void onEditClick( View view ){
        editButton.setVisibility( View.GONE );
        publishTextView.setVisibility( View.GONE );
        findViewById( R.id.recipe_details_top_divider ).setVisibility( View.GONE );

        saveButton.setVisibility( View.VISIBLE );
        cancelButton.setVisibility( View.VISIBLE );
        addIngredientButton.setVisibility( View.VISIBLE );
        addImageButton.setVisibility( View.VISIBLE );

        descriptionField.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        prepTimeField.setInputType( InputType.TYPE_CLASS_NUMBER );
        cookTimeField.setInputType( InputType.TYPE_CLASS_NUMBER );
        servingsField.setInputType( InputType.TYPE_CLASS_NUMBER );
        instructionsField.setInputType( InputType.TYPE_TEXT_FLAG_MULTI_LINE );

    }

    public void disabledEditOnViews() {
        editButton.setVisibility( View.VISIBLE );
        publishTextView.setVisibility( View.VISIBLE );
        findViewById( R.id.recipe_details_top_divider ).setVisibility( View.VISIBLE );

        saveButton.setVisibility( View.GONE );
        cancelButton.setVisibility( View.GONE );
        addIngredientButton.setVisibility( View.GONE );
        addImageButton.setVisibility( View.GONE );

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

        if (imagePath != null) {
            imageFilePath = storage.addImage( imagePath, recipe.getId() );
        }


        recipe.setDescription( newDescription );
        recipe.setPrepTime( new Integer( newPrepTime ));
        recipe.setCookTime( new Integer( newCookTime ));
        recipe.setServings( new Double( newServings ));
        recipe.setComments( comments );
        recipe.setInstructions( newInstructions );
        recipe.setIngredients(storage.makeIngredientMapForRecipe( ingredientMap ));
        recipe.setImageFilePath(imageFilePath);

        storage.addRecipe( recipe );
        disabledEditOnViews();
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

    @Override
    public void onConfirmClick(ArrayList<Ingredient> ingredientsToAdd, HashMap<String, Double> countsToAdd) {
        ingredients.clear();
        this.ingredientMap.clear();
        for( Ingredient i : ingredientsToAdd ) {
            ingredients.add( i );
            HashMap< String, Object > tempMap = new HashMap<>();
            tempMap.put("count", countsToAdd.get( i.getId() ) );
            this.ingredientMap.put( i.getId(), tempMap );
        }
        adapter.notifyDataSetChanged();
    }

    private Uri saveImage(Bitmap image) {
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

    public void onPublishClick( View view ) {
        if( !recipe.getCreator().equals( storage.getCurrentUser() ) ) {
            InputErrorFragment notifyFragment = new InputErrorFragment(
                    "Recipe Not Published",
                    "You cannot publish someone else's recipe!",
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
    protected void onResume() {
        super.onResume();
        adapter.notifyDataSetChanged();
    }
}