package com.example.reels.Activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.reels.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding activityMainBinding;

    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;
    private Intent intent;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityMainBinding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(activityMainBinding.getRoot());
        this.getWindow().setStatusBarColor(Color.parseColor("#4AA7F3"));
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        activityMainBinding.tvCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, SignUpActivity.class));
                finish();
            }
        });
        activityMainBinding.btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences sharedPreferences = getSharedPreferences("Flag", MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("login", true);
                editor.apply();

                if (!Patterns.EMAIL_ADDRESS.matcher(activityMainBinding.tvPhonenumber.getText().toString()).matches()) {
                    activityMainBinding.tvPhonenumber.setError("check Email");
                } else if (activityMainBinding.tvPassword.getText().toString().length() != 6) {
                    activityMainBinding.tvPassword.setError("check Password");
                } else {
                    activityMainBinding.progressignin.setVisibility(View.VISIBLE);
                    Indentifyuser(activityMainBinding.tvPhonenumber.getText().toString(),
                            activityMainBinding.tvPassword.getText().toString());
                }
            }
        });
    }

    private void Indentifyuser(String username, String password) {
        firebaseAuth.signInWithEmailAndPassword(username, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                firebaseUser = firebaseAuth.getCurrentUser();
                uid = firebaseUser.getUid();
                Toast.makeText(MainActivity.this, "Login-SuccessFull", Toast.LENGTH_SHORT).show();
                intent = new Intent(MainActivity.this, VideoPalyingactivity.class);
                startActivity(intent);
                finish();
            }
        });
    }
}