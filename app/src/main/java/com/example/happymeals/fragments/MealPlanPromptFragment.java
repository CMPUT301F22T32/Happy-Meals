package com.example.happymeals.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.happymeals.R;
import com.example.happymeals.mealplan.CreateMealPlanActivity;

import java.util.Date;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MealPlanPromptFragment} factory method to
 * create an instance of this fragment.
 */
public class MealPlanPromptFragment extends DialogFragment {

    private Context context;
    private Date dateExtra = null;

    public MealPlanPromptFragment() {
    }

    public MealPlanPromptFragment( Date date ) {
        this.dateExtra = date;
    }

    @Override
    public void onAttach( @NonNull Context context ) {
        super.onAttach( context );
        this.context = context;
    }

    @Override
    public Dialog onCreateDialog( Bundle savedInstanceState ) {
        // Inflate the layout for this fragment
        View view = LayoutInflater
                .from( getActivity() )
                .inflate( R.layout.meal_plan_prompt_fragment, null );

        Button autogen = view.findViewById( R.id.autogenerate_button );
        Button selfmade = view.findViewById( R.id.selfmade_button );

        Fragment fragment = this;

        autogen.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                Intent intent = new Intent( context, CreateMealPlanActivity.class );
                intent.putExtra( CreateMealPlanActivity.NEW_MEAPLAN_EXTRA, true );
                intent.putExtra( CreateMealPlanActivity.AUTOGEN_EXTRA, true );
                if ( dateExtra != null )
                    intent.putExtra( CreateMealPlanActivity.DATE_EXTRA, dateExtra.toInstant().getEpochSecond() );
                startActivity( intent );
                getActivity().getSupportFragmentManager().beginTransaction().remove( fragment ).commit();
            }
        } );

        selfmade.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                Intent intent = new Intent( context, CreateMealPlanActivity.class );
                intent.putExtra( CreateMealPlanActivity.NEW_MEAPLAN_EXTRA, true );
                intent.putExtra( CreateMealPlanActivity.AUTOGEN_EXTRA, false );
                if ( dateExtra != null )
                    intent.putExtra( CreateMealPlanActivity.DATE_EXTRA, dateExtra.toInstant().getEpochSecond() );
                startActivity( intent );
                getActivity().getSupportFragmentManager().beginTransaction().remove( fragment ).commit();
            }
        } );

        AlertDialog.Builder builder = new AlertDialog.Builder( getContext() );

        return builder.setView( view ).create();
    }

}