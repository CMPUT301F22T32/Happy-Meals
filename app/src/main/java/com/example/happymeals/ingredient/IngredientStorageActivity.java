package com.example.happymeals.ingredient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.Spinner;

import com.example.happymeals.MainActivity;
import com.example.happymeals.adapters.IngredientStorageArrayAdapter;
import com.example.happymeals.database.DatasetWatcher;
import com.example.happymeals.R;
import com.example.happymeals.databinding.BottomNavigationBarBinding;
import com.example.happymeals.fragments.ModifyConfirmationFragment;
import com.example.happymeals.mealplan.MealPlanActivity;
import com.example.happymeals.recipe.RecipeStorageActivity;
import com.example.happymeals.shoppinglist.ShoppingListActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import java.util.Comparator;

/**
 * This is the activity that will display a list of the ingredients in the storage. The list will
 * be persistent and display ingredient data that has been stored in the database. Ingredients can
 * be selected to view details, and new ingredients can be added by hitting the plus button.
 * @author kstark sruduke
 */
public class IngredientStorageActivity extends AppCompatActivity implements DatasetWatcher {

    private Context context;
    /**
     * The {@link ListView} that displays the {@link Ingredient} objects currently stored.
     */
    private ListView storageListView;

    /**
     * The {@link IngredientStorageArrayAdapter} used to display the brief details of each
     * {@link Ingredient}.
     */
    private IngredientStorageArrayAdapter storageAdapter;

