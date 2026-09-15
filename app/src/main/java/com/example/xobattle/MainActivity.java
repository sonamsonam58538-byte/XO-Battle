package com.example.xobattle;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.splashscreen.SplashScreen;

public class MainActivity extends AppCompatActivity {

    Button btnOnline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        SplashScreen.installSplashScreen(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        btnOnline = findViewById(R.id.btnOnline);


        btnOnline.setOnClickListener(v -> {

            Intent intent = new Intent(
                    MainActivity.this,
                    OnlineActivity.class
            );

            startActivity(intent);

        });

    }
}