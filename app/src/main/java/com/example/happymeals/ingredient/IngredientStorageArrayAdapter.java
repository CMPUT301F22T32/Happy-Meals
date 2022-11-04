package com.example.happymeals.ingredient;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.happymeals.R;
import com.example.happymeals.ingredient.Ingredient;

import java.util.ArrayList;

/**
 * This class is used to display a list of ingredients in the Storage activity.
 * @author kstark
 */

public class IngredientStorageArrayAdapter extends ArrayAdapter<Ingredient> {

    private ArrayList<Ingredient> storageList;
    private Context context;

    /**
     * This initializes the IngredientArrayAdapter
     * @param context {@link Context}
     * This is the application's environment
     * @param storageList {@link ArrayList<Ingredient>}
     * This is the list of ingredients to be displayed
     */

    public IngredientStorageArrayAdapter(@NonNull Context context, ArrayList<Ingredient> storageList) {
        super(context, 0, storageList);
        this.storageList = storageList;
        this.context = context;
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.ingredient_storage_content, parent, false);
        }

            Ingredient ingredient = storageList.get(position);

            TextView name = view.findViewById( R.id.recipe_list_name_field);
            TextView description = view.findViewById(R.id.recipe_list_description_field);
            TextView location = view.findViewById(R.id.recipe_list_servings_field);
            TextView amount = view.findViewById(R.id.amount_text);
            TextView unit = view.findViewById(R.id.unit_text);

            name.setText( ingredient.getName() );
            description.setText(ingredient.getDescription());
            location.setText(ingredient.getLocation().toString());
            amount.setText(ingredient.getAmount().toString());
            unit.setText(ingredient.getUnit().toString());

        return view;
    }
}
