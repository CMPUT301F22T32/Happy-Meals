package com.example.happymeals.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.happymeals.R;
import com.example.happymeals.ingredient.Ingredient;

/**
 * @author jeastgaa
 * @version 1.00.01
 * This fragment class gets a {@link Double} value from the user, returning it to the listening
 * class which implements this nested {@link GetCountFragmentListener} interface.
 */
public class GetCountFragment extends DialogFragment {

    /**
     * @author jeastgaa
     * @version 1.00.01
     * This interface allows classes which call this fragment to receive the inputted double value.
     * This will also pass the required ingredient that was being interacted with back to the
     * listening class.
     */
    public interface GetCountFragmentListener{
        /**
         * Contains the count and {@link Ingredient} which will be passed back to the calling
         * class.
         * @param count The {@link Double} which holds the count inputted by the user.
         * @param ingredient The {@link Ingredient} which the count is attatched to.
         */
        void onConfirmClick( double count, Ingredient ingredient );
    }

    private GetCountFragment.GetCountFragmentListener listener;
    private EditText getCountField;
    private Ingredient ingredientToHold;

    /**
     * Base constructor requiring a {@link GetCountFragmentListener} and {@link Ingredient} to be
     * passed in order to return this information on the fragment confirmation.
     * @param listener The {@link GetCountFragmentListener} which the confirmation method will be
     *                 called to.
     * @param ingredient The {@link Ingredient} which will be returned to the given listener.
     */
    public GetCountFragment( GetCountFragmentListener listener, Ingredient ingredient ) {
        this.listener = listener;
        this.ingredientToHold = ingredient;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void onAttach( Context context ) {
        super.onAttach( context );
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public Dialog onCreateDialog( @Nullable Bundle savedInstance ) {
        View view = LayoutInflater.from( getActivity() ).inflate( R.layout.select_count_fragment, null );

        getCountField = view.findViewById( R.id.select_count_fragment_content_field );

        AlertDialog.Builder builder = new AlertDialog.Builder( getActivity() );
        return builder
                .setView( view )
                .setTitle( "Set Amount of Ingredient Required:" )
                .setPositiveButton( "Confirm", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick( DialogInterface dialogInterface, int i ) {
                        String givenCount = getCountField.getText().toString();
                        if( !givenCount.equals( "" ) ) {
                            try{
                                listener.onConfirmClick( Double.parseDouble( 
                                        getCountField.getText().toString() ), ingredientToHold );
                            } catch ( Exception e ) {
                                Log.e( "Get Count", e.toString() );
                            }
                        }
                    }
                } )
                .setNegativeButton( "Cancel", null )
                .create();
    }
}