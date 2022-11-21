package com.example.happymeals.mealplan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.happymeals.R;
import com.example.happymeals.shoppinglist.ShoppingListActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MealPlanListViewActivity extends AppCompatActivity {

    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan_list_view);

        FloatingActionButton makeNewMP = findViewById(R.id.add_meal_plan_btn);
        makeNewMP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, CreateMealPlanActivity.class);
                startActivity(intent);
            }
        });
    }
}