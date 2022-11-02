package com.example.happymeals;

/**
 * This is a class defines an Ingredient
 */
public class Ingredient extends DatabaseObject{

    private String name;
    private String description;
    private String bestBeforeDate;
    private Constants.Location location;
    private int amount;
    private Constants.AmountUnit unit;
    private Constants.IngredientCategory category;

    public Ingredient() {

    }

    public Ingredient( String name, String description, String bestBeforeDate,
                       Constants.Location location, Integer amount, Constants.AmountUnit unit,
                       Constants.IngredientCategory category ) {
        this.name = name;
        this.description = description;
        this.bestBeforeDate = bestBeforeDate;
        this.location = location;
        this.amount = amount;
        this.unit = unit;
        this.category = category;
    }

    /**
     * This returns the name of the Ingredient
     * @return description
     */
    public String getName() { return name; }

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
    public String getBestBeforeDate() {
        return bestBeforeDate;
    }

    /**
     * This returns the enum Location of the Ingredient
     * @return location
     */
    public Constants.Location getLocation() {
        return location;
    }

    /**
     * This returns the number amount of the Ingredient
     * @return amount
     */
    public Integer getAmount() {
        return amount;
    }

    /**
     * This returns the units of the amount of the Ingredient
     * @return unit
     */
    public Constants.AmountUnit getUnit() {
        return unit;
    }

    /**
     * This returns the enum IngredientCategory of the Ingredient
     * @return category
     */
    public Constants.IngredientCategory getCategory() {
        return category;
    }

    /**
     * This sets the name of the Ingredient
     * @param name {@link String}
     * This is the description of the Ingredient
     */
    public void setName( String name ) { this.name = name; }

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
    public void setBestBeforeDate( String bestBeforeDate ) {
        this.bestBeforeDate = bestBeforeDate;
    }

    /**
     * This sets the location of the Ingredient
     * @param location {@link Constants.Location}
     * This is the location of the Ingredient
     */
    public void setLocation( Constants.Location location ) {
        this.location = location;
    }

    /**
     * This sets the number amount of the Ingredient
     * @param amount {@link Integer}
     * This is the number amount of the Ingredient
     */
    public void setAmount( int amount ) {
        this.amount = amount;
    }

    /**
     * This sets the enum AmountUnit for the amount of the Ingredient
     * @param unit {@link Constants.AmountUnit}
     * This is the unit for the amount for the Ingredient
     */
    public void setUnit( Constants.AmountUnit unit ) {
        this.unit = unit;
    }

    /**
     * This sets the category for the Ingredient
     * @param category {@link Constants.IngredientCategory}
     * This is the category for the Ingredient
     */
    public void setCategory( Constants.IngredientCategory category ) {
        this.category = category;
    }
}
