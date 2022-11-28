package com.example.happymeals.mealplan;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.happymeals.R;

/**
 * This activity is where users can select ingredients or recipes
 * for their meal plan.
 */
public class SelectMealsActivity extends AppCompatActivity {

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_select_meals );
    }
}