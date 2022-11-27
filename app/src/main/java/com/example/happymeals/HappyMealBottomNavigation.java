package com.example.happymeals;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;

import com.example.happymeals.ingredient.IngredientStorageActivity;
import com.example.happymeals.mealplan.MealPlanActivity;
import com.example.happymeals.recipe.RecipeStorageActivity;
import com.example.happymeals.shoppinglist.ShoppingListActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class HappyMealBottomNavigation {
    BottomNavigationView bottomBar;
    Context context;
    Activity activity;

    public <T extends android.view.View> HappyMealBottomNavigation( T view, Context context, int resourceId ) {
        if( view.getClass() == BottomNavigationView.class ) {
            this.context = context;
            activity = (Activity) context;
            bottomBar = (BottomNavigationView) view;
            bottomBar.setSelectedItemId( resourceId );
        } else {
            bottomBar = null;
        }
    }

    public void setupBarListener() {
        bottomBar.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override

            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(activity).toBundle();
                switch (item.getItemId()) {

                    case R.id.home_menu:
                        Intent home_intent = new Intent(context, MainActivity.class);
                        activity.startActivity(home_intent, bundle);
                        break;
                    case R.id.recipe_menu:
                        Intent recipe_intent = new Intent(context, RecipeStorageActivity.class);
                        activity.startActivity(recipe_intent, bundle);
                        break;

                    case R.id.ingredient_menu:
                        Intent ingredient_intent = new Intent(context, IngredientStorageActivity.class);
                        activity.startActivity(ingredient_intent, bundle);
                        break;

                    case R.id.mealplan_menu:
                        Intent mealplan_intent = new Intent(context, MealPlanActivity.class);
                        activity.startActivity(mealplan_intent, bundle);
                        break;

                    case R.id.shopping_menu:
                        Intent shoppinglist_intent = new Intent(context, ShoppingListActivity.class);
                        activity.startActivity(shoppinglist_intent, bundle);
                        break;
                    default:
                }

                return true;

            }
        });
    }
}
