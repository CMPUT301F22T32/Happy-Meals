package com.example.happymeals.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * This class is used to generate the fragments that should appear on the screen when a user wishes
 * to save or delete an object. Since no complex views are necessary for the fragments -- at most
 * they only require text and an action to be carried out -- there was no need to inherit from a
 * Dialog object.
 * @author sruduke
 */
public class ModifyConfirmationFragment {

    /**
     * This variable represents the generated fragment ({@link AlertDialog}).
     */
    private AlertDialog fragment;

    /**
     * This is the constructor to create a ModifyConfirmationFragment.
     * @param title The title of the fragment ({@link String}).
     * @param message The message to display in the fragment ({@link String}).
     * @param context The {@link Context} of the current activity so that the fragment will properly appear on the screen.
     * @param actionListener The action to carry out upon the user selecting 'Confirm' ({@link DialogInterface.OnClickListener}
     */
    public ModifyConfirmationFragment(String title, String message, Context context, DialogInterface.OnClickListener actionListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("Confirm", actionListener)
                .setNegativeButton("Cancel", null);
        fragment = builder.create();
    }

    /**
     * The constructor only creates the object, while this method actually shows the {@link ModifyConfirmationFragment}
     * on the screen.
     */
    public void display() {
        fragment.show();
    }
}