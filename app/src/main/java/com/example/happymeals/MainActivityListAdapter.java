package com.example.happymeals;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class MainActivityListAdapter extends ArrayAdapter<String> {
    private final String[] options = new String[]{"Ingredient Storage", "Recipes", "Shopping List", "Meal Plan"};
    private Context context;

    public MainActivityListAdapter(Context context, int resource) {
        super(context, resource, new String[]{"Ingredient Storage", "Recipes", "Shopping List", "Meal Plan"});
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.main_activity_list_adapter, parent,false);
        }
        String option = options[position];
        TextView name = view.findViewById(R.id.item_text);
        ImageView image = view.findViewById(R.id.item_image);
        name.setText(option);
        // will have to set image, not sure of format yet
        // image.setImage...()
        return view;
    }
}
