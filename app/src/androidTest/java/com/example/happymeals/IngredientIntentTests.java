package com.example.happymeals;


import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@LargeTest
@RunWith( AndroidJUnit4.class )
public class IngredientIntentTests {

    @Rule
    public ActivityScenarioRule<MainActivity> mActivityScenarioRule =
            new ActivityScenarioRule<>( MainActivity.class );

    @Test
    public void ingredientIntentTests() {
        ViewInteraction button = onView( 
                allOf( withId( R.id.ingredient_storage_button ), withText( "INGREDIENT STORAGE" ),
                        withParent( withParent( withId( android.R.id.content ) ) ),
                        isDisplayed() ) );
        button.check( matches( isDisplayed() ) );

        ViewInteraction materialButton = onView( 
                allOf( withId( R.id.ingredient_storage_button ), withText( "Ingredient Storage" ),
                        childAtPosition( 
                                childAtPosition( 
                                        withId( android.R.id.content ),
                                        0 ),
                                2 ),
                        isDisplayed() ) );
        materialButton.perform( click() );

        ViewInteraction textView = onView( 
                allOf( withId( R.id.textView2 ), withText( "Ingredient Storage" ),
                        withParent( withParent( withId( android.R.id.content ) ) ),
                        isDisplayed() ) );
        textView.check( matches( withText( "Ingredient Storage" ) ) );

        ViewInteraction textView2 = onView( 
                allOf( withId( R.id.textView3 ), withText( "Sort by" ),
                        withParent( withParent( withId( android.R.id.content ) ) ),
                        isDisplayed() ) );
        textView2.check( matches( withText( "Sort by" ) ) );

        ViewInteraction textView3 = onView( 
                allOf( withId( R.id.recipe_storage_description_label ), withText( "Description" ),
                        withParent( withParent( withId( android.R.id.content ) ) ),
                        isDisplayed() ) );
        textView3.check( matches( withText( "Description" ) ) );

        ViewInteraction imageButton = onView( 
                allOf( withId( R.id.recipe_storage_add_button ),
                        withParent( withParent( withId( android.R.id.content ) ) ),
                        isDisplayed() ) );
        imageButton.check( matches( isDisplayed() ) );

        ViewInteraction listView = onView( 
                allOf( withId( R.id.storage_list ),
                        withParent( withParent( withId( android.R.id.content ) ) ),
                        isDisplayed() ) );
        listView.check( matches( isDisplayed() ) );

        ViewInteraction floatingActionButton = onView( 
                allOf( withId( R.id.recipe_storage_add_button ),
                        childAtPosition( 
                                childAtPosition( 
                                        withId( android.R.id.content ),
                                        0 ),
                                4 ),
                        isDisplayed() ) );
        floatingActionButton.perform( click() );

        ViewInteraction materialButton2 = onView( 
                allOf( withId( R.id.ing_view_save_button ), withText( "Save" ),
                        childAtPosition( 
                                childAtPosition( 
                                        withClassName( is( "android.widget.LinearLayout" ) ),
                                        12 ),
                                1 ) ) );
        materialButton2.perform( scrollTo(), click() );

        ViewInteraction textView4 = onView( 
                allOf( IsInstanceOf.<View>instanceOf( android.widget.TextView.class ), withText( "Ingredient Save Error" ),
                        withParent( allOf( IsInstanceOf.<View>instanceOf( android.widget.LinearLayout.class ),
                                withParent( IsInstanceOf.<View>instanceOf( android.widget.LinearLayout.class ) ) ) ),
                        isDisplayed() ) );
        textView4.check( matches( withText( "Ingredient Save Error" ) ) );

        ViewInteraction materialButton3 = onView( 
                allOf( withId( android.R.id.button1 ), withText( "OK" ),
                        childAtPosition( 
                                childAtPosition( 
                                        withClassName( is( "android.widget.ScrollView" ) ),
                                        0 ),
                                3 ) ) );
        materialButton3.perform( scrollTo(), click() );

        ViewInteraction appCompatEditText = onView( 
                allOf( withId( R.id.ing_content_name_input ),
                        childAtPosition( 
                                childAtPosition( 
                                        withId( R.id.scrollView2 ),
                                        0 ),
                                1 ) ) );
        appCompatEditText.perform( scrollTo(), replaceText( "Apple" ), closeSoftKeyboard() );

        ViewInteraction materialButton4 = onView( 
                allOf( withId( R.id.ing_view_save_button ), withText( "Save" ),
                        childAtPosition( 
                                childAtPosition( 
                                        withClassName( is( "android.widget.LinearLayout" ) ),
                                        12 ),
                                1 ) ) );
        materialButton4.perform( scrollTo(), click() );

        ViewInteraction materialButton5 = onView( 
                allOf( withId( android.R.id.button1 ), withText( "OK" ),
                        childAtPosition( 
                                childAtPosition( 
                                        withClassName( is( "android.widget.ScrollView" ) ),
                                        0 ),
                                3 ) ) );
        materialButton5.perform( scrollTo(), click() );

        ViewInteraction appCompatEditText2 = onView( 
                allOf( withId( R.id.ing_content_desc_input ),
                        childAtPosition( 
                                childAtPosition( 
                                        withId( R.id.scrollView2 ),
                                        0 ),
                                3 ) ) );
        appCompatEditText2.perform( scrollTo(), replaceText( "Red" ), closeSoftKeyboard() );

        ViewInteraction appCompatEditText3 = onView( 
                allOf( withId( R.id.ing_content_date_input ),
                        childAtPosition( 
                                childAtPosition( 
                                        withId( R.id.scrollView2 ),
                                        0 ),
                                5 ) ) );
        appCompatEditText3.perform( scrollTo(), replaceText( "2022-08-08" ), closeSoftKeyboard() );

        ViewInteraction materialButton6 = onView( 
                allOf( withId( R.id.ing_view_save_button ), withText( "Save" ),
                        childAtPosition( 
                                childAtPosition( 
                                        withClassName( is( "android.widget.LinearLayout" ) ),
                                        12 ),
                                1 ) ) );
        materialButton6.perform( scrollTo(), click() );

        ViewInteraction textView5 = onView( 
                allOf( IsInstanceOf.<View>instanceOf( android.widget.TextView.class ), withText( "Ingredient Save Error" ),
                        withParent( allOf( IsInstanceOf.<View>instanceOf( android.widget.LinearLayout.class ),
                                withParent( IsInstanceOf.<View>instanceOf( android.widget.LinearLayout.class ) ) ) ),
                        isDisplayed() ) );
        textView5.check( matches( withText( "Ingredient Save Error" ) ) );

        ViewInteraction materialButton7 = onView( 
                allOf( withId( android.R.id.button1 ), withText( "OK" ),
                        childAtPosition( 
                                childAtPosition( 
                                        withClassName( is( "android.widget.ScrollView" ) ),
                                        0 ),
                                3 ) ) );
        materialButton7.perform( scrollTo(), click() );

        ViewInteraction appCompatEditText4 = onView( 
                allOf( withId( R.id.ing_content_quantity_input ),
                        childAtPosition( 
                                childAtPosition( 
                                        withClassName( is( "android.widget.LinearLayout" ) ),
                                        7 ),
                                0 ) ) );
        appCompatEditText4.perform( scrollTo(), replaceText( "5" ), closeSoftKeyboard() );

        ViewInteraction appCompatSpinner = onView( 
                allOf( withId( R.id.ing_content_location_input ),
                        childAtPosition( 
                                childAtPosition( 
                                        withId( R.id.scrollView2 ),
                                        0 ),
                                9 ) ) );
        appCompatSpinner.perform( scrollTo(), click() );

        ViewInteraction appCompatSpinner2 = onView( 
                allOf( withId( R.id.ing_content_category_input ),
                        childAtPosition( 
                                childAtPosition( 
                                        withId( R.id.scrollView2 ),
                                        0 ),
                                11 ) ) );
        appCompatSpinner2.perform( scrollTo(), click() );

        DataInteraction materialTextView = onData( anything() )
                .inAdapterView( childAtPosition( 
                        withClassName( is( "android.widget.PopupWindow$PopupBackgroundView" ) ),
                        0 ) )
                .atPosition( 2 );
        materialTextView.perform( click() );

        ViewInteraction materialButton8 = onView( 
                allOf( withId( R.id.ing_view_save_button ), withText( "Save" ),
                        childAtPosition( 
                                childAtPosition( 
                                        withClassName( is( "android.widget.LinearLayout" ) ),
                                        12 ),
                                1 ) ) );
        materialButton8.perform( scrollTo(), click() );

        ViewInteraction textView6 = onView( 
                allOf( IsInstanceOf.<View>instanceOf( android.widget.TextView.class ), withText( "Save Ingredient" ),
                        withParent( allOf( IsInstanceOf.<View>instanceOf( android.widget.LinearLayout.class ),
                                withParent( IsInstanceOf.<View>instanceOf( android.widget.LinearLayout.class ) ) ) ),
                        isDisplayed() ) );
        textView6.check( matches( withText( "Save Ingredient" ) ) );

        ViewInteraction textView7 = onView( 
                allOf( withId( android.R.id.message ), withText( "Do you wish to save your changes?" ),
                        withParent( withParent( IsInstanceOf.<View>instanceOf( android.widget.ScrollView.class ) ) ),
                        isDisplayed() ) );
        textView7.check( matches( withText( "Do you wish to save your changes?" ) ) );

        ViewInteraction button2 = onView( 
                allOf( withId( android.R.id.button2 ), withText( "CANCEL" ),
                        withParent( withParent( IsInstanceOf.<View>instanceOf( android.widget.ScrollView.class ) ) ),
                        isDisplayed() ) );
        button2.check( matches( isDisplayed() ) );

        ViewInteraction button3 = onView( 
                allOf( withId( android.R.id.button1 ), withText( "CONFIRM" ),
                        withParent( withParent( IsInstanceOf.<View>instanceOf( android.widget.ScrollView.class ) ) ),
                        isDisplayed() ) );
        button3.check( matches( isDisplayed() ) );

        ViewInteraction materialButton9 = onView( 
                allOf( withId( android.R.id.button1 ), withText( "Confirm" ),
                        childAtPosition( 
                                childAtPosition( 
                                        withClassName( is( "android.widget.ScrollView" ) ),
                                        0 ),
                                3 ) ) );
        materialButton9.perform( scrollTo(), click() );

        ViewInteraction textView8 = onView( 
                allOf( withId( R.id.ingredient_specific_list_name_field ), withText( "Apple" ),
                        withParent( withParent( withId( R.id.storage_list ) ) ),
                        isDisplayed() ) );
        textView8.check( matches( withText( "Apple" ) ) );

        ViewInteraction textView9 = onView( 
                allOf( withId( R.id.recipe_list_description_field ), withText( "Red" ),
                        withParent( withParent( withId( R.id.storage_list ) ) ),
                        isDisplayed() ) );
        textView9.check( matches( withText( "Red" ) ) );

        ViewInteraction textView10 = onView( 
                allOf( withId( R.id.recipe_list_servings_field ), withText( "PANTRY" ),
                        withParent( withParent( withId( R.id.storage_list ) ) ),
                        isDisplayed() ) );
        textView10.check( matches( withText( "PANTRY" ) ) );

        ViewInteraction textView11 = onView( 
                allOf( withId( R.id.ingredient_specific_amount_text ), withText( "5" ),
                        withParent( withParent( withId( R.id.storage_list ) ) ),
                        isDisplayed() ) );
        textView11.check( matches( withText( "5" ) ) );

        ViewInteraction textView12 = onView( 
                allOf( withId( R.id.ingredient_specific_amount_unit_text ), withText( "COUNT" ),
                        withParent( withParent( withId( R.id.storage_list ) ) ),
                        isDisplayed() ) );
        textView12.check( matches( withText( "COUNT" ) ) );

        DataInteraction constraintLayout = onData( anything() )
                .inAdapterView( allOf( withId( R.id.storage_list ),
                        childAtPosition( 
                                withClassName( is( "androidx.constraintlayout.widget.ConstraintLayout" ) ),
                                0 ) ) )
                .atPosition( 0 );
        constraintLayout.perform( click() );

        ViewInteraction editText = onView( 
                allOf( withId( R.id.ing_content_name_input ), withText( "Apple" ),
                        withParent( withParent( withId( R.id.scrollView2 ) ) ),
                        isDisplayed() ) );
        editText.check( matches( withText( "Apple" ) ) );

        ViewInteraction editText2 = onView( 
                allOf( withId( R.id.ing_content_desc_input ), withText( "Red" ),
                        withParent( withParent( withId( R.id.scrollView2 ) ) ),
                        isDisplayed() ) );
        editText2.check( matches( withText( "Red" ) ) );

        ViewInteraction editText3 = onView( 
                allOf( withId( R.id.ing_content_quantity_input ), withText( "5" ),
                        withParent( withParent( IsInstanceOf.<View>instanceOf( android.widget.LinearLayout.class ) ) ),
                        isDisplayed() ) );
        editText3.check( matches( withText( "5" ) ) );

        ViewInteraction textView13 = onView( 
                allOf( withId( android.R.id.text1 ), withText( "COUNT" ),
                        withParent( allOf( withId( R.id.ing_content_unit_input ),
                                withParent( IsInstanceOf.<View>instanceOf( android.widget.LinearLayout.class ) ) ) ),
                        isDisplayed() ) );
        textView13.check( matches( withText( "COUNT" ) ) );

        ViewInteraction textView14 = onView( 
                allOf( withId( android.R.id.text1 ), withText( "PANTRY" ),
                        withParent( allOf( withId( R.id.ing_content_location_input ),
                                withParent( IsInstanceOf.<View>instanceOf( android.widget.LinearLayout.class ) ) ) ),
                        isDisplayed() ) );
        textView14.check( matches( withText( "PANTRY" ) ) );

        ViewInteraction textView15 = onView( 
                allOf( withId( android.R.id.text1 ), withText( "FRUIT" ),
                        withParent( allOf( withId( R.id.ing_content_category_input ),
                                withParent( IsInstanceOf.<View>instanceOf( android.widget.LinearLayout.class ) ) ) ),
                        isDisplayed() ) );
        textView15.check( matches( withText( "FRUIT" ) ) );

        ViewInteraction button4 = onView( 
                allOf( withId( R.id.ing_view_delete_button ), withText( "DELETE" ),
                        withParent( withParent( IsInstanceOf.<View>instanceOf( android.widget.LinearLayout.class ) ) ),
                        isDisplayed() ) );
        button4.check( matches( isDisplayed() ) );

        ViewInteraction appCompatEditText5 = onView( 
                allOf( withId( R.id.ing_content_desc_input ), withText( "Red" ),
                        childAtPosition( 
                                childAtPosition( 
                                        withId( R.id.scrollView2 ),
                                        0 ),
                                3 ) ) );
        appCompatEditText5.perform( scrollTo(), replaceText( "Green" ) );

        ViewInteraction appCompatEditText6 = onView( 
                allOf( withId( R.id.ing_content_desc_input ), withText( "Green" ),
                        childAtPosition( 
                                childAtPosition( 
                                        withId( R.id.scrollView2 ),
                                        0 ),
                                3 ),
                        isDisplayed() ) );
        appCompatEditText6.perform( closeSoftKeyboard() );

        ViewInteraction appCompatEditText7 = onView( 
                allOf( withId( R.id.ing_content_quantity_input ), withText( "5" ),
                        childAtPosition( 
                                childAtPosition( 
                                        withClassName( is( "android.widget.LinearLayout" ) ),
                                        7 ),
                                0 ) ) );
        appCompatEditText7.perform( scrollTo(), replaceText( "3" ) );

        ViewInteraction appCompatEditText8 = onView( 
                allOf( withId( R.id.ing_content_quantity_input ), withText( "3" ),
                        childAtPosition( 
                                childAtPosition( 
                                        withClassName( is( "android.widget.LinearLayout" ) ),
                                        7 ),
                                0 ),
                        isDisplayed() ) );
        appCompatEditText8.perform( closeSoftKeyboard() );

        ViewInteraction materialButton10 = onView( 
                allOf( withId( R.id.ing_view_save_button ), withText( "Save" ),
                        childAtPosition( 
                                childAtPosition( 
                                        withClassName( is( "android.widget.LinearLayout" ) ),
                                        12 ),
                                1 ) ) );
        materialButton10.perform( scrollTo(), click() );

        ViewInteraction materialButton11 = onView( 
                allOf( withId( android.R.id.button1 ), withText( "Confirm" ),
                        childAtPosition( 
                                childAtPosition( 
                                        withClassName( is( "android.widget.ScrollView" ) ),
                                        0 ),
                                3 ) ) );
        materialButton11.perform( scrollTo(), click() );

        ViewInteraction textView16 = onView( 
                allOf( withId( R.id.recipe_list_description_field ), withText( "Green" ),
                        withParent( withParent( withId( R.id.storage_list ) ) ),
                        isDisplayed() ) );
        textView16.check( matches( withText( "Green" ) ) );

        ViewInteraction textView17 = onView( 
                allOf( withId( R.id.ingredient_specific_amount_text ), withText( "3" ),
                        withParent( withParent( withId( R.id.storage_list ) ) ),
                        isDisplayed() ) );
        textView17.check( matches( withText( "3" ) ) );

        DataInteraction constraintLayout2 = onData( anything() )
                .inAdapterView( allOf( withId( R.id.storage_list ),
                        childAtPosition( 
                                withClassName( is( "androidx.constraintlayout.widget.ConstraintLayout" ) ),
                                0 ) ) )
                .atPosition( 0 );
        constraintLayout2.perform( click() );

        ViewInteraction editText4 = onView( 
                allOf( withId( R.id.ing_content_desc_input ), withText( "Green" ),
                        withParent( withParent( withId( R.id.scrollView2 ) ) ),
                        isDisplayed() ) );
        editText4.check( matches( withText( "Green" ) ) );

        ViewInteraction editText5 = onView( 
                allOf( withId( R.id.ing_content_quantity_input ), withText( "3" ),
                        withParent( withParent( IsInstanceOf.<View>instanceOf( android.widget.LinearLayout.class ) ) ),
                        isDisplayed() ) );
        editText5.check( matches( withText( "3" ) ) );

        ViewInteraction materialButton12 = onView( 
                allOf( withId( R.id.ing_view_delete_button ), withText( "Delete" ),
                        childAtPosition( 
                                childAtPosition( 
                                        withClassName( is( "android.widget.LinearLayout" ) ),
                                        12 ),
                                0 ) ) );
        materialButton12.perform( scrollTo(), click() );

        ViewInteraction textView18 = onView( 
                allOf( IsInstanceOf.<View>instanceOf( android.widget.TextView.class ), withText( "Remove Ingredient" ),
                        withParent( allOf( IsInstanceOf.<View>instanceOf( android.widget.LinearLayout.class ),
                                withParent( IsInstanceOf.<View>instanceOf( android.widget.LinearLayout.class ) ) ) ),
                        isDisplayed() ) );
        textView18.check( matches( withText( "Remove Ingredient" ) ) );

        ViewInteraction button5 = onView( 
                allOf( withId( android.R.id.button2 ), withText( "CANCEL" ),
                        withParent( withParent( IsInstanceOf.<View>instanceOf( android.widget.ScrollView.class ) ) ),
                        isDisplayed() ) );
        button5.check( matches( isDisplayed() ) );

        ViewInteraction button6 = onView( 
                allOf( withId( android.R.id.button1 ), withText( "CONFIRM" ),
                        withParent( withParent( IsInstanceOf.<View>instanceOf( android.widget.ScrollView.class ) ) ),
                        isDisplayed() ) );
        button6.check( matches( isDisplayed() ) );

        ViewInteraction materialButton13 = onView( 
                allOf( withId( android.R.id.button1 ), withText( "Confirm" ),
                        childAtPosition( 
                                childAtPosition( 
                                        withClassName( is( "android.widget.ScrollView" ) ),
                                        0 ),
                                3 ) ) );
        materialButton13.perform( scrollTo(), click() );
    }

    private static Matcher<View> childAtPosition( 
            final Matcher<View> parentMatcher, final int position ) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo( Description description ) {
                description.appendText( "Child at position " + position + " in parent " );
                parentMatcher.describeTo( description );
            }

            @Override
            public boolean matchesSafely( View view ) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches( parent )
                        && view.equals( ( ( ViewGroup ) parent ).getChildAt( position ) );
            }
        };
    }
}
