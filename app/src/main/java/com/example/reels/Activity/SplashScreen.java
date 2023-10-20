package com.example.reels.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.example.reels.R;

public class SplashScreen extends AppCompatActivity {

    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        this.getWindow().setStatusBarColor(Color.parseColor("#4AA7F3"));


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences sharedPreferences = getSharedPreferences("Flag", MODE_PRIVATE);
                boolean b = sharedPreferences.getBoolean("login", false);
                if (b) {
                    intent = new Intent(SplashScreen.this, VideoPalyingactivity.class);
                } else {
                    intent = new Intent(SplashScreen.this, MainActivity.class);
                }

                Toast.makeText(SplashScreen.this, "Welcome-Reels-App", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish();
            }
        }, 3000);

    }
}