package com.example.happymeals.userlogin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.example.happymeals.R;

public class StartScreenActivity extends AppCompatActivity {

    private Button loginBtn, registerBtn;

    @Override
    protected void onCreate ( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.start_screen_activity);
        loginBtn = findViewById(R.id.login_redirect);
        registerBtn = findViewById(R.id.register_redirect);

        loginBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartScreenActivity.this, LoginActivity.class));
            }
        });

        registerBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(StartScreenActivity.this, RegisterActivity.class));
            }
        });






    }


}
