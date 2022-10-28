package com.example.happymeals;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import java.util.Date;

public class MainActivity extends AppCompatActivity implements DatabaseListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FireStoreManager fm = new FireStoreManager();

        testClass tc = new testClass("Apple", 23, new Date() );

        fm.addData("Ingredients", tc.getName(), tc);
        fm.getData( "Ingredients", tc.getName(), this, new testClass());
        fm.getData( fm.getReferenceTo("Ingredients", tc.getName()), this, new testClass());
    }

    @Override
    public void onDataFetchSuccess(DatabaseObject data) {
        System.out.println( data.getClass() );
    }
}