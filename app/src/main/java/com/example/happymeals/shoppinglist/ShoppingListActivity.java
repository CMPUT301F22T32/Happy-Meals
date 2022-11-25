package com.example.happymeals.shoppinglist;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.happymeals.R;
import com.example.happymeals.adapters.ShoppingListAdapter;
import com.example.happymeals.fragments.ModifyConfirmationFragment;

public class ShoppingListActivity extends AppCompatActivity {

    private ShoppingList shoppingList;
    private ShoppingListAdapter shoppingListAdapter;

    private ListView ingredientListView;
    private final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping_list);

        shoppingList = ShoppingList.getInstance();
        ingredientListView = findViewById(R.id.shopping_list_ingredients);

        shoppingListAdapter = new ShoppingListAdapter( this,  shoppingList.getShoppingList(), shoppingList.getIngredientsToBuy() ) ;
        ingredientListView.setAdapter( shoppingListAdapter );

        TextView total = findViewById(R.id.shopping_list_total);
        total.setText(Integer.toString(shoppingList.getSize()));

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
}