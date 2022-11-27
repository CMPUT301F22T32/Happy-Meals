package com.example.happymeals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataPump {
    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        List<String> Breakfast = new ArrayList<String>();
            Breakfast.add("Placeholder");

        List<String> Lunch = new ArrayList<String>();
            Lunch.add("Placeholder");

        List<String> Dinner = new ArrayList<String>();
            Dinner.add("Placeholder");

        expandableListDetail.put("Breakfast", Breakfast);
        expandableListDetail.put("Lunch", Lunch);
        expandableListDetail.put("Dinner", Dinner);
        return expandableListDetail;
    }
}