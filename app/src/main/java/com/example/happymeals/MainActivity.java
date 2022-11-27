package com.example.happymeals;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.app.ActivityOptions;
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
import android.widget.Toast;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;

import com.example.happymeals.database.FireStoreManager;
import com.example.happymeals.database.FirebaseAuthenticationHandler;
import com.example.happymeals.fragments.ModifyConfirmationFragment;
import com.example.happymeals.ingredient.IngredientStorage;
import com.example.happymeals.mealplan.MealPlanActivity;
import com.example.happymeals.mealplan.MealPlanStorage;
import com.example.happymeals.recipe.PublicRecipeActivity;
import com.example.happymeals.recipe.RecipeStorage;

import com.example.happymeals.userlogin.LoginActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.firestore.DocumentReference;


import com.example.happymeals.ingredient.IngredientStorageActivity;
import com.example.happymeals.recipe.RecipeStorageActivity;
import com.example.happymeals.shoppinglist.ShoppingListActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class is the entry point of the application and serves as the home
 * page for navigation. From this activity, all of the other main activities -- such as the
 * Ingredient Storage, Recipes, Meal Plan, and Shopping List -- can be viewed.
 */
public class MainActivity extends AppCompatActivity {

    private Context context;
    //testing for todays meals
    ExpandableListView expandableListView;
    ExpandableListAdapter expandableListAdapter;
    List<String> expandableListTitle;
    HashMap<String, List<String>> expandableListDetail;

    BottomNavigationView bottomNavMenu;
    private FireStoreManager fsm;

    // Notification
    private NotificationManagerClass notification;
    private IngredientStorage ingredientStorage; // needed to genertate notifications


    /**
     * This is the function called whenever the MainActivity is created -- in our
     * case, this is on the launch of the app or when navigating back to the home page.
     * It it responsible for sending the intents to access all the other main views.
     * @param savedInstanceState The instance state to restore the activity to (if applicable) {@link Bundle}
     */
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            Toolbar toolbar = findViewById( R.id.appbar);
            setSupportActionBar(toolbar);
            // Create the firebase manager connection along with all the storage classes.
            fsm = FireStoreManager.getInstance();
            RecipeStorage.getInstance();
            IngredientStorage.getInstance();
            MealPlanStorage.getInstance();

        context = this;
        // Global Recipes Button
        TextView globalRecipes;

        expandableListView = (ExpandableListView) findViewById(R.id.expandableListView);
        expandableListDetail = ExpandableListDataPump.getData();
        expandableListTitle = new ArrayList<String>(expandableListDetail.keySet());
        expandableListAdapter = new CustomExpandableListAdapter(this, expandableListTitle, expandableListDetail);
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        expandableListTitle.get(groupPosition) + " List Expanded.",
                        Toast.LENGTH_SHORT).show();
            }
        });

        expandableListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getApplicationContext(),
                        expandableListTitle.get(groupPosition) + " List Collapsed.",
                        Toast.LENGTH_SHORT).show();

            }
        });

        expandableListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                Toast.makeText(
                        getApplicationContext(),
                        expandableListTitle.get(groupPosition)
                                + " -> "
                                + expandableListDetail.get(
                                expandableListTitle.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT
                ).show();
                return false;
            }
        });


        globalRecipes = findViewById(R.id.find_recipes);

        globalRecipes.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, PublicRecipeActivity.class));
            }
        });

        // Display the username of user
        TextView welcomeMessage = findViewById(R.id.user_welcome);
        welcomeMessage.setText("Enjoy a Happy Meal "
                + FirebaseAuthenticationHandler.getFireAuth().authenticate.getCurrentUser().getDisplayName());
        // The 4 buttons to access the other activities


        // Notification
        notification = new NotificationManagerClass("Update Information",
                "You have ingredients in storage that need to be updated", context,
                "UpdateInfo", 0);
        if ( ingredientStorage.isIngredientsMissingInfo() ) {
            notification.addNotification();
        }

        // Navigation
        bottomNavMenu = findViewById(R.id.bottomNavigationView);


        bottomNavMenu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override

            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this).toBundle();
                switch (item.getItemId()) {

                    case R.id.recipe_menu:
                        Toast.makeText(MainActivity.this, "Recipes", Toast.LENGTH_LONG).show();

                        Intent recipe_intent = new Intent(context, RecipeStorageActivity.class);
                        startActivity(recipe_intent, bundle);
                        break;

                    case R.id.ingredient_menu:
                        Toast.makeText(MainActivity.this, "Ingredients", Toast.LENGTH_LONG).show();
                        Intent ingredient_intent = new Intent(context, IngredientStorageActivity.class);
                        startActivity(ingredient_intent, bundle);
                        break;

                    case R.id.mealplan_menu:
                        Toast.makeText(MainActivity.this, "Meal Plan", Toast.LENGTH_LONG).show();
                        Intent mealplan_intent = new Intent(context, MealPlanActivity.class);
                        startActivity(mealplan_intent, bundle);
                        break;

                    case R.id.shopping_menu:
                        Toast.makeText(MainActivity.this, "Shopping List", Toast.LENGTH_LONG).show();
                        Intent shoppinglist_intent = new Intent(context, ShoppingListActivity.class);
                        startActivity(shoppinglist_intent, bundle);
                        break;
                    default:
                }

                return true;

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
            Intent intent = new Intent( context, SpinnerSettingsActivity.class );
            startActivity( intent );
        }
        return super.onOptionsItemSelected( item );
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
                        FireStoreManager.clearInstance();
                        finish();
                    }
                } );
        deleteFragment.display();
    }
}
