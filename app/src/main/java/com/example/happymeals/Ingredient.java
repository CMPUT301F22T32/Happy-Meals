package com.example.happymeals;

public class Ingredient {
    private String description;
    private String bestBeforeDate;
    private String location;
    private int amount;
    private String unit;
    private String category;

    public Ingredient(String description, String bestBeforeDate, String location, int amount, String unit, String category) {
        this.description = description;
        this.bestBeforeDate = bestBeforeDate;
        this.location = location;
        this.amount = amount;
        this.unit = unit;
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public String getBestBeforeDate() {
        return bestBeforeDate;
    }

    public String getLocation() {
        return location;
    }

    public int getAmount() {
        return amount;
    }

    public String getUnit() {
        return unit;
    }

    public String getCategory() {
        return category;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
