package com.example.happymeals.mealplan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.happymeals.R;
import com.example.happymeals.fragments.MealPlanPromptFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MealPlanListViewActivity extends AppCompatActivity implements MealPlanPromptFragment.OnFragmentInteractionListener{

    private FloatingActionButton makeNewMP;

    private Context context = this;
    private MealPlanStorage mps;
    private ArrayList<MealPlan> mealPlans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan_list_view);

        //TODO set listening activity here

        makeNewMP = findViewById(R.id.add_meal_plan_btn);
        makeNewMP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MealPlanPromptFragment frag = new MealPlanPromptFragment();
                frag.show(getSupportFragmentManager(), "MEAL_PROMPT_FRAGMENT");
            }
        });
    }

    @Override
    public void autoGeneratePressed() {
        Intent intent = new Intent(context, CreateMealPlanActivity.class);
        startActivity(intent);
    }

    @Override
    public void makeSelfPressed() {
        Intent intent = new Intent(context, CreateMealPlanActivity.class);
        startActivity(intent);
    }
}