    private IngredientStorage ingredientStorage;
    private CheckBox viewMissingInfo;
    private BottomNavigationView bottomNavMenu;
    /**
     * This function is called whenever the activity is spawned; it initializes the {@link #storageAdapter}
     * and {@link #storageListView} to properly display the polled data. Action listeners are also set
     * in this step; clicking on an {@link Ingredient} will open a view containing ingredient details
     * while clicking the plus button in the corner will open a view to add a new ingredient.
     * Also deals with sorting the ingredients.
     * @param savedInstanceState The state to restore the view to (if applicable).
     */
    @Override
    protected void onCreate( Bundle savedInstanceState)  {
        super.onCreate( savedInstanceState) ;
        setContentView( R.layout.activity_ingredient_storage ) ;

        Intent inIntent = getIntent();

        context = this;

        ingredientStorage = IngredientStorage.getInstance();

        storageListView = findViewById( R.id.storage_list) ;
        ingredientStorage.setListeningActivity(this);

        viewMissingInfo = findViewById( R.id.missing_ingredients_check);
        Boolean missingChecked = inIntent.getBooleanExtra("MissingCheck", false);
        viewMissingInfo.setChecked( missingChecked );
        if ( missingChecked ) {
            storageAdapter = new IngredientStorageArrayAdapter( this, ingredientStorage.getIngredientsMissingInfo() ) ;
        }
        else {
            storageAdapter = new IngredientStorageArrayAdapter( this, ingredientStorage.getIngredients() );
        }


        storageListView.setAdapter( storageAdapter );

        // Navigation
        bottomNavMenu = findViewById(R.id.bottomNavigationView);

        bottomNavMenu.setSelectedItemId(R.id.ingredient_menu);
        bottomNavMenu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override

            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(
                        (Activity) context).toBundle();
                switch (item.getItemId()) {
                    case R.id.home_menu:
                        Intent home_intent = new Intent(context, MainActivity.class);
                        startActivity(home_intent, bundle);
                        break;

                    case R.id.recipe_menu:
                        Intent recipe_intent = new Intent(context, RecipeStorageActivity.class);
                        startActivity(recipe_intent, bundle);
                        break;

                    case R.id.ingredient_menu:
                        Intent ingredient_intent = new Intent(context, IngredientStorageActivity.class);
                        startActivity(ingredient_intent, bundle);
                        break;

                    case R.id.mealplan_menu:
                        Intent mealplan_intent = new Intent(context, MealPlanActivity.class);
                        startActivity(mealplan_intent, bundle);
                        break;

                    case R.id.shopping_menu:
                        Intent shoppinglist_intent = new Intent(context, ShoppingListActivity.class);
                        startActivity(shoppinglist_intent, bundle);
                        break;
                    default:
                }

                return true;

            }
        });


        Boolean missingInfo = ingredientStorage.isIngredientsMissingInfo();
        if ( missingInfo ) {
            viewMissingInfo.setVisibility(View.VISIBLE);
        }
        else {
            viewMissingInfo.setVisibility(View.GONE);
        }






        FloatingActionButton add_button = findViewById( R.id.add_new_ingredient_button) ;

        Spinner IngredientSort = findViewById(R.id.ingredient_filter);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(IngredientStorageActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.ingredient_options));
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        IngredientSort.setAdapter(dataAdapter);

        IngredientSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String itemSelected = adapterView.getItemAtPosition(i).toString();

                //if sort by "Amount" is selected
                if(itemSelected.equals("Amount")){
                    storageAdapter.sort(new Comparator<Ingredient>() {
                        @Override
                        public int  compare(Ingredient i1, Ingredient i2) {
                            if ((i1.getCategory().equals(i2.getCategory())) && (i1.getAmount() < i2.getAmount()) )
                                return 1;
                            else if ((i1.getCategory().equals(i2.getCategory())) && (i1.getAmount() > i2.getAmount()) )
                                return -1;
                            else
                                return 0;
                        }
                    });

                }

                if(itemSelected.equals("Best Before Date")){
                    storageAdapter.sort(new Comparator<Ingredient>() {
                        @Override
                        public int compare(Ingredient i1, Ingredient i2) {
                            if (i1.getBestBeforeDate().compareTo(i2.getBestBeforeDate()) > 0 )
                                return 1;
                            else if (i1.getBestBeforeDate().compareTo(i2.getBestBeforeDate()) < 0 )
                                return -1;
                            else
                                return 0;
                        }
                    });
                }
                if(itemSelected.equals("DefaultLocationSpinners")){
                    storageAdapter.sort(new Comparator<Ingredient>() {
                        @Override
                        public int compare(Ingredient i1, Ingredient i2) {
                            // compares and checks best before dates
                            return i1.getLocation().compareTo(i2.getLocation());
                        }
                    });
                }

                if(itemSelected.equals("Ingredient Category")){
                    storageAdapter.sort(new Comparator<Ingredient>() {
                        @Override
                        public int compare(Ingredient i1, Ingredient i2) {
                           return i1.getCategory().compareTo(i2.getCategory());
                        }
                    });
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        storageListView.setAdapter( storageAdapter );
        storageListView.setOnItemClickListener( new AdapterView.OnItemClickListener( )  {
            @Override
            public void onItemClick( AdapterView<?> adapterView, View view, int i, long l )  {
                startIngredientActivity( false, i) ;
            }
        }) ;

        storageListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Ingredient ingredient = ingredientStorage.getIngredients().get( i );
                ModifyConfirmationFragment modifyConfirmationFragment =
                        new ModifyConfirmationFragment(
                        "Remove Ingredient",
                    "Are you sure you want to remove " +
                            ingredient.getName() + " as an Ingredient",
                            context,
                            new DialogInterface.OnClickListener() {

                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    IngredientStorage.getInstance().removeIngredient( ingredient );
                                }
                            }
                        );
                modifyConfirmationFragment.display();
                return true;
            }
        });

        add_button.setOnClickListener( new View.OnClickListener( )  {
            @Override
            public void onClick( View view)  {
                startIngredientActivity( true ) ;
            }
        }) ;

        viewMissingInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if ( viewMissingInfo.isChecked() ) {
                    storageAdapter.updateList( ingredientStorage.getIngredientsMissingInfo() );
                    signalChangeToAdapter();
                }
                else {
                    storageAdapter.updateList( ingredientStorage.getIngredients() );
                    signalChangeToAdapter();
                }
            }
        });
    }


    /**
     * This function facilitates the process of creating an intent for the edit/view activity for an
     * {@link Ingredient}. The parameters represent the extras to pass to the {@link IngredientViewActivity}
     * and the function starts the activity.
     * @param addingNewIngredient true when the user is adding a new {@link Ingredient}, false when the user
     *                            is trying to view an existing one. ({@link Boolean})
     * @param index optional parameter representing the index of the ingredient in the {@link IngredientStorage}
     *              list, which the {@link IngredientViewActivity} will use to get {@link Ingredient} data.
     */
    private void startIngredientActivity( boolean addingNewIngredient, int... index )  {
        Intent ingredientIntent = new Intent(  this, IngredientViewActivity.class ) ;
        ingredientIntent.putExtra( IngredientViewActivity.ADD_INGREDIENT, addingNewIngredient ) ;
        if ( index.length > 0 )
            ingredientIntent.putExtra( IngredientViewActivity.INGREDIENT_INDEX, index[0] ) ;
        startActivity( ingredientIntent ) ;
    }

    /**
     * Tells the adapter that the data set has been updated.
     */
    @Override
    public void signalChangeToAdapter() {
        storageAdapter.notifyDataSetChanged();
    }
}