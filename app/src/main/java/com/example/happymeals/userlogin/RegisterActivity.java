package com.example.happymeals.userlogin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.happymeals.MainActivity;
import com.example.happymeals.R;
import com.example.happymeals.database.FireStoreManager;
import com.example.happymeals.database.FirebaseAuthenticationHandler;
import com.example.happymeals.recipe.PublicRecipeActivity;

public class RegisterActivity extends AppCompatActivity{
    private Button registerBtn;
    private TextView returnLogin;
    private EditText emailField, passwordField, confirmPasswordField, usernameField;
    private FirebaseAuthenticationHandler fireAuth;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_register_screen );

        // initialize all used objects

        registerBtn = findViewById( R.id.register_button );

        emailField = findViewById( R.id.register_email_field );
        passwordField = findViewById( R.id.register_password_field );
        confirmPasswordField = findViewById( R.id.register_confirm_pass_field );
        usernameField = findViewById( R.id.register_username_field );
        returnLogin = findViewById( R.id.return_login );


        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userEmail = emailField.getText().toString();
                String newPass = passwordField.getText().toString();
                String userName = usernameField.getText().toString();

                if( !checkValidEntries() ){
                    return;
                }
                fireAuth.getFireAuth().userRegister(userEmail, newPass, userName, new OutputListener() {
                    @Override
                    public void onSuccess() {
                        Log.d("RegisterActivity", "User was created.");
                        fireAuth.getFireAuth().userLogin(userEmail, newPass, new OutputListener() {
                            @Override
                            public void onSuccess() {
                                Log.d("RegisterActivity", "Login from registration success.");
                            }

                            @Override
                            public void onFailure(Exception e) {
                                Log.d("RegisterActivity", "Login from registration failure.");
                            }
                        });
                        FireStoreManager.getInstance().setUser( userEmail );
                        FireStoreManager.getInstance().addDefaultSpinners();
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
                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    // close this activity
                    finish();
                }
            });

        }

    /**
     * Checks to see if all the registration information is valid.
     * @return The {@link Boolean} True if the information is valid, false otherwise.
     */
    private boolean checkValidEntries() {
            String userEmail = emailField.getText().toString();
            String newPass = passwordField.getText().toString();
            String userName = usernameField.getText().toString();

            if ( userName.length() < 1 || userEmail.length() < 1 ) {
                emailField.setError( "Username and Email cannot be empty" );
                return false;
            } else if ( !userEmail.contains("@") ) {
                emailField.setError( "Must be a valid Email" );
                return false;
            }
            if ( newPass.length() < 8 ) {
                passwordField.setError( "Password must be greater than 8 characters" );
                return false;
            } else if ( !confirmPasswordField.getText().toString().equals(newPass) ) {
                confirmPasswordField.setError( "Passwords must match" );
                return false;
            }
            if( userName.contains("_") ) {
                usernameField.setError( "Contains Illegal Character '_'" );
                return false;
            }
            return true;
        }
    }
