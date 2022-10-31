package com.example.happymeals;

import android.widget.EditText;
import android.widget.Spinner;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class InputValidator {

    private StringBuilder errorString = new StringBuilder();

    public void checkText(EditText text, String name) {
        // must explicitly check for empty string and not isEmpty()
        if (text.getText().toString().equals(""))
            errorString.append(String.format("- %s cannot be empty.\n", name));
    }

    public void checkDate(EditText date) {
        // ensure the date is in yyyy-mm-dd form; will change with implementation of widget
        try {
            String dateStr = date.getText().toString();
            LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (Exception e) {
            errorString.append(String.format("- Date cannot be empty and must be in YYYY-MM-DD format.\n"));
        }
    }

    public void checkNum(EditText field, String name) {
        // ensures numbers are entered, numbers are ints, and numbers > 0
        boolean valid = true;
        try {
            Integer input = Integer.parseInt(field.getText().toString());
            if (input <= 0)
                valid = false;
        } catch (Exception e) {
            valid = false;
        }
        if (!valid)
            errorString.append(String.format("- %s cannot be empty and must be a positive integer.\n", name));
    }

    public void checkSpinner(Spinner spinner, String name) {
        if (spinner.getSelectedItem().toString().isEmpty())
            errorString.append(String.format("- %s cannot be empty.\n", name));
    }

    public String getErrors() {
        if (!errorString.toString().isEmpty())
            return errorString.toString();
        else
            return null;
    }
}
