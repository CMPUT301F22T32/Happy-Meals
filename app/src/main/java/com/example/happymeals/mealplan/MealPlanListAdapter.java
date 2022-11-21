package com.example.happymeals.mealplan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.example.happymeals.R;

import java.util.ArrayList;

public class MealPlanListAdapter extends ArrayAdapter<MealPlanStorage> {
        private Context context;
        private ArrayList<MealPlanStorage> mealplans;
        //private Button addRecipesButton;

        public MealPlanListAdapter(@NonNull Context context, ArrayList<MealPlanStorage> mealplans) {
                super(context, 0, mealplans);
                this.context = context;
                this.mealplans = mealplans;
                //this.addRecipesButton = (Button) addButton();

        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = convertView;

                if (view == null) {
                        view = LayoutInflater.from(context).inflate(R.layout.meal_recipe_content, parent, false);
                }

                return view;
        }
}