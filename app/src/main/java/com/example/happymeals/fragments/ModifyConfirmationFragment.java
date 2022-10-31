package com.example.happymeals.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.happymeals.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SaveFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModifyConfirmationFragment extends DialogFragment {

    private ModifyFragmentInteractionListener listener;

    public interface ModifyFragmentInteractionListener {
        void onConfirmPressed(FragmentAction action);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        String title = "";
        String message = "";
        FragmentAction action = null;

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        //View view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_save, null);

        Bundle inputBundle = getArguments();
        if (inputBundle != null) {
            title = inputBundle.getString("title");
            message = inputBundle.getString("message");
            action = (FragmentAction) inputBundle.getSerializable("action");
        }

        FragmentAction finalAction = action;
        return builder
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    listener.onConfirmPressed(finalAction);
                    }
                })
                .setNegativeButton("Cancel", null)
                .create();
    }

    public static ModifyConfirmationFragment newInstance(String title, String message, FragmentAction action) {
        // method necessary to provide food information to the fragment
        // index represents the index of the current food in the ArrayList (managed by EditActivity)
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("message", message);
        args.putSerializable("action", action);

        ModifyConfirmationFragment fragment = new ModifyConfirmationFragment();
        fragment.setArguments(args);
        return fragment;
    }
}