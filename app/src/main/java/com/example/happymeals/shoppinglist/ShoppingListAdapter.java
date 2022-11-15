package com.example.happymeals.shoppinglist;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.happymeals.R;
import com.example.happymeals.ingredient.Ingredient;

import java.util.ArrayList;
import java.util.List;

public class ShoppingListAdapter extends ArrayAdapter<Ingredient> {

    private ArrayList<Ingredient> ingredientsInStorage;
    private ArrayList<Ingredient> ingredientsToBuy;
    private Context context;

    public ShoppingListAdapter(@NonNull Context context, ArrayList<Ingredient> ingredientsToBuy, ArrayList<Ingredient> ingredientsInStorage) {
        super(context, 0, ingredientsToBuy);
        this.context = context;
        this.ingredientsToBuy = ingredientsToBuy;
        this.ingredientsInStorage = ingredientsInStorage;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.activity_shopping_list_adapter, parent, false);
        }

        Ingredient ingredient = ingredientsToBuy.get(position);
        Integer amountInStorage = 0;

        for (Ingredient storedIngredient : ingredientsInStorage) {
            if (storedIngredient.getName().equals(ingredient.getName()))
                amountInStorage = storedIngredient.getAmount();
        }

        TextView storedAmountView = view.findViewById(R.id.shop_list_storage_amount_value);
        TextView neededAmountView = view.findViewById(R.id.shop_list_buy_amount_value);
        TextView unitView = view.findViewById(R.id.shop_list_unit_value);
        TextView categoryView = view.findViewById(R.id.shop_list_category_value);

        storedAmountView.setText(amountInStorage);
        neededAmountView.setText(ingredient.getAmount());
        unitView.setText(ingredient.getUnit().toString());
        categoryView.setText(ingredient.getCategory().toString());

        return view;
    }
}