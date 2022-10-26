package com.example.happymeals;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class StorageArrayAdapter extends ArrayAdapter<Ingredient> {
    private ArrayList<Ingredient> storageList;
    private Context context;

    public StorageArrayAdapter(@NonNull Context context, ArrayList<Ingredient> storageList) {
        super(context, 0, storageList);
        this.storageList = storageList;
        this.context = context;
    }

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
            location.setText(ingredient.getLocation());
            amount.setText(ingredient.getAmount().toString());
            unit.setText(ingredient.getUnit());

        return view;
    }
}
