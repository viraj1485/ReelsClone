package com.example.reels.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Toast;

import com.example.reels.Model.SignUp;
import com.example.reels.databinding.ActivitySignUpBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.regex.Pattern;

public class SignUpActivity extends AppCompatActivity {

    ActivitySignUpBinding activitySignUpBinding;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activitySignUpBinding = ActivitySignUpBinding.inflate(getLayoutInflater());
        setContentView(activitySignUpBinding.getRoot());
        this.getWindow().setStatusBarColor(Color.parseColor("#4AA7F3"));
        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        activitySignUpBinding.btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Patterns.EMAIL_ADDRESS.matcher(activitySignUpBinding.tvPhonenumber.getText().toString()).matches()) {
                    activitySignUpBinding.tvPhonenumber.setError("Invalid Email");
                } else if (!activitySignUpBinding.tvPasswordsignuo.getText().toString().matches("^[a-zA-Z\\s]+")) {
                    activitySignUpBinding.tvPasswordsignuo.setError("Invalid name");
                } else if (!activitySignUpBinding.tvFullnamesignup.getText().toString().matches("^[A-Za-z]\\w{5,29}$")) {
                    activitySignUpBinding.tvFullnamesignup.setError("Invalid Username");
                } else if (activitySignUpBinding.tvUsername.getText().toString().length() != 6) {
                    activitySignUpBinding.tvUsername.setError("Invalid Password");
                } else {
                    activitySignUpBinding.progresssignup.setVisibility(View.VISIBLE);
                    RegisterUser(activitySignUpBinding.tvPhonenumber.getText().toString(), activitySignUpBinding.tvUsername.getText().toString());
                }
            }
        });
    }

    private void RegisterUser(String email, String password) {
        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    FirebaseUser firebaseUser1 = firebaseAuth.getCurrentUser();
                    String uid = firebaseUser1.getUid();
                    DatabaseReference databaseReference1 = FirebaseDatabase.getInstance().getReference("Users");
                    SignUp signUp = new SignUp(email, activitySignUpBinding.tvPasswordsignuo.getText().toString(), activitySignUpBinding.tvFullnamesignup.getText().toString(), activitySignUpBinding.tvUsername.getText().toString(), uid);
                    databaseReference1.child(uid).setValue(signUp).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(SignUpActivity.this, "Register SuccessFully", Toast.LENGTH_SHORT).show();
                                Toast.makeText(SignUpActivity.this, "Please Login For Verification", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(SignUpActivity.this, "Register UnSuccessFully", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}