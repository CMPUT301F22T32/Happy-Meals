package com.example.happymeals.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.happymeals.R;
import com.example.happymeals.ingredient.Ingredient;

public class GetCountFragment extends DialogFragment {

    public interface GetCountFragmentListener{
        void onConfirmClick( double count, Ingredient ingredient );
    }

    private GetCountFragment.GetCountFragmentListener listener;
    private AlertDialog fragment;
    private Context context;
    private EditText getCountField;
    private Ingredient ingredientToHold;

    public GetCountFragment( Context context, GetCountFragmentListener listener, Ingredient ingredient ) {
        this.listener = listener;
        this.context = context;
        this.ingredientToHold = ingredient;
    }
    @Override
    public void onAttach( Context context ) {
        super.onAttach( context );
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstance ) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.select_count_fragment, null);

        getCountField = view.findViewById( R.id.select_count_fragment_content_field );

        AlertDialog.Builder builder = new AlertDialog.Builder( getActivity() );
        return builder
                .setView( view )
                .setTitle( "Set Amount of Ingredient Required:" )
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick( DialogInterface dialogInterface, int i ) {
                        listener.onConfirmClick( Double.parseDouble( getCountField.getText().toString() ), ingredientToHold );
                    }
                })
                .setNegativeButton( "Cancel", null )
                .create();
    }
}
