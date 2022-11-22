package com.example.happymeals.mealplan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.GridView;
import android.widget.ListView;

import com.example.happymeals.Constants;
import com.example.happymeals.R;
import com.example.happymeals.fragments.MealPlanItemsFragment;
import com.example.happymeals.ingredient.Ingredient;
import com.example.happymeals.ingredient.IngredientStorageArrayAdapter;
import com.example.happymeals.recipe.Recipe;
import com.example.happymeals.recipe.RecipeStorageAdapter;
import com.google.android.material.datepicker.DateSelector;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.ArrayList;
import java.util.Date;

public class CreateMealPlanActivity extends AppCompatActivity implements MealPlanItemsFragment.OnFragmentInteractionListener{

    private Constants.MEAL_OF_DAY mealTime;

    private Button breakfastAdd;
    private Button lunchAdd;
    private Button dinnerAdd;

    private Button breakfastClear;
    private Button lunchClear;
    private Button dinnerClear;

    private ListView breakfastGrid;
    private ListView lunchGrid;
    private ListView dinnerGrid;

    private Button[] addButtons;
    private Button[] clearButtons;
    private ListView[] lists;

    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meal_plan);

        context = this;

        breakfastAdd = findViewById(R.id.mp_breakfast_add);
        lunchAdd = findViewById(R.id.mp_lunch_add);
        dinnerAdd = findViewById(R.id.mp_dinner_add);

        breakfastClear = findViewById(R.id.mp_breakfast_clear);
        lunchClear = findViewById(R.id.mp_lunch_clear);
        dinnerClear = findViewById(R.id.mp_dinner_clear);

        breakfastGrid = findViewById(R.id.mp_breakfast_items);
        lunchGrid = findViewById(R.id.mp_lunch_items);
        dinnerGrid = findViewById(R.id.mp_dinner_items);

        addButtons = new Button[]{breakfastAdd, lunchAdd, dinnerAdd};
        clearButtons = new Button[]{breakfastClear, lunchClear, dinnerClear};
        lists = new ListView[]{breakfastGrid, lunchGrid, dinnerGrid};

        for (Button button: clearButtons)
            button.setEnabled(false);

        setAddListeners();
        setClearListeners();

        }

    private void setAddListeners() {
        Constants.MEAL_OF_DAY[] times = Constants.MEAL_OF_DAY.values();
        MealPlanItemsFragment frag = new MealPlanItemsFragment();

        for ( int i = 0; i < addButtons.length; i++ ) {
            int index = i;
            addButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mealTime = times[index];
                    frag.show(getSupportFragmentManager(), "MEAL_PLAN_ITEM_SELECT");
                }
            });
        }
    }

    private void setClearListeners() {
        for (int i = 0; i < clearButtons.length; i++) {
            // had to set variable as 'effectively final'
            int index = i;
            clearButtons[index].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    lists[index].setAdapter( null );
                    addButtons[index].setEnabled(true);
                    clearButtons[index].setEnabled(false);
                }
            });
        }
    }

    @Override
    public void selectionIngredients(ArrayList<Ingredient> ingredients) {
        IngredientStorageArrayAdapter adapter = new IngredientStorageArrayAdapter( context, ingredients ) ;
        if (mealTime == Constants.MEAL_OF_DAY.BREAKFAST) {
            breakfastGrid.setAdapter( adapter ) ;
            breakfastAdd.setEnabled(false);
            breakfastClear.setEnabled(true);
        }
        else if (mealTime == Constants.MEAL_OF_DAY.LUNCH) {
            lunchGrid.setAdapter( adapter );
            lunchAdd.setEnabled(false);
            lunchClear.setEnabled(true);
        }
        else if (mealTime == Constants.MEAL_OF_DAY.DINNER) {
            dinnerGrid.setAdapter( adapter );
            dinnerAdd.setEnabled(false);
            dinnerClear.setEnabled(true);
        }
    }

    @Override
    public void selectionRecipes(ArrayList<Recipe> recipes) {
        RecipeStorageAdapter adapter = new RecipeStorageAdapter( context, recipes ) ;
        if (mealTime == Constants.MEAL_OF_DAY.BREAKFAST) {
            breakfastGrid.setAdapter( adapter ) ;
            breakfastAdd.setEnabled(false);
            breakfastClear.setEnabled(true);
        }
        else if (mealTime == Constants.MEAL_OF_DAY.LUNCH) {
            lunchGrid.setAdapter( adapter );
            lunchAdd.setEnabled(false);
            lunchClear.setEnabled(true);
        }
        else if (mealTime == Constants.MEAL_OF_DAY.DINNER) {
            dinnerGrid.setAdapter( adapter );
            dinnerAdd.setEnabled(false);
            dinnerClear.setEnabled(true);
        }
    }
}