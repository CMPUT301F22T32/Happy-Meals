package com.example.happymeals.mealplan;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CalendarView;

import com.example.happymeals.R;
import com.google.android.material.datepicker.DateSelector;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.util.Date;

public class CreateMealPlanActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meal_plan);

        CalendarView cv = findViewById(R.id.meal_plan_calendar_view);
        //cv.setFirstDayOfWeek();

        MaterialCalendarView mv = null;

        MaterialDatePicker m = MaterialDatePicker.Builder.datePicker().build();
        //m.set
        }
}