package com.example.happymeals.mealplan;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.happymeals.Constants;
import com.example.happymeals.HappyMealBottomNavigation;
import com.example.happymeals.R;
import com.example.happymeals.adapters.IngredientStorageArrayAdapter;
import com.example.happymeals.adapters.RecipeStorageAdapter;
import com.example.happymeals.database.DatasetWatcher;
import com.example.happymeals.fragments.MealPlanPromptFragment;
import com.example.happymeals.fragments.ModifyConfirmationFragment;
import com.example.happymeals.ingredient.Ingredient;
import com.example.happymeals.recipe.Recipe;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

/**
 * This activity acts as the 'home page' for MealPlans. It features a calendar which can be selected
 * and will display the planned meals for the day (if applicable). From this activity users can
 * see a list of their meal plans or add new meal plans, while being able to explore the meals
 * they currently have set.
 */
public class MealPlanActivity extends AppCompatActivity implements DatasetWatcher {
    private LinearLayout breakfastTab;
    private LinearLayout lunchTab;
    private LinearLayout dinnerTab;

    private ConstraintLayout mealPlanTab;
    private ConstraintLayout legend;
    private TextView startDay;
    private TextView endDay;

    private TextView noMPTab;

    private TextView breakfastText;
    private TextView lunchText;
    private TextView dinnerText;

    private CheckBox breakfastBox;
    private CheckBox lunchBox;
    private CheckBox dinnerBox;

    private Button breakfastDetails;
    private Button lunchDetails;
    private Button dinnerDetails;

    private Button viewAll;
    private TextView calendarDate;
    private CalendarView calendarView;

    private Context context = this;
    private Date date;

    private RecipeStorageAdapter recipeAdapter = null;
    private IngredientStorageArrayAdapter ingredientAdapter = null;

    private MealPlanStorage mps;
    private MealPlan mealPlan;

    private BottomNavigationView bottomNavMenu;

