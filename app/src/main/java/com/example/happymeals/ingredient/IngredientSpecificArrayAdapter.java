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

import java.util.ArrayList;
import java.util.HashMap;

/**
 * This class is used to display a list of ingredients in the Storage activity.
 * @author kstark
 */

public class IngredientSpecificArrayAdapter extends ArrayAdapter<Ingredient> {

    private ArrayList<Ingredient> storageList;
    private Context context;
    private HashMap< String, Double > countMap;

    /**
     * This initializes the IngredientArrayAdapter
     * @param context {@link Context}
     * This is the application's environment
     * @param storageList {@link ArrayList<Ingredient>}
     * This is the list of ingredients to be displayed
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
            view = LayoutInflater.from(context).inflate(R.layout.ingredient_specific_content, parent, false);
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
