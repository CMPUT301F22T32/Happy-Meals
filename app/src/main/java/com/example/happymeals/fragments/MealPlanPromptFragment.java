package com.example.happymeals.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.happymeals.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MealPlanPromptFragment} factory method to
 * create an instance of this fragment.
 */
public class MealPlanPromptFragment extends DialogFragment {

    private OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener {
        void autoGeneratePressed();
        void makeSelfPressed();
    }

    public MealPlanPromptFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        listener = (OnFragmentInteractionListener) context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = LayoutInflater
                .from(getActivity())
                .inflate(R.layout.meal_plan_prompt_fragment, null);

        Button autogen = view.findViewById(R.id.autogenerate_button);
        Button selfmade = view.findViewById(R.id.selfmade_button);

        autogen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.makeSelfPressed();
            }
        });

        selfmade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.autoGeneratePressed();
            }
        });

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        return builder.setView(view).create();
    }
}