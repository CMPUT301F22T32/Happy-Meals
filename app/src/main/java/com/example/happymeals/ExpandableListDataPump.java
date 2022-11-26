package com.example.happymeals;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ExpandableListDataPump {
    public static HashMap<String, List<String>> getData() {
        HashMap<String, List<String>> expandableListDetail = new HashMap<String, List<String>>();

        List<String> Breakfast = new ArrayList<String>();
        Breakfast.add("India");
        Breakfast.add("Pakistan");
        Breakfast.add("Australia");
        Breakfast.add("England");
        Breakfast.add("South Africa");

        List<String> Lunch = new ArrayList<String>();
        Lunch.add("Brazil");
        Lunch.add("Spain");
        Lunch.add("Germany");
        Lunch.add("Netherlands");
        Lunch.add("Italy");

        List<String> Dinner = new ArrayList<String>();
        Dinner.add("United States");
        Dinner.add("Spain");
        Dinner.add("Argentina");
        Dinner.add("France");
        Dinner.add("Russia");

        expandableListDetail.put("Breakfast", Breakfast);
        expandableListDetail.put("Lunch", Lunch);
        expandableListDetail.put("Dinner", Dinner);
        return expandableListDetail;
    }
}