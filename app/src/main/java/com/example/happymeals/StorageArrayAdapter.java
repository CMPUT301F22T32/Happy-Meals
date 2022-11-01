package com.example.happymeals;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * This class is used to display a list of ingredients in the Storage activity
 */
public class StorageArrayAdapter extends ArrayAdapter<Ingredient> {
    private ArrayList<Ingredient> storageList;
    private Context context;

    /**
     * This initializes the StorageArrayAdapter
     * @param context {@link Context}
     * This is the application's environment
     * @param storageList {@link ArrayList<Ingredient>}
     * This is the list of ingredients to be displayed
     */
    public StorageArrayAdapter(@NonNull Context context, ArrayList<Ingredient> storageList) {
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

            TextView description = view.findViewById(R.id.description_textView);
            TextView location = view.findViewById(R.id.location_text);
            TextView amount = view.findViewById(R.id.amount_text);
            TextView unit = view.findViewById(R.id.unit_text);

            description.setText(ingredient.getDescription());
            location.setText(ingredient.getLocation().toString());
            amount.setText(ingredient.getAmount().toString());
            unit.setText(ingredient.getUnit().toString());

        return view;
    }
}
