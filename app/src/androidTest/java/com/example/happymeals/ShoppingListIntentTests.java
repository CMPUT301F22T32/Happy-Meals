package com.example.happymeals;


import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

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
public class ShoppingListIntentTests {

    @Rule
    public ActivityScenarioRule<StartScreenActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>(StartScreenActivity.class);

    @Test
    public void shoppingListIntentTests() {
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
                allOf(withId(R.id.shopping_menu), withContentDescription("Shopping"),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.bottomNavigationView),
                                        0),
                                4),
                        isDisplayed()));
        bottomNavigationItemView.perform(click());

        ViewInteraction textView = onView(
                allOf(withId(R.id.ing_content_title2), withText("Shopping List"),
                        withParent(allOf(withId(R.id.linearLayout2),
                                withParent(IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class)))),
                        isDisplayed()));
        textView.check(matches(withText("Shopping List")));

        ViewInteraction textView2 = onView(
                allOf(withId(R.id.textView), withText("Sort By:"),
                        withParent(allOf(withId(R.id.linearLayout),
                                withParent(withId(R.id.linearLayout2)))),
                        isDisplayed()));
        textView2.check(matches(withText("Sort By:")));

        ViewInteraction textView3 = onView(
                allOf(withId(android.R.id.text1), withText("Amount Needed"),
                        withParent(allOf(withId(R.id.shopping_list_spinner),
                                withParent(withId(R.id.linearLayout)))),
                        isDisplayed()));
        textView3.check(matches(withText("Amount Needed")));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.textView20), withText("Total Items:"),
                        withParent(withParent(withId(R.id.linearLayout2))),
                        isDisplayed()));
        textView4.check(matches(withText("Total Items:")));

        ViewInteraction button = onView(
                allOf(withId(R.id.shopping_list_buy), withText("I HAVE PICKED UP THESE ITEMS"),
                        withParent(allOf(withId(R.id.linearLayout2),
                                withParent(IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class)))),
                        isDisplayed()));
        button.check(matches(isDisplayed()));

        ViewInteraction listView = onView(
                allOf(withId(R.id.shopping_list_ingredients),
                        withParent(allOf(withId(R.id.linearLayout2),
                                withParent(IsInstanceOf.<View>instanceOf(android.view.ViewGroup.class)))),
                        isDisplayed()));
        listView.check(matches(isDisplayed()));
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
