package com.example.happymeals.mealplan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;

import com.example.happymeals.R;
import com.example.happymeals.fragments.MealPlanIngredientFragment;
import com.example.happymeals.fragments.MealPlanRecipeFragment;
import com.example.happymeals.mealplan.MealRecipeAdapter;
import com.example.happymeals.recipe.Recipe;
import com.example.happymeals.recipe.RecipeStorageActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class MealPlanDeltailsActivity extends AppCompatActivity implements MealPlanRecipeFragment.OnFragmentInteractionListener, MealPlanIngredientFragment.OnFragmentInteractionListener {
    TabLayout weektab;
    GridView breakfastView;
    MealRecipeAdapter breakfastAdapter;
    FloatingActionButton addBreakfast;
    Button addBreakfastIngredient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan_deltails);

        Intent intent = getIntent();
        Date date = (Date) intent.getSerializableExtra("Date");
        weektab = findViewById(R.id.week_tab);
        breakfastView = findViewById(R.id.breakfast_gridview);
        addBreakfast = findViewById(R.id.add_breakfast_recipe);
        addBreakfastIngredient = findViewById(R.id.add_breakfast_ingredient);

        ArrayList<Recipe> recipes = new ArrayList<>();
        recipes.add(new Recipe("Tacos"));
        recipes.add(new Recipe("Alfredo"));
        breakfastAdapter = new MealRecipeAdapter(this, recipes);

        breakfastView.setAdapter(breakfastAdapter);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int day = calendar.get(Calendar.DAY_OF_WEEK);

        weektab.selectTab(weektab.getTabAt(day - 1));
        Context context = this;

        addBreakfast.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MealPlanRecipeFragment().show(getSupportFragmentManager(), "RECIPE");
            }
        });

        addBreakfastIngredient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MealPlanIngredientFragment().show(getSupportFragmentManager(), "INGREDIENT");
            }
        });



    }

    @Override
    public void onOkPressed() {

    }
}