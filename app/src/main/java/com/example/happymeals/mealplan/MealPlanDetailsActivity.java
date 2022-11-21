package com.example.happymeals.mealplan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import com.example.happymeals.R;
import com.example.happymeals.fragments.MealPlanIngredientFragment;
import com.example.happymeals.fragments.MealPlanRecipeFragment;
import com.example.happymeals.recipe.Recipe;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class MealPlanDetailsActivity extends AppCompatActivity implements MealPlanRecipeFragment.OnFragmentInteractionListener, MealPlanIngredientFragment.OnFragmentInteractionListener {
    TabLayout weektab;
    TextView mealPlanTitle;

    GridView breakfastView;
    GridView lunchView;
    GridView dinnerView;
    GridView otherView;

    MealRecipeAdapter breakfastAdapter;
    MealRecipeAdapter lunchAdapter;
    MealRecipeAdapter dinnerAdapter;
    MealRecipeAdapter otherAdapter;

    Button addBreakfast;
    Button addLunch;
    Button addDinner;
    Button addOther;

    Button addIngredient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan_details);

        Intent intent = getIntent();

        // tab set up
        Calendar cal = (Calendar) intent.getSerializableExtra("Date");
        weektab = findViewById(R.id.week_tab);
        int day = cal.get(Calendar.DAY_OF_WEEK);
        weektab.selectTab(weektab.getTabAt(day-2));

        mealPlanTitle = findViewById(R.id.meal_plan_date);
        //MealPlanStorage mp = new MealPlanStorage(new Date(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), (cal.get(Calendar.DAY_OF_MONTH) - day + 1)));
        //mealPlanTitle.setText(mp.getName());

        // GridView setup
        breakfastView = findViewById(R.id.breakfast_gridview);
        ArrayList<Recipe> recipes = new ArrayList<>();
        recipes.add(new Recipe("Tacos"));
        recipes.add(new Recipe("Alfredo"));
        recipes.add(new Recipe()); // add a blank recipe at the end
        breakfastAdapter = new MealRecipeAdapter(this, recipes);
        breakfastView.setAdapter(breakfastAdapter);
        addBreakfast = (Button) breakfastAdapter.getAddButton();

        lunchView = findViewById(R.id.lunch_gridview);
        lunchAdapter = new MealRecipeAdapter(this, recipes);
        lunchView.setAdapter(lunchAdapter);
        addLunch = (Button) lunchAdapter.getAddButton();

        dinnerView = findViewById(R.id.dinner_gridview);
        dinnerAdapter = new MealRecipeAdapter(this, recipes);
        dinnerView.setAdapter(dinnerAdapter);
        addDinner = (Button) dinnerAdapter.getAddButton();

        otherView = findViewById(R.id.other_gridview);
        otherAdapter = new MealRecipeAdapter(this, recipes);
        otherView.setAdapter(otherAdapter);
        addOther = (Button) otherAdapter.getAddButton();

        addBreakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MealPlanRecipeFragment().show(getSupportFragmentManager(), "RECIPE");
            }
        });

        addLunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MealPlanRecipeFragment().show(getSupportFragmentManager(), "RECIPE");
            }
        });

        addDinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MealPlanRecipeFragment().show(getSupportFragmentManager(), "RECIPE");
            }
        });

        addOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MealPlanRecipeFragment().show(getSupportFragmentManager(), "RECIPE");
            }
        });
    }

    @Override
    public void onOkPressed() {

    }
}