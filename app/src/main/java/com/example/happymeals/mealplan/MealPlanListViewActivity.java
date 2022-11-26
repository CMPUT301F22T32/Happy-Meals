package com.example.happymeals.mealplan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import com.example.happymeals.R;
import com.example.happymeals.adapters.MealPlanListAdapter;
import com.example.happymeals.database.DatasetWatcher;
import com.example.happymeals.fragments.MealPlanPromptFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MealPlanListViewActivity extends AppCompatActivity implements DatasetWatcher {

    private FloatingActionButton makeNewMP;

    private Context context = this;
    private MealPlanStorage mps;

    private MealPlanListAdapter adapter;
    private ArrayList<MealPlan> mealPlans;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan_list_view);

        mps = MealPlanStorage.getInstance();
        mps.setListeningActivity(this);

        ListView mealPlanStorage = findViewById(R.id.meal_plan_storage_list);
        adapter = new MealPlanListAdapter(this, mps.getMealPlans());
        mealPlanStorage.setAdapter(adapter);

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
    public void signalChangeToAdapter() {
        adapter.notifyDataSetChanged();
        if (mps.getMealPlans().size() == 0)
            finish();
    }
}