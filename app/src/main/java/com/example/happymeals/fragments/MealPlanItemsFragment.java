package com.example.happymeals.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.example.happymeals.Constants;
import com.example.happymeals.R;
import com.example.happymeals.adapters.IngredientStorageArrayAdapter;
import com.example.happymeals.adapters.RecipeStorageAdapter;
import com.example.happymeals.ingredient.Ingredient;
import com.example.happymeals.ingredient.IngredientStorage;
import com.example.happymeals.recipe.Recipe;
import com.example.happymeals.recipe.RecipeStorage;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MealPlanItemsFragment} factory method to
 * create an instance of this fragment.
 */
public class MealPlanItemsFragment extends DialogFragment {

    private boolean showIngredients;

    private IngredientStorage ingredientStorage;
    private RecipeStorage recipeStorage;

    private Context context;

    private ListView list;
    private RadioGroup radioGroup;
    private Button save;
    private Button cancel;

    String type = null;
    ArrayList<String> selectedNames = null;

    private HashSet<Integer> selected;

    private OnFragmentInteractionListener listener;

    public interface OnFragmentInteractionListener {
        void selectionIngredients(ArrayList<Ingredient> ingredients);
        void selectionRecipes(ArrayList<Recipe> recipes);
    }

    public MealPlanItemsFragment() {
        // Required empty public constructor
    }

    public static MealPlanItemsFragment newInstance(String type, ArrayList<String> selectedIndices) {
        Bundle args = new Bundle();
        args.putStringArrayList("selected", selectedIndices);
        args.putString("type", type);
        MealPlanItemsFragment fragment = new MealPlanItemsFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
        listener = (OnFragmentInteractionListener) context;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = LayoutInflater
                .from(getActivity())
                .inflate(R.layout.activity_select_meals, null);

        selected = new HashSet<>();

        ingredientStorage = IngredientStorage.getInstance();
        recipeStorage = RecipeStorage.getInstance();

        RadioButton ingredientRadio = view.findViewById(R.id.ingredient_radio);
        RadioButton recipeRadio = view.findViewById(R.id.recipe_radio);
        radioGroup = view.findViewById(R.id.mp_type_of_item);
        list = view.findViewById(R.id.meal_plan_item_list);
        save = view.findViewById(R.id.mp_items_save);
        cancel = view.findViewById(R.id.mp_items_cancel);

        int checked = radioGroup.getCheckedRadioButtonId();
        int ingredientID = ingredientRadio.getId();
        int recipeID = recipeRadio.getId();

        showIngredients = (ingredientID == checked);

        Bundle inputBundle = getArguments();
        if (inputBundle != null) {
            type = inputBundle.getString("type");
            selectedNames = inputBundle.getStringArrayList("selected");
            if (type.equals(Constants.COLLECTION_NAME.RECIPES.toString())) {
                showIngredients = false;
                radioGroup.check(recipeID);
            }
        }

        setListeners();
        switchList();

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        return builder.setView(view).create();
    }

    private void setListeners() {
        Fragment fragment = this;

        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                for (Integer index : selected) {
                    if (index == i) {
                        view.setBackgroundColor(Color.WHITE);
                        selected.remove(i);
                        return;
                    }
                }
                view.setBackgroundColor(Color.LTGRAY);
                selected.add(i);
            }
        });

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                showIngredients = !showIngredients;
                switchList();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (showIngredients) {
                    ArrayList<Ingredient> items = new ArrayList<>();
                    for (Integer index : selected) {
                        items.add(ingredientStorage.getIngredientByIndex(index));
                    }
                    listener.selectionIngredients(items);
                }
                else {
                    ArrayList<Recipe> items = new ArrayList<>();
                    for (Integer index : selected) {
                        items.add(recipeStorage.getRecipeByIndex(index));
                    }
                    listener.selectionRecipes(items);
                }
                getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().getSupportFragmentManager().beginTransaction().remove(fragment).commit();
            }
        });
    }

    private void switchList() {
        selected.clear();
        if (showIngredients) {
            IngredientStorageArrayAdapter adapter = new IngredientStorageArrayAdapter( context, ingredientStorage.getIngredients() ) ;
            list.setAdapter( adapter ) ;
            if (selectedNames != null) {
                for (int i = 0; i < adapter.getCount(); i++) {
                    if (selectedNames.contains(adapter.getItem(i).getName())) {
                        adapter.getView(i, null, list).setBackgroundColor(Color.LTGRAY);
                        selected.add(i);
                    }
                }
            }
        }
        else {
            RecipeStorageAdapter adapter = new RecipeStorageAdapter( context, recipeStorage.getRecipes() );
            list.setAdapter( adapter );
            if (selectedNames != null) {
                for (int i = 0; i < adapter.getCount(); i++) {
                    if (selectedNames.contains(adapter.getItem(i).getName())) {
                        adapter.getView(i, null, list).setBackgroundColor(Color.LTGRAY);
                        selected.add(i);
                        //adapter.notifyDataSetChanged();
                    }
                }
            }
        }
    }
}