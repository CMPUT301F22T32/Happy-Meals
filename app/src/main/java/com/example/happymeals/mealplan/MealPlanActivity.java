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
import java.util.Calendar;
import java.util.Date;


public class MealPlanActivity extends AppCompatActivity {
    CalendarView calendarView;
    Button mealPlanButton;
    Calendar cal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan);

        calendarView = findViewById(R.id.meal_plan_calendar_view);
        //mealPlanButton = findViewById(R.id.meal_plan_edit);
        cal = Calendar.getInstance();

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
               cal.set(year-1900, month, dayOfMonth);
               // constructor: Calendar.set(year+1900, month, dayOfMonth)
            }
        });

        calendarView.getFirstDayOfWeek();

        mealPlanButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //startMealPlanDetailActivity(cal);

            }
        });
    }

    private void startMealPlanDetailActivity(Calendar cal) {
        Intent mealPlanIntent = new Intent(this, MealPlanDeltailsActivity.class);
        mealPlanIntent.putExtra("Date", (Serializable) cal);
        startActivity(mealPlanIntent) ;
    }
}