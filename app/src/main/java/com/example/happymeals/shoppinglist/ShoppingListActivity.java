package com.example.happymeals.shoppinglist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import com.example.happymeals.R;
import com.example.happymeals.ingredient.IngredientStorageArrayAdapter;

public class ShoppingListActivity extends AppCompatActivity {

    private ShoppingList shoppingList;
    private ShoppingListAdapter shoppingListAdapter;
    private ListView ingredientListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        ingredientListView = findViewById(R.id.shopping_list_ingredients);
        shoppingList = ShoppingList.getInstance();

        shoppingListAdapter = new ShoppingListAdapter( this, shoppingList.getIngredientsToBuy(), shoppingList.getStoredIngredients() ) ;
        ingredientListView.setAdapter( shoppingListAdapter );
    }
}