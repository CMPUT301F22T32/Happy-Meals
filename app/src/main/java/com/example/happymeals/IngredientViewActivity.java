package com.example.happymeals;

import android.content.Intent;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.appcompat.app.AppCompatActivity;

import com.example.happymeals.fragments.InputErrorFragment;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class IngredientViewActivity extends AppCompatActivity {

    private EditText description = findViewById(R.id.ing_content_desc_input);
    private EditText date = findViewById(R.id.ing_content_date_input);
    private EditText quantity = findViewById(R.id.ing_content_quantity_input);
    private EditText unitCost = findViewById(R.id.ing_content_cost_input);
    private Spinner locationSpinner = findViewById(R.id.ing_content_location_input);
    private Spinner categorySpinner = findViewById(R.id.ing_content_category_input);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ingredient_content_view);

        Intent intent = getIntent();
        if (!intent.getBooleanExtra(ADD_INGREDIENT))
            fillFields();
    }

    private void fillFields() {
        Ingredient ingredient = intent.getSerializableExtra(INGREDIENT_EXTRA);
        description.setText(ingredient.getDescription());
        date.setText(ingredient.getBestBeforeDate());
        quantity.setText(ingredient.getAmount());
        unitCost.setText(ingredient.getUnit());
        // locationSpinner.setSelection();
        // categorySpinner.setSelection();
    }

    private void checkInput() {
        InputValidator validator = new InputValidator();
        String errorString = "The ingredient couldn't be saved for the following reasons:\n";

        validator.checkText(description, "Description");
        validator.checkDate(date);
        validator.checkNum(quantity, "Quantity");
        validator.checkNum(unitCost, "Unit Cost");
        validator.checkSpinner(locationSpinner, "Location");
        validator.checkSpinner(categorySpinner, "Category");

        String errors = validator.getErrors();
        if (errors != null) {
            errorString += errors;
            InputErrorFragment errorFragment = new InputErrorFragment("Ingredient Save Error", errorString, getBaseContext());
            errorFragment.display();
        }
        else {
            // open save fragment
        }
    }

}
