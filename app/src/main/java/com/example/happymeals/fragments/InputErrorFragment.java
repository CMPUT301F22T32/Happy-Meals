package com.example.happymeals.fragments;

import android.app.AlertDialog;
import android.content.Context;

/**
 * This class is used to generate the fragment that should appear when a user does not enter valid
 * input. This fragment varies from the {@link ModifyConfirmationFragment} class, for it does not
 * require an action listener and only displays a message to the user. The two Fragment classes
 * will eventually be merged into a single FragmentCreator class which will have methods to create
 * both types of fragments. Since no complex views are necessary for the fragments -- at most
 * they only require text and an action to be carried out -- there was no need to inherit from a
 * Dialog object.
 * @author sruduke
 */
public class InputErrorFragment {

    /**
     * This variable represents the generated fragment ( {@link AlertDialog} ).
     */
    private AlertDialog fragment;

    /**
     * This is the constructor to create a InputErrorFragment object.
     * @param title The title of the fragment ( {@link String} ).
     * @param message The message to display in the fragment ( {@link String} ).
     * @param context The {@link Context} of the current activity so that the fragment will properly appear on the screen.
     */
    public InputErrorFragment( String title, String message, Context context ) {
        AlertDialog.Builder builder = new AlertDialog.Builder( context );
        builder.setTitle( title );
        builder.setMessage( message );
        builder.setPositiveButton( "OK", null );
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