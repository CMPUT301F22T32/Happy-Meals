package com.example.happymeals.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.happymeals.Constants;
import com.example.happymeals.R;
import com.example.happymeals.SpinnerSettingsActivity;
import com.example.happymeals.fragments.ModifyConfirmationFragment;
import java.util.ArrayList;

/**
 * @author jeastgaa
 * @version 1.00.01
 * This allows the viewing of a simple list of strings which hold the values for spinners.
 */
public class SpinnerViewAdapter extends ArrayAdapter<String> {
    private Context context;
    private Constants.StoredSpinnerChoices spinnerCat;
    private ArrayList<String> spinnerList;

    /**
     * Constructor which requires {@link Context} and an {@link ArrayList} to keep track of.
     * This will simply show the string value for each {@link String} in the list.
     * @param context The {@link Context} which will instantiate this adapter.
     * @param spinnerList The {@link ArrayList} which is being viewed through this adapter.
     */
    public SpinnerViewAdapter(@NonNull Context context, ArrayList<String> spinnerList,
                              Constants.StoredSpinnerChoices spinnerCat ) {
        super(context, 0 , spinnerList );
        this.spinnerCat = spinnerCat;
        this.spinnerList = spinnerList;
        this.context = context;
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.content_string, parent, false);

        TextView amountValue = listItem.findViewById(R.id.string_content_field);

        amountValue.setText( spinnerList.get( position ) );

        listItem.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ModifyConfirmationFragment deleteFragment = new ModifyConfirmationFragment(
                        "Remove Spinner",
                        String.format("Are you sure you want to remove\n\"%s\" as a spinner?",
                                amountValue.getText().toString() ),
                        context,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                ( (SpinnerSettingsActivity) context).removeSpinnerFromList(
                                        spinnerCat, position );
                            }
                        } );
                deleteFragment.display();
                return true;

            }
        });

        return listItem;
    }

    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
    }
}
