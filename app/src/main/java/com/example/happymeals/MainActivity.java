package com.example.happymeals;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.happymeals.database.FireStoreManager;
import com.example.happymeals.database.FirebaseAuthenticationHandler;
import com.example.happymeals.fragments.ModifyConfirmationFragment;
import com.example.happymeals.ingredient.IngredientStorage;
import com.example.happymeals.mealplan.MealPlanStorage;
import com.example.happymeals.recipe.PublicRecipeActivity;
import com.example.happymeals.recipe.RecipeStorage;
import com.example.happymeals.userlogin.StartScreenActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.time.Instant;
import java.util.Calendar;

/**
 * This class is the entry point of the application and serves as the home
 * page for navigation. From this activity, all of the other main activities -- such as the
 * Ingredient Storage, Recipes, Meal Plan, and Shopping List -- can be viewed.
 */
public class MainActivity extends AppCompatActivity {

    private Context context;
    //testing for todays meals

    CalendarView calendarView;

    BottomNavigationView bottomNavMenu;
    private FireStoreManager fsm;

    // Notification
    private NotificationManagerClass notification;
    private IngredientStorage ingredientStorage; // needed to genertate notifications


    /**
     * This is the function called whenever the MainActivity is created -- in our
     * case, this is on the launch of the app or when navigating back to the home page.
     * It it responsible for sending the intents to access all the other main views.
     *
     * @param savedInstanceState The instance state to restore the activity to ( if applicable ) {@link Bundle}
     */
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_main );
        getWindow().setEnterTransition( null );

        Toolbar toolbar = findViewById( R.id.appbar );
        setSupportActionBar( toolbar );
        // Create the firebase manager connection along with all the storage classes.
        fsm = FireStoreManager.getInstance();
        RecipeStorage.getInstance();
        MealPlanStorage.getInstance();
        ingredientStorage = IngredientStorage.getInstance();

        calendarView = findViewById( R.id.main_activity_calendar_view );
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis( Instant.now().toEpochMilli() );
        calendarView.setDate( c.getTimeInMillis() );

        context = this;
        // Global Recipes Button
        TextView globalRecipes;

        HappyMealBottomNavigation bottomNavMenu =
                new HappyMealBottomNavigation( 
                        findViewById( R.id.bottomNavigationView ), this, R.id.home_menu );

        bottomNavMenu.setupBarListener();

        globalRecipes = findViewById( R.id.find_recipes );

        globalRecipes.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick( View view ) {
                startActivity( new Intent( MainActivity.this, PublicRecipeActivity.class ) );
            }
        } );

        // Display the username of user
        TextView welcomeMessage = findViewById( R.id.user_welcome );
        String welcomeMessagePrompt = String.format( "Enjoy a Happy Meal %s", FirebaseAuthenticationHandler.getFireAuth().authenticate.getCurrentUser().getDisplayName() );
        welcomeMessage.setText( welcomeMessagePrompt );

        globalRecipes = findViewById( R.id.find_recipes );
        globalRecipes.setOnClickListener( new View.OnClickListener() {

            @Override
            public void onClick( View view ) {
                startActivity( new Intent ( MainActivity.this, PublicRecipeActivity.class ) );
            }
        } );

        // Notification
        notification = new NotificationManagerClass( "Update Information",
                "You have ingredients in storage that need to be updated", context,
                "UpdateInfo", 0 );
        if ( ingredientStorage.isIngredientsMissingInfo() ) {
            notification.addNotification();
        }

    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu ) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate( R.menu.menu, menu );
        return super.onCreateOptionsMenu( menu );
    }

    @Override
    public boolean onOptionsItemSelected( @NonNull MenuItem item ) {
        if ( item.getItemId() == R.id.action_settings ) {
            Intent intent = new Intent( context, SpinnerSettingsActivity.class );
            startActivity( intent );
        }
        return super.onOptionsItemSelected( item );
    }

    /**
     * When the user clicks log out a {@link ModifyConfirmationFragment} will be launched.
     * If the user clicks "confirm" they will be brough back to the login page.
     *
     * @param view
     */
    public void confirmLogOut( View view ) {
        ModifyConfirmationFragment deleteFragment = new ModifyConfirmationFragment( 
                "Log Out",
                "Are you sure you want to log out?",
                context,
                new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick( DialogInterface dialogInterface, int i ) {
                        FirebaseAuthenticationHandler.getFireAuth().authenticate.signOut();
                        FireStoreManager.clearInstance();
                        Intent loginIntent = new Intent( getApplicationContext(), StartScreenActivity.class );
                        loginIntent.addFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP );
                        startActivity( loginIntent );
                    }
                } );
        deleteFragment.display();
    }

    @Override
    protected void onPause() {
        super.onPause();
        getWindow().setExitTransition( null );
    }


}
