package com.example.happymeals.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.happymeals.R;
import com.example.happymeals.fragments.ModifyConfirmationFragment;
import com.example.happymeals.mealplan.CreateMealPlanActivity;
import com.example.happymeals.mealplan.MealPlan;
import com.example.happymeals.mealplan.MealPlanStorage;

import java.util.ArrayList;

public class MealPlanListAdapter extends ArrayAdapter<MealPlan> {
    private Context context;
    private ArrayList<MealPlan> mealplans;
    private MealPlanStorage mps;

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

        mps = MealPlanStorage.getInstance();
        MealPlan mealplan = mealplans.get(position);

        TextView start = view.findViewById(R.id.mp_adapter_start_date);
        TextView end = view.findViewById(R.id.mp_adapter_end_date);

        start.setText(mealplan.getStartDateString());
        end.setText(mealplan.getEndDateString());

        Button details = view.findViewById(R.id.mp_view);
        Button edit = view.findViewById(R.id.mp_edit);
        Button delete = view.findViewById(R.id.mp_delete);

        details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CreateMealPlanActivity.class);
                intent.putExtra(CreateMealPlanActivity.NEW_MEAPLAN_EXTRA, false);
                intent.putExtra(CreateMealPlanActivity.MEALPLAN_INDEX_EXTRA, position);
                intent.putExtra(CreateMealPlanActivity.EDITABLE_EXTRA, false);
                context.startActivity(intent);
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CreateMealPlanActivity.class);
                intent.putExtra(CreateMealPlanActivity.NEW_MEAPLAN_EXTRA, false);
                intent.putExtra(CreateMealPlanActivity.MEALPLAN_INDEX_EXTRA, position);
                intent.putExtra(CreateMealPlanActivity.EDITABLE_EXTRA, true);
                context.startActivity(intent);
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ModifyConfirmationFragment("Delete Meal Plan",
                        String.format("Are you sure you wish to delete the meal plan from %s to %s?", mealplan.getStartDateString(), mealplan.getEndDateString()),
                        context,
                        (dialogInterface, i) -> mps.removeMealPlan(mealplan)).display();
            }
        });

        return view;
    }
}