package com.example.happymeals.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.renderscript.ScriptGroup;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.happymeals.R;
import com.example.happymeals.ingredient.Ingredient;

import java.util.ArrayList;

/**
 * @author jeastgaa
 * @version 1.00.01
 * This fragment class will get a string from the user and return it to the
 * {@link InputStringFragmentListener} that was passed in the launch of the fragment.
 */
public class InputStringFragment extends DialogFragment {

    /**
     * @author jeastgaa
     * @version 1.00.01
     * Listener interface to be implemented by any class that wants to call this fragment to
     * receive the string input.
     */
    public interface InputStringFragmentListener{
        /**
         * This method will hold the {@link String} value that was inputted by the fragment.
         * @param str The passed {@link String}
         */
        void onConfirmClick( String str );
    }

    private EditText inputTextView;
    private InputStringFragment.InputStringFragmentListener listener;

    private int maxCharLength;

    private String message;

    public InputStringFragment( String message, int maxCharLength ) {
        super();
        this.maxCharLength = maxCharLength;
        this.message = message;
    }
    /**
     * {@inheritDoc}
     */
    @Override
    public void onAttach( Context context ) {
        super.onAttach( context );
        if ( context instanceof InputStringFragment.InputStringFragmentListener) {
            listener = (InputStringFragmentListener) context;
        } else {
            throw new RuntimeException( context.toString() + " must implement listener.");
        }
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstance ) {
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.input_string_fragment, null);

        inputTextView = view.findViewById( R.id.edit_text_empty_field );
        inputTextView.setFilters(new InputFilter[] {new InputFilter.LengthFilter(maxCharLength)});

        AlertDialog.Builder builder = new AlertDialog.Builder( getActivity() );
        return builder
                .setView( view )
                .setTitle( message )
                .setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick( DialogInterface dialogInterface, int i ) {
                        if( !inputTextView.getText().toString().equals("") ){
                            listener.onConfirmClick( inputTextView.getText().toString() );
                        }
                    }
                })
                .setNegativeButton( "Cancel", null )
                .create();
    }
}