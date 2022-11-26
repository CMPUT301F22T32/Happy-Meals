package com.example.happymeals.adapters;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.example.happymeals.R;
import com.example.happymeals.fragments.InputErrorFragment;
import com.example.happymeals.ingredient.Ingredient;
import com.example.happymeals.shoppinglist.ShoppingListItem;

import java.util.ArrayList;

/**
 * This class is used to display a list of ShoppingListItems in the Storage activity.
 * @author sruduke
 */
public class ShoppingListAdapter extends ArrayAdapter<Ingredient> {

    private ArrayList<ShoppingListItem> items;
    private ArrayList<Ingredient> selected;
    private Context context;

    /**
     * This initializes the ShoppingListAdapter
     * @param context {@link Context}
     * @param items {@link ArrayList<ShoppingListItem>}
     * @param ingredients {@link ArrayList<Ingredient>}
     */
    public ShoppingListAdapter(@NonNull Context context, ArrayList<ShoppingListItem> items, ArrayList<Ingredient> ingredients) {
        super(context, 0, ingredients);
        this.context = context;
        this.items = items;
        selected = new ArrayList<>();
    }

    /**
     * {@inheritDoc}
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.activity_shopping_list_adapter, parent, false);
        }

        ShoppingListItem item = items.get(position);
        Ingredient ingredient = item.getIngredient();
        Double amount = item.getAmount();
        boolean pickedUp = ingredient.getNeedsUpdate();
        ArrayList<String> recipeNames = item.getRecipes();

        Button showRecipes = view.findViewById(R.id.show_list_recipe_button);

        TextView title = view.findViewById(R.id.show_list_item_title);
        TextView storedAmountView = view.findViewById(R.id.shop_list_storage_amount_value);
        TextView neededAmountView = view.findViewById(R.id.shop_list_buy_amount_value);
        TextView unitView1 = view.findViewById(R.id.shop_list_unit_value1);
        TextView unitView2 = view.findViewById(R.id.shop_list_unit_value2);
        TextView categoryView = view.findViewById(R.id.shop_list_category_value);

        CheckBox checkBox = view.findViewById(R.id.shopping_list_checkbox);

        title.setText(ingredient.getName());
        categoryView.setText(ingredient.getCategory().toString());

        if (recipeNames == null)
            showRecipes.setVisibility(View.GONE);

        if (pickedUp) {
            checkBox.setChecked(true);
            checkBox.setEnabled(false);

            String message = "Storage information needs to be updated.";
            TextView amountStoredHeader = view.findViewById(R.id.shopping_list_storage_title);
            amountStoredHeader.setText(message);

            TextView amountNeededHeader = view.findViewById(R.id.shopping_list_needed_title);
            amountNeededHeader.setVisibility(View.GONE);

            ConstraintLayout background = view.findViewById(R.id.shopping_list_adapter_view);
            int c = context.getResources().getColor(R.color.black_overlay, null);
            background.setBackgroundColor(c);

            checkBox.setPadding(0,50,0,0);
            checkBox.setScaleX(2);
            checkBox.setScaleY(2);
            checkBox.setY(120);

            int d = context.getResources().getColor(R.color.blue, null);
            checkBox.setButtonTintList(ColorStateList.valueOf(d));
        }
        else {
            storedAmountView.setText(ingredient.getAmount().toString());
            unitView1.setText(ingredient.getUnit().toString());

            neededAmountView.setText(amount.toString());
            unitView2.setText(ingredient.getUnit().toString());
        }

        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBox.isChecked())
                    selected.add(ingredient);
                else
                    selected.remove(ingredient);
            }
        });

        showRecipes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StringBuilder sb = new StringBuilder("This ingredient is included in the following recipes:\n");
                for (String s : recipeNames) {
                    sb.append(String.format(" - %s\n", s));
                }
                new InputErrorFragment("Included Recipes", sb.toString(), context).display();
            }
        });

        return view;
    }

    /**
     * Returns the selected Ingredients
     * @return selected {@link ArrayList<Ingredient>}
     */
    public ArrayList<Ingredient> getSelected() {
        return selected;
    }

    /**
     * Returns true if the selected size is greater than 0 and false if the selected size is less
     * than or equal to 0
     * @return {@link boolean}
     */
    public boolean getChanged() {
        return selected.size() > 0;
    }
}