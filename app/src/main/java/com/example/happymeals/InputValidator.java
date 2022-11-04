package com.example.happymeals;

import android.widget.EditText;
import android.widget.Spinner;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * This class is used to minimize the risk of save failure within the database. It consists of
 * methods to check all possible input components: {@link EditText}, {@link Spinner}. Besides
 * just these objects, it checks based on the type of data as well: string, number, date, etc.
 * Concurrently, it builds an error string which can be used to display to the user to inform them
 * of which fields they are missing/need to fix.
 *
 * For a specific view, it will instantiate a {@link InputValidator} object and each input component
 * will be checked using its corresponding method ({@link #checkText(EditText, String)},
 * {@link #checkDate(EditText)}, etc.).
 *
 * After all have been checked, {@link #getErrors()} will return null if there were no errors;
 * otherwise, it will return an error string consisting of fields that need to be fixed.
 * @author sruduke
 */
public class InputValidator {

    private StringBuilder errorString = new StringBuilder();

    /**
     * Adds to the error string if empty; does nothing otherwise.
     * @param text The input component to check {@link EditText}
     * @param name The name of the input {@link String}
     */
    public void checkText(EditText text, String name) {
        // must explicitly check for empty string and not isEmpty()
        if (text.getText().toString().equals(""))
            errorString.append(String.format("- %s cannot be empty.\n", name));
    }

    /**
     * Adds to the error string if improper format / no date provided; does nothing otherwise
     * @param date The input component to check {@link EditText}
     */
    public void checkDate(EditText date) {
        // ensure the date is in yyyy-mm-dd form; will change with implementation of widget
        try {
            String dateStr = date.getText().toString();
            LocalDate.parse(dateStr, DateTimeFormatter.ISO_LOCAL_DATE);
        } catch (Exception e) {
            errorString.append(String.format("- Date cannot be empty and must be in YYYY-MM-DD format.\n"));
        }
    }

    /**
     * Adds to the error string if the number is not an integer greater than or equal to 0.
     * @param field The input component to check {@link EditText}
     * @param name The name of the input {@link String}
     */
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

    /**
     * Adds to the error string if the spinner does not have anything selected (should not occur
     * based on our implementation).
     * @param spinner The input component to check {@link EditText}
     * @param name The name of the input {@link String}
     */
    public void checkSpinner(Spinner spinner, String name) {
        if (spinner.getSelectedItem().toString().isEmpty())
            errorString.append(String.format("- %s cannot be empty.\n", name));
    }

    /**
     * This function should be called ONLY when all components have been checked with the previous
     * methods.
     * @return null if there were no errors; otherwise, it returns a {@link String} representing the
     * error / input fields that need to be fixed by the user to proceed further (to be used by
     * a {@link com.example.happymeals.fragments.InputErrorFragment}.
     */
    public String getErrors() {
        if (!errorString.toString().isEmpty())
            return errorString.toString();
        else
            return null;
    }
}
