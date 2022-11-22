package com.example.happymeals.mealplan;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.happymeals.Constants;
import com.example.happymeals.R;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;


public class MealPlanActivity extends AppCompatActivity {
    private TextView breakfastText;
    private TextView lunchText;
    private TextView dinnerText;

    private CheckBox breakfastBox;
    private CheckBox lunchBox;
    private CheckBox dinnerBox;

    private Button breakfastDetails;
    private Button lunchDetails;
    private Button dinnerDetails;

    private Button viewAll;
    private TextView calendarDate;
    private CalendarView calendarView;

    private Context context = this;
    private MealPlanStorage mps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan);

        mps = MealPlanStorage.getInstance();

        breakfastText = findViewById(R.id.breakfast_recipe);
        lunchText = findViewById(R.id.lunch_recipe);
        dinnerText = findViewById(R.id.dinner_recipe);

        breakfastBox = findViewById(R.id.breakfast_checkbox);
        lunchBox = findViewById(R.id.lunch_checkbox);
        dinnerBox = findViewById(R.id.dinner_checkbox);

        breakfastDetails = findViewById(R.id.breakfast_details);
        lunchDetails = findViewById(R.id.lunch_details);
        dinnerDetails = findViewById(R.id.dinner_details);

        calendarView = findViewById(R.id.meal_plan_calendar_view);
        calendarDate = findViewById(R.id.meal_plan_view_date);
        viewAll = findViewById(R.id.view_all_meal_plans);

        Date today = Calendar.getInstance().getTime();

        setMeals(today);

        calendarView.setDate(Calendar.getInstance().getTimeInMillis());

        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                Calendar cal = Calendar.getInstance();
                cal.set(year, month, dayOfMonth);

                Date date = cal.getTime();

                String dateStr = new SimpleDateFormat("EEEE, MMM d", Locale.CANADA).format(date);
                calendarDate.setText(dateStr);

                setMeals(date);
            }
        });


        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, MealPlanListViewActivity.class);
                startActivity(intent);
            }
        });

    }

    private void setMeals(Date date) {
        MealPlan mp = mps.getMealPlanForDay(date);

        if (mp == null)
            return;

        HashMap<String, Meal> meals = mp.getMealsForDay(date);

        Meal breakfast = meals.get(Constants.MEAL_OF_DAY.BREAKFAST.toString());
        Meal lunch = meals.get(Constants.MEAL_OF_DAY.LUNCH.toString());
        Meal dinner = meals.get(Constants.MEAL_OF_DAY.DINNER.toString());

        breakfastText.setText(breakfast.getDisplayName());
        lunchText.setText(lunch.getDisplayName());
        dinnerText.setText(dinner.getDisplayName());

        breakfastBox.setChecked(breakfast.getMade());
        lunchBox.setChecked(lunch.getMade());
        dinnerBox.setChecked(dinner.getMade());
    }

}