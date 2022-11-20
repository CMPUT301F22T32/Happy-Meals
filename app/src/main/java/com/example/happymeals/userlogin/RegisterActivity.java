package com.example.happymeals.userlogin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.happymeals.R;
import com.example.happymeals.database.FirebaseAuthenticationHandler;

public class RegisterActivity extends AppCompatActivity{
    private Button registerBtn;
    private TextView returnLogin;
    private EditText userinputField, passinputField;
    private FirebaseAuthenticationHandler fireAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_screen);

        // initialize all used objects

        registerBtn = findViewById(R.id.register_button);
        userinputField = findViewById(R.id.create_username);
        passinputField = findViewById(R.id.create_password);
        returnLogin = findViewById(R.id.return_login);


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newUser = userinputField.getText().toString();
                String newPass = passinputField.getText().toString();
                //1 empty user passed

                if (TextUtils.isEmpty(newUser)) {
                    userinputField.setError("User cannot be empty");
                }

                //2 empty password passed

                if (TextUtils.isEmpty(newPass)) {
                    passinputField.setError("Password cannot be empty");
                }
                //3 Password length cannot be less than 8 chars

                if (newPass.length() < 8) {
                    passinputField.setError("Password must be greater than 8 characters");
                }
                fireAuth.getFireAuth().userRegister(newUser, newPass, new OutputListener() {
                    @Override
                    public void onSuccess() {
                        Log.d("RegisterActivity", "User was created.");
                        finish();
                    }

                    @Override
                    public void onFailure(Exception e) {
                        Log.d("RegisterActivity", "User could not be created." );
                        }
                    });

                }
            });
            returnLogin.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {

                    // close this activity
                    finish();
                }
            });

        }







    }
