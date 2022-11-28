package com.example.happymeals.mealplan;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.happymeals.HappyMealBottomNavigation;
import com.example.happymeals.R;
import com.example.happymeals.adapters.MealPlanListAdapter;
import com.example.happymeals.database.DatasetWatcher;
import com.example.happymeals.fragments.MealPlanPromptFragment;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

/**
 * This view shows the list of all weekly meal plans that the user has made. A user can view,
 * edit, or delete MealPlans from this activity.
 */
public class MealPlanListViewActivity extends AppCompatActivity implements DatasetWatcher {

    private FloatingActionButton makeNewMP;

    private Context context = this;
    private MealPlanStorage mps;

    private MealPlanListAdapter adapter;

    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan_list_view);

        mps = MealPlanStorage.getInstance();
        mps.setListeningActivity(this);

        ListView mealPlanStorage = findViewById(R.id.meal_plan_storage_list);
        adapter = new MealPlanListAdapter(this, mps.getMealPlans());
        mealPlanStorage.setAdapter(adapter);

        HappyMealBottomNavigation bottomNavMenu =
                new HappyMealBottomNavigation(
                        findViewById(R.id.bottomNavigationView), this, R.id.mealplan_menu);


        bottomNavMenu.setupBarListener();

        findViewById(R.id.mp_list_back_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void signalChangeToAdapter() {
        adapter.notifyDataSetChanged();
        if ( mps.getMealPlans().size() == 0 )
            finish();
    }
}