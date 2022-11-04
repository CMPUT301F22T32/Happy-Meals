package com.example.happymeals;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.firestore.DocumentReference;

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
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );

        // Create the firebase manager connection along with all the storage classes.
        FireStoreManager.getInstance();
        RecipeStorage.getInstance();
        // The 4 buttons to access the other activities
        Button ingredientStorageButton = findViewById( R.id.ingredient_storage_button );
        Button recipesButton = findViewById( R.id.recipes_button );
        Button mealPlannerButton = findViewById( R.id.meal_planner_button );
        Button shoppingListButton = findViewById( R.id.shopping_list_button );

//        Ingredient tomato = new Ingredient("Tomato", "This is a nice red fuit/veggie", new Date(),
//                Constants.Location.FRIDGE, 3, Constants.AmountUnit.COUNT,
//                Constants.IngredientCategory.FRUIT);
//        Ingredient Pickle = new Ingredient("Pickle", "This is a nice red fuit/veggie", new Date(),
//                Constants.Location.FRIDGE, 3, Constants.AmountUnit.COUNT,
//                Constants.IngredientCategory.FRUIT);
//        Ingredient Bread = new Ingredient("Bread", "This is a nice red fuit/veggie", new Date(),
//                Constants.Location.FRIDGE, 3, Constants.AmountUnit.COUNT,
//                Constants.IngredientCategory.FRUIT);
//        ArrayList< Ingredient > ilist = new ArrayList<>();
//        ilist.add( tomato );
//        ilist.add( Pickle );
//        ilist.add( Bread );
//        ArrayList< String > comments = new ArrayList<>();
//        comments.add( "This is a comment");
//        comments.add( "This is the second comment" );
//        fsm.addData(Constants.COLLECTION_NAME.INGREDIENTS, tomato);
//        fsm.addData(Constants.COLLECTION_NAME.INGREDIENTS, Pickle);
//        fsm.addData(Constants.COLLECTION_NAME.INGREDIENTS, Bread);
//        HashMap< String, DocumentReference > mapi = new HashMap<>();
//        mapi.put( tomato.getName(), fsm.getDocReferenceTo(Constants.COLLECTION_NAME.INGREDIENTS, tomato) );
//        mapi.put( Pickle.getName(), fsm.getDocReferenceTo(Constants.COLLECTION_NAME.INGREDIENTS, Pickle));
//        mapi.put( Bread.getName(), fsm.getDocReferenceTo(Constants.COLLECTION_NAME.INGREDIENTS, Bread) );
//        Recipe testipi = new Recipe("Good Good", 12.5, "This is that good good", comments,
//                mapi,
//                comments,
//        23, 3);
//        fsm.addData(Constants.COLLECTION_NAME.RECIPES, testipi);
        ingredientStorageButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                //TODO: Send intent for Ingredient Storage Activity
            }
        });

        recipesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                //TODO: Send intent for Recipe View Activity
                Intent intent = new Intent( MainActivity.this, RecipeStorageActivity.class );
                startActivity( intent );
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