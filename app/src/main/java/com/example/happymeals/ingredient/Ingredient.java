package com.example.happymeals.ingredient;

import com.example.happymeals.Constants;
import com.example.happymeals.database.DatabaseObject;

import java.io.Serializable;
import java.util.Date;

/**
 * This is a class defines an Ingredient
 * @author kstark jeastgaa sruduke
 */

public class Ingredient extends DatabaseObject {

    private String description;
    private Date bestBeforeDate;
    private String location;
    private Double amount;
    private String unit;
    private String category;
    private Boolean needsUpdate = false;

    /**
     * This is an empty constructor needed for Firestore construction.
     */
    public Ingredient() {

    }

    /**
     * This is the loaded constructor for an {@link Ingredient} object. It contains all of the
     * necessary parameters for instantiation.
     * @param name The name of the ingredient ({@link String}).
     * @param description An optional description of the ingredient ({@link String}).
     * @param bestBeforeDate The expiry date in YYYY-MM-DD ISO Format ({@link Date}).
     * @param location The location where the ingredient is stored ({@link Constants.Location}).
     * @param amount The positive floating point representation (Double) amount of the ingredient ({@link Double}.
     * @param unit The unit the ingredient is stored by ({@link Constants.AmountUnit}).
     * @param category The food category that the ingredient falls under ({@link Constants.IngredientCategory}).
     */
    public Ingredient( String name, String description, Date bestBeforeDate,
                       String location, Double amount, String unit,
                       String category ) {
        this.name = name;
        this.description = description;
        this.bestBeforeDate = bestBeforeDate;
        this.location = location;
        this.amount = amount;
        this.unit = unit;
        this.category = category;
    }

    /**
     * This returns the description of the Ingredient
     * @return description
     */
    public String getDescription() {
        return description;
    }

    /**
     * This returns the Best Before Date of the Ingredient
     * @return bestBeforeDate
     */
    public Date getBestBeforeDate() {
        return bestBeforeDate;
    }

    public String getBestBeforeDateAsString() {
        int year = bestBeforeDate.getYear();
        int month = bestBeforeDate.getMonth();
        int day = bestBeforeDate.getDay();
        return String.format("%04d-%02d-%02d", year, month, day);
    }

    /**
     * This returns the enum Location of the Ingredient
     * @return location
     */
    public String getLocation() {
        return location;
    }

    /**
     * This returns the number amount of the Ingredient
     * @return amount
     */
    public Double getAmount() {
        return amount;
    }

    /**
     * This returns the units of the amount of the Ingredient
     * @return unit
     */
    public String getUnit() {
        return unit;
    }

    /**
     * This returns the enum IngredientCategory of the Ingredient
     * @return category
     */
    public String getCategory() {
        return category;
    }

    public Boolean getNeedsUpdate() {
        return needsUpdate;
    }

    /**
     * This sets the name of the Ingredient
     * @param name {@link String}
     * This is the name of the Ingredient
     */
    public void setName( String name ) {
        this.name = name;
    }

    /**
     * This sets the description of the Ingredient
     * @param description {@link String}
     * This is the description of the Ingredient
     */
    public void setDescription( String description ) {
        this.description = description;
    }

    /**
     * This sets the bestBeforeDate of the Ingredient
     * @param bestBeforeDate {@link String}
     * This is the best before date of the Ingredient
     */
    public void setBestBeforeDate( Date bestBeforeDate ) {
        this.bestBeforeDate = bestBeforeDate;
    }

    /**
     * This sets the location of the Ingredient
     * @param location {@link Constants.Location}
     * This is the location of the Ingredient
     */
    public void setLocation( String location ) {
        this.location = location;
    }

    /**
     * This sets the number amount of the Ingredient
     * @param amount {@link Integer}
     * This is the number amount of the Ingredient
     */
    public void setAmount( Double amount ) {
        this.amount = amount;
    }

    /**
     * This sets the enum AmountUnit for the amount of the Ingredient
     * @param unit {@link Constants.AmountUnit}
     * This is the unit for the amount for the Ingredient
     */
    public void setUnit( String unit ) {
        this.unit = unit;
    }

    /**
     * This sets the category for the Ingredient
     * @param category {@link Constants.IngredientCategory}
     * This is the category for the Ingredient
     */
    public void setCategory( String category ) {
        this.category = category;
    }

    public void setNeedsUpdate(Boolean update) {
        this.needsUpdate = update;
    }
}
