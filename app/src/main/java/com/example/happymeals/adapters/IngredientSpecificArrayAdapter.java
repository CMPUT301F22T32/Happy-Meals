package com.example.happymeals.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.happymeals.R;
import com.example.happymeals.adapters.IngredientStorageArrayAdapter;
import com.example.happymeals.ingredient.Ingredient;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author jeastgaa
 * This class is used adapt the {@link Ingredient} classes information specifically to a
 * provided recipe. This adapter will present the count which is passed in through a
 * {@link HashMap}. This will be simular to the {@link IngredientStorageArrayAdapter} with the
 * exception of using a different content .xml file.
 * @see IngredientStorageArrayAdapter
 */

public class IngredientSpecificArrayAdapter extends ArrayAdapter<Ingredient> {

    private ArrayList<Ingredient> storageList;
    private Context context;
    private HashMap< String, Double > countMap;

    /**
     * Base constructor that initializes the IngredientArrayAdapter
     * @param context {@link Context}
     * This is the application's environment
     * @param storageList {@link ArrayList<Ingredient>}
     * This is the list of ingredients to be displayed
     * @param countMap The {@link HashMap} which maps a count of the required ingredients
     *                 to the passed in ingredients. The map should contain keys which match
     *                 the {@link Ingredient}'s name value.
     */

    public IngredientSpecificArrayAdapter(@NonNull Context context,
                                          ArrayList<Ingredient> storageList,
                                          HashMap< String, Double > countMap ) {
        super(context, 0, storageList);
        this.storageList = storageList;
        this.context = context;
        this.countMap = countMap;
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.content_ingredient_specific, parent, false);
        }

        Ingredient ingredient = storageList.get(position);

        TextView name = view.findViewById( R.id.ingredient_specific_list_name_field);
        TextView amount = view.findViewById(R.id.ingredient_specific_amount_text);
        TextView unit = view.findViewById(R.id.ingredient_specific_amount_unit_text);

        name.setText( ingredient.getName() );
        amount.setText( countMap.get( ingredient.getName() ).toString() );
        unit.setText(ingredient.getUnit().toString());

        return view;
    }
}
