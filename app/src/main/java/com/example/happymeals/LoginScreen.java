package com.example.happymeals;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class LoginScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        TextView forgetPassword = findViewById(R.id.forgot_password);
        Button login = findViewById(R.id.login_button);

        forgetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open new activity or fragment to deal with password reset
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // first check that username and password have been filled
                EditText username = findViewById(R.id.input_username);
                EditText password = findViewById(R.id.input_password);

                if (username.getText().length() <= 0 || password.getText().length() <= 0) {
                    // fragment pop-up saying fields are not filled
                } else {
                    // then check entered account against database
                }
            }
        });



    }
}
