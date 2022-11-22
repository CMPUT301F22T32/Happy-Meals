package com.example.happymeals.mealplan;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.GridView;

import com.example.happymeals.R;
import com.example.happymeals.fragments.MealPlanItemsFragment;
import com.google.android.material.datepicker.DateSelector;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.Date;

public class CreateMealPlanActivity extends AppCompatActivity {

    private Button breakfastAdd;
    private Button lunchAdd;
    private Button dinnerAdd;

    private Button breakfastClear;
    private Button lunchClear;
    private Button dinnerClear;

    private GridView breakfastGrid;
    private GridView lunchGrid;
    private GridView dinnerGrid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meal_plan);

        breakfastAdd = findViewById(R.id.mp_breakfast_add);
        lunchAdd = findViewById(R.id.mp_lunch_add);
        dinnerAdd = findViewById(R.id.mp_dinner_add);

        //breakfastClear = findViewById(R.id.)
        //lunchClear = findViewById(R.id.)
        //dinnerClear = findViewById(R.id.)

        breakfastGrid = findViewById(R.id.breakfast_gridview);
        lunchGrid = findViewById(R.id.lunch_gridview);
        dinnerGrid = findViewById(R.id.dinner_gridview);

        View.OnClickListener addListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MealPlanItemsFragment frag = new MealPlanItemsFragment();
                frag.show(getSupportFragmentManager(), "MEAL_PLAN_ITEM_SELECT");
                //getSupportFragmentManager().setFragmentResultListener();
            }
        };

        breakfastAdd.setOnClickListener(addListener);
        lunchAdd.setOnClickListener(addListener);
        dinnerAdd.setOnClickListener(addListener);

        }
}