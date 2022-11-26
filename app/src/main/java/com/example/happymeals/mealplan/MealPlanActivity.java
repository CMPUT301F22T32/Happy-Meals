package com.example.happymeals.mealplan;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.happymeals.Constants;
import com.example.happymeals.R;
import com.example.happymeals.adapters.IngredientStorageArrayAdapter;
import com.example.happymeals.adapters.RecipeStorageAdapter;
import com.example.happymeals.database.DatasetWatcher;
import com.example.happymeals.fragments.MealPlanPromptFragment;
import com.example.happymeals.fragments.ModifyConfirmationFragment;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


public class MealPlanActivity extends AppCompatActivity implements DatasetWatcher {
    private LinearLayout breakfastTab;
    private LinearLayout lunchTab;
    private LinearLayout dinnerTab;

    private ConstraintLayout mealPlanTab;
    private ConstraintLayout legend;
    private TextView startDay;
    private TextView endDay;

    private TextView noMPTab;

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
    private Date date;

    private RecipeStorageAdapter recipeAdapter = null;
    private IngredientStorageArrayAdapter ingredientAdapter = null;
    private ArrayAdapter<MealPlan> mealPlanAdapter = null;

    private MealPlanStorage mps;
    private MealPlan mp;

    boolean noStoredMealPlans = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan);

        mps = MealPlanStorage.getInstance();
        mps.setListeningActivity(this);

        breakfastTab = findViewById( R.id.mp_breakfast_tab );
        lunchTab = findViewById(R.id.mp_lunch_tab);
        dinnerTab = findViewById(R.id.mp_dinner_tab);
        mealPlanTab = findViewById(R.id.current_mp);
        legend = findViewById(R.id.mp_legend);

        startDay = findViewById(R.id.current_mp_start);
        endDay = findViewById(R.id.current_mp_end);
        noMPTab = findViewById(R.id.no_mp_tab);

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

        date = Calendar.getInstance().getTime();
        Calendar c = Calendar.getInstance();
        c.setTimeInMillis(Instant.now().toEpochMilli());
        calendarView.setDate(c.getTimeInMillis());

        mp = mps.getMealPlanForDay(date);

        mealPlanAdapter = new ArrayAdapter<MealPlan>(context, 0, mps.getMealPlans());

        if (mp != null) {
            recipeAdapter = new RecipeStorageAdapter(context, mp.recipeList());
            ingredientAdapter = new IngredientStorageArrayAdapter(context, mp.ingredientList());
        }

        setCalendarListeners();
        setButtonListener();
        setCheckBoxListeners();
        setDetailsListeners();

        calendarView.setDate(Calendar.getInstance().getTimeInMillis());

        setMeals();
    }

    private void changeViewForEmptyStorage() {
        if (mps.getMealPlans().size() == 0) {
            noStoredMealPlans = true;
            String buttonMessage = "Make a meal plan";
            viewAll.setText(buttonMessage);
            String prompt = "Looks like you don't have any meal plans yet... make one in just a few clicks.";
            noMPTab.setText(prompt);
        }
        else
            noStoredMealPlans = false;
    }

    private void setCalendarListeners() {
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int dayOfMonth) {
                Calendar cal = Calendar.getInstance();
                cal.set(year, month, dayOfMonth);

                date = cal.getTime();

                String dateStr = new SimpleDateFormat("EEEE, MMM d", Locale.CANADA).format(date);
                calendarDate.setText(dateStr);

                setMeals();
            }
        });
    }

    private void setButtonListener() {
        viewAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent;
                if (noStoredMealPlans) {
                    MealPlanPromptFragment frag = new MealPlanPromptFragment();
                    frag.show(getSupportFragmentManager(), "MEAL_PROMPT_FRAGMENT");
                }
                else {
                    intent = new Intent(context, MealPlanListViewActivity.class);
                    startActivity(intent);
                }
            }
        });
    }

    private void setCheckBoxListeners() {
        //TODO some sort of rating system fragment

        DialogInterface.OnClickListener breakfastListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                breakfastBox.setChecked(true);
                mp.setMealMade(date, Constants.MEAL_OF_DAY.BREAKFAST, true);
                mps.updateMealPlan(mp);
                breakfastBox.setClickable(false);
                mp.consumeMeal(date, Constants.MEAL_OF_DAY.BREAKFAST);
            }
        };

        DialogInterface.OnClickListener lunchListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                lunchBox.setChecked(true);
                mp.setMealMade(date, Constants.MEAL_OF_DAY.LUNCH, true);
                mps.updateMealPlan(mp);
                lunchBox.setClickable(false);
                mp.consumeMeal(date, Constants.MEAL_OF_DAY.LUNCH);
            }
        };

        DialogInterface.OnClickListener dinnerListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dinnerBox.setChecked(true);
                mp.setMealMade(date, Constants.MEAL_OF_DAY.DINNER, true);
                mps.updateMealPlan(mp);
                dinnerBox.setClickable(false);
                mp.consumeMeal(date, Constants.MEAL_OF_DAY.DINNER);
            }
        };

        CheckBox[] boxes = new CheckBox[]{breakfastBox, lunchBox, dinnerBox};
        DialogInterface.OnClickListener[] listeners = new DialogInterface.OnClickListener[]{breakfastListener, lunchListener, dinnerListener};
        for (int i = 0; i < boxes.length; i++) {
            int index = i;
            boxes[index].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boxes[index].setChecked(false);
                    ModifyConfirmationFragment frag = new ModifyConfirmationFragment("Meal Confirmation", "Did you make the meal? This cannot be undone.", context, listeners[index]);
                    frag.display();
                }
            });
        }
    }

    private void setDetailsListeners() {
        Button[] buttons = new Button[]{breakfastDetails, lunchDetails, dinnerDetails};
        Constants.MEAL_OF_DAY[] mealTimes = Constants.MEAL_OF_DAY.values();

        for (int i = 0; i < buttons.length; i++) {
            int index = i;
            buttons[index].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mp.getMealType(date, mealTimes[index]) == Constants.COLLECTION_NAME.INGREDIENTS) {
                        mp.getMealIngredients(date, mealTimes[index], true);
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);

                        builder.setTitle("Ingredients")
                                .setView(R.layout.list_view_fragment)
                                .setAdapter(ingredientAdapter, null)
                                .setPositiveButton("OK", null);

                        AlertDialog fragment = builder.create();
                        fragment.show();

                    } else if (mp.getMealType(date, mealTimes[index]) == Constants.COLLECTION_NAME.RECIPES) {
                        mp.getMealRecipes(date, mealTimes[index]);
                        AlertDialog.Builder builder = new AlertDialog.Builder(context);

                        builder.setTitle("Recipes")
                                .setView(R.layout.list_view_fragment)
                                .setAdapter(recipeAdapter, null)
                                .setPositiveButton("OK", null);

                        AlertDialog fragment = builder.create();
                        fragment.show();
                    }
                }
            });
        }
    }

    private void setMeals() {
        mp = mps.getMealPlanForDay(date);

        LinearLayout[] tabs = new LinearLayout[]{breakfastTab, lunchTab, dinnerTab};

        if (mp == null) {
            noMPTab.setVisibility(View.VISIBLE);
            String prompt = "There is no meal planner for this week.";
            noMPTab.setText(prompt);
            changeViewForEmptyStorage();
            for (ConstraintLayout tab : new ConstraintLayout[]{mealPlanTab, legend})
                tab.setVisibility(View.GONE);
            for (LinearLayout tab : tabs)
                tab.setVisibility(View.GONE);
            return;
        }

        noStoredMealPlans = false;
        String buttonMessage = "View all meal plans";
        viewAll.setText(buttonMessage);

        recipeAdapter = new RecipeStorageAdapter(context, mp.recipeList());
        ingredientAdapter = new IngredientStorageArrayAdapter(context, mp.ingredientList());

        setDetailsListeners();
        setCheckBoxListeners();

        mp.setListeningActivity(this);
        mealPlanTab.setVisibility(View.VISIBLE);
        startDay.setText(mp.getStartDateString());
        endDay.setText(mp.getEndDateString());

        String prompt = "There are no meals planned for this day.";
        noMPTab.setText(prompt);

        if (mp.getNumberOfMealsForDay(date) == 0) {
            noMPTab.setVisibility(View.VISIBLE);
            legend.setVisibility(View.GONE);

            for (LinearLayout tab : tabs)
                tab.setVisibility(View.GONE);
        }
        else {
            noMPTab.setVisibility(View.GONE);
            legend.setVisibility(View.VISIBLE);

            ArrayList<String> names = mp.getMealNamesForDay(date);
            ArrayList<Boolean> made = mp.getMealMadeForDay(date);

            TextView[] views = new TextView[]{breakfastText, lunchText, dinnerText};
            CheckBox[] boxes = new CheckBox[]{breakfastBox, lunchBox, dinnerBox};

            for (int i = 0; i < 3; i++) {
                if (names.get(i) == null)
                    tabs[i].setVisibility(View.GONE);
                else {
                    tabs[i].setVisibility(View.VISIBLE);
                    views[i].setText(names.get(i));
                    boxes[i].setChecked(made.get(i));
                    if (boxes[i].isChecked())
                        boxes[i].setClickable(false);
                }
            }
        }
    }

    @Override
    public void signalChangeToAdapter() {
        if (recipeAdapter != null)
            recipeAdapter.notifyDataSetChanged();

        if (ingredientAdapter != null)
            ingredientAdapter.notifyDataSetChanged();

        if (mealPlanAdapter != null) {
            mealPlanAdapter.notifyDataSetChanged();
            setMeals();
        }
    }
}