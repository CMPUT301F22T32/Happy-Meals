package com.example.happymeals.mealplan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;

import com.example.happymeals.R;
import com.example.happymeals.shoppinglist.ShoppingListActivity;

import java.io.Serializable;
import java.util.Calendar;


public class MealPlanActivity extends AppCompatActivity {
    CalendarView calendarView;
    Button mealPlanButton;
    Calendar cal;
    Context context = this;

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

        Button viewAll = findViewById(R.id.view_all_meal_plans);
        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MealPlanListViewActivity.class);
                startActivity(intent);
            }
        });

    }

    private void startMealPlanDetailActivity(Calendar cal) {
        Intent mealPlanIntent = new Intent(this, MealPlanDetailsActivity.class);
        mealPlanIntent.putExtra("Date", (Serializable) cal);
        startActivity(mealPlanIntent) ;
    }
}