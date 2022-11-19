package com.example.happymeals;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.happymeals.database.FireAuth;

public class Register extends AppCompatActivity{
    private Button register;
    private TextView returnLogin;
    private EditText user, password;
    private FireAuth fireAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);

        // initialize all used objects

        register = findViewById(R.id.register_button);
        user = findViewById(R.id.create_username);
        password = findViewById(R.id.create_password);
        returnLogin = findViewById(R.id.return_login);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newUser = user.getText().toString();
                String newPass = password.getText().toString();
                //1 empty user passed

                if (TextUtils.isEmpty(newUser)) {
                    user.setError("User cannot be empty");
                }

                //2 empty password passed

                if (TextUtils.isEmpty(newPass)) {
                    password.setError("Password cannot be empty");
                }
                //3 Password length cannot be less than 8 chars

                if (newPass.length() < 8) {
                    password.setError("Password must be greater than 8 characters");
                }
                fireAuth.getFireauth().userLogin(newUser, newPass, new OutputListener() {
                    @Override
                    public void onSuccess() {
                        startActivity(new Intent(Register.this, MainActivity.class));
                    }

                    @Override
                    public void onFailure(Exception e) {
                        return;}
                    });

                }
            });
            returnLogin.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(Register.this, Login.class));
                }
            });

        }







    }
