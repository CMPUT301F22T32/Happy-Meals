package com.example.happymeals.recipe;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;

import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.happymeals.MainActivity;
import com.example.happymeals.adapters.RecipeStorageAdapter;
import com.example.happymeals.database.DatasetWatcher;
import com.example.happymeals.R;

import com.example.happymeals.fragments.ModifyConfirmationFragment;
import com.example.happymeals.ingredient.Ingredient;
import com.example.happymeals.ingredient.IngredientStorageActivity;

import com.example.happymeals.ingredient.IngredientViewActivity;
import com.example.happymeals.mealplan.MealPlanActivity;
import com.example.happymeals.shoppinglist.ShoppingListActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Locale;


/**
 * Class that holds the {@link AppCompatActivity} which will run the activity for the user.
 * This activity will show all the recipes saved by the user and allow for the start
 * of new activities. It will display simple details of the recipe and allow the user to launch
 * activity to create a new recipe.
 * Also deals with sorting the recipes.
 */
public class RecipeStorageActivity extends AppCompatActivity implements DatasetWatcher {

    private ListView recipeListView;
    private RecipeStorage recipeStorage;
    private RecipeStorageAdapter adapter;
    private FloatingActionButton newRecipeButton;
    private Context context;
    BottomNavigationView bottomNavMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_storage);
        getWindow().setEnterTransition(null);
        context = this;
        recipeListView = findViewById(R.id.recipe_list);
        recipeStorage = RecipeStorage.getInstance();
        recipeStorage.setListeningActivity(this);
        ArrayList< Recipe > storedRecipes = recipeStorage.getRecipes();
        adapter = new RecipeStorageAdapter(this, storedRecipes );

        recipeListView.setAdapter(adapter);
        recipeListView.setOnItemClickListener( new AdapterView.OnItemClickListener( )  {
            @Override
            public void onItemClick( AdapterView<?> adapterView, View view, int i, long l )  {
                Intent intent = new Intent(  context, RecipeDetailsActivity.class ) ;
                intent.putExtra( "Index", i ) ;
                startActivity( intent ) ;
            }
        }) ;

        // Navigation
        bottomNavMenu = findViewById(R.id.bottomNavigationView);

        bottomNavMenu.setSelectedItemId(R.id.recipe_menu);
        bottomNavMenu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override

            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(
                        RecipeStorageActivity.this).toBundle();
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

        recipeListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                Recipe recipe = storedRecipes.get( i );
                ModifyConfirmationFragment deleteFragment = new ModifyConfirmationFragment(
                        "Remove Recipe",
                        String.format("Are you sure you want to remove %s?",
                        storedRecipes.get( i ).getName() ),
                        context,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int in) {
                                recipeStorage.removeRecipe( recipe );
                            }
                        });
                deleteFragment.display();
                return true;
            }
        });


        Spinner RecipeSort = findViewById(R.id.recipe_filter);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(RecipeStorageActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.recipe_options));
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        RecipeSort.setAdapter(dataAdapter);
        RecipeSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String itemSelected = adapterView.getItemAtPosition(i).toString();
                //if sort by "Title" is selected
                if(itemSelected.equals("Title")){
                    adapter.sort(new Comparator<Recipe>() {
                        @Override
                        public int compare(Recipe r1, Recipe r2) {
                            return r1.getName().toLowerCase().compareTo(r2.getName().toLowerCase());
                        }

                    });
                    signalChangeToAdapter();
                    dataAdapter.notifyDataSetChanged();
                }


                //if sort by "Total Time" is selected
                 if(itemSelected.equals("Total Time")){
                    adapter.sort(new Comparator<Recipe>() {
                        @Override
                        public int compare(Recipe r1, Recipe r2) {
                            if ((r1.getPrepTime() + r1.getCookTime()) < (r2.getPrepTime() + r2.getCookTime()))
                                return 1;

                            else if ((r1.getPrepTime() + r1.getCookTime()) > (r2.getPrepTime() + r2.getCookTime()))
                                return -1;

                            else
                                return 0;


                        }

                    });
                    signalChangeToAdapter();
                    dataAdapter.notifyDataSetChanged();
                }

                if(itemSelected.equals("Serving")){
                    adapter.sort(new Comparator<Recipe>() {
                        @Override
                        public int compare(Recipe r1, Recipe r2) {
                            return Double.compare(r2.getServings(), r1.getServings());
                        }
                    });
                    signalChangeToAdapter();
                }
                if(itemSelected.equals("Description")){
                    adapter.sort(new Comparator<Recipe>() {
                        @Override
                        public int compare(Recipe r1, Recipe r2) {
                            return r1.getDescription().toLowerCase().compareTo(r2.getDescription().toLowerCase());
                        }
                    });
                    signalChangeToAdapter();
                }
            }




            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });

        signalChangeToAdapter();
        TextView exploreRecipesText = findViewById( R.id.recipe_storage_explore_label );
        exploreRecipesText.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( context, PublicRecipeActivity.class );
                startActivity( intent );
            }
        });

        signalChangeToAdapter();
        newRecipeButton = findViewById( R.id.recipe_storage_add_button );

        newRecipeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent( context, RecipeAddActivity.class );
                startActivity( intent );
            }
        });
    }

    public void signalChangeToAdapter() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void finish() {
        super.finish();
    }

    public void onGoBack( View view ) {
        // This will resume the parent activity.
        this.onBackPressed();
    }

    @Override
    public void onPause() {
        super.onPause();
        getWindow().setExitTransition(null);
    }
}