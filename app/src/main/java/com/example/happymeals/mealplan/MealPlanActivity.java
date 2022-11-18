package com.example.happymeals.mealplan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;

import com.example.happymeals.R;
import com.example.happymeals.mealplan.MealPlanDeltailsActivity;

import java.io.Serializable;
import java.util.Date;


public class MealPlanActivity extends AppCompatActivity {
    CalendarView calendar;
    Button mealPlanButton;
    Date date;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan);

        calendar = findViewById(R.id.meal_plan_calendar);
        mealPlanButton = findViewById(R.id.meal_plan_edit);
        date = new Date();

        calendar.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                date = new Date(year, month, dayOfMonth);
            }
        });

        mealPlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startMealPlanDetailActivity(date);

            }
        });
    }

    private void startMealPlanDetailActivity(Date date) {
        Intent mealPlanIntent = new Intent(this, MealPlanDeltailsActivity.class);
        mealPlanIntent.putExtra("Date", (Serializable) date);
        startActivity(mealPlanIntent) ;
    }
}