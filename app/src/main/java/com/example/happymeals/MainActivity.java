package com.example.happymeals;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayAdapter<String> adapter = new MainActivityListAdapter(this, 0);
        ListView options = findViewById(R.id.option_list);
        options.setAdapter(adapter);

        // testing login screen
        //Intent intent = new Intent(this, LoginScreen.class);
        //startActivity(intent);

    }
}