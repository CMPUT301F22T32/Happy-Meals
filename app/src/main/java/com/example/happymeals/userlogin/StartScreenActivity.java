package com.example.happymeals.userlogin;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.happymeals.MainActivity;
import com.example.happymeals.R;
import com.example.happymeals.database.FireStoreManager;
import com.example.happymeals.database.FirebaseAuthenticationHandler;

public class StartScreenActivity extends AppCompatActivity {

    private Button loginBtn, registerBtn;
    private FirebaseAuthenticationHandler fireAuth;

    @Override
    protected void onCreate ( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS);
        setContentView(R.layout.start_screen_activity);
        loginBtn = findViewById(R.id.login_redirect);
        registerBtn = findViewById(R.id.register_redirect);
        fireAuth = FirebaseAuthenticationHandler.getFireAuth();

        if( isLoggedIn() ) {
            FireStoreManager.getInstance().setUser( fireAuth.authenticate.getCurrentUser()
                    .getEmail() );
            startActivity(new Intent(StartScreenActivity.this, MainActivity.class));
        }

        loginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(StartScreenActivity.this).toBundle();
                startActivity(new Intent(StartScreenActivity.this, LoginActivity.class), bundle);
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Bundle bundle = ActivityOptions.makeSceneTransitionAnimation(StartScreenActivity.this).toBundle();
                startActivity(new Intent(StartScreenActivity.this, RegisterActivity.class), bundle);
            }
        });


    }

    private boolean isLoggedIn() {
        return FirebaseAuthenticationHandler.getFireAuth().authenticate.getCurrentUser() != null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if( isLoggedIn() ) {
            FireStoreManager.getInstance().setUser( fireAuth.authenticate.getCurrentUser().getEmail() );
            startActivity(new Intent(StartScreenActivity.this, MainActivity.class));
        }
    }
}
