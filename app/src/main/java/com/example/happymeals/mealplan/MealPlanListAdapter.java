package com.example.happymeals.mealplan;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.happymeals.R;

import java.util.ArrayList;

public class MealPlanListAdapter extends ArrayAdapter<MealPlan> {
        private Context context;
        private ArrayList<MealPlan> mealplans;
        //private Button addRecipesButton;

        public MealPlanListAdapter(@NonNull Context context, ArrayList<MealPlan> mealplans) {
                super(context, 0, mealplans);
                this.context = context;
                this.mealplans = mealplans;
        }

        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = convertView;

                if (view == null) {
                        view = LayoutInflater.from(context).inflate(R.layout.meal_plan_list_adapter, parent, false);
                }

                MealPlan mealplan = mealplans.get(position);

                TextView start = view.findViewById(R.id.mp_adapter_start_date);
                TextView end = view.findViewById(R.id.mp_adapter_end_date);

                start.setText(mealplan.getStartDateString());
                end.setText(mealplan.getEndDateString());

                //TODO set buttons

                return view;
        }
}