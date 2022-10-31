package com.example.happymeals.fragments;

import android.app.AlertDialog;
import android.content.Context;
import androidx.fragment.app.Fragment;

public class InputErrorFragment {

    private AlertDialog fragment;

    public InputErrorFragment(String title, String message, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton("OK", null);
        fragment = builder.create();
    }

    public void display() {
        fragment.show();
    }

}