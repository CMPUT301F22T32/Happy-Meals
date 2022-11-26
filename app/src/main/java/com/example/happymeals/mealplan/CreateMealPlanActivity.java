package com.example.happymeals.mealplan;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.happymeals.Constants;
import com.example.happymeals.R;
import com.example.happymeals.adapters.IngredientStorageArrayAdapter;
import com.example.happymeals.adapters.RecipeStorageAdapter;
import com.example.happymeals.database.DatasetWatcher;
import com.example.happymeals.fragments.InputErrorFragment;
import com.example.happymeals.fragments.MealPlanItemsFragment;
import com.example.happymeals.fragments.ModifyConfirmationFragment;
import com.example.happymeals.ingredient.Ingredient;
import com.example.happymeals.recipe.Recipe;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class CreateMealPlanActivity extends AppCompatActivity implements MealPlanItemsFragment.OnFragmentInteractionListener, DatasetWatcher {

    public static final String NEW_MEAPLAN_EXTRA = "com.example.happymeals.mealplan.new";
    public static final String EDITABLE_EXTRA = "com.example.happymeals.mealplan.editable";
    public static final String AUTOGEN_EXTRA = "com.example.happymeals.mealplan.autogenerated";
    public static final String MEALPLAN_INDEX_EXTRA = "com.example.happymeals.mealplan.index";

    private MealPlan mealplan;
    private MealPlanStorage storage;

    private ListView itemsView;

    private TextView duplicateWarning;
    private TextView emptyListText;
    private TextView title;

    private FloatingActionButton add;
    private FloatingActionButton edit;
    private FloatingActionButton clear;
    private FloatingActionButton help;

    private Button mpSave;
    private Button mpCancel;
    private Button calendarPrevious;
    private Button calendarNext;

    private Constants.DAY_OF_WEEK[] weekDayConstants = Constants.DAY_OF_WEEK.values();
    private Constants.MEAL_OF_DAY[] mealTimeConstants = Constants.MEAL_OF_DAY.values();

    private HashMap<Constants.MEAL_OF_DAY, Pair < Constants.COLLECTION_NAME, ArrayList <?> > > itemsForAutoGen;
    private MaterialCalendarView calendar;

    private TabLayout weekTab;
    private TabLayout mealsTab;

    private LocalDateTime start;
    private LocalDateTime end;

    private Context context;

    private IngredientStorageArrayAdapter ingredientAdapter = null;
    private RecipeStorageAdapter recipeAdapter = null;

    private Boolean createNewMP = false;
    private Boolean autogen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_meal_plan);

        context = this;
        storage = MealPlanStorage.getInstance();

        duplicateWarning = findViewById(R.id.mp_duplicate_warning);
        title = findViewById(R.id.mp_items_title);

        add = findViewById(R.id.mp_add_item);
        edit = findViewById(R.id.mp_edit_item);
        clear = findViewById(R.id.mp_delete_items);
        help = findViewById(R.id.mp_help_button);

        itemsView = findViewById(R.id.mp_items);
        emptyListText = findViewById(R.id.mp_empty_prompt);

        mpSave = findViewById(R.id.mp_save);
        mpCancel = findViewById(R.id.mp_cancel);

        weekTab = findViewById(R.id.week_tab);
        mealsTab = findViewById(R.id.meal_tab);

        calendar = findViewById(R.id.mp_week_calendar);
        calendarPrevious = findViewById(R.id.calendar_previous);
        calendarNext = findViewById(R.id.calendar_next);

        start = LocalDateTime.now().with(LocalTime.MIN).with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY));
        end = LocalDateTime.now().with(LocalTime.MIN).with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        end = end.minusSeconds(1);

        setListeners();

        Bundle extras = getIntent().getExtras();
        createNewMP = extras.getBoolean(CreateMealPlanActivity.NEW_MEAPLAN_EXTRA);

        if (createNewMP) {
            mealplan = new MealPlan();
            autogen = extras.getBoolean(CreateMealPlanActivity.AUTOGEN_EXTRA);
            if (autogen) {
                itemsForAutoGen = new HashMap<>();
                setEnvironmentAutoGen();
            }
        }
        else {
            Integer index = extras.getInt(MEALPLAN_INDEX_EXTRA);
            mealplan = storage.getMealPlanByIndex(index);
            Boolean editable = extras.getBoolean(CreateMealPlanActivity.EDITABLE_EXTRA);
            showMealPlanDate();
            if (!editable)
                setEnvironmentNonEditable();
        }

        recipeAdapter = new RecipeStorageAdapter(context, mealplan.recipeList());
        ingredientAdapter = new IngredientStorageArrayAdapter(context, mealplan.ingredientList());
        mealplan.setListeningActivity(this);

        resetListView();
        checkDuplicate();

        }

    private void setListeners() {
        setAddListeners();
        setEditListeners();
        setClearListeners();
        setFunctionButtonListeners();
        setCalendarListeners();
        setTabListener();
    }

    private void checkDuplicate() {
        if (storage.getMealPlanForDay(Date.from(start.toInstant(ZoneOffset.MIN))) != null) {
            int color = getResources().getColor(com.google.android.material.R.color.material_on_primary_disabled, null);
            calendar.setBackgroundColor(color);
            duplicateWarning.setVisibility(View.VISIBLE);
        }
        else {
            //Drawable drawable = ResourcesCompat.getDrawable(getResources(), android.R.drawable.editbox_dropdown_light_frame, null);
            int color = getResources().getColor(R.color.white, null);
            calendar.setBackgroundColor(color);
            duplicateWarning.setVisibility(View.GONE);
        }
    }

    private void setEnvironmentAutoGen() {
        findViewById(R.id.mp_day_of_week_title).setVisibility(View.GONE);
        weekTab.setVisibility(View.GONE);
    }

    private void setEnvironmentNonEditable() {
        //int color = getResources().getColor(R.color.blue, null);
        //duplicateWarning.setBackgroundColor(color);

        emptyListText.setPadding(0, 0, 0, 0);
        mealsTab.setTranslationX(-40);
        emptyListText.setTranslationX(-40);

        findViewById(R.id.mp_table_linear_layout).setPadding(120,0,0,0);
        findViewById(R.id.mp_week_of_mealplan).setVisibility(View.GONE);

        ConstraintLayout modifyButtons = findViewById(R.id.mp_edit_buttons);
        modifyButtons.setVisibility(View.GONE);

        //duplicateWarning.setVisibility(View.GONE);

        findViewById(R.id.mp_create_function_buttons).setVisibility(View.GONE);

        String emptyPrompt = "Doesn't look like there's anything stored for this day.";
        emptyListText.setText(emptyPrompt);
    }

    private void showMealPlanDate() {
        calendarNext.setVisibility(View.GONE);
        calendarPrevious.setVisibility(View.GONE);
        calendar.setVisibility(View.GONE);

        duplicateWarning.setGravity(Gravity.LEFT);
        duplicateWarning.setTextSize(18);
        duplicateWarning.setPadding(0,80,0,0);

        String week = String.format("%s - %s", mealplan.getStartDateString(), mealplan.getEndDateString());
        duplicateWarning.setText(week);
    }

    private void resetListView() {
        int weekDay = weekTab.getSelectedTabPosition();
        int mealTime = mealsTab.getSelectedTabPosition();

        String titleStr;
        if (autogen) {
            titleStr = String.format("Items for %s", mealTimeConstants[mealTime].toString().toLowerCase());
        }
        else {
            titleStr = String.format("Items for %s on %s", mealTimeConstants[mealTime].toString().toLowerCase(),
                    weekDayConstants[weekDay].toString().charAt(0) + weekDayConstants[weekDay].toString().toLowerCase().substring(1));
        }

        title.setText(titleStr);
        Constants.COLLECTION_NAME type = mealplan.getMealType(weekDayConstants[weekDay], mealTimeConstants[mealTime]);

        if (type == Constants.COLLECTION_NAME.INGREDIENTS) {
            mealplan.getMealIngredients(weekDayConstants[weekDay], mealTimeConstants[mealTime], true);
            itemsView.setAdapter(ingredientAdapter);
            emptyListText.setVisibility(View.GONE);
            add.setEnabled(false);
            edit.setEnabled(true);
            clear.setEnabled(true);
        }

        else if (type == Constants.COLLECTION_NAME.RECIPES) {
            mealplan.getMealRecipes(weekDayConstants[weekDay], mealTimeConstants[mealTime]);
            itemsView.setAdapter(recipeAdapter);
            emptyListText.setVisibility(View.GONE);
            add.setEnabled(false);
            edit.setEnabled(true);
            clear.setEnabled(true);
        }

        else {
            itemsView.setAdapter(null);
            emptyListText.setVisibility(View.VISIBLE);
            add.setEnabled(true);
            edit.setEnabled(false);
            clear.setEnabled(false);
        }
    }

    private void setCalendarListeners() {
        calendarPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.goToPrevious();
                start = start.minusWeeks(1);
                end = end.minusWeeks(1);
                checkDuplicate();
            }
        });

        calendarNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar.goToNext();
                start = start.plusWeeks(1);
                end = end.plusWeeks(1);
                checkDuplicate();
            }
        });
    }

    private void setAddListeners() {
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MealPlanItemsFragment frag = new MealPlanItemsFragment();
                frag.show(getSupportFragmentManager(), "MEAL_PLAN_ITEM_SELECT");
            }
        });
    }

    private void setEditListeners() {
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ArrayList<String> names = new ArrayList<>();

                int weekDay = weekTab.getSelectedTabPosition();
                int mealTime = mealsTab.getSelectedTabPosition();
                Constants.COLLECTION_NAME type = mealplan.getMealType(weekDayConstants[weekDay], mealTimeConstants[mealTime]);

                if (type == Constants.COLLECTION_NAME.INGREDIENTS) {
                    for (int i = 0; i < ingredientAdapter.getCount(); i++)
                        names.add(ingredientAdapter.getItem(i).getName());
                }
                else if (type == Constants.COLLECTION_NAME.RECIPES) {
                    for (int i = 0; i < recipeAdapter.getCount(); i++)
                        names.add(recipeAdapter.getItem(i).getName());
                }

                MealPlanItemsFragment frag = MealPlanItemsFragment.newInstance(type.toString(), names);
                frag.show(getSupportFragmentManager(), "MEAL_PLAN_ITEM_SELECT");
            }
        });
    }

    private void setClearListeners() {

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int weekDay = weekTab.getSelectedTabPosition();
                int mealTime = mealsTab.getSelectedTabPosition();

                DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mealplan.removeMeal(weekDayConstants[weekDay], mealTimeConstants[mealTime]);
                        resetListView();
                    }
                };

                new ModifyConfirmationFragment("Remove Items",
                        "Are you sure you want to remove these items? This cannot be undone.",
                        context,
                        listener).display();
            }
        });
    }

    private void setFunctionButtonListeners() {
        help.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new InputErrorFragment("Help", getResources().getString(R.string.meal_plan_description), context).display();
            }
        });

        mpCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        mpSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ZoneOffset offset = ZoneId.of("America/Edmonton").getRules().getOffset(Instant.now());
                mealplan.setStartDate(Date.from(start.toInstant(offset)));
                mealplan.setEndDate(Date.from(end.toInstant(offset)));

                if (createNewMP && duplicateWarning.getVisibility() == View.VISIBLE) {
                    new InputErrorFragment("Save Error", "A meal plan for the selected week already exists. Please select another week.", context).display();
                    return;
                }

                if (autogen)
                    mealplan.generateMealPlan(itemsForAutoGen);

                if (createNewMP)
                    storage.addMealPlan(mealplan);
                else
                    storage.updateMealPlan(mealplan);

                finish();
            }
        });
    }

    private void setTabListener() {
        TabLayout.OnTabSelectedListener listener = new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                resetListView();
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
            }
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
            }
        };
        weekTab.addOnTabSelectedListener(listener);
        mealsTab.addOnTabSelectedListener(listener);
    }


    @Override
    public void selectionIngredients(ArrayList<Ingredient> ingredients) {
        int weekDay = weekTab.getSelectedTabPosition();
        int mealTime = mealsTab.getSelectedTabPosition();

        if (autogen)
            itemsForAutoGen.put(mealTimeConstants[mealTime], new Pair<>(Constants.COLLECTION_NAME.INGREDIENTS, ingredients));

        mealplan.setMealItemsIngredients(weekDayConstants[weekDay], mealTimeConstants[mealTime], ingredients);

        resetListView();
    }

    @Override
    public void selectionRecipes(ArrayList<Recipe> recipes) {
        int weekDay = weekTab.getSelectedTabPosition();
        int mealTime = mealsTab.getSelectedTabPosition();

        if (autogen)
            itemsForAutoGen.put(mealTimeConstants[mealTime], new Pair<>(Constants.COLLECTION_NAME.RECIPES, recipes));

        mealplan.setMealItemsRecipe(weekDayConstants[weekDay], mealTimeConstants[mealTime], recipes);

        resetListView();
    }

    @Override
    public void signalChangeToAdapter() {
        if (ingredientAdapter != null)
            ingredientAdapter.notifyDataSetChanged();
        if (recipeAdapter != null)
            recipeAdapter.notifyDataSetChanged();
    }

}