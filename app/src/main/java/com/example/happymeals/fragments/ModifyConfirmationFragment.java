package com.example.happymeals.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class ModifyConfirmationFragment {

    private AlertDialog fragment;

    public ModifyConfirmationFragment(String title, String message, Context context, DialogInterface.OnClickListener actionListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton("Confirm", actionListener)
                .setNegativeButton("Cancel", null);
        fragment = builder.create();
    }

    public void display() {
        fragment.show();
    }
}