package com.example.happymeals;

/**
 * This is a class defines an Ingredient
 */
public class Ingredient {
    private String description;
    private String bestBeforeDate;
    private Constant.Location location;
    private int amount;
    private Constant.AmountUnit unit;
    private Constant.IngredientCategory category;

    public Ingredient(String description, String bestBeforeDate, Constant.Location location, Integer amount, Constant.AmountUnit unit, Constant.IngredientCategory category) {
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
    public String getBestBeforeDate() {
        return bestBeforeDate;
    }

    /**
     * This returns the enum Location of the Ingredient
     * @return location
     */
    public Constant.Location getLocation() {
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
    public Constant.AmountUnit getUnit() {
        return unit;
    }

    /**
     * This returns the enum IngredientCategory of the Ingredient
     * @return category
     */
    public Constant.IngredientCategory getCategory() {
        return category;
    }

    /**
     * This sets the description of the Ingredient
     * @param description {@link String}
     * This is the description of the Ingredient
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * This sets the bestBeforeDate of the Ingredient
     * @param bestBeforeDate {@link String}
     * This is the best before date of the Ingredient
     */
    public void setBestBeforeDate(String bestBeforeDate) {
        this.bestBeforeDate = bestBeforeDate;
    }

    /**
     * This sets the location of the Ingredient
     * @param location {@link Constant.Location}
     * This is the location of the Ingredient
     */
    public void setLocation(Constant.Location location) {
        this.location = location;
    }

    /**
     * This sets the number amount of the Ingredient
     * @param amount {@link Integer}
     * This is the number amount of the Ingredient
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }

    /**
     * This sets the enum AmountUnit for the amount of the Ingredient
     * @param unit {@link Constant.AmountUnit}
     * This is the unit for the amount for the Ingredient
     */
    public void setUnit(Constant.AmountUnit unit) {
        this.unit = unit;
    }

    /**
     * This sets the category for the Ingredient
     * @param category {@link Constant.IngredientCategory}
     * This is the category for the Ingredient
     */
    public void setCategory(Constant.IngredientCategory category) {
        this.category = category;
    }
}
