package com.example.happymeals.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.happymeals.R;
import com.example.happymeals.ingredient.Ingredient;

import java.util.ArrayList;

public class InputStringFragment extends DialogFragment {

    public interface InputStringFragmentListener{
        void onConfirmClick( String str );
    }

    private EditText inputTextView;
    private InputStringFragment.InputStringFragmentListener listener;


    @Override
    public void onAttach( Context context ) {
        super.onAttach( context );
        if ( context instanceof InputStringFragment.InputStringFragmentListener) {
            listener = (InputStringFragmentListener) context;
        } else {
            throw new RuntimeException( context.toString() + " must implement listener.");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstance ) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.input_string_fragment, null);

        inputTextView = view.findViewById( R.id.edit_text_empty_field );

        AlertDialog.Builder builder = new AlertDialog.Builder( getActivity() );
        return builder
                .setView( view )
                .setTitle( "Comment To Add To Recipe" )
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick( DialogInterface dialogInterface, int i ) {
                        listener.onConfirmClick( inputTextView.getText().toString() );
                    }
                })
                .setNegativeButton( "Cancel", null )
                .create();
    }
}
