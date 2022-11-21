package com.example.happymeals.mealplan;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.happymeals.R;
import com.google.android.material.datepicker.MaterialDatePicker;

public class CreateMealPlanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meal_plan);

        MaterialDatePicker materialDatePicker = MaterialDatePicker.Builder.dateRangePicker().build();
        materialDatePicker.show(getSupportFragmentManager(), "DatePicker");
    }
}