package com.example.happymeals.shoppinglist;

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
import com.example.happymeals.R;
import com.example.happymeals.adapters.ShoppingListAdapter;
import com.example.happymeals.fragments.ModifyConfirmationFragment;
import com.example.happymeals.ingredient.Ingredient;
import com.example.happymeals.ingredient.IngredientStorageActivity;
import com.example.happymeals.mealplan.MealPlanActivity;
import com.example.happymeals.recipe.RecipeStorageActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

import java.util.ArrayList;
import java.util.Comparator;

public class ShoppingListActivity extends AppCompatActivity {

    private ShoppingList shoppingList;
    private ShoppingListAdapter shoppingListAdapter;

    private ListView ingredientListView;
    private final Context context = this;
    private BottomNavigationView bottomNavMenu;
    private ArrayList<ShoppingListItem> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);
        getWindow().setEnterTransition(null);

        shoppingList = ShoppingList.getInstance();
        ingredientListView = findViewById(R.id.shopping_list_ingredients);

        shoppingListAdapter = new ShoppingListAdapter( this,  shoppingList.getShoppingList(), shoppingList.getIngredientsToBuy() ) ;
        ingredientListView.setAdapter( shoppingListAdapter );

        TextView total = findViewById(R.id.shopping_list_total);
        total.setText(Integer.toString(shoppingList.getSize()));

        Spinner IngredientSort = findViewById(R.id.shopping_list_spinner);
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(ShoppingListActivity.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.shopping_list_options));
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        IngredientSort.setAdapter(dataAdapter);

        // Navigation
        bottomNavMenu = findViewById(R.id.bottomNavigationView);

        bottomNavMenu.setSelectedItemId(R.id.shopping_menu);
        bottomNavMenu.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override

            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(
                        ShoppingListActivity.this).toBundle();
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
        IngredientSort.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String itemSelected = adapterView.getItemAtPosition(i).toString();

                //if sort by "Amount" is selected
                if (itemSelected.equals("Amount Needed")) {
                    items = shoppingList.getShoppingList();
                    items.sort(new Comparator<ShoppingListItem>() {
                        @Override
                        public int compare(ShoppingListItem item1, ShoppingListItem item2) {
                            if (item1.getAmount() < item2.getAmount()) {
                                return 1;
                            } else if (item1.getAmount() > item2.getAmount()) {
                                return -1;
                            } else {
                                return 0;
                            }
                        }
                    });
                    shoppingListAdapter = new ShoppingListAdapter( ShoppingListActivity.this,  items, shoppingList.getIngredientsToBuy() ) ;
                    ingredientListView.setAdapter( shoppingListAdapter );
                }

                // sort by category
                else if (itemSelected.equals("Category")) {
                    items = shoppingList.getShoppingList();
                    items.sort(new Comparator<ShoppingListItem>() {
                        @Override
                        public int compare(ShoppingListItem item1, ShoppingListItem item2) {
                            return item1.getIngredient().getCategory().compareTo(item2.getIngredient().getCategory());
                        }
                    });
                    shoppingListAdapter = new ShoppingListAdapter( ShoppingListActivity.this,  items, shoppingList.getIngredientsToBuy() ) ;
                    ingredientListView.setAdapter( shoppingListAdapter );
                }

                
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                return;
            }
        });


        shoppingListAdapter.notifyDataSetChanged();
        DialogInterface.OnClickListener onItemsPickedUpListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                shoppingList.pickUpItems(shoppingListAdapter.getSelected());
                finish();
            }
        };

        findViewById(R.id.shopping_list_buy).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (shoppingListAdapter.getChanged()) {
                    new ModifyConfirmationFragment("Ingredient Pickup", "Have you picked up all these ingredients?", context, onItemsPickedUpListener).display();
                }
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        getWindow().setExitTransition(null);
    }
}