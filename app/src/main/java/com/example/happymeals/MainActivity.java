package com.example.happymeals;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.happymeals.database.DatasetWatcher;
import com.example.happymeals.database.FireStoreManager;
import com.example.happymeals.database.FirebaseAuthenticationHandler;
import com.example.happymeals.fragments.ModifyConfirmationFragment;
import com.example.happymeals.ingredient.IngredientStorage;
import com.example.happymeals.recipe.RecipeStorage;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import com.example.happymeals.ingredient.IngredientStorageActivity;
import com.example.happymeals.recipe.RecipeStorageActivity;

/**
 * This class is the entry point of the application and serves as the home
 * page for navigation. From this activity, all of the other main activities -- such as the
 * Ingredient Storage, Recipes, Meal Plan, and Shopping List -- can be viewed.
 */
public class MainActivity extends AppCompatActivity implements DatasetWatcher {

    private Context context;
    private NotificationManagerClass notification;
    /**
     * This is the function called whenever the MainActivity is created -- in our
     * case, this is on the launch of the app or when navigating back to the home page.
     * It it responsible for sending the intents to access all the other main views.
     * @param savedInstanceState The instance state to restore the activity to (if applicable) {@link Bundle}
     */
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        Toolbar toolbar = findViewById( R.id.appbar);
        setSupportActionBar(toolbar);
        // Create the firebase manager connection along with all the storage classes.
        FireStoreManager.getInstance();
        RecipeStorage.getInstance();
        IngredientStorage.getInstance();

        IngredientStorage ingredientStorage = IngredientStorage.getInstance();
        ingredientStorage.setListeningActivity(this);

        context = this;

        // Notification
        notification = new NotificationManagerClass("Fill out missing information", "You have ingredients with missing information", context, "Missing Info", 0);



        // Display the username of user
        TextView welcomeMessage = findViewById( R.id.user_welcome );
        welcomeMessage.setText("Enjoy a Happy Meal "
                + FirebaseAuthenticationHandler.getFireAuth().authenticate.getCurrentUser().getDisplayName() );
        // The 4 buttons to access the other activities
        Button ingredientStorageButton = findViewById( R.id.ingredient_storage_button );
        Button recipesButton = findViewById( R.id.recipes_button );
        Button mealPlannerButton = findViewById( R.id.meal_planner_button );
        Button shoppingListButton = findViewById( R.id.shopping_list_button );

        FloatingActionButton ingredientMissingInfoButton = findViewById( R.id.ingredient_missing_info_button);
        Boolean missingInfo = ingredientStorage.isIngredientsMissingInfo();
        System.out.println(missingInfo);

        notification.addNotification();

        if ( ingredientStorage.isIngredientsMissingInfo() ) {
            ingredientMissingInfoButton.setVisibility(View.VISIBLE);

        }
        else {
            ingredientMissingInfoButton.setVisibility(View.GONE);
        }

        // Intent to open Ingredient Storage Activity
        ingredientStorageButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                Intent intent = new Intent(context, IngredientStorageActivity.class);
                intent.putExtra("MissingCheck", false);
                startActivity(intent);
            }
        });

        // Intent to open Recipe Storage Activity
        recipesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                Intent intent = new Intent( context, RecipeStorageActivity.class );
                startActivity( intent );
            }
        });

        // Intent to open MealPlanner Activity
        mealPlannerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                //TODO: Send intent for Meal Planner Activity
            }
        });

        // Intent to open Shopping List Activity
        shoppingListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                //TODO: Send intent for Shopping List Activity
            }
        });

        ingredientMissingInfoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( context, IngredientStorageActivity.class );
                intent.putExtra("MissingCheck", true);
                startActivity( intent );
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate( R.menu.menu, menu );
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if( item.getItemId() == R.id.action_settings ) {
            Intent intent = new Intent(context, SpinnerSettingsActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * When the user clicks log out a {@link ModifyConfirmationFragment} will be launched.
     * If the user clicks "confirm" they will be brough back to the login page.
     * @param view
     */
    public void confirmLogOut( View view ) {
        ModifyConfirmationFragment deleteFragment = new ModifyConfirmationFragment(
                "Log Out",
                "Are you sure you want to log out?",
                context,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        FirebaseAuthenticationHandler.getFireAuth().authenticate.signOut();
                        finish();
                    }
                } );
        deleteFragment.display();
    }

    @Override
    public void signalChangeToAdapter() {

    }
}