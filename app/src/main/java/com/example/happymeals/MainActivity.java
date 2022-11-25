package com.example.happymeals;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.happymeals.database.FireStoreManager;
import com.example.happymeals.ingredient.IngredientStorage;
import com.example.happymeals.recipe.RecipeStorage;
import com.example.happymeals.userlogin.StartScreenActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.DocumentReference;

import com.example.happymeals.ingredient.IngredientStorageActivity;
import com.example.happymeals.ingredient.IngredientViewActivity;
import com.example.happymeals.recipe.RecipeStorageActivity;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * This class is the entry point of the application and serves as the home
 * page for navigation. From this activity, all of the other main activities -- such as the
 * Ingredient Storage, Recipes, Meal Plan, and Shopping List -- can be viewed.
 */
public class MainActivity extends AppCompatActivity {

    /**
     * This is the function called whenever the MainActivity is created -- in our
     * case, this is on the launch of the app or when navigating back to the home page.
     * It it responsible for sending the intents to access all the other main views.
     * @param savedInstanceState The instance state to restore the activity to (if applicable) {@link Bundle}
     */

    BottomNavigationView bottomNavMenu;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        // Create the firebase manager connection along with all the storage classes.
        FireStoreManager.getInstance();
        RecipeStorage.getInstance();
        IngredientStorage.getInstance();

        Context context = this;

        // The 4 buttons to access the other activities


        // Navigation
        bottomNavMenu = findViewById(R.id.bottomNavigationView);


        bottomNavMenu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle();
                switch (item.getItemId()) {

                    case R.id.recipe_menu:
                        Toast.makeText(MainActivity.this, "Recipes", Toast.LENGTH_LONG).show();

                        Intent recipe_intent = new Intent( context, RecipeStorageActivity.class );
                        startActivity( recipe_intent, bundle );
                        break;

                    case R.id.ingredient_menu:
                        Toast.makeText(MainActivity.this, "Ingredients", Toast.LENGTH_LONG).show();
                        Intent ingredient_intent = new Intent(context, IngredientStorageActivity.class);
                        startActivity(ingredient_intent, bundle);
                        break;

                    case R.id.mealplan_menu:
                        Toast.makeText(MainActivity.this, "Meal Plan", Toast.LENGTH_LONG).show();
                        break;

                    case R.id.shopping_menu:
                        Toast.makeText(MainActivity.this, "Shopping List", Toast.LENGTH_LONG).show();
                        break;
                    default:
                }

                return true;

            }
        });


    }
}