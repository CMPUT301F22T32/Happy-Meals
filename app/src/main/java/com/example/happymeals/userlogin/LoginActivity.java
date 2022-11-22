package com.example.happymeals.userlogin;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.happymeals.MainActivity;
import com.example.happymeals.R;
import com.example.happymeals.database.FireStoreManager;
import com.example.happymeals.database.FirebaseAuthenticationHandler;

/** Initial UI layer for user login
 * This class is a candidate to be the entry point of the application -- its implementation has yet
 *  to be approved. It features a login screen and will authenticate users. Upon successful authentication,
 *  users can then access all their persistent data.
 * @author sruduke bfiogbe
 *
 */


public class LoginActivity extends AppCompatActivity {

    /**
     * This is the function called whenever the LoginScreen is created -- in our
     * case, this is on the launch of the app (if approved by client). If implemented,
     * upon successful user authentication, it will send an intent to open an instance of
     * the MainActivity. From there, users will be able to access all of their data -- ingredients,
     * recipes, meal plans, etc.
     * @param savedInstanceState The instance state to restore the activity to (if applicable) {@link Bundle}
     */


    private Button login, register;
    private TextView forgotPassword;
    private EditText userInputField, passwordInputField;
    private FirebaseAuthenticationHandler fireAuth;


    @Override
    protected void onCreate( Bundle savedInstanceState ) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login_screen);
        fireAuth = FirebaseAuthenticationHandler.getFireAuth();
        // initialize all used objects

        login = findViewById(R.id.login_button);
        userInputField = findViewById(R.id.input_username);
        passwordInputField = findViewById(R.id.input_password);
        forgotPassword = findViewById(R.id.forgot_password);
        register = findViewById(R.id.register_button);

        // user clicks "login" button
        login.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                // get input from user
                String inputUser = userInputField.getText().toString();
                String inputPass = passwordInputField.getText().toString();


                // check for errors

                // use string strip userInput.getText().toString()."Strip".Len > 0

                //1 empty user passed

                if( TextUtils.isEmpty(inputUser.trim())) {
                    userInputField.setError("User cannot be empty");
                }

                //2 empty password passed

                if( TextUtils.isEmpty(inputPass.trim())) {
                    passwordInputField.setError("Password cannot be empty");
                }
                //3 Password length cannot be less than 8 chars

                fireAuth.getFireAuth().userLogin(inputUser,inputPass, new OutputListener() {
                    @Override
                    public void onSuccess() {
                        FireStoreManager.getInstance().setUser( inputUser );
                        startActivity(new Intent(LoginActivity.this, MainActivity.class));
                    }

                    @Override
                    public void onFailure(Exception e) {
                        return;
                    }

                });

            }
        });

        // user clicks "register" button
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
                // close this activity
            }
        });

        // user clicks "forgot password" button

    }







}
