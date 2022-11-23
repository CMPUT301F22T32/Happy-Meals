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
import com.google.android.material.datepicker.MaterialCalendar;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.DocumentReference;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;

public class CreateMealPlanActivity extends AppCompatActivity implements MealPlanItemsFragment.OnFragmentInteractionListener{

    private Constants.MEAL_OF_DAY mealTime;

    private MealPlan mealplan;
    private MealPlanStorage storage;

    private Button breakfastAdd;
    private Button lunchAdd;
    private Button dinnerAdd;

    private Button breakfastClear;
    private Button lunchClear;
    private Button dinnerClear;

    private ListView breakfastGrid;
    private ListView lunchGrid;
    private ListView dinnerGrid;

    private Button mpSave;
    private Button mpCancel;

    private Constants.DAY_OF_WEEK[] weekDayConstants = Constants.DAY_OF_WEEK.values();
    private Constants.MEAL_OF_DAY[] mealTimeConstants = Constants.MEAL_OF_DAY.values();

    private MaterialCalendarView calendar;
    private TabLayout weekTab;

    private Button[] addButtons;
    private Button[] clearButtons;
    private ListView[] lists;

    private Integer dayIndex = null;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meal_plan);

        context = this;
        mealplan = new MealPlan();
        storage = MealPlanStorage.getInstance();

        breakfastAdd = findViewById(R.id.mp_breakfast_add);
        lunchAdd = findViewById(R.id.mp_lunch_add);
        dinnerAdd = findViewById(R.id.mp_dinner_add);

        breakfastClear = findViewById(R.id.mp_breakfast_clear);
        lunchClear = findViewById(R.id.mp_lunch_clear);
        dinnerClear = findViewById(R.id.mp_dinner_clear);

        breakfastGrid = findViewById(R.id.mp_breakfast_items);
        lunchGrid = findViewById(R.id.mp_lunch_items);
        dinnerGrid = findViewById(R.id.mp_dinner_items);

        mpSave = findViewById(R.id.mp_save);
        mpCancel = findViewById(R.id.mp_cancel);

        weekTab = findViewById(R.id.week_tab);
        calendar = findViewById(R.id.mp_week_calendar);

        addButtons = new Button[]{breakfastAdd, lunchAdd, dinnerAdd};
        clearButtons = new Button[]{breakfastClear, lunchClear, dinnerClear};
        lists = new ListView[]{breakfastGrid, lunchGrid, dinnerGrid};

        for (Button button: clearButtons)
            button.setEnabled(false);

        setAddListeners();
        setClearListeners();
        setFunctionButtonListeners();

        }

    private void setAddListeners() {
        MealPlanItemsFragment frag = new MealPlanItemsFragment();

        for ( int i = 0; i < addButtons.length; i++ ) {
            int index = i;
            addButtons[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mealTime = mealTimeConstants[index];
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

    private void setFunctionButtonListeners() {
        mpCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mpSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO some sort of check if the mealplan week already exists
                // TODO pop up fragment for saving
                CalendarDay start = calendar.getMinimumDate();
                System.out.println(start.toString());
                //Date startDate = Date.from(LocalDate.of(start.getYear(), start.getMonth(), start.getDay()).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

                CalendarDay end = calendar.getMaximumDate();
                System.out.println(end.toString());
                //Date endDate = Date.from(LocalDate.of(end.getYear(), end.getMonth(), end.getDay()).atStartOfDay().atZone(ZoneId.systemDefault()).toInstant());

                mealplan.setStartDate(Date.from(Instant.now()));
                mealplan.setEndDate(Date.from(Instant.now()));
                storage.addMealPlan(mealplan);

                finish();
            }
        });
    }

    private void setTabListener() {
        weekTab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (dayIndex == null)
                    dayIndex = weekTab.getSelectedTabPosition();
                else if ( dayIndex != null && weekTab.getSelectedTabPosition() != dayIndex ) {
                }
            }
        });
    }

    @Override
    public void selectionIngredients(ArrayList<Ingredient> ingredients) {
        IngredientStorageArrayAdapter adapter = new IngredientStorageArrayAdapter( context, ingredients ) ;
        dayIndex = weekTab.getSelectedTabPosition();
        Constants.DAY_OF_WEEK day = weekDayConstants[dayIndex];

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
        mealplan.setMealItemsIngredients(day, mealTime, ingredients);
    }

    @Override
    public void selectionRecipes(ArrayList<Recipe> recipes) {
        RecipeStorageAdapter adapter = new RecipeStorageAdapter( context, recipes ) ;
        dayIndex = weekTab.getSelectedTabPosition();
        Constants.DAY_OF_WEEK day = weekDayConstants[dayIndex];

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
        mealplan.setMealItemsRecipe(day, mealTime, recipes);
    }
}