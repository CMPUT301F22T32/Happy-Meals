package com.example.happymeals;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.happymeals.ingredient.IngredientStorageActivity;
import com.example.happymeals.ingredient.IngredientViewActivity;

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
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        Context context = this;

        // The 4 buttons to access the other activities
        Button ingredientStorageButton = findViewById( R.id.ingredient_storage_button );
        Button recipesButton = findViewById( R.id.recipes_button );
        Button mealPlannerButton = findViewById( R.id.meal_planner_button );
        Button shoppingListButton = findViewById( R.id.shopping_list_button );

        ingredientStorageButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                Intent intent = new Intent(context, IngredientStorageActivity.class);
                startActivity(intent);
            }
        });

        recipesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                //TODO: Send intent for Recipe View Activity
            }
        });

        mealPlannerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                //TODO: Send intent for Meal Planner Activity
            }
        });

        shoppingListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                //TODO: Send intent for Shopping List Activity
            }
        });
    }
}