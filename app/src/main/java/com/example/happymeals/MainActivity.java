package com.example.happymeals;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Ingredient i = new Ingredient(
                "Apple",
                "2022-09-09",
                Location.Freezer,
                25,
                AmountUnit.mg,
                IngredientCategory.Meat
        );

        Intent intent = new Intent(this, IngredientViewActivity.class);
        intent.putExtra(IngredientViewActivity.INGREDIENT_EXTRA, i);
        intent.putExtra(IngredientViewActivity.ADD_INGREDIENT, false);
        startActivity(intent);
    }
}