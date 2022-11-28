package com.example.happymeals;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.example.happymeals.R;
import com.example.happymeals.userlogin.StartScreenActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class MealPlanIntentTests {

    @Rule
    public ActivityScenarioRule<StartScreenActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(StartScreenActivity.class);

    @Test
    public void mealPlanIntentTests() {
        ViewInteraction materialButton = onView(
                allOf(withId(R.id.login_redirect), withText("Login"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        materialButton.perform(click());

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.input_username),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                5),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("sophisdope@cool.com"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.input_password),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("password"), closeSoftKeyboard());

        ViewInteraction materialButton2 = onView(
                allOf(withId(R.id.login_button), withText("Login"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                7),
                        isDisplayed()));
        materialButton2.perform(click());

        ViewInteraction bottomNavigationItemView = onView(
                allOf(withId(R.id.mealplan_menu), withContentDescription("Meal Plan"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.bottomNavigationView),
                                        0),
                                3),
                        isDisplayed()));
        bottomNavigationItemView.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.textView6), withText("Meal Planner"),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class))),
                        isDisplayed()));
        textView.check(matches(withText("Meal Planner")));

        ViewInteraction imageButton = onView(
                allOf(withId(R.id.add_meal_plan_floating_button),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class))),
                        isDisplayed()));
        imageButton.check(matches(isDisplayed()));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.meal_plan_view_date), withText("today"),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class))),
                        isDisplayed()));
        textView2.check(matches(withText("today")));

        ViewInteraction button = onView(
                allOf(withId(R.id.view_all_meal_plans), withText("VIEW ALL MEAL PLANS"),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class))),
                        isDisplayed()));
        button.check(matches(isDisplayed()));

        ViewInteraction materialButton3 = onView(
                allOf(withId(R.id.view_all_meal_plans), withText("View all meal plans"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        1),
                                8)));
        materialButton3.perform(scrollTo(), click());

        ViewInteraction imageButton2 = onView(
                allOf(withId(R.id.mp_list_back_button),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        imageButton2.check(matches(isDisplayed()));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.textView7), withText("Your Meal Plans"),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        textView3.check(matches(withText("Your Meal Plans")));

        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.mp_list_back_button),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction floatingActionButton2 = onView(
                allOf(withId(R.id.add_meal_plan_floating_button),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        0),
                                1)));
        floatingActionButton2.perform(scrollTo(), click());

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.textView24), withText("How would you like to make your meal plan?"),
                        withParent(withParent(withId(R.id.meal_plan_fragment))),
                        isDisplayed()));
        textView4.check(matches(withText("How would you like to make your meal plan?")));

        ViewInteraction textView5 = onView(
                allOf(withId(R.id.textView25), withText("You can manually select the ingredients or recipes you'd want to eat each day. If you don't feel like clicking so much, you can just give us a list of ingredients or recipes and we'll make you a meal plan."),
                        withParent(withParent(withId(R.id.meal_plan_fragment))),
                        isDisplayed()));
        textView5.check(matches(withText("You can manually select the ingredients or recipes you'd want to eat each day. If you don't feel like clicking so much, you can just give us a list of ingredients or recipes and we'll make you a meal plan.")));

        ViewInteraction button2 = onView(
                allOf(withId(R.id.selfmade_button), withText("I WANT TO MAKE MY OWN"),
                        withParent(withParent(withId(R.id.meal_plan_fragment))),
                        isDisplayed()));
        button2.check(matches(isDisplayed()));

        ViewInteraction button3 = onView(
                allOf(withId(R.id.autogenerate_button), withText("MAKE ME A MEAL PLAN"),
                        withParent(withParent(withId(R.id.meal_plan_fragment))),
                        isDisplayed()));
        button3.check(matches(isDisplayed()));

        ViewInteraction materialButton4 = onView(
                allOf(withId(R.id.selfmade_button), withText("I want to make my own"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.meal_plan_fragment),
                                        0),
                                1),
                        isDisplayed()));
        materialButton4.perform(click());

        ViewInteraction textView6 = onView(
                allOf(withId(R.id.textView8), withText("Meal Planner"),
                        withParent(withParent(withId(R.id.scrollView3))),
                        isDisplayed()));
        textView6.check(matches(withText("Meal Planner")));

        ViewInteraction textView7 = onView(
                allOf(withId(R.id.mp_week_of_mealplan), withText("Week of Meal Plan"),
                        withParent(withParent(withId(R.id.scrollView3))),
                        isDisplayed()));
        textView7.check(matches(withText("Week of Meal Plan")));

        ViewInteraction button4 = onView(
                allOf(withId(R.id.calendar_previous),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class))),
                        isDisplayed()));
        button4.check(matches(isDisplayed()));

        ViewInteraction button5 = onView(
                allOf(withId(R.id.calendar_next),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class))),
                        isDisplayed()));
        button5.check(matches(isDisplayed()));

        ViewInteraction textView8 = onView(
                allOf(withId(R.id.mp_day_of_week_title), withText("Day of Week"),
                        withParent(withParent(withId(R.id.scrollView3))),
                        isDisplayed()));
        textView8.check(matches(withText("Day of Week")));

        ViewInteraction horizontalScrollView = onView(
                allOf(withId(R.id.meal_tab),
                        withParent(withParent(withId(R.id.scrollView3))),
                        isDisplayed()));
        horizontalScrollView.check(matches(isDisplayed()));

        ViewInteraction imageButton3 = onView(
                allOf(withId(R.id.mp_help_button),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class))),
                        isDisplayed()));
        imageButton3.check(matches(isDisplayed()));

        ViewInteraction textView9 = onView(
                allOf(withId(R.id.textView19), withText("Meal of Day"),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class))),
                        isDisplayed()));
        textView9.check(matches(withText("Meal of Day")));

        ViewInteraction horizontalScrollView2 = onView(
                allOf(withId(R.id.week_tab),
                        withParent(withParent(withId(R.id.scrollView3))),
                        isDisplayed()));
        horizontalScrollView2.check(matches(isDisplayed()));

        ViewInteraction textView10 = onView(
                allOf(withId(R.id.mp_items_title), withText("Items for breakfast on Sunday"),
                        withParent(allOf(withId(R.id.mp_table_linear_layout),
                                withParent(IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class)))),
                        isDisplayed()));
        textView10.check(matches(withText("Items for breakfast on Sunday")));

        ViewInteraction tabView = onView(
                allOf(withContentDescription("W"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.week_tab),
                                        0),
                                3)));
        tabView.perform(scrollTo(), click());

        ViewInteraction tabView2 = onView(
                allOf(withContentDescription("Lunch"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.meal_tab),
                                        0),
                                1)));
        tabView2.perform(scrollTo(), click());

        ViewInteraction textView11 = onView(
                allOf(withId(R.id.mp_items_title), withText("Items for lunch on Wednesday"),
                        withParent(allOf(withId(R.id.mp_table_linear_layout),
                                withParent(IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class)))),
                        isDisplayed()));
        textView11.check(matches(withText("Items for lunch on Wednesday")));

        ViewInteraction floatingActionButton3 = onView(
                allOf(withId(R.id.mp_add_item),
                        childAtPosition(
                                allOf(withId(R.id.mp_edit_buttons),
                                        childAtPosition(
                                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                                1)),
                                0)));
        floatingActionButton3.perform(scrollTo(), click());

        ViewInteraction textView12 = onView(
                allOf(withId(R.id.textView11), withText("Selection"),
                        withParent(withParent(withId(android.R.id.custom))),
                        isDisplayed()));
        textView12.check(matches(withText("Selection")));

        ViewInteraction radioGroup = onView(
                allOf(withId(R.id.mp_type_of_item),
                        withParent(withParent(withId(android.R.id.custom))),
                        isDisplayed()));
        radioGroup.check(matches(isDisplayed()));

        ViewInteraction radioButton = onView(
                allOf(withId(R.id.ingredient_radio), withText("Ingredient Storage"),
                        withParent(allOf(withId(R.id.mp_type_of_item),
                                withParent(IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class)))),
                        isDisplayed()));
        radioButton.check(matches(isDisplayed()));

        ViewInteraction radioButton2 = onView(
                allOf(withId(R.id.recipe_radio), withText("Recipes"),
                        withParent(allOf(withId(R.id.mp_type_of_item),
                                withParent(IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class)))),
                        isDisplayed()));
        radioButton2.check(matches(isDisplayed()));

        ViewInteraction listView = onView(
                allOf(withId(R.id.meal_plan_item_list),
                        withParent(withParent(withId(android.R.id.custom))),
                        isDisplayed()));
        listView.check(matches(isDisplayed()));

        ViewInteraction button6 = onView(
                allOf(withId(R.id.mp_items_cancel), withText("CANCEL"),
                        withParent(withParent(withId(android.R.id.custom))),
                        isDisplayed()));
        button6.check(matches(isDisplayed()));

        ViewInteraction button7 = onView(
                allOf(withId(R.id.mp_items_save), withText("SAVE"),
                        withParent(withParent(withId(android.R.id.custom))),
                        isDisplayed()));
        button7.check(matches(isDisplayed()));

        ViewInteraction materialButton5 = onView(
                allOf(withId(R.id.mp_items_cancel), withText("Cancel"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.custom),
                                        0),
                                1),
                        isDisplayed()));
        materialButton5.perform(click());

        ViewInteraction floatingActionButton4 = onView(
                allOf(withId(R.id.mp_help_button),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.LinearLayout")),
                                        6),
                                1)));
        floatingActionButton4.perform(scrollTo(), click());

        ViewInteraction textView13 = onView(
                allOf(IsInstanceOf.<View>instanceOf(android.widget.TextView.class), withText("Help"),
                        withParent(allOf(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                withParent(IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class)))),
                        isDisplayed()));
        textView13.check(matches(withText("Help")));

        ViewInteraction textView14 = onView(
                allOf(withId(android.R.id.message), withText("You can build a meal plan using either your stored ingredients or stored recipes. You can choose any number of ingredients or recipes; a meal plan will be curated based on your selections."),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class))),
                        isDisplayed()));
        textView14.check(matches(withText("You can build a meal plan using either your stored ingredients or stored recipes. You can choose any number of ingredients or recipes; a meal plan will be curated based on your selections.")));

        ViewInteraction button8 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class))),
                        isDisplayed()));
        button8.check(matches(isDisplayed()));

        ViewInteraction materialButton6 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("android.widget.ScrollView")),
                                        0),
                                3)));
        materialButton6.perform(scrollTo(), click());

        ViewInteraction materialButton7 = onView(
                allOf(withId(R.id.mp_cancel), withText("back"),
                        childAtPosition(
                                allOf(withId(R.id.mp_create_function_buttons),
                                        childAtPosition(
                                                withClassName(is("android.widget.LinearLayout")),
                                                12)),
                                1)));
        materialButton7.perform(scrollTo(), click());
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }
}
