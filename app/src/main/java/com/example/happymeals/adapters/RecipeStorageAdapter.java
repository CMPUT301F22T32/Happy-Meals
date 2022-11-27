package com.example.happymeals.adapters;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.happymeals.R;
import com.example.happymeals.database.DatabaseListener;
import com.example.happymeals.fragments.MealPlanItemsFragment;
import com.example.happymeals.fragments.ModifyConfirmationFragment;
import com.example.happymeals.ingredient.Ingredient;
import com.example.happymeals.recipe.Recipe;
import com.example.happymeals.recipe.RecipeDetailsActivity;
import com.example.happymeals.recipe.RecipeStorage;

import java.util.ArrayList;

/**
 * @author jeastgaa
 * @version 1.00.01
 * This allows the viewing of a list of {@link Recipe}s. The viewing will be defined through
 * the content_recipe resource layout.
 */
public class RecipeStorageAdapter extends ArrayAdapter<Recipe> {
    private ArrayList<Recipe> recipeStorageList;
    private Context context;
    private Recipe currentRecipe;

    private Boolean showScaleSlider = false;
    private ArrayList<Double> scaleAmounts = null;

    private SeekBarChangeListener listener;

    public interface SeekBarChangeListener {
        void changedValue(String recipeName, Double scale);
    }

    /**
     * Base constructor which will assign {@link Context} and the {@link ArrayList} which is being
     * viewed and adapted.
     * @param context The {@link Context} of the class which instantiates this adapter.
     * @param recipeStorageList The {@link ArrayList} which is being adapted and viewed.
     */
    public RecipeStorageAdapter(@NonNull Context context, ArrayList<Recipe> recipeStorageList, Boolean... showScaleSlider ) {
        super(context, 0 , recipeStorageList );
        this.context = context;
        this.recipeStorageList = recipeStorageList;
        this.currentRecipe = null;

        if (showScaleSlider.length > 0) {
            this.showScaleSlider = showScaleSlider[0];
        }
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItem = convertView;
        if (listItem == null)
            listItem = LayoutInflater.from(context).inflate(R.layout.content_recipe, parent, false);

        currentRecipe = recipeStorageList.get( position );

        TextView name = listItem.findViewById(R.id.ingredient_specific_list_name_field);
        TextView servings = listItem.findViewById( R.id.recipe_list_servings_field );
        TextView description = listItem.findViewById( R.id.recipe_list_description_field );
        TextView prepTime = listItem.findViewById( R.id.recipe_content_prep_time_field );
        TextView cookTime = listItem.findViewById( R.id.recipe_content_cook_time_field );
        TextView creatorName = listItem.findViewById( R.id.recipe_content_creator_field );

        name.setText( currentRecipe.getName() );
        servings.setText( String.valueOf( currentRecipe.getServings() ) );
        description.setText(
                currentRecipe.getDescription().equals("") ?
                        "No Description" : currentRecipe.getDescription() );
        prepTime.setText( "Prep Time: " + currentRecipe.getPrepTime() + " mins" );
        cookTime.setText( "Cook Time: " + currentRecipe.getCookTime() + " mins" );
        creatorName.setText( currentRecipe.getCreator() );

        if (showScaleSlider) {
            listItem.findViewById(R.id.recipe_scaler).setVisibility(View.VISIBLE);

            TextView amount = listItem.findViewById(R.id.recipe_scale_amount);
            SeekBar slider = listItem.findViewById(R.id.recipe_scale_slider);

            String defaultValue;

            if (scaleAmounts == null)
                defaultValue = "1.0";
            else {
                Double scale = scaleAmounts.get(position);
                defaultValue = Double.toString(scale);
                slider.setProgress((int) (scale * 2) - 1);
                servings.setText(String.valueOf(scale * currentRecipe.getServings()));
            }
            amount.setText(defaultValue);

            slider.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    Double scale = (Double) (i / 2.0) + 0.5;
                    amount.setText(String.valueOf(scale));
                    servings.setText( String.valueOf(scale * currentRecipe.getServings()) );
                    listener.changedValue(currentRecipe.getName(), scale);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                }
            });
        }
        else
            listItem.findViewById(R.id.recipe_scaler).setVisibility(View.GONE);

//        listItem.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent( context, RecipeDetailsActivity.class );
//                intent.putExtra("recipe", currentRecipe.getName() );
//                context.startActivity( intent );
//            }
//        });
//
//        listItem.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View view) {
//                ModifyConfirmationFragment deleteFragment = new ModifyConfirmationFragment(
//                        "Remove Recipe",
//                        String.format("Are you sure you want to remove %s?", currentRecipe.getName() ),
//                        context,
//                        getDeleteListener() );
//                deleteFragment.display();
//                return true;
//
//            }
//        });

        return listItem;
    }

    public void setScales(ArrayList<Double> scaleAmounts) {
        this.scaleAmounts = scaleAmounts;
    }

    public void setListener(SeekBarChangeListener listener) {
        this.listener = listener;
    }

    /**
     * This will listen for the removal of a recipe from the list. At this point
     * the {@link RecipeStorage} will be updated along with the database.
     * @return
     */
    private DialogInterface.OnClickListener getDeleteListener() {

        return new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                RecipeStorage.getInstance().removeRecipe( currentRecipe );
            }
        };
    }
}