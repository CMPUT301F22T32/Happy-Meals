package com.example.happymeals.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.happymeals.R;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
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