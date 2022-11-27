package com.example.happymeals.userlogin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.happymeals.R;

/**
 * This class is a candidate to be the entry point of the application -- its implementation has yet
 * to be approved. It features a login screen and will authenticate users. Upon successful authentication,
 * users can then access all their persistent data.
 * @author sruduke
 */
public class LoginScreen extends AppCompatActivity {

    /**
     * This is the function called whenever the LoginScreen is created -- in our
     * case, this is on the launch of the app (if approved by client). If implemented,
     * upon successful user authentication, it will send an intent to open an instance of
     * the MainActivity. From there, users will be able to access all of their data -- ingredients,
     * recipes, meal plans, etc.
     * @param savedInstanceState The instance state to restore the activity to (if applicable) {@link Bundle}
     */
    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate( savedInstanceState );
        setContentView( R.layout.activity_login_screen );

        TextView forgetPassword = findViewById( R.id.forgot_password);
        Button login = findViewById( R.id.login_button );

        forgetPassword.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                //TODO: open new activity or fragment to deal with password reset
            }
        });

        login.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick( View view ) {
                // first check that username and password have been filled
                EditText username = findViewById( R.id.input_username);
                EditText password = findViewById( R.id.input_password );

                if ( username.getText().length() <= 0 || password.getText().length() <= 0 ) {
                    // TODO fragment pop-up saying fields are not filled
                } else {
                    // TODO: check entered account against database
                }
            }
        });



    }
}