    @Override
    protected void onPause() {
        super.onPause();
        getWindow().setExitTransition( null );
    }

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_meal_plan );
        getWindow().setEnterTransition( null );

        mps = MealPlanStorage.getInstance();
        mps.setListeningActivity( this );

        breakfastTab = findViewById( R.id.mp_breakfast_tab );
        lunchTab = findViewById( R.id.mp_lunch_tab );
        dinnerTab = findViewById( R.id.mp_dinner_tab );
        mealPlanTab = findViewById( R.id.current_mp );
        legend = findViewById( R.id.mp_legend );

        startDay = findViewById( R.id.current_mp_start );
        endDay = findViewById( R.id.current_mp_end );
        noMPTab = findViewById( R.id.no_mp_tab );

        breakfastText = findViewById( R.id.breakfast_recipe );
        lunchText = findViewById( R.id.lunch_recipe );
        dinnerText = findViewById( R.id.dinner_recipe );

        breakfastBox = findViewById( R.id.breakfast_checkbox );
        lunchBox = findViewById( R.id.lunch_checkbox );
        dinnerBox = findViewById( R.id.dinner_checkbox );

        breakfastDetails = findViewById( R.id.breakfast_details );
        lunchDetails = findViewById( R.id.lunch_details );
        dinnerDetails = findViewById( R.id.dinner_details );

        calendarView = findViewById( R.id.meal_plan_calendar_view );
        calendarDate = findViewById( R.id.meal_plan_view_date );
        viewAll = findViewById( R.id.view_all_meal_plans );

        date = Calendar.getInstance().getTime();
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis( Instant.now().toEpochMilli() );
        calendarView.setDate( c.getTimeInMillis() );

        mealPlan = mps.getMealPlanForDay( date );

        HappyMealBottomNavigation bottomNavMenu =
                new HappyMealBottomNavigation( 
                        findViewById( R.id.bottomNavigationView ), this, R.id.mealplan_menu );


        bottomNavMenu.setupBarListener();

        setCalendarListeners();
        setButtonListener();
        setCheckBoxListeners();
        setDetailsListeners();

        calendarView.setDate( Calendar.getInstance().getTimeInMillis() );

        setMeals();
    }

    private void changeViewForEmptyStorage() {
        if ( mps.getMealPlans().size() == 0 ) {
            viewAll.setVisibility( View.GONE );
            String prompt = "Looks like you don't have any meal plans yet... make one in just a few clicks.";
            noMPTab.setText( prompt );
        }
        else {
            viewAll.setVisibility( View.VISIBLE );
        }
    }

    private void setCalendarListeners() {
        calendarView.setOnDateChangeListener( new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange( @NonNull CalendarView calendarView, int year, int month, int dayOfMonth ) {
                Calendar cal = Calendar.getInstance();
                cal.set( year, month, dayOfMonth );

                date = cal.getTime();

                String dateStr = new SimpleDateFormat( "EEEE, MMM d", Locale.CANADA ).format( date );
                calendarDate.setText( dateStr );

                setMeals();
            }
        } );
    }

    private void setButtonListener() {
        viewAll.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                Intent intent = new Intent( context, MealPlanListViewActivity.class );
                startActivity( intent );
            }
        } );

        findViewById( R.id.add_meal_plan_floating_button ).setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                MealPlanPromptFragment frag = new MealPlanPromptFragment( date );
                frag.show( getSupportFragmentManager(), "MEAL_PROMPT_FRAGMENT" );
            }
        } );
    }

    private void setCheckBoxListeners() {
        //TODO some sort of rating system fragment

        DialogInterface.OnClickListener breakfastListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick( DialogInterface dialogInterface, int i ) {
                breakfastBox.setChecked( true );
                mealPlan.setMealMade( date, Constants.MEAL_OF_DAY.BREAKFAST, true );
                mps.updateMealPlan( mealPlan );
                breakfastBox.setClickable( false );
                mealPlan.consumeMeal( date, Constants.MEAL_OF_DAY.BREAKFAST );
            }
        };

        DialogInterface.OnClickListener lunchListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick( DialogInterface dialogInterface, int i ) {
                lunchBox.setChecked( true );
                mealPlan.setMealMade( date, Constants.MEAL_OF_DAY.LUNCH, true );
                mps.updateMealPlan( mealPlan );
                lunchBox.setClickable( false );
                mealPlan.consumeMeal( date, Constants.MEAL_OF_DAY.LUNCH );
            }
        };

        DialogInterface.OnClickListener dinnerListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick( DialogInterface dialogInterface, int i ) {
                dinnerBox.setChecked( true );
                mealPlan.setMealMade( date, Constants.MEAL_OF_DAY.DINNER, true );
                mps.updateMealPlan( mealPlan );
                dinnerBox.setClickable( false );
                mealPlan.consumeMeal( date, Constants.MEAL_OF_DAY.DINNER );
            }
        };

        CheckBox[] boxes = new CheckBox[]{breakfastBox, lunchBox, dinnerBox};
        DialogInterface.OnClickListener[] listeners = new DialogInterface.OnClickListener[]{breakfastListener, lunchListener, dinnerListener};
        for ( int i = 0; i < boxes.length; i++ ) {
            int index = i;
            boxes[index].setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick( View view ) {
                    boxes[index].setChecked( false );
                    ModifyConfirmationFragment frag = new ModifyConfirmationFragment( "Meal Confirmation", "Did you make the meal? This cannot be undone.", context, listeners[index] );
                    frag.display();
                }
            } );
        }
    }

    private void setDetailsListeners() {
        Button[] buttons = new Button[]{breakfastDetails, lunchDetails, dinnerDetails};
        Constants.MEAL_OF_DAY[] mealTimes = Constants.MEAL_OF_DAY.values();

        for ( int i = 0; i < buttons.length; i++ ) {
            int index = i;
            buttons[index].setOnClickListener( new View.OnClickListener() {
                @Override
                public void onClick( View view ) {
                    if ( mealPlan.getMeal( date, mealTimes[index] ) == null )
                        return;

                    if ( mealPlan.getMealType( date, mealTimes[index] ) == Constants.COLLECTION_NAME.INGREDIENTS ) {
                        ArrayList<Ingredient> ingredients = mealPlan.getMealIngredients( date, mealTimes[index] );
                        IngredientStorageArrayAdapter ingredientAdapter = new IngredientStorageArrayAdapter( context, ingredients );

                        AlertDialog.Builder builder = new AlertDialog.Builder( context );

                        builder.setTitle( "Ingredients" )
                                .setView( R.layout.list_view_fragment )
                                .setAdapter( ingredientAdapter, null )
                                .setPositiveButton( "OK", null );

                        AlertDialog fragment = builder.create();
                        fragment.show();

                    } else if ( mealPlan.getMealType( date, mealTimes[index] ) == Constants.COLLECTION_NAME.RECIPES ) {

                        ArrayList<Recipe>recipes = mealPlan.getMealRecipes( date, mealTimes[index] );
                        RecipeStorageAdapter recipeAdapter = new RecipeStorageAdapter( context, recipes );

                        AlertDialog.Builder builder = new AlertDialog.Builder( context );

                        builder.setTitle( "Recipes" )
                                .setView( R.layout.list_view_fragment )
                                .setAdapter( recipeAdapter, null )
                                .setPositiveButton( "OK", null );

                        AlertDialog fragment = builder.create();
                        fragment.show();
                    }
                }
            } );
        }
    }

    private void setMeals() {
        mealPlan = mps.getMealPlanForDay( date );

        LinearLayout[] tabs = new LinearLayout[]{breakfastTab, lunchTab, dinnerTab};

        if ( mealPlan == null ) {
            noMPTab.setVisibility( View.VISIBLE );
            String prompt = "There is no meal planner for this week.";
            noMPTab.setText( prompt );
            changeViewForEmptyStorage();
            for ( ConstraintLayout tab : new ConstraintLayout[]{mealPlanTab, legend} )
                tab.setVisibility( View.GONE );
            for ( LinearLayout tab : tabs )
                tab.setVisibility( View.GONE );
            return;
        }

        String buttonMessage = "View all meal plans";
        viewAll.setText( buttonMessage );

        setDetailsListeners();
        setCheckBoxListeners();

        mealPlanTab.setVisibility( View.VISIBLE );
        startDay.setText( mealPlan.getStartDateString() );
        endDay.setText( mealPlan.getEndDateString() );

        String prompt = "There are no meals planned for this day.";
        noMPTab.setText( prompt );

        if ( mealPlan.getNumberOfMealsForDay( date ) == 0 ) {
            noMPTab.setVisibility( View.VISIBLE );
            legend.setVisibility( View.GONE );

            for ( LinearLayout tab : tabs )
                tab.setVisibility( View.GONE );
        }
        else {
            noMPTab.setVisibility( View.GONE );
            legend.setVisibility( View.VISIBLE );

            ArrayList<String> names = new ArrayList<>();
            ArrayList<Boolean> made = new ArrayList<>();

            for ( Constants.MEAL_OF_DAY mealTime : Constants.MEAL_OF_DAY.values() ) {
                Meal meal = mealPlan.getMeal( date, mealTime );
                if ( meal == null ) {
                    names.add( null );
                    made.add( null );
                }
                else {
                    names.add( buildDisplayString( meal.getItems() ) );
                    made.add( meal.isMade() );
                }
            }

            TextView[] views = new TextView[]{breakfastText, lunchText, dinnerText};
            CheckBox[] boxes = new CheckBox[]{breakfastBox, lunchBox, dinnerBox};

            for ( int i = 0; i < 3; i++ ) {
                if ( names.get( i ) == null )
                    tabs[i].setVisibility( View.GONE );
                else {
                    tabs[i].setVisibility( View.VISIBLE );
                    views[i].setText( names.get( i ) );
                    boxes[i].setChecked( made.get( i ) );
                    if ( boxes[i].isChecked() )
                        boxes[i].setClickable( false );
                }
            }
        }
    }

    private String buildDisplayString( HashMap<String, Double> mealPlanItems ) {
        String[] names = mealPlanItems.keySet().toArray( new String[0] );
        StringBuilder sb = new StringBuilder();
        int size = names.length - 1;

        if ( size < 0 ) {
            Log.e( "MealPlanDisplay", "No recipe provided for meal." );
            return null;
        }

        for ( int i = 0; i < size; i++ ) {
            sb.append( names[i] );
            sb.append( ", " );
        }

        sb.append( names[size] );

        if ( sb.length() > 20 ) {
            sb.setLength( 20 );
            sb.append( " ... " );
        }

        return sb.toString();
    }

    @Override
    public void signalChangeToAdapter() {
        if ( recipeAdapter != null )
            recipeAdapter.notifyDataSetChanged();

        if ( ingredientAdapter != null )
            ingredientAdapter.notifyDataSetChanged();
    }
